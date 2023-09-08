package com.example.footballmanager.service.impl;

import com.example.footballmanager.exception.InsufficientBudgetException;
import com.example.footballmanager.exception.PlayerAlreadyExistsException;
import com.example.footballmanager.exception.PlayerAlreadyOnTeamException;
import com.example.footballmanager.exception.PlayerNotFoundException;
import com.example.footballmanager.exception.TeamNotFoundException;
import com.example.footballmanager.model.Player;
import com.example.footballmanager.model.Team;
import com.example.footballmanager.repository.PlayerRepository;
import com.example.footballmanager.service.PlayerService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

public class PlayerServiceImplTest {
    @Mock
    private PlayerRepository playerRepository;

    private PlayerService playerService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        playerService = new PlayerServiceImpl(playerRepository);
    }

    @Test
    public void testGetById() {
        Long playerId = 1L;
        Player expectedPlayer = new Player();
        expectedPlayer.setId(playerId);

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(expectedPlayer));

        Player actualPlayer = playerService.getById(playerId);

        assertNotNull(actualPlayer);
        assertEquals(expectedPlayer, actualPlayer);
    }

    @Test
    public void testGetByIdNotFound() {
        Long playerId = 1L;

        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        assertThrows(PlayerNotFoundException.class, () -> playerService.getById(playerId));
    }

    @Test
    public void testCreate() {
        Player newPlayer = new Player();
        newPlayer.setFirstname("New Player");

        when(playerRepository.save(newPlayer)).thenReturn(newPlayer);

        Player createdPlayer = playerService.create(newPlayer);

        assertNotNull(createdPlayer);
        assertEquals(newPlayer.getFirstname(), createdPlayer.getFirstname());
    }

    @Test
    public void testCreateWithId() {
        Player playerWithId = new Player();
        playerWithId.setId(1L);

        assertThrows(PlayerAlreadyExistsException.class, () -> playerService.create(playerWithId));
    }

    @Test
    public void testUpdateById() {
        Long playerId = 1L;
        Player existingPlayer = new Player();
        existingPlayer.setId(playerId);

        Player updatedPlayer = new Player();
        updatedPlayer.setId(playerId);
        updatedPlayer.setFirstname("Updated Player");

        when(playerRepository.existsById(playerId)).thenReturn(true);
        when(playerRepository.save(updatedPlayer)).thenReturn(updatedPlayer);

        Player result = playerService.updateById(playerId, updatedPlayer);

        assertNotNull(result);
        assertEquals(updatedPlayer.getFirstname(), result.getFirstname());
    }

    @Test
    public void testUpdateByIdNotFound() {
        Long playerId = 1L;
        Player updatedPlayer = new Player();
        updatedPlayer.setId(playerId);

        when(playerRepository.existsById(playerId)).thenReturn(false);

        assertThrows(PlayerNotFoundException.class, () -> playerService.updateById(playerId, updatedPlayer));
    }

    @Test
    public void testDeleteById() {
        Long playerId = 1L;
        when(playerRepository.existsById(playerId)).thenReturn(true);

        playerService.deleteById(playerId);
    }

    @Test
    public void testDeleteByIdNotFound() {
        Long playerId = 1L;
        when(playerRepository.existsById(playerId)).thenReturn(false);

        assertThrows(PlayerNotFoundException.class, () -> playerService.deleteById(playerId));
    }

    @Test
    public void testAddUnassignedPlayerToTeam() {
        Player player = new Player();
        Team team = new Team();

        when(playerRepository.existsById(player.getId())).thenReturn(true);
        when(playerRepository.save(player)).thenReturn(player);

        Player resultPlayer = playerService.addUnassignedPlayerToTeam(player, team);

        assertNotNull(resultPlayer);
        assertEquals(team, resultPlayer.getTeam());
    }

    @Test
    public void testAddUnassignedPlayerToTeamAlreadyOnTeam() {
        Team team = new Team();
        Long teamId = 1L;
        team.setId(teamId);

        Player playerWithTeam = new Player();
        playerWithTeam.setTeam(team);

        assertThrows(PlayerAlreadyOnTeamException.class,
                () -> playerService.addUnassignedPlayerToTeam(playerWithTeam, team));
    }

    @Test
    public void testTransferPlayerToTeam() {
        BigDecimal playerCost = new BigDecimal("44000.00");
        BigDecimal zeroCost = new BigDecimal("0.00");

        Team buyingTeam = new Team();
        buyingTeam.setId(1L);
        buyingTeam.setBudget(playerCost);

        Team sellingTeam = new Team();
        sellingTeam.setId(2L);
        sellingTeam.setBudget(zeroCost);
        sellingTeam.setPlayerTransferCommission(BigDecimal.valueOf(10));

        Player player = new Player();
        player.setId(1L);
        player.setTeam(sellingTeam);
        player.setCareerStartDate(LocalDate.now().minusYears(1));
        player.setBirthDate(LocalDate.now().minusYears(30));

        when(playerRepository.existsById(player.getId())).thenReturn(true);
        when(playerRepository.save(player)).thenReturn(player);

        Player resultPlayer = playerService.transferPlayerToTeam(player, buyingTeam);

        assertEquals(zeroCost, buyingTeam.getBudget());
        assertEquals(playerCost, sellingTeam.getBudget());
        assertNotNull(resultPlayer);
        assertEquals(buyingTeam, resultPlayer.getTeam());
    }

    @Test
    public void testTransferPlayerToTeamNotBelongToTeam() {
        Player player = new Player();
        Team buyingTeam = new Team();

        assertThrows(TeamNotFoundException.class, () -> playerService.transferPlayerToTeam(player, buyingTeam));
    }

    @Test
    public void testTransferPlayerToTeamAlreadyOnTeam() {
        Team buyingTeam = new Team();
        buyingTeam.setId(1L);

        Player player = new Player();
        player.setTeam(buyingTeam);

        assertThrows(PlayerAlreadyOnTeamException.class, () -> playerService.transferPlayerToTeam(player, buyingTeam));
    }

    @Test
    public void testTransferPlayerToTeamInsufficientBudget() {
        Team buyingTeam = new Team();
        buyingTeam.setId(1L);

        Team sellingTeam = new Team();
        sellingTeam.setId(2L);
        sellingTeam.setPlayerTransferCommission(BigDecimal.valueOf(10));

        Player player = new Player();
        player.setTeam(sellingTeam);
        player.setCareerStartDate(LocalDate.now().minusYears(1));
        player.setBirthDate(LocalDate.now().minusYears(30));

        buyingTeam.setBudget(BigDecimal.valueOf(100));

        when(playerRepository.existsById(player.getId())).thenReturn(true);
        when(playerRepository.save(player)).thenReturn(player);

        assertThrows(InsufficientBudgetException.class, () -> playerService.transferPlayerToTeam(player, buyingTeam));
    }
}
