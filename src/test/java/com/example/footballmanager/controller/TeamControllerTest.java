package com.example.footballmanager.controller;

import com.example.footballmanager.dto.mapper.DtoMapper;
import com.example.footballmanager.dto.request.TeamRequestDto;
import com.example.footballmanager.dto.response.TeamResponseDto;
import com.example.footballmanager.exception.EntityNotFoundException;
import com.example.footballmanager.model.Team;
import com.example.footballmanager.service.TeamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(TeamController.class)
public class TeamControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TeamService teamService;
    @MockBean
    private DtoMapper<Team, TeamRequestDto, TeamResponseDto> teamDtoMapper;
    private final Team team;
    private final TeamRequestDto teamRequestDto;
    private final TeamResponseDto teamResponseDto;
    private static final long DEFAULT_TEAM_ID = 1L;
    private static final String DEFAULT_TEAM_NAME = "Test Team Name";
    private static final BigDecimal DEFAULT_TEAM_BUDGET = BigDecimal.valueOf(1000000);
    private static final BigDecimal DEFAULT_TEAM_TRANSFER_COMMISSION = BigDecimal.valueOf(5);

    public TeamControllerTest() {
        team = new Team();
        teamRequestDto = new TeamRequestDto();
        teamResponseDto = new TeamResponseDto();
    }

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        team.setId(DEFAULT_TEAM_ID);
        team.setName(DEFAULT_TEAM_NAME);
        team.setBudget(DEFAULT_TEAM_BUDGET);
        team.setPlayerTransferCommission(DEFAULT_TEAM_TRANSFER_COMMISSION);

        teamRequestDto.setName(DEFAULT_TEAM_NAME);
        teamRequestDto.setBudget(DEFAULT_TEAM_BUDGET);
        teamRequestDto.setPlayerTransferCommission(DEFAULT_TEAM_TRANSFER_COMMISSION);

        teamResponseDto.setId(DEFAULT_TEAM_ID);
        teamResponseDto.setName(DEFAULT_TEAM_NAME);
        teamResponseDto.setBudget(DEFAULT_TEAM_BUDGET);
        teamResponseDto.setPlayerTransferCommission(DEFAULT_TEAM_TRANSFER_COMMISSION);
    }

    @Test
    public void testGetById_Ok() throws Exception {
        when(teamService.getById(DEFAULT_TEAM_ID)).thenReturn(team);
        when(teamDtoMapper.toResponseDto(team)).thenReturn(teamResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/teams/" + DEFAULT_TEAM_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(DEFAULT_TEAM_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(DEFAULT_TEAM_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.budget").value(DEFAULT_TEAM_BUDGET))
                .andExpect(MockMvcResultMatchers.jsonPath("$.playerTransferCommission")
                        .value(DEFAULT_TEAM_TRANSFER_COMMISSION));
    }

    @Test
    public void testGetByIdNotFound_NotOk() throws Exception {
        when(teamService.getById(DEFAULT_TEAM_ID))
                .thenThrow(new EntityNotFoundException("No team present with id " + DEFAULT_TEAM_ID));

        mockMvc.perform(MockMvcRequestBuilders.get("/teams/" + DEFAULT_TEAM_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testGetAll_Ok() throws Exception {
        Team firstTeam = new Team();
        firstTeam.setId(1L);

        Team secondTeam = new Team();
        secondTeam.setId(2L);

        List<Team> teams = List.of(firstTeam, secondTeam);

        TeamResponseDto firstTeamResponseDto = new TeamResponseDto();
        firstTeamResponseDto.setId(firstTeam.getId());
        TeamResponseDto secondTeamResponseDto = new TeamResponseDto();
        secondTeamResponseDto.setId(secondTeam.getId());

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<Team> page = new PageImpl<>(teams, pageable, teams.size());

        when(teamService.getAll(pageable)).thenReturn(page);
        when(teamDtoMapper.toResponseDto(firstTeam)).thenReturn(firstTeamResponseDto);
        when(teamDtoMapper.toResponseDto(secondTeam)).thenReturn(secondTeamResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/teams")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2));
    }

    @Test
    public void testCreate_Ok() throws Exception {
        when(teamDtoMapper.toModel(teamRequestDto)).thenReturn(team);
        when(teamService.create(team)).thenReturn(team);
        when(teamDtoMapper.toResponseDto(team)).thenReturn(teamResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(teamRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(DEFAULT_TEAM_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(DEFAULT_TEAM_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.budget").value(DEFAULT_TEAM_BUDGET))
                .andExpect(MockMvcResultMatchers.jsonPath("$.playerTransferCommission")
                        .value(DEFAULT_TEAM_TRANSFER_COMMISSION));
    }

    @Test
    public void testUpdateTeamById_Ok() throws Exception {
        String updatedName = "Updated Test Team Name";

        team.setName(updatedName);
        teamResponseDto.setName(updatedName);
        teamResponseDto.setName(updatedName);

        when(teamDtoMapper.toModel(teamRequestDto)).thenReturn(team);
        when(teamService.updateById(DEFAULT_TEAM_ID, team)).thenReturn(team);
        when(teamDtoMapper.toResponseDto(team)).thenReturn(teamResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/teams/" + DEFAULT_TEAM_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(teamRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(DEFAULT_TEAM_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(updatedName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.budget").value(DEFAULT_TEAM_BUDGET))
                .andExpect(MockMvcResultMatchers.jsonPath("$.playerTransferCommission")
                        .value(DEFAULT_TEAM_TRANSFER_COMMISSION));
    }

    @Test
    public void testUpdateTeamByIdNotFound_NotOk() throws Exception {
        when(teamDtoMapper.toModel(teamRequestDto)).thenReturn(team);
        when(teamService.updateById(DEFAULT_TEAM_ID, team))
                .thenThrow(new EntityNotFoundException("No team present with id " + DEFAULT_TEAM_ID));

        mockMvc.perform(MockMvcRequestBuilders.put("/teams/" + DEFAULT_TEAM_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(teamRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testDeleteTeamById_Ok() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/teams/" + DEFAULT_TEAM_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(teamService, times(1)).deleteById(DEFAULT_TEAM_ID);
    }

    @Test
    public void testDeleteTeamByIdNotFound_NotOk() throws Exception {
        doThrow(new EntityNotFoundException("No team present with id " + DEFAULT_TEAM_ID))
                .when(teamService).deleteById(DEFAULT_TEAM_ID);

        mockMvc.perform(MockMvcRequestBuilders.delete("/teams/" + DEFAULT_TEAM_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(teamService, times(1)).deleteById(DEFAULT_TEAM_ID);
    }
}
