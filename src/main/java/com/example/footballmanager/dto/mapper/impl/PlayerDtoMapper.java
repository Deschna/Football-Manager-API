package com.example.footballmanager.dto.mapper.impl;

import com.example.footballmanager.dto.mapper.DtoMapper;
import com.example.footballmanager.dto.request.PlayerRequestDto;
import com.example.footballmanager.dto.response.PlayerResponseDto;
import com.example.footballmanager.model.Player;
import com.example.footballmanager.service.TeamService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayerDtoMapper implements DtoMapper<Player, PlayerRequestDto, PlayerResponseDto> {
    private final TeamService teamService;

    @Override
    public Player toModel(PlayerRequestDto dto) {
        Player model = new Player();
        model.setFirstname(dto.getFirstname());
        model.setLastname(dto.getLastname());
        model.setBirthDate(dto.getBirthDate());
        model.setCareerStartDate(dto.getCareerStartDate());
        if (dto.getTeamId() != null) {
            model.setTeam(teamService.getById(dto.getTeamId()));
        }
        return model;
    }

    @Override
    public PlayerResponseDto toResponseDto(Player model) {
        PlayerResponseDto dto = new PlayerResponseDto();
        dto.setId(model.getId());
        dto.setFirstname(model.getFirstname());
        dto.setLastname(model.getLastname());
        dto.setBirthDate(model.getBirthDate());
        dto.setCareerStartDate(model.getCareerStartDate());
        if (model.getTeam() != null) {
            dto.setTeamId(model.getTeam().getId());
        }
        return dto;
    }
}
