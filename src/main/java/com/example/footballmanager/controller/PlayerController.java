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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<PlayerResponseDto> getPlayerById(@PathVariable Long id) {
        Player player = playerService.getById(id);
        if (player == null) {
            return ResponseEntity.notFound().build();
        }
        PlayerResponseDto responseDto = playerDtoMapper.toResponseDto(player);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(description = "Get all players by team ID")
    @GetMapping
    public ResponseEntity<List<PlayerResponseDto>> getAllPlayersByTeamId(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam Long teamId
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        List<PlayerResponseDto> playerResponseDtos = playerService.getAllByTeamId(pageable, teamId)
                .stream()
                .map(playerDtoMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(playerResponseDtos);
    }

    @Operation(description = "Create a new player")
    @PostMapping
    public ResponseEntity<PlayerResponseDto> createPlayer(@Valid @RequestBody PlayerRequestDto requestDto) {
        Player player = playerService.create(playerDtoMapper.toModel(requestDto));
        PlayerResponseDto responseDto = playerDtoMapper.toResponseDto(player);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(description = "Add an unassigned player to a team")
    @PostMapping("/{playerId}/add-to-team")
    public ResponseEntity<PlayerResponseDto> addUnassignedPlayerToTeam(@PathVariable Long playerId, @RequestParam Long teamId) {
        Player player = playerService.getById(playerId);
        Team team = teamService.getById(teamId);
        Player updatedPlayer = playerService.addUnassignedPlayerToTeam(player, team);
        PlayerResponseDto responseDto = playerDtoMapper.toResponseDto(updatedPlayer);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(description = "Transfer a player to a team")
    @PostMapping("/{playerId}/transfer")
    public ResponseEntity<PlayerResponseDto> transferPlayer(@PathVariable Long playerId, @RequestParam Long teamId) {
        Player player = playerService.getById(playerId);
        Team team = teamService.getById(teamId);
        Player updatedPlayer = playerService.transferPlayerToTeam(player, team);
        PlayerResponseDto responseDto = playerDtoMapper.toResponseDto(updatedPlayer);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(description = "Update player by ID")
    @PutMapping("/{id}")
    public ResponseEntity<PlayerResponseDto> updatePlayerById(@PathVariable Long id, @RequestBody @Valid PlayerRequestDto requestDto) {
        Player updatedPlayer = playerService.updateById(id, playerDtoMapper.toModel(requestDto));
        PlayerResponseDto responseDto = playerDtoMapper.toResponseDto(updatedPlayer);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(description = "Delete player by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayerById(@PathVariable Long id) {
        playerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
