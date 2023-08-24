package com.example.footballmanager.dto.mapper.impl;

import com.example.footballmanager.dto.mapper.DtoMapper;
import com.example.footballmanager.dto.request.TeamRequestDto;
import com.example.footballmanager.dto.response.TeamResponseDto;
import com.example.footballmanager.model.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamDtoMapper implements DtoMapper<Team, TeamRequestDto, TeamResponseDto> {
    @Override
    public Team toModel(TeamRequestDto dto) {
        Team model = new Team();
        model.setName(dto.getName());
        model.setPlayerTransferCommission(dto.getPlayerTransferCommission());
        model.setBudget(dto.getBudget());
        return model;
    }

    @Override
    public TeamResponseDto toResponseDto(Team model) {
        TeamResponseDto dto = new TeamResponseDto();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setPlayerTransferCommission(model.getPlayerTransferCommission());
        dto.setBudget(model.getBudget());
        return dto;
    }
}
