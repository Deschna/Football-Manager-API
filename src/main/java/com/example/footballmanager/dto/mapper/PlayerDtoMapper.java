package com.example.footballmanager.dto.mapper;

import com.example.footballmanager.config.MapperConfig;
import com.example.footballmanager.dto.request.PlayerRequestDto;
import com.example.footballmanager.dto.response.PlayerResponseDto;
import com.example.footballmanager.model.Player;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface PlayerDtoMapper extends DtoMapper<Player, PlayerRequestDto, PlayerResponseDto> {
    Player toModel(PlayerRequestDto dto);

    PlayerResponseDto toResponseDto(Player model);
}
