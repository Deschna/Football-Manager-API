package com.example.footballmanager.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class TeamRequestDto {
    @NotBlank(message = "Team name is required")
    private String name;

    @NotNull(message = "Player transfer commission is required")
    @DecimalMin(value = "0.0", message = "Player transfer commission must be greater than or equal to 0")
    @DecimalMax(value = "10.0", message = "Player transfer commission must be less than or equal to 10")
    private BigDecimal playerTransferCommission;

    @NotNull(message = "Budget is required")
    @DecimalMin(value = "0.0", message = "Budget must be greater than or equal to 0")
    @DecimalMax(value = "9999999999999.99", message = "The maximum allowable budget is 9999999999999.99")
    private BigDecimal budget;
}
