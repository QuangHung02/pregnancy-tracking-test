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

📂 5. Test Directory Structure

/src/test/java/com/example/pregnancy_tracking/

  ├── controller/        # Controller layer tests
  
  │    ├── PregnancyControllerTest.java

  │    ├── MotherRecordControllerTest.java

  
  ├── service/           # Service layer tests
  
  │    ├── CreatePregnancyServiceTest.java
  
  │    ├── UpdatePregnancyServiceTest.java

  │    ├── CheckMotherHealthServiceTest.java

  │    ├── CreateMotherRecordServiceTest.java
  
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

🛠 7. Debugging & Troubleshooting
Common Issues:

javax.validation.ConstraintViolationException

No validator could be found

Solution:

Check field constraints (@NotNull, @Size)

Ensure spring-boot-starter-validation is included in pom.xml

🎯 8. Conclusion

This README provides a clear guide to setting up and running field validation tests in your Spring Boot project. If any issues arise, check the logs or reach out to the development team. 🚀

🚀 9. Test Coverage Explanation for CreatePregnancyServiceTest

Overview

The CreatePregnancyServiceTest class provides comprehensive unit testing for the PregnancyService#createPregnancy method. The test cases validate business logic, input constraints, exception handling, and repository interactions, ensuring that the pregnancy creation process works as expected under various conditions.

Coverage Breakdown

1. Successful Pregnancy Creation

Test: createPregnancy_ShouldCreateAndReturnPregnancy

Scenario: Verifies that a new pregnancy is correctly created when valid input is provided.

Assertions:

The pregnancy object is successfully created and linked to the user.

The startDate is correctly calculated from the examDate and gestational age.

The dueDate is accurately computed (280 days from startDate).

Default values such as status = ONGOING, timestamps (createdAt, lastUpdatedAt) are correctly set.

The user repository is updated to reflect the pregnancy count.

The pregnancy is successfully persisted in the repository.

2. User Validation Scenarios

Test: createPregnancy_ShouldThrowException_WhenUserNotFound

Scenario: Ensures that an exception is thrown when the provided userId does not exist in the repository.

Assertions:

A RuntimeException with message "User not found" is thrown.

No interactions with pregnancyRepository occur.

Test: createPregnancy_ShouldThrowException_WhenUserIdIsNull

Scenario: Ensures that a NullPointerException is thrown when userId is null.

Assertions:

The exception message is "User ID cannot be null".

No repository operations are performed.

3. Input Validation on Pregnancy Details

Test: createPregnancy_ShouldThrowException_WhenExamDateIsNull

Scenario: Ensures that an exception is thrown when examDate is null.

Assertions:

A NullPointerException with message "Exam date cannot be null" is thrown.

Test: createPregnancy_ShouldThrowException_WhenGestationalWeeksIsNegative

Scenario: Ensures that a negative gestational week value is rejected.

Assertions:

An IllegalArgumentException with message "Gestational weeks must be >= 0" is thrown.

Test: createPregnancy_ShouldThrowException_WhenGestationalWeeksIsTooLarge

Scenario: Ensures that gestational weeks greater than the maximum realistic threshold (42 weeks) are rejected.

Assertions:

An IllegalArgumentException with message "Gestational weeks must be realistic (0-42)" is thrown.

Test: createPregnancy_ShouldThrowException_WhenGestationalDaysIsNegative

Scenario: Ensures that gestational days cannot be negative.

Assertions:

An IllegalArgumentException with message "Gestational days must be between 0 and 6" is thrown.

Test: createPregnancy_ShouldThrowException_WhenGestationalDaysIsTooLarge

Scenario: Ensures that gestational days cannot exceed 6 (as a week has 7 days).

Assertions:

An IllegalArgumentException with message "Gestational days must be between 0 and 6" is thrown.

4. Repository Save Failures

Test: createPregnancy_ShouldThrowException_WhenUserSaveFails

Scenario: Ensures that failure in updating the user repository results in a proper exception.

Assertions:

A RuntimeException with message "Failed to save user" is thrown.

The pregnancy repository is not called.

Test: createPregnancy_ShouldThrowException_WhenPregnancySaveFails

Scenario: Ensures that failure in persisting the pregnancy entity results in an appropriate exception.

Assertions:

A RuntimeException with message "Failed to save pregnancy" is thrown.

The user repository remains unaffected.

5. Conclusion

This test suite achieves high coverage of the createPregnancy method by handling all possible input conditions, boundary values, and failure scenarios. The tests ensure that:

Valid pregnancies are created correctly.

Input constraints are properly enforced.

Repository interactions behave as expected.

Exceptions are thrown when necessary, preventing incorrect data manipulation.

With this level of coverage, the createPregnancy functionality is well-tested and robust against edge cases and erroneous inputs.

🚀 10. Test Coverage Explanation for UpdatePregnancyServiceTest

1. Overview

The UpdatePregnancyServiceTest class provides comprehensive test coverage for the updatePregnancy method in PregnancyService. The test cases validate both positive and negative scenarios, ensuring that the method behaves correctly in different situations.

2. Tested Scenarios

✅ Successful Pregnancy Update

updatePregnancy_ShouldUpdateSuccessfully(): Ensures that when a valid pregnancy record is found, and valid data is provided, the update operation successfully updates the pregnancy details and saves them to the repository.

❌ Error Handling and Edge Cases

Pregnancy Not Found

updatePregnancy_ShouldThrowException_WhenPregnancyNotFound(): Validates that an exception is thrown when the specified pregnancy ID does not exist in the repository.

Null Inputs

updatePregnancy_ShouldThrowException_WhenPregnancyDTOIsNull(): Ensures that passing a null DTO results in a NullPointerException.

updatePregnancy_ShouldThrowException_WhenExamDateIsNull(): Ensures that a missing examDate in the DTO results in a NullPointerException.

Invalid Gestational Weeks

updatePregnancy_ShouldThrowException_WhenGestationalWeeksIsNegative(): Validates that a negative value for gestationalWeeks triggers an IllegalArgumentException.

updatePregnancy_ShouldThrowException_WhenGestationalWeeksExceedsLimit(): Ensures that a value greater than the realistic limit (42 weeks) results in an IllegalArgumentException.

Invalid Gestational Days

updatePregnancy_ShouldThrowException_WhenGestationalDaysIsNegative(): Ensures that negative gestational days are not allowed.

updatePregnancy_ShouldThrowException_WhenGestationalDaysExceedsLimit(): Ensures that gestational days greater than 6 (since a week only has 7 days) result in an exception.

Boundary Cases for Gestational Weeks

updatePregnancy_ShouldHandleGestationalWeeksBoundary_WhenZero(): Ensures the service correctly handles the minimum value (0 weeks).

updatePregnancy_ShouldHandleGestationalWeeksBoundary_WhenFortyOne(): Ensures the service correctly handles a valid upper boundary value (41 weeks).

Edge Case for Exam Date

updatePregnancy_ShouldHandleCurrentDateAsExamDate(): Tests if the service correctly updates the pregnancy when the current date is used as the examDate.

Repository Save Failures

updatePregnancy_ShouldThrowException_WhenSaveFails(): Ensures the service throws an exception when saving the updated pregnancy fails.

3. Test Coverage Summary

The test suite effectively covers:

✅ Valid updates

✅ Missing or invalid inputs

✅ Edge cases (gestational weeks/days, current date usage)

✅ Repository failures

This ensures robustness in handling pregnancy updates and proper exception handling in different failure scenarios.

🚀 11. Test Coverage Explanation for PregnancyControllerTest

Overview

The test suite for PregnancyController aims to cover various scenarios for the createPregnancy and updatePregnancy endpoints. The tests validate the expected behavior of these endpoints under normal, erroneous, and edge-case conditions.

Test Coverage Analysis

1. createPregnancy Endpoint Coverage

Valid Request (createPregnancy_ShouldReturn200_WhenValidRequest)

Ensures that when a valid PregnancyDTO is provided, the controller correctly processes the request and returns 200 OK with the expected response.

Uses a valid JWT token to simulate authentication.

Mocks pregnancyService.createPregnancy to return a pregnancy object.

Invalid Data (createPregnancy_ShouldReturn400_WhenInvalidData)

Tests various invalid input scenarios, including:

userId is null.

examDate is null.

gestationalWeeks is negative.

gestationalDays exceeds a valid range.

Verifies that the controller responds with 400 Bad Request.

Missing Request Body (createPregnancy_ShouldReturn400_WhenRequestBodyIsNull)

Ensures the API returns 400 Bad Request when an empty JSON object {} is sent in the request body.

2. updatePregnancy Endpoint Coverage

Valid Update Request (updatePregnancy_ShouldReturn200_WhenValidRequest)

Tests the successful update of an existing pregnancy record.

Mocks pregnancyService.updatePregnancy to return an updated pregnancy object.

Validates that the response contains updated values for gestationalWeeks and gestationalDays.

Invalid Data (updatePregnancy_ShouldReturn400_WhenInvalidData)

Ensures that invalid input data (such as null values, negative weeks, and excessive days) results in 400 Bad Request.

Non-Existent Pregnancy (updatePregnancy_ShouldReturn404_WhenPregnancyNotFound)

Simulates a case where the requested pregnancy ID does not exist.

Mocks pregnancyService.updatePregnancy to throw a ResponseStatusException with 404 Not Found.

Confirms that the API returns the expected 404 response.

Test Coverage Summary

The test suite effectively covers the following aspects:

Success scenarios (Valid requests return expected responses).

Error handling (Bad requests, missing data, and non-existent resources).

Security enforcement (Authentication via @WithMockUser).

Possible Improvements

Add test cases for authorization failures: Currently, all tests assume a valid authenticated user. A test should be added to check unauthorized access (e.g., requests without a valid JWT token should return 401 Unauthorized).

Expand validation checks: More edge cases for gestationalWeeks and gestationalDays could be tested to ensure robustness.

With the existing test cases, the PregnancyController achieves high test coverage, ensuring that both happy paths and edge cases are well-handled.
