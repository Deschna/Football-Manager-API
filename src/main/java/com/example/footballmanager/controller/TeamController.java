package com.example.footballmanager.controller;

import com.example.footballmanager.dto.mapper.DtoMapper;
import com.example.footballmanager.dto.request.TeamRequestDto;
import com.example.footballmanager.dto.response.TeamResponseDto;
import com.example.footballmanager.model.Team;
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
@RequestMapping("/teams")
public class TeamController {
    private final TeamService teamService;
    private final DtoMapper<Team, TeamRequestDto, TeamResponseDto> teamDtoMapper;

    public TeamController(
            TeamService teamService,
            DtoMapper<Team, TeamRequestDto, TeamResponseDto> teamDtoMapper
    ) {
        this.teamService = teamService;
        this.teamDtoMapper = teamDtoMapper;
    }

    @Operation(description = "Get team by ID")
    @GetMapping("/{id}")
    public ResponseEntity<TeamResponseDto> getTeamById(@PathVariable Long id) {
        Team team = teamService.getById(id);
        if (team == null) {
            return ResponseEntity.notFound().build();
        }
        TeamResponseDto responseDto = teamDtoMapper.toResponseDto(team);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(description = "Get all teams")
    @GetMapping
    public ResponseEntity<List<TeamResponseDto>> getAllTeams(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        List<TeamResponseDto> teamResponseDtos = teamService.getAll(pageable)
                .stream()
                .map(teamDtoMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(teamResponseDtos);
    }

    @Operation(description = "Create a new team")
    @PostMapping
    public ResponseEntity<TeamResponseDto> createTeam(@RequestBody @Valid TeamRequestDto requestDto) {
        Team team = teamService.create(teamDtoMapper.toModel(requestDto));
        TeamResponseDto responseDto = teamDtoMapper.toResponseDto(team);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(description = "Update team by ID")
    @PutMapping("/{id}")
    public ResponseEntity<TeamResponseDto> updateTeamById(@PathVariable Long id, @RequestBody @Valid TeamRequestDto requestDto) {
        Team updatedTeam = teamService.updateById(id, teamDtoMapper.toModel(requestDto));
        TeamResponseDto responseDto = teamDtoMapper.toResponseDto(updatedTeam);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(description = "Delete team by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeamById(@PathVariable Long id) {
        teamService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
