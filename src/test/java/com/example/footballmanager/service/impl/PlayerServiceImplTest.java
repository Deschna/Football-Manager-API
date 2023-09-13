package com.example.footballmanager.service.impl;

import com.example.footballmanager.exception.BadRequestException;
import com.example.footballmanager.exception.EntityNotFoundException;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PlayerServiceImplTest {
    @Mock
    private PlayerRepository playerRepository;
    private PlayerService playerService;
    private final Player playerWithId;
    private final Player playerWithoutInitialId;
    private final Team buyingTeam;
    private final Team sellingTeam;
    private static final Long DEFAULT_PLAYER_ID = 1L;
    private static final String DEFAULT_PLAYER_FIRST_NAME = "FirstName";
    private static final String DEFAULT_PLAYER_LAST_NAME = "LastName";
    private static final LocalDate DEFAULT_PLAYER_CAREER_START_DATE = LocalDate.now().minusYears(1);
    private static final LocalDate DEFAULT_PLAYER_BIRTH_DATE = LocalDate.now().minusYears(30);
    private static final Long DEFAULT_BUYING_TEAM_ID = 11L;
    private static final BigDecimal DEFAULT_BUYING_TEAM_BUDGET = new BigDecimal("44000.00");
    private static final BigDecimal DEFAULT_BUYING_TEAM_BUDGET_AFTER_TRANSFER = new BigDecimal("0.00");
    private static final Long DEFAULT_SELLING_TEAM_ID = 12L;
    private static final BigDecimal DEFAULT_SELLING_TEAM_BUDGET = new BigDecimal("0.00");
    private static final BigDecimal DEFAULT_SELLING_TEAM_BUDGET_AFTER_TRANSFER = new BigDecimal("44000.00");
    private static final BigDecimal DEFAULT_SELLING_TEAM_TRANSFER_COMMISSION = BigDecimal.valueOf(10);

    public PlayerServiceImplTest() {
        playerWithId = new Player();
        playerWithoutInitialId = new Player();

        buyingTeam = new Team();
        sellingTeam = new Team();
    }

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        playerService = new PlayerServiceImpl(playerRepository);

        playerWithId.setId(DEFAULT_PLAYER_ID);
        playerWithId.setFirstname(DEFAULT_PLAYER_FIRST_NAME);
        playerWithId.setLastname(DEFAULT_PLAYER_LAST_NAME);
        playerWithId.setTeam(null);
        playerWithId.setCareerStartDate(DEFAULT_PLAYER_CAREER_START_DATE);
        playerWithId.setBirthDate(DEFAULT_PLAYER_BIRTH_DATE);

        playerWithoutInitialId.setId(null);
        playerWithoutInitialId.setFirstname(DEFAULT_PLAYER_FIRST_NAME);
        playerWithoutInitialId.setLastname(DEFAULT_PLAYER_LAST_NAME);
        playerWithoutInitialId.setTeam(null);
        playerWithoutInitialId.setCareerStartDate(DEFAULT_PLAYER_CAREER_START_DATE);
        playerWithoutInitialId.setBirthDate(DEFAULT_PLAYER_BIRTH_DATE);

        buyingTeam.setId(DEFAULT_BUYING_TEAM_ID);
        buyingTeam.setBudget(DEFAULT_BUYING_TEAM_BUDGET);

        sellingTeam.setId(DEFAULT_SELLING_TEAM_ID);
        sellingTeam.setBudget(DEFAULT_SELLING_TEAM_BUDGET);
        sellingTeam.setPlayerTransferCommission(DEFAULT_SELLING_TEAM_TRANSFER_COMMISSION);
    }

    @Test
    public void testGetById_Ok() {
        when(playerRepository.findById(DEFAULT_PLAYER_ID)).thenReturn(Optional.of(playerWithId));

        Player actualPlayer = playerService.getById(DEFAULT_PLAYER_ID);

        assertNotNull(actualPlayer);
        assertEquals(playerWithId, actualPlayer);
    }

    @Test
    public void testGetByIdNotFound_NotOk() {
        when(playerRepository.findById(DEFAULT_PLAYER_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> playerService.getById(DEFAULT_PLAYER_ID));
    }

    @Test
    public void testCreate_Ok() {
        when(playerRepository.save(playerWithoutInitialId)).thenReturn(playerWithId);

        Player createdPlayer = playerService.create(playerWithoutInitialId);

        assertNotNull(createdPlayer);
        assertEquals(playerWithId, createdPlayer);
    }

    @Test
    public void testCreateWithId_NotOk() {
        assertThrows(BadRequestException.class, () -> playerService.create(playerWithId));
    }

    @Test
    public void testUpdateById_Ok() {
        when(playerRepository.existsById(DEFAULT_PLAYER_ID)).thenReturn(true);
        when(playerRepository.save(playerWithoutInitialId)).thenReturn(playerWithoutInitialId);

        Player updatedPlayer = playerService.updateById(DEFAULT_PLAYER_ID, playerWithoutInitialId);

        assertNotNull(updatedPlayer);
        assertEquals(DEFAULT_PLAYER_ID, updatedPlayer.getId());
    }

    @Test
    public void testUpdateByIdNotFound_NotOk() {
        when(playerRepository.existsById(DEFAULT_PLAYER_ID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> playerService.updateById(DEFAULT_PLAYER_ID, playerWithoutInitialId));
    }

    @Test
    public void testDeleteById_Ok() {
        when(playerRepository.existsById(DEFAULT_PLAYER_ID)).thenReturn(true);

        playerService.deleteById(DEFAULT_PLAYER_ID);

        verify(playerRepository, times(1)).deleteById(DEFAULT_PLAYER_ID);
    }

    @Test
    public void testDeleteByIdNotFound_NotOk() {
        when(playerRepository.existsById(DEFAULT_PLAYER_ID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> playerService.deleteById(DEFAULT_PLAYER_ID));

        verify(playerRepository, times(0)).deleteById(DEFAULT_PLAYER_ID);
    }

    @Test
    public void testAddUnassignedPlayerToTeam_Ok() {
        when(playerRepository.existsById(DEFAULT_PLAYER_ID)).thenReturn(true);
        when(playerRepository.save(playerWithId)).thenReturn(playerWithId);

        Player resultPlayer = playerService.addUnassignedPlayerToTeam(playerWithId, buyingTeam);

        assertNotNull(resultPlayer);
        assertEquals(buyingTeam, resultPlayer.getTeam());
    }

    @Test
    public void testAddPlayerAlreadyInTeamToAnotherTeam_NotOk() {
        playerWithId.setTeam(sellingTeam);

        assertThrows(BadRequestException.class,
                () -> playerService.addUnassignedPlayerToTeam(playerWithId, buyingTeam));
    }

    @Test
    public void testTransferPlayerToTeam_Ok() {
        playerWithId.setTeam(sellingTeam);

        when(playerRepository.existsById(DEFAULT_PLAYER_ID)).thenReturn(true);
        when(playerRepository.save(playerWithId)).thenReturn(playerWithId);

        Player resultPlayer = playerService.transferPlayerToTeam(playerWithId, buyingTeam);

        assertEquals(DEFAULT_BUYING_TEAM_BUDGET_AFTER_TRANSFER, buyingTeam.getBudget());
        assertEquals(DEFAULT_SELLING_TEAM_BUDGET_AFTER_TRANSFER, sellingTeam.getBudget());
        assertNotNull(resultPlayer);
        assertEquals(buyingTeam, resultPlayer.getTeam());
    }

    @Test
    public void testTransferPlayerToTeamNotBelongToTeam_NotOk() {
        assertThrows(EntityNotFoundException.class, () -> playerService.transferPlayerToTeam(playerWithId, buyingTeam));
    }

    @Test
    public void testTransferPlayerToTeamHeAlreadyOn_NotOk() {
        playerWithId.setTeam(buyingTeam);

        assertThrows(BadRequestException.class,
                () -> playerService.transferPlayerToTeam(playerWithId, buyingTeam));
    }

    @Test
    public void testTransferPlayerToTeamInsufficientBudget_NotOk() {
        playerWithId.setTeam(sellingTeam);
        buyingTeam.setBudget(BigDecimal.ZERO);

        when(playerRepository.existsById(DEFAULT_PLAYER_ID)).thenReturn(true);
        when(playerRepository.save(playerWithId)).thenReturn(playerWithId);

        assertThrows(BadRequestException.class,
                () -> playerService.transferPlayerToTeam(playerWithId, buyingTeam));
    }
}
