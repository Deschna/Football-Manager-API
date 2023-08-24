package com.example.footballmanager.dto.request;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class PlayerRequestDto {
    @NotBlank(message = "First name is required")
    private String firstname;

    @NotBlank(message = "Last name is required")
    private String lastname;

    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthDate;

    @NotNull(message = "Career start date is required")
    @Past(message = "Career start date must be in the past")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate careerStartDate;

    @Positive
    private Long teamId;
}
