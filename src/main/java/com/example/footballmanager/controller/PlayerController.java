package com.example.footballmanager.controller;

import com.example.footballmanager.dto.mapper.DtoMapper;
import com.example.footballmanager.dto.request.PlayerRequestDto;
import com.example.footballmanager.dto.response.PlayerResponseDto;
import com.example.footballmanager.model.Player;
import com.example.footballmanager.model.Team;
import com.example.footballmanager.service.PlayerService;
import com.example.footballmanager.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/players")
public class PlayerController {
    private final PlayerService playerService;
    private final TeamService teamService;
    private final DtoMapper<Player, PlayerRequestDto, PlayerResponseDto> playerDtoMapper;

    public PlayerController(
            PlayerService playerService,
            TeamService teamService,
            DtoMapper<Player, PlayerRequestDto, PlayerResponseDto> playerDtoMapper
    ) {
        this.playerService = playerService;
        this.teamService = teamService;
        this.playerDtoMapper = playerDtoMapper;
    }

    @Operation(description = "Get player by ID")
    @GetMapping("/{id}")
    public PlayerResponseDto getPlayerById(@PathVariable Long id) {
        return playerDtoMapper.toResponseDto(playerService.getById(id));
    }

    @Operation(description = "Get all players by team ID")
    @GetMapping("/all")
    public List<PlayerResponseDto> getAllPlayersByTeamId(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam Long teamId
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return playerService.getAllByTeamId(pageable, teamId)
                .stream()
                .map(playerDtoMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Operation(description = "Create a new player")
    @PostMapping
    public PlayerResponseDto createPlayer(@Valid @RequestBody PlayerRequestDto requestDto) {
        return playerDtoMapper.toResponseDto(playerService.create(playerDtoMapper.toModel(requestDto)));
    }

    @Operation(description = "Add an unassigned player to a team")
    @PostMapping("/{playerId}/add-to-team")
    public PlayerResponseDto addUnassignedPlayerToTeam(@PathVariable Long playerId, @RequestParam Long teamId) {
        Player player = playerService.getById(playerId);
        Team team = teamService.getById(teamId);
        return playerDtoMapper.toResponseDto(playerService.addUnassignedPlayerToTeam(player, team));
    }

    @Operation(description = "Transfer a player to a team")
    @PostMapping("/{playerId}/transfer")
    public PlayerResponseDto transferPlayer(@PathVariable Long playerId, @RequestParam Long teamId) {
        Player player = playerService.getById(playerId);
        Team team = teamService.getById(teamId);
        return playerDtoMapper.toResponseDto(playerService.transferPlayerToTeam(player, team));
    }

    @Operation(description = "Update player by ID")
    @PutMapping("/{id}")
    public PlayerResponseDto updatePlayerById(@PathVariable Long id, @RequestBody @Valid PlayerRequestDto requestDto) {
        return playerDtoMapper.toResponseDto(playerService.updateById(id, playerDtoMapper.toModel(requestDto)));
    }

    @Operation(description = "Delete player by ID")
    @DeleteMapping("/{id}")
    public void deletePlayerById(@PathVariable Long id) {
        playerService.deleteById(id);
    }
}
