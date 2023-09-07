package com.example.footballmanager.dto.mapper;

import com.example.footballmanager.config.MapperConfig;
import com.example.footballmanager.dto.request.TeamRequestDto;
import com.example.footballmanager.dto.response.TeamResponseDto;
import com.example.footballmanager.model.Team;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface TeamDtoMapper extends DtoMapper<Team, TeamRequestDto, TeamResponseDto>{
    Team toModel(TeamRequestDto dto);

    TeamResponseDto toResponseDto(Team model);
}
