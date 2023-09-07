package com.example.footballmanager.dto.mapper;

import com.example.footballmanager.config.MapperConfig;
import com.example.footballmanager.dto.request.PlayerRequestDto;
import com.example.footballmanager.dto.response.PlayerResponseDto;
import com.example.footballmanager.model.Player;
import com.example.footballmanager.service.TeamService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = { TeamService.class })
public interface PlayerDtoMapper extends DtoMapper<Player, PlayerRequestDto, PlayerResponseDto> {
    @Mapping(source = "teamId", target = "team")
    Player toModel(PlayerRequestDto dto);

    @Mapping(source = "team.id", target = "teamId")
    PlayerResponseDto toResponseDto(Player model);
}
