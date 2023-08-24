package com.example.footballmanager.dto.request;

import com.example.footballmanager.validation.PastOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import lombok.Data;

@Data
public class PlayerRequestDto {
    @NotBlank(message = "First name is required")
    private String firstname;

    @NotBlank(message = "Last name is required")
    private String lastname;

    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    @NotNull(message = "Career start date is required")
    @PastOrPresent(message = "Career start date can't be in the future")
    private LocalDate careerStartDate;

    @Positive
    private Long teamId;
}
