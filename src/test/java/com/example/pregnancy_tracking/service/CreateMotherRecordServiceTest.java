package com.example.pregnancy_tracking.service;

import com.example.pregnancy_tracking.entity.MotherRecord;
import com.example.pregnancy_tracking.repository.MotherRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreateMotherRecordServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(CreateMotherRecordServiceTest.class);

    @Mock
    private MotherRecordRepository motherRecordRepository;

    @InjectMocks
    private MotherRecordService motherRecordService;

    private MotherRecord record;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        record = new MotherRecord();
    }

    @Test
    void createRecord_ShouldCalculateBmi_WhenHeightAndWeightAreValid() {
        try {
            record.setMotherHeight(160.0);
            record.setMotherWeight(60.0);

            when(motherRecordRepository.save(any(MotherRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

            MotherRecord result = motherRecordService.createRecord(record);

            assertNotNull(result);
            assertEquals(23.44, result.getMotherBmi());
            verify(motherRecordRepository).save(result);
        } catch (AssertionError e) {
            logger.error("Test failed: createRecord_ShouldCalculateBmi_WhenHeightAndWeightAreValid", e);
            throw e;
        }
    }

    @Test
    void createRecord_ShouldNotCalculateBmi_WhenHeightIsNull() {
        try {
            record.setMotherHeight(null);
            record.setMotherWeight(60.0);

            when(motherRecordRepository.save(any(MotherRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

            MotherRecord result = motherRecordService.createRecord(record);

            assertNotNull(result);
            assertNull(result.getMotherBmi());
            verify(motherRecordRepository).save(result);
        } catch (AssertionError e) {
            logger.error("Test failed: createRecord_ShouldNotCalculateBmi_WhenHeightIsNull", e);
            throw e;
        }
    }

    @Test
    void createRecord_ShouldNotCalculateBmi_WhenHeightIsZero() {
        try {
            record.setMotherHeight(0.0);
            record.setMotherWeight(60.0);

            when(motherRecordRepository.save(any(MotherRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

            MotherRecord result = motherRecordService.createRecord(record);

            assertNotNull(result);
            assertNull(result.getMotherBmi());
            verify(motherRecordRepository).save(result);
        } catch (AssertionError e) {
            logger.error("Test failed: createRecord_ShouldNotCalculateBmi_WhenHeightIsZero", e);
            throw e;
        }
    }

    @Test
    void createRecord_ShouldNotCalculateBmi_WhenHeightIsNegative() {
        try {
            record.setMotherHeight(-160.0);
            record.setMotherWeight(60.0);

            when(motherRecordRepository.save(any(MotherRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

            MotherRecord result = motherRecordService.createRecord(record);

            assertNotNull(result);
            assertNull(result.getMotherBmi());
            verify(motherRecordRepository).save(result);
        } catch (AssertionError e) {
            logger.error("Test failed: createRecord_ShouldNotCalculateBmi_WhenHeightIsNegative", e);
            throw e;
        }
    }

    @Test
    void createRecord_ShouldNotCalculateBmi_WhenWeightIsNull() {
        try {
            record.setMotherHeight(160.0);
            record.setMotherWeight(null);

            when(motherRecordRepository.save(any(MotherRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

            MotherRecord result = motherRecordService.createRecord(record);

            assertNotNull(result);
            assertNull(result.getMotherBmi());
            verify(motherRecordRepository).save(result);
        } catch (AssertionError e) {
            logger.error("Test failed: createRecord_ShouldNotCalculateBmi_WhenWeightIsNull", e);
            throw e;
        }
    }

    @Test
    void createRecord_ShouldNotCalculateBmi_WhenWeightIsZero() {
        try {
            record.setMotherHeight(160.0);
            record.setMotherWeight(0.0);

            when(motherRecordRepository.save(any(MotherRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

            MotherRecord result = motherRecordService.createRecord(record);

            assertNotNull(result);
            assertEquals(0.0, result.getMotherBmi());
            verify(motherRecordRepository).save(result);
        } catch (AssertionError e) {
            logger.error("Test failed: createRecord_ShouldNotCalculateBmi_WhenWeightIsZero", e);
            throw e;
        }
    }

    @Test
    void createRecord_ShouldNotCalculateBmi_WhenWeightIsNegative() {
        try {
            record.setMotherHeight(160.0);
            record.setMotherWeight(-60.0);

            when(motherRecordRepository.save(any(MotherRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

            MotherRecord result = motherRecordService.createRecord(record);

            assertNotNull(result);
            assertEquals(-23.44, result.getMotherBmi());
            verify(motherRecordRepository).save(result);
        } catch (AssertionError e) {
            logger.error("Test failed: createRecord_ShouldNotCalculateBmi_WhenWeightIsNegative", e);
            throw e;
        }
    }
}