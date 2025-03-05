package com.example.pregnancy_tracking.service;

import com.example.pregnancy_tracking.dto.PregnancyDTO;
import com.example.pregnancy_tracking.entity.Pregnancy;
import com.example.pregnancy_tracking.entity.PregnancyStatus;
import com.example.pregnancy_tracking.entity.User;
import com.example.pregnancy_tracking.repository.PregnancyRepository;
import com.example.pregnancy_tracking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreatePregnancyServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PregnancyRepository pregnancyRepository;
    
    @InjectMocks
    private PregnancyService pregnancyService;

    private User mockUser;
    private PregnancyDTO pregnancyDTO;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setTotalPregnancies(2);

        pregnancyDTO = new PregnancyDTO();
        pregnancyDTO.setUserId(1L);
        pregnancyDTO.setExamDate(LocalDate.of(2024, 3, 1));
        pregnancyDTO.setGestationalWeeks(10);
        pregnancyDTO.setGestationalDays(5);
    }

    @Test
    void createPregnancy_ShouldCreateAndReturnPregnancy() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(pregnancyRepository.save(any(Pregnancy.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        Pregnancy result = pregnancyService.createPregnancy(pregnancyDTO);
        System.out.println("Created Pregnancy: " + result);
        assertNotNull(result);
        assertEquals(mockUser, result.getUser());
        assertEquals(LocalDate.of(2023, 12, 17), result.getStartDate()); // 10 tuần 5 ngày trước ngày khám
        assertEquals(LocalDate.of(2024, 9, 22), result.getDueDate()); // 280 ngày từ ngày bắt đầu
        assertEquals(10, result.getGestationalWeeks());
        assertEquals(5, result.getGestationalDays());
        assertEquals(PregnancyStatus.ONGOING, result.getStatus());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getLastUpdatedAt());

        verify(userRepository).save(mockUser);
        verify(pregnancyRepository).save(any(Pregnancy.class));
    }

    @Test
    void createPregnancy_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> pregnancyService.createPregnancy(pregnancyDTO));
        assertEquals("User not found", exception.getMessage());
        
        verify(userRepository, never()).save(any(User.class));
        verify(pregnancyRepository, never()).save(any(Pregnancy.class));
    }

    @Test
    void createPregnancy_ShouldThrowException_WhenUserIdIsNull() {
        pregnancyDTO.setUserId(null);
        
        NullPointerException exception = assertThrows(NullPointerException.class, () -> pregnancyService.createPregnancy(pregnancyDTO));
        assertEquals("User ID cannot be null", exception.getMessage());
    }

    @Test
    void createPregnancy_ShouldThrowException_WhenExamDateIsNull() {
        pregnancyDTO.setExamDate(null);
        
        NullPointerException exception = assertThrows(NullPointerException.class, () -> pregnancyService.createPregnancy(pregnancyDTO));
        assertEquals("Exam date cannot be null", exception.getMessage());
    }

    @Test
    void createPregnancy_ShouldThrowException_WhenGestationalWeeksIsNegative() {
        pregnancyDTO.setGestationalWeeks(-1);
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> pregnancyService.createPregnancy(pregnancyDTO));
        assertEquals("Gestational weeks must be >= 0", exception.getMessage());
    }

    @Test
    void createPregnancy_ShouldThrowException_WhenGestationalWeeksIsTooLarge() {
        pregnancyDTO.setGestationalWeeks(50);
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> pregnancyService.createPregnancy(pregnancyDTO));
        assertEquals("Gestational weeks must be realistic (0-42)", exception.getMessage());
    }

    @Test
    void createPregnancy_ShouldThrowException_WhenGestationalDaysIsNegative() {
        pregnancyDTO.setGestationalDays(-1);
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> pregnancyService.createPregnancy(pregnancyDTO));
        assertEquals("Gestational days must be between 0 and 6", exception.getMessage());
    }

    @Test
    void createPregnancy_ShouldThrowException_WhenGestationalDaysIsTooLarge() {
        pregnancyDTO.setGestationalDays(7);
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> pregnancyService.createPregnancy(pregnancyDTO));
        assertEquals("Gestational days must be between 0 and 6", exception.getMessage());
    }

    @Test
    void createPregnancy_ShouldThrowException_WhenUserSaveFails() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        doThrow(new RuntimeException("Failed to save user")).when(userRepository).save(mockUser);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> pregnancyService.createPregnancy(pregnancyDTO));
        assertEquals("Failed to save user", exception.getMessage());
    }

    @Test
    void createPregnancy_ShouldThrowException_WhenPregnancySaveFails() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(pregnancyRepository.save(any(Pregnancy.class))).thenThrow(new RuntimeException("Failed to save pregnancy"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> pregnancyService.createPregnancy(pregnancyDTO));
        assertEquals("Failed to save pregnancy", exception.getMessage());
    }
}
