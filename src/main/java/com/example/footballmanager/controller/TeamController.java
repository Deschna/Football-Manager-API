package com.example.footballmanager.controller;

import com.example.footballmanager.dto.mapper.DtoMapper;
import com.example.footballmanager.dto.request.TeamRequestDto;
import com.example.footballmanager.dto.response.TeamResponseDto;
import com.example.footballmanager.model.Team;
import com.example.footballmanager.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public TeamResponseDto getTeamById(@PathVariable Long id) {
        return teamDtoMapper.toResponseDto(teamService.getById(id));
    }

    @Operation(description = "Get all teams")
    @GetMapping("/all")
    public List<TeamResponseDto> getAllTeams(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return teamService.getAll(pageable)
                .stream()
                .map(teamDtoMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Operation(description = "Create a new team")
    @PostMapping
    public TeamResponseDto createTeam(@RequestBody @Valid TeamRequestDto requestDto) {
        return teamDtoMapper.toResponseDto(teamService.create(teamDtoMapper.toModel(requestDto)));
    }

    @Operation(description = "Update team by ID")
    @PutMapping("/{id}")
    public TeamResponseDto updateTeamById(@PathVariable Long id, @RequestBody @Valid TeamRequestDto requestDto) {
        return teamDtoMapper.toResponseDto(teamService.updateById(id, teamDtoMapper.toModel(requestDto)));
    }

    @Operation(description = "Delete team by ID")
    @DeleteMapping("/{id}")
    public void deleteTeamById(@PathVariable Long id) {
        teamService.deleteById(id);
    }
}
