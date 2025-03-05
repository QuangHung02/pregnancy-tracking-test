package com.example.pregnancy_tracking.service;

import com.example.pregnancy_tracking.dto.PregnancyDTO;
import com.example.pregnancy_tracking.entity.Pregnancy;
import com.example.pregnancy_tracking.repository.PregnancyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdatePregnancyServiceTest {

    @Mock
    private PregnancyRepository pregnancyRepository;

    @InjectMocks
    private PregnancyService pregnancyService;

    private Pregnancy mockPregnancy;
    private PregnancyDTO pregnancyDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Pregnancy existingPregnancy = new Pregnancy();
        existingPregnancy.setPregnancyId(1L);

        when(pregnancyRepository.findById(1L)).thenReturn(Optional.of(existingPregnancy));
        mockPregnancy = new Pregnancy();
        mockPregnancy.setPregnancyId(1L);
        mockPregnancy.setExamDate(LocalDate.of(2024, 3, 1));
        mockPregnancy.setGestationalWeeks(10);
        mockPregnancy.setGestationalDays(5);
        mockPregnancy.setStartDate(LocalDate.of(2023, 12, 17));
        mockPregnancy.setDueDate(LocalDate.of(2024, 8, 22));
        mockPregnancy.setLastUpdatedAt(LocalDateTime.now());

        pregnancyDTO = new PregnancyDTO();
        pregnancyDTO.setExamDate(LocalDate.of(2024, 5, 1));
        pregnancyDTO.setGestationalWeeks(12);
        pregnancyDTO.setGestationalDays(3);
    }

    @Test
    void updatePregnancy_ShouldUpdateSuccessfully() {
        when(pregnancyRepository.findById(1L)).thenReturn(Optional.of(mockPregnancy));
        when(pregnancyRepository.save(any(Pregnancy.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pregnancy result = pregnancyService.updatePregnancy(1L, pregnancyDTO);

        assertNotNull(result);
        assertEquals(LocalDate.of(2024, 5, 1), result.getExamDate());
        assertEquals(LocalDate.of(2024, 2, 4), result.getStartDate());
        assertEquals(LocalDate.of(2024, 11, 10), result.getDueDate());
        assertEquals(12, result.getGestationalWeeks());
        assertEquals(3, result.getGestationalDays());
        assertNotNull(result.getLastUpdatedAt());

        verify(pregnancyRepository).save(result);
    }

    @Test
    void updatePregnancy_ShouldThrowException_WhenPregnancyNotFound() {
        when(pregnancyRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> pregnancyService.updatePregnancy(1L, pregnancyDTO));
        assertEquals("Pregnancy not found", exception.getMessage());

        verify(pregnancyRepository, never()).save(any(Pregnancy.class));
    }

    @Test
    void updatePregnancy_ShouldThrowException_WhenPregnancyDTOIsNull() {
        assertThrows(NullPointerException.class, () -> pregnancyService.updatePregnancy(1L, null));
    }

    @Test
    void updatePregnancy_ShouldThrowException_WhenExamDateIsNull() {
        pregnancyDTO.setExamDate(null);
        assertThrows(NullPointerException.class, () -> pregnancyService.updatePregnancy(1L, pregnancyDTO));
    }

    @Test
    void updatePregnancy_ShouldThrowException_WhenGestationalWeeksIsNegative() {
        pregnancyDTO.setGestationalWeeks(-1);
        assertThrows(IllegalArgumentException.class, () -> pregnancyService.updatePregnancy(1L, pregnancyDTO));
    }

    @Test
    void updatePregnancy_ShouldThrowException_WhenGestationalWeeksExceedsLimit() {
        pregnancyDTO.setGestationalWeeks(43);
        assertThrows(IllegalArgumentException.class, () -> pregnancyService.updatePregnancy(1L, pregnancyDTO));
    }

    @Test
    void updatePregnancy_ShouldThrowException_WhenGestationalDaysIsNegative() {
        pregnancyDTO.setGestationalDays(-1);
        assertThrows(IllegalArgumentException.class, () -> pregnancyService.updatePregnancy(1L, pregnancyDTO));
    }

    @Test
    void updatePregnancy_ShouldThrowException_WhenGestationalDaysExceedsLimit() {
        pregnancyDTO.setGestationalDays(7);
        assertThrows(IllegalArgumentException.class, () -> pregnancyService.updatePregnancy(1L, pregnancyDTO));
    }

    @Test
    void updatePregnancy_ShouldHandleGestationalWeeksBoundary_WhenZero() {
        pregnancyDTO.setGestationalWeeks(0);
        when(pregnancyRepository.findById(1L)).thenReturn(Optional.of(mockPregnancy));
        when(pregnancyRepository.save(any(Pregnancy.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pregnancy result = pregnancyService.updatePregnancy(1L, pregnancyDTO);
        assertEquals(0, result.getGestationalWeeks());
    }

    @Test
    void updatePregnancy_ShouldHandleGestationalWeeksBoundary_WhenFortyOne() {
        pregnancyDTO.setGestationalWeeks(41);
        when(pregnancyRepository.findById(1L)).thenReturn(Optional.of(mockPregnancy));
        when(pregnancyRepository.save(any(Pregnancy.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pregnancy result = pregnancyService.updatePregnancy(1L, pregnancyDTO);
        assertEquals(41, result.getGestationalWeeks());
    }

    @Test
    void updatePregnancy_ShouldHandleCurrentDateAsExamDate() {
        pregnancyDTO.setExamDate(LocalDate.now());
        when(pregnancyRepository.findById(1L)).thenReturn(Optional.of(mockPregnancy));
        when(pregnancyRepository.save(any(Pregnancy.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pregnancy result = pregnancyService.updatePregnancy(1L, pregnancyDTO);
        assertEquals(LocalDate.now(), result.getExamDate());
    }

    @Test
    void updatePregnancy_ShouldThrowException_WhenSaveFails() {
        when(pregnancyRepository.findById(1L)).thenReturn(Optional.of(mockPregnancy));
        when(pregnancyRepository.save(any(Pregnancy.class)))
                .thenThrow(new RuntimeException("Failed to save pregnancy"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> pregnancyService.updatePregnancy(1L, pregnancyDTO));
        assertEquals("Failed to save pregnancy", exception.getMessage());
    }
}
