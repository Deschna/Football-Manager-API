package com.example.footballmanager.dto.response;

import java.time.LocalDate;
import lombok.Data;

@Data
public class PlayerResponseDto {
    private Long id;
    private String firstname;
    private String lastname;
    private LocalDate birthDate;
    private LocalDate careerStartDate;
    private Long teamId;
}
