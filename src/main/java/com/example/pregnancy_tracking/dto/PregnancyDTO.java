package com.example.pregnancy_tracking.dto;

import lombok.Data;
import java.time.LocalDate;

import jakarta.validation.constraints.*;

@Data
public class PregnancyDTO {
    @NotNull(message = "User ID cannot be null")
    private Long userId;
    @NotNull(message = "Exam date cannot be null")
    private LocalDate examDate;
    @Min(value = 0, message = "Gestational weeks must be at least 0")
    @Max(value = 42, message = "Gestational weeks cannot exceed 42")
    private Integer gestationalWeeks;
    @Min(value = 0, message = "Gestational days must be at least 0")
    @Max(value = 6, message = "Gestational days cannot exceed 6")
    private Integer gestationalDays;
}
