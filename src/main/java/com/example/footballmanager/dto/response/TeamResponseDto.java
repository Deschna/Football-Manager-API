package com.example.footballmanager.dto.response;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class TeamResponseDto {
    private Long id;
    private String name;
    private BigDecimal playerTransferCommission;
    private BigDecimal budget;
}
