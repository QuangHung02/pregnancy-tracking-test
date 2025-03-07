 # SWP391-Group4-Backend-Pregnancy-tracking-test
📖 1. Introduction
This guide provides instructions for setting up and running validation tests in a Spring Boot project. The tests focus on:

✅ Unit Testing - Ensuring validation logic works correctly.

✅ Field Validation Testing - Checking input constraints (e.g., @NotNull, @Size, @Pattern).

✅ Mock Testing (if applicable) - Using Mockito for service-layer validation.

⚙️ 2. System Requirements

Ensure you have the following installed before running tests:

Tool	Required Version

Java: 17 or higher

Maven: 3.8+

Spring Boot: 3.x

JUnit 5: Default with Spring Boot

Mockito: Optional (for mocking validation logic)

🛠 3. Environment Setup

🔹 1. Clone the repository

git clone https://github.com/QuangHung02/pregnancy-tracking-test.git

cd pregnancy-tracking-test

🔹 2. Install dependencies

mvn clean install

🚀 4. Running Tests

✅ 1. Run all tests

mvn test

✅ 2. Run only validation tests

If your tests are tagged with a specific group, you can run them separately:

mvn test -Dgroups=validation

📂 5. Test Directory Structure

/src/test/java/com/example/pregnancy_tracking/

  ├── controller/        # Controller layer tests
  
  │    ├── PregnancyControllerTest.java
  
  ├── service/           # Service layer tests
  
  │    ├── CreatePregnancyServiceTest.java
  
  │    ├── UpdatePregnancyServiceTest.java
  
📊 6. Test Examples

✅ Example: Testing Field Validation (DTO Validation Test)

A sample JUnit 5 + Spring Boot Validation Test for validating a PregnancyDTO:

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
    void createPregnancy_ShouldThrowException_WhenUserIdIsNull() {
        pregnancyDTO.setUserId(null);
        
        NullPointerException exception = assertThrows(NullPointerException.class, () -> pregnancyService.createPregnancy(pregnancyDTO));
        assertEquals("User ID cannot be null", exception.getMessage());
    }
}
🎯 8. Conclusion

This README provides a clear guide to setting up and running field validation tests in your Spring Boot project. If any issues arise, check the logs or reach out to the development team. 🚀
