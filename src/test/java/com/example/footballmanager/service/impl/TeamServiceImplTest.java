package com.example.footballmanager.service.impl;

import com.example.footballmanager.exception.BadRequestException;
import com.example.footballmanager.exception.EntityNotFoundException;
import com.example.footballmanager.model.Team;
import com.example.footballmanager.repository.TeamRepository;
import com.example.footballmanager.service.TeamService;
import java.math.BigDecimal;
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

public class TeamServiceImplTest {
    @Mock
    private TeamRepository teamRepository;
    private TeamService teamService;
    private final Team teamWithId;
    private final Team teamWithoutInitialId;
    private static final Long DEFAULT_TEAM_ID = 1L;
    private static final String DEFAULT_TEAM_NAME = "Test Team";
    private static final BigDecimal DEFAULT_TEAM_BUDGET = BigDecimal.valueOf(1000000);
    private static final BigDecimal DEFAULT_TEAM_TRANSFER_COMMISSION = BigDecimal.valueOf(5);

    public TeamServiceImplTest() {
        teamWithId = new Team();
        teamWithoutInitialId = new Team();
    }

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        teamService = new TeamServiceImpl(teamRepository);

        teamWithId.setId(DEFAULT_TEAM_ID);
        teamWithId.setName(DEFAULT_TEAM_NAME);
        teamWithId.setBudget(DEFAULT_TEAM_BUDGET);
        teamWithId.setPlayerTransferCommission(DEFAULT_TEAM_TRANSFER_COMMISSION);

        teamWithoutInitialId.setId(null);
        teamWithoutInitialId.setName(DEFAULT_TEAM_NAME);
        teamWithoutInitialId.setBudget(DEFAULT_TEAM_BUDGET);
        teamWithoutInitialId.setPlayerTransferCommission(DEFAULT_TEAM_TRANSFER_COMMISSION);
    }

    @Test
    public void testGetById_Ok() {
        when(teamRepository.findById(DEFAULT_TEAM_ID)).thenReturn(Optional.of(teamWithId));

        Team actualTeam = teamService.getById(DEFAULT_TEAM_ID);

        assertNotNull(actualTeam);
        assertEquals(teamWithId, actualTeam);
    }

    @Test
    public void testGetByIdNotFound_NotOk() {
        when(teamRepository.findById(DEFAULT_TEAM_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> teamService.getById(DEFAULT_TEAM_ID));
    }

    @Test
    public void testCreate_Ok() {
        when(teamRepository.save(teamWithoutInitialId)).thenReturn(teamWithId);

        Team createdTeam = teamService.create(teamWithoutInitialId);

        assertNotNull(createdTeam);
        assertEquals(teamWithId, createdTeam);
    }

    @Test
    public void testCreateWithId_NotOk() {
        assertThrows(BadRequestException.class, () -> teamService.create(teamWithId));
    }

    @Test
    public void testUpdateById_Ok() {
        when(teamRepository.existsById(DEFAULT_TEAM_ID)).thenReturn(true);
        when(teamRepository.save(teamWithoutInitialId)).thenReturn(teamWithoutInitialId);

        Team updatedTeam = teamService.updateById(DEFAULT_TEAM_ID, teamWithoutInitialId);

        assertNotNull(updatedTeam);
        assertEquals(DEFAULT_TEAM_ID, updatedTeam.getId());
    }

    @Test
    public void testUpdateByIdNotFound_NotOk() {
        when(teamRepository.existsById(DEFAULT_TEAM_ID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> teamService.updateById(DEFAULT_TEAM_ID, teamWithoutInitialId));
    }

    @Test
    public void testDeleteById_Ok() {
        when(teamRepository.existsById(DEFAULT_TEAM_ID)).thenReturn(true);

        teamService.deleteById(DEFAULT_TEAM_ID);

        verify(teamRepository, times(1)).deleteById(DEFAULT_TEAM_ID);
    }

    @Test
    public void testDeleteByIdNotFound_NotOk() {
        when(teamRepository.existsById(DEFAULT_TEAM_ID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> teamService.deleteById(DEFAULT_TEAM_ID));
      
        verify(teamRepository, times(0)).deleteById(DEFAULT_TEAM_ID);
    }
}
