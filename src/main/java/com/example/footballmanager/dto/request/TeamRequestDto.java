package com.example.footballmanager.dto.request;

import java.math.BigDecimal;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TeamRequestDto {
    @NotBlank(message = "Team name is required")
    private String name;

    @NotNull(message = "Player transfer commission is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Player transfer commission must be greater than 0")
    @DecimalMax(value = "100.0", inclusive = false, message = "Player transfer commission must be less than 100")
    private BigDecimal playerTransferCommission;

    @NotNull(message = "Budget is required")
    @DecimalMin(value = "0.0", message = "Budget must be greater than or equal to 0")
    private BigDecimal budget;
}
