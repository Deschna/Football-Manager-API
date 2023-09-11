package com.example.footballmanager.controller;

import com.example.footballmanager.dto.mapper.DtoMapper;
import com.example.footballmanager.dto.request.PlayerRequestDto;
import com.example.footballmanager.dto.request.TeamRequestDto;
import com.example.footballmanager.dto.response.PlayerResponseDto;
import com.example.footballmanager.dto.response.TeamResponseDto;
import com.example.footballmanager.exception.PlayerAlreadyOnTeamException;
import com.example.footballmanager.exception.PlayerNotFoundException;
import com.example.footballmanager.exception.TeamNotFoundException;
import com.example.footballmanager.model.Player;
import com.example.footballmanager.model.Team;
import com.example.footballmanager.service.PlayerService;
import com.example.footballmanager.service.TeamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.math.BigDecimal;
import java.time.LocalDate;
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
@WebMvcTest(PlayerController.class)
public class PlayerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PlayerService playerService;
    @MockBean
    private TeamService teamService;
    @MockBean
    private DtoMapper<Player, PlayerRequestDto, PlayerResponseDto> playerDtoMapper;
    private final ObjectMapper objectMapper;
    private final Player player;
    private final PlayerRequestDto playerRequestDto;
    private final  PlayerResponseDto playerResponseDto;
    private final Team team;
    private final TeamRequestDto teamRequestDto;
    private final TeamResponseDto teamResponseDto;
    private static final Long DEFAULT_PLAYER_ID = 1L;
    private static final String DEFAULT_PLAYER_FIRST_NAME = "FirstName";
    private static final String DEFAULT_PLAYER_LAST_NAME = "LastName";
    private static final LocalDate DEFAULT_PLAYER_CAREER_START_DATE = LocalDate.now().minusYears(1);
    private static final LocalDate DEFAULT_PLAYER_BIRTH_DATE = LocalDate.now().minusYears(30);
    private static final Long DEFAULT_TEAM_ID = 11L;
    private static final String DEFAULT_TEAM_NAME = "Test Team Name";
    private static final BigDecimal DEFAULT_TEAM_BUDGET = BigDecimal.valueOf(1000000);
    private static final BigDecimal DEFAULT_TEAM_TRANSFER_COMMISSION = BigDecimal.valueOf(5);

    public PlayerControllerTest() {
        objectMapper = new ObjectMapper();

        player = new Player();
        playerRequestDto = new PlayerRequestDto();
        playerResponseDto = new PlayerResponseDto();

        team = new Team();
        teamRequestDto = new TeamRequestDto();
        teamResponseDto = new TeamResponseDto();
    }

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper.registerModule(new JavaTimeModule());

        player.setId(DEFAULT_PLAYER_ID);
        player.setFirstname(DEFAULT_PLAYER_FIRST_NAME);
        player.setLastname(DEFAULT_PLAYER_LAST_NAME);
        player.setBirthDate(DEFAULT_PLAYER_BIRTH_DATE);
        player.setCareerStartDate(DEFAULT_PLAYER_CAREER_START_DATE);

        playerRequestDto.setFirstname(DEFAULT_PLAYER_FIRST_NAME);
        playerRequestDto.setLastname(DEFAULT_PLAYER_LAST_NAME);
        playerRequestDto.setBirthDate(DEFAULT_PLAYER_BIRTH_DATE);
        playerRequestDto.setCareerStartDate(DEFAULT_PLAYER_CAREER_START_DATE);

        playerResponseDto.setFirstname(DEFAULT_PLAYER_FIRST_NAME);
        playerResponseDto.setLastname(DEFAULT_PLAYER_LAST_NAME);
        playerResponseDto.setBirthDate(DEFAULT_PLAYER_BIRTH_DATE);
        playerResponseDto.setCareerStartDate(DEFAULT_PLAYER_CAREER_START_DATE);

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
    public void testGetPlayerById_Ok() throws Exception {
        when(playerService.getById(DEFAULT_PLAYER_ID)).thenReturn(player);
        when(playerDtoMapper.toResponseDto(player)).thenReturn(playerResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/players/{id}", DEFAULT_PLAYER_ID))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstname").value(DEFAULT_PLAYER_FIRST_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastname").value(DEFAULT_PLAYER_LAST_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthDate")
                        .value(DEFAULT_PLAYER_BIRTH_DATE.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.careerStartDate")
                        .value(DEFAULT_PLAYER_CAREER_START_DATE.toString()));
    }

    @Test
    public void testGetPlayerByIdNotFound_NotOk() throws Exception {
        when(playerService.getById(DEFAULT_PLAYER_ID)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/players/{id}", DEFAULT_PLAYER_ID))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testGetAllPlayersByTeamId_Ok() throws Exception {
        Long teamId = 1L;

        Player firstPlayer = new Player();
        firstPlayer.setId(1L);

        Player secondPlayer = new Player();
        secondPlayer.setId(2L);

        List<Player> players = List.of(firstPlayer, secondPlayer);

        PlayerResponseDto firstPlayerResponseDto = new PlayerResponseDto();
        firstPlayerResponseDto.setId(firstPlayer.getId());
        PlayerResponseDto secondPlayerResponseDto = new PlayerResponseDto();
        secondPlayerResponseDto.setId(secondPlayer.getId());

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<Player> page = new PageImpl<>(players, pageable, players.size());

        when(playerService.getAllByTeamId(pageable, teamId)).thenReturn(page);
        when(playerDtoMapper.toResponseDto(firstPlayer)).thenReturn(firstPlayerResponseDto);
        when(playerDtoMapper.toResponseDto(secondPlayer)).thenReturn(secondPlayerResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/players")
                        .param("teamId", String.valueOf(teamId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2));
    }

    @Test
    public void testCreatePlayer_Ok() throws Exception {
        when(playerService.create(player)).thenReturn(player);
        when(playerDtoMapper.toModel(playerRequestDto)).thenReturn(player);
        when(playerDtoMapper.toResponseDto(player)).thenReturn(playerResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstname").value(DEFAULT_PLAYER_FIRST_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastname").value(DEFAULT_PLAYER_LAST_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthDate")
                        .value(DEFAULT_PLAYER_BIRTH_DATE.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.careerStartDate")
                        .value(DEFAULT_PLAYER_CAREER_START_DATE.toString()));
    }

    @Test
    public void testAddUnassignedPlayerToTeam_Ok() throws Exception {
        when(playerService.getById(DEFAULT_PLAYER_ID)).thenReturn(player);
        when(teamService.getById(DEFAULT_TEAM_ID)).thenReturn(team);
        when(playerService.addUnassignedPlayerToTeam(player, team)).thenReturn(player);
        when(playerDtoMapper.toResponseDto(player)).thenReturn(playerResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/players/{playerId}/add-to-team?teamId={teamId}",
                        DEFAULT_PLAYER_ID, DEFAULT_TEAM_ID))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstname").value(DEFAULT_PLAYER_FIRST_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastname").value(DEFAULT_PLAYER_LAST_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthDate")
                        .value(DEFAULT_PLAYER_BIRTH_DATE.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.careerStartDate")
                        .value(DEFAULT_PLAYER_CAREER_START_DATE.toString()));
    }

    @Test
    public void testAddPlayerAlreadyInTeamToAnotherTeam_NotOk() throws Exception {
        when(playerService.getById(DEFAULT_PLAYER_ID)).thenReturn(player);
        when(teamService.getById(DEFAULT_TEAM_ID)).thenReturn(team);
        when(playerService.addUnassignedPlayerToTeam(player, team))
                .thenThrow(new PlayerAlreadyOnTeamException("The player is already on the team"));

        mockMvc.perform(MockMvcRequestBuilders.post("/players/{playerId}/add-to-team?teamId={teamId}",
                        DEFAULT_PLAYER_ID, DEFAULT_TEAM_ID))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testTransferPlayer_Ok() throws Exception {
        when(playerService.getById(DEFAULT_PLAYER_ID)).thenReturn(player);
        when(teamService.getById(DEFAULT_TEAM_ID)).thenReturn(team);
        when(playerService.transferPlayerToTeam(player, team)).thenReturn(player);
        when(playerDtoMapper.toResponseDto(player)).thenReturn(playerResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/players/{playerId}/transfer?teamId={teamId}",
                        DEFAULT_PLAYER_ID, DEFAULT_TEAM_ID))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstname").value(DEFAULT_PLAYER_FIRST_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastname").value(DEFAULT_PLAYER_LAST_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthDate")
                        .value(DEFAULT_PLAYER_BIRTH_DATE.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.careerStartDate")
                        .value(DEFAULT_PLAYER_CAREER_START_DATE.toString()));
    }

    @Test
    public void testTransferPlayerNotFoundStatus_NotOk() throws Exception {
        when(playerService.getById(DEFAULT_PLAYER_ID)).thenReturn(player);
        when(teamService.getById(DEFAULT_TEAM_ID)).thenReturn(team);
        when(playerService.transferPlayerToTeam(player, team))
                .thenThrow(new TeamNotFoundException("Player does not belong to any team"));

        mockMvc.perform(MockMvcRequestBuilders.post("/players/{playerId}/transfer?teamId={teamId}",
                        DEFAULT_PLAYER_ID, DEFAULT_TEAM_ID))
                .andExpect(MockMvcResultMatchers.status().isNotFound());


    }

    @Test
    public void testTransferPlayerBadRequestStatus_NotOk() throws Exception {
        player.setTeam(team);
        when(playerService.getById(DEFAULT_PLAYER_ID)).thenReturn(player);
        when(teamService.getById(DEFAULT_TEAM_ID)).thenReturn(team);
        when(playerService.transferPlayerToTeam(player, team))
                .thenThrow(new PlayerAlreadyOnTeamException("Can't transfer a player to a team he's already on"));

        mockMvc.perform(MockMvcRequestBuilders.post("/players/{playerId}/transfer?teamId={teamId}",
                        DEFAULT_PLAYER_ID, DEFAULT_TEAM_ID))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testUpdatePlayerById_Ok() throws Exception {
        when(playerDtoMapper.toModel(playerRequestDto)).thenReturn(player);
        when(playerService.updateById(DEFAULT_PLAYER_ID, player)).thenReturn(player);
        when(playerDtoMapper.toResponseDto(player)).thenReturn(playerResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/players/{id}", DEFAULT_PLAYER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstname").value(DEFAULT_PLAYER_FIRST_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastname").value(DEFAULT_PLAYER_LAST_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthDate")
                        .value(DEFAULT_PLAYER_BIRTH_DATE.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.careerStartDate")
                        .value(DEFAULT_PLAYER_CAREER_START_DATE.toString()));
    }

    @Test
    public void testUpdatePlayerByIdNotFound_NotOk() throws Exception {
        when(playerDtoMapper.toModel(playerRequestDto)).thenReturn(player);
        when(playerService.updateById(DEFAULT_PLAYER_ID, player))
                .thenThrow(new PlayerNotFoundException("No player present with id " + DEFAULT_PLAYER_ID));

        mockMvc.perform(MockMvcRequestBuilders.put("/players/" + DEFAULT_PLAYER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testDeletePlayerById_Ok() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/players/{id}", DEFAULT_PLAYER_ID))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(playerService, times(1)).deleteById(DEFAULT_PLAYER_ID);
    }

    @Test
    public void testDeletePlayerByIdNotFound_NotOk() throws Exception {
        doThrow(new PlayerNotFoundException("No player present with id " + DEFAULT_PLAYER_ID))
                .when(playerService).deleteById(DEFAULT_PLAYER_ID);

        mockMvc.perform(MockMvcRequestBuilders.delete("/players/{id}", DEFAULT_PLAYER_ID))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(playerService, times(1)).deleteById(DEFAULT_PLAYER_ID);
    }
}
