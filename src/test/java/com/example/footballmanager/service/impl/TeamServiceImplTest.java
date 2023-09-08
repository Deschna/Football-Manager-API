package com.example.footballmanager.service.impl;

import com.example.footballmanager.exception.TeamAlreadyExistsException;
import com.example.footballmanager.exception.TeamNotFoundException;
import com.example.footballmanager.model.Team;
import com.example.footballmanager.repository.TeamRepository;
import com.example.footballmanager.service.TeamService;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

public class TeamServiceImplTest {
    @Mock
    private TeamRepository teamRepository;
    private TeamService teamService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        teamService = new TeamServiceImpl(teamRepository);
    }

    @Test
    public void testGetById() {
        Long teamId = 1L;
        Team expectedTeam = new Team();
        expectedTeam.setId(teamId);

        when(teamRepository.findById(teamId)).thenReturn(Optional.of(expectedTeam));

        Team actualTeam = teamService.getById(teamId);

        assertNotNull(actualTeam);
        assertEquals(expectedTeam, actualTeam);
    }

    @Test
    public void testGetByIdNotFound() {
        Long teamId = 1L;

        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

        assertThrows(TeamNotFoundException.class, () -> teamService.getById(teamId));
    }

    @Test
    public void testCreate() {
        Team newTeam = new Team();
        newTeam.setName("New Team");

        when(teamRepository.save(newTeam)).thenReturn(newTeam);

        Team createdTeam = teamService.create(newTeam);

        assertNotNull(createdTeam);
        assertEquals(newTeam.getName(), createdTeam.getName());
    }

    @Test
    public void testCreateWithId() {
        Team teamWithId = new Team();
        teamWithId.setId(1L);

        assertThrows(TeamAlreadyExistsException.class, () -> teamService.create(teamWithId));
    }

    @Test
    public void testUpdateById() {
        Long teamId = 1L;
        Team existingTeam = new Team();
        existingTeam.setId(teamId);

        Team updatedTeam = new Team();
        updatedTeam.setId(teamId);
        updatedTeam.setName("Updated Team");

        when(teamRepository.existsById(teamId)).thenReturn(true);
        when(teamRepository.save(updatedTeam)).thenReturn(updatedTeam);

        Team result = teamService.updateById(teamId, updatedTeam);

        assertNotNull(result);
        assertEquals(updatedTeam.getName(), result.getName());
    }

    @Test
    public void testUpdateByIdNotFound() {
        Long teamId = 1L;
        Team updatedTeam = new Team();
        updatedTeam.setId(teamId);

        when(teamRepository.existsById(teamId)).thenReturn(false);

        assertThrows(TeamNotFoundException.class, () -> teamService.updateById(teamId, updatedTeam));
    }

    @Test
    public void testDeleteById() {
        Long teamId = 1L;

        when(teamRepository.existsById(teamId)).thenReturn(true);

        teamService.deleteById(teamId);
    }

    @Test
    public void testDeleteByIdNotFound() {
        Long teamId = 1L;

        when(teamRepository.existsById(teamId)).thenReturn(false);

        assertThrows(TeamNotFoundException.class, () -> teamService.deleteById(teamId));
    }
}
