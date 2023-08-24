package com.example.footballmanager.dto.mapper;

public interface DtoMapper <M, Q, S>{
    M toModel(Q dto);

    S toResponseDto(M model);
}
