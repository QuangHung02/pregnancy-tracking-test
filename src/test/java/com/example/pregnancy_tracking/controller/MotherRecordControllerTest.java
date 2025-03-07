package com.example.pregnancy_tracking.controller;

import com.example.pregnancy_tracking.entity.MotherRecord;
import com.example.pregnancy_tracking.entity.Pregnancy;
import com.example.pregnancy_tracking.service.MotherRecordService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

public class MotherRecordControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(MotherRecordControllerTest.class);

    @Test
    public void test_create_record_with_valid_data_returns_ok() {
        try {
            // Arrange
            Long pregnancyId = 1L;
            MotherRecord inputRecord = new MotherRecord();
            inputRecord.setWeek(12);
            inputRecord.setMotherWeight(65.0);
            inputRecord.setMotherHeight(165.0);

            MotherRecord savedRecord = new MotherRecord();
            savedRecord.setRecordId(1L);
            savedRecord.setWeek(12);
            savedRecord.setMotherWeight(65.0);
            savedRecord.setMotherHeight(165.0);
            savedRecord.setMotherBmi(23.88);

            MotherRecordService mockService = Mockito.mock(MotherRecordService.class);
            Mockito.when(mockService.createRecord(Mockito.any(MotherRecord.class))).thenReturn(savedRecord);

            MotherRecordController controller = new MotherRecordController();
            ReflectionTestUtils.setField(controller, "motherRecordService", mockService);

            // Act
            ResponseEntity<MotherRecord> response = controller.createRecord(pregnancyId, inputRecord);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1L, response.getBody().getRecordId());
            assertEquals(12, response.getBody().getWeek());
            assertEquals(65.0, response.getBody().getMotherWeight());
            assertEquals(165.0, response.getBody().getMotherHeight());
            assertEquals(23.88, response.getBody().getMotherBmi());

            Mockito.verify(mockService).createRecord(Mockito.any(MotherRecord.class));
        } catch (AssertionError e) {
            logger.error("Test failed: test_create_record_with_valid_data_returns_ok", e);
            throw e;
        }
    }

    @Test
    public void test_create_record_with_missing_fields() {
        try {
            // Arrange
            Long pregnancyId = 1L;
            MotherRecord inputRecord = new MotherRecord();
            // Missing week, weight and height

            MotherRecord savedRecord = new MotherRecord();
            savedRecord.setRecordId(1L);
            // Service should still save the record even with missing fields

            MotherRecordService mockService = Mockito.mock(MotherRecordService.class);
            Mockito.when(mockService.createRecord(Mockito.any(MotherRecord.class))).thenReturn(savedRecord);

            MotherRecordController controller = new MotherRecordController();
            ReflectionTestUtils.setField(controller, "motherRecordService", mockService);

            // Act
            ResponseEntity<MotherRecord> response = controller.createRecord(pregnancyId, inputRecord);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1L, response.getBody().getRecordId());
            assertNull(response.getBody().getWeek());
            assertNull(response.getBody().getMotherWeight());
            assertNull(response.getBody().getMotherHeight());
            assertNull(response.getBody().getMotherBmi());

            Mockito.verify(mockService).createRecord(Mockito.any(MotherRecord.class));
        } catch (AssertionError e) {
            logger.error("Test failed: test_create_record_with_missing_fields", e);
            throw e;
        }
    }

    @Test
    public void test_create_record_associates_pregnancy_id() {
        try {
            // Arrange
            Long pregnancyId = 1L;
            MotherRecord inputRecord = new MotherRecord();
            inputRecord.setWeek(12);
            inputRecord.setMotherWeight(65.0);
            inputRecord.setMotherHeight(165.0);
            Pregnancy pregnancy = new Pregnancy();
            pregnancy.setPregnancyId(pregnancyId);
            inputRecord.setPregnancy(pregnancy);

            MotherRecord savedRecord = new MotherRecord();
            savedRecord.setRecordId(1L);
            savedRecord.setWeek(12);
            savedRecord.setMotherWeight(65.0);
            savedRecord.setMotherHeight(165.0);
            savedRecord.setMotherBmi(23.88);
            savedRecord.setPregnancy(pregnancy);

            MotherRecordService mockService = Mockito.mock(MotherRecordService.class);
            Mockito.when(mockService.createRecord(Mockito.any(MotherRecord.class))).thenReturn(savedRecord);

            MotherRecordController controller = new MotherRecordController();
            ReflectionTestUtils.setField(controller, "motherRecordService", mockService);

            // Act
            ResponseEntity<MotherRecord> response = controller.createRecord(pregnancyId, inputRecord);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1L, response.getBody().getRecordId());
            assertEquals(12, response.getBody().getWeek());
            assertEquals(65.0, response.getBody().getMotherWeight());
            assertEquals(165.0, response.getBody().getMotherHeight());
            assertEquals(23.88, response.getBody().getMotherBmi());
            assertEquals(pregnancyId, response.getBody().getPregnancy().getPregnancyId());

            Mockito.verify(mockService).createRecord(Mockito.any(MotherRecord.class));
        } catch (AssertionError e) {
            logger.error("Test failed: test_create_record_associates_pregnancy_id", e);
            throw e;
        }
    }

    @Test
    public void test_create_record_with_non_existent_pregnancy_id() {
        try {
            // Arrange
            Long nonExistentPregnancyId = 999L;
            MotherRecord inputRecord = new MotherRecord();
            inputRecord.setWeek(12);
            inputRecord.setMotherWeight(65.0);
            inputRecord.setMotherHeight(165.0);
            inputRecord.setPregnancy(null); // Simulate non-existent pregnancy

            MotherRecordService mockService = Mockito.mock(MotherRecordService.class);
            Mockito.when(mockService.createRecord(Mockito.any(MotherRecord.class)))
                    .thenThrow(new IllegalArgumentException("Pregnancy ID does not exist"));

            MotherRecordController controller = new MotherRecordController();
            ReflectionTestUtils.setField(controller, "motherRecordService", mockService);

            // Act & Assert
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                controller.createRecord(nonExistentPregnancyId, inputRecord);
            });

            assertEquals("Pregnancy ID does not exist", exception.getMessage());
            Mockito.verify(mockService).createRecord(Mockito.any(MotherRecord.class));
        } catch (AssertionError e) {
            logger.error("Test failed: test_create_record_with_non_existent_pregnancy_id", e);
            throw e;
        }
    }

    @Test
    public void test_create_record_with_negative_or_zero_height_weight() {
        try {
            // Arrange
            Long pregnancyId = 1L;
            MotherRecord inputRecord = new MotherRecord();
            inputRecord.setWeek(12);
            inputRecord.setMotherWeight(0.0);
            inputRecord.setMotherHeight(-165.0);

            MotherRecord savedRecord = new MotherRecord();
            savedRecord.setRecordId(1L);
            savedRecord.setWeek(12);
            savedRecord.setMotherWeight(0.0);
            savedRecord.setMotherHeight(-165.0);
            savedRecord.setMotherBmi(null); // BMI should not be calculated

            MotherRecordService mockService = Mockito.mock(MotherRecordService.class);
            Mockito.when(mockService.createRecord(Mockito.any(MotherRecord.class))).thenReturn(savedRecord);

            MotherRecordController controller = new MotherRecordController();
            ReflectionTestUtils.setField(controller, "motherRecordService", mockService);

            // Act
            ResponseEntity<MotherRecord> response = controller.createRecord(pregnancyId, inputRecord);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1L, response.getBody().getRecordId());
            assertEquals(12, response.getBody().getWeek());
            assertEquals(0.0, response.getBody().getMotherWeight());
            assertEquals(-165.0, response.getBody().getMotherHeight());
            assertNull(response.getBody().getMotherBmi());

            Mockito.verify(mockService).createRecord(Mockito.any(MotherRecord.class));
        } catch (AssertionError e) {
            logger.error("Test failed: test_create_record_with_negative_or_zero_height_weight", e);
            throw e;
        }
    }

    @Test
    public void test_create_record_with_extremely_large_values() {
        try {
            // Arrange
            Long pregnancyId = 1L;
            MotherRecord inputRecord = new MotherRecord();
            inputRecord.setWeek(12);
            inputRecord.setMotherWeight(1000.0); // Extremely large weight
            inputRecord.setMotherHeight(300.0); // Extremely large height

            MotherRecord savedRecord = new MotherRecord();
            savedRecord.setRecordId(1L);
            savedRecord.setWeek(12);
            savedRecord.setMotherWeight(1000.0);
            savedRecord.setMotherHeight(300.0);
            double heightInMeters = 300.0 / 100.0;
            double bmi = 1000.0 / (heightInMeters * heightInMeters);
            savedRecord.setMotherBmi(Math.round(bmi * 100.0) / 100.0);

            MotherRecordService mockService = Mockito.mock(MotherRecordService.class);
            Mockito.when(mockService.createRecord(Mockito.any(MotherRecord.class))).thenReturn(savedRecord);

            MotherRecordController controller = new MotherRecordController();
            ReflectionTestUtils.setField(controller, "motherRecordService", mockService);

            // Act
            ResponseEntity<MotherRecord> response = controller.createRecord(pregnancyId, inputRecord);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1L, response.getBody().getRecordId());
            assertEquals(12, response.getBody().getWeek());
            assertEquals(1000.0, response.getBody().getMotherWeight());
            assertEquals(300.0, response.getBody().getMotherHeight());
            assertEquals(Math.round(bmi * 100.0) / 100.0, response.getBody().getMotherBmi());

            Mockito.verify(mockService).createRecord(Mockito.any(MotherRecord.class));
        } catch (AssertionError e) {
            logger.error("Test failed: test_create_record_with_extremely_large_values", e);
            throw e;
        }
    }

    @Test
    public void test_concurrent_create_record_requests() throws InterruptedException {
        try {
            // Arrange
            Long pregnancyId = 1L;
            MotherRecord inputRecord1 = new MotherRecord();
            inputRecord1.setWeek(12);
            inputRecord1.setMotherWeight(65.0);
            inputRecord1.setMotherHeight(165.0);

            MotherRecord inputRecord2 = new MotherRecord();
            inputRecord2.setWeek(13);
            inputRecord2.setMotherWeight(66.0);
            inputRecord2.setMotherHeight(166.0);

            MotherRecord savedRecord1 = new MotherRecord();
            savedRecord1.setRecordId(1L);
            savedRecord1.setWeek(12);
            savedRecord1.setMotherWeight(65.0);
            savedRecord1.setMotherHeight(165.0);
            savedRecord1.setMotherBmi(23.88);

            MotherRecord savedRecord2 = new MotherRecord();
            savedRecord2.setRecordId(2L);
            savedRecord2.setWeek(13);
            savedRecord2.setMotherWeight(66.0);
            savedRecord2.setMotherHeight(166.0);
            savedRecord2.setMotherBmi(23.94);

            MotherRecordService mockService = Mockito.mock(MotherRecordService.class);
            Mockito.when(mockService.createRecord(Mockito.any(MotherRecord.class)))
                    .thenReturn(savedRecord1)
                    .thenReturn(savedRecord2);

            MotherRecordController controller = new MotherRecordController();
            ReflectionTestUtils.setField(controller, "motherRecordService", mockService);

            // Act
            Runnable task1 = () -> controller.createRecord(pregnancyId, inputRecord1);
            Runnable task2 = () -> controller.createRecord(pregnancyId, inputRecord2);

            Thread thread1 = new Thread(task1);
            Thread thread2 = new Thread(task2);

            thread1.start();
            thread2.start();

            thread1.join();
            thread2.join();

            // Assert
            Mockito.verify(mockService, Mockito.times(2)).createRecord(Mockito.any(MotherRecord.class));
        } catch (AssertionError e) {
            logger.error("Test failed: test_concurrent_create_record_requests", e);
            throw e;
        }
    }
}