package com.example.pregnancy_tracking.service;

import com.example.pregnancy_tracking.entity.MotherRecord;
import com.example.pregnancy_tracking.repository.MotherRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class CreateMotherRecordServiceTest {
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
        record.setMotherHeight(160.0);
        record.setMotherWeight(60.0);

        when(motherRecordRepository.save(any(MotherRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MotherRecord result = motherRecordService.createRecord(record);

        assertNotNull(result);
        assertEquals(23.44, result.getMotherBmi());
        verify(motherRecordRepository).save(result);
    }

    @Test
    void createRecord_ShouldNotCalculateBmi_WhenHeightIsNull() {
        record.setMotherHeight(null);
        record.setMotherWeight(60.0);

        when(motherRecordRepository.save(any(MotherRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MotherRecord result = motherRecordService.createRecord(record);

        assertNotNull(result);
        assertNull(result.getMotherBmi());
        verify(motherRecordRepository).save(result);
    }

    @Test
    void createRecord_ShouldNotCalculateBmi_WhenHeightIsZero() {
        record.setMotherHeight(0.0);
        record.setMotherWeight(60.0);

        when(motherRecordRepository.save(any(MotherRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MotherRecord result = motherRecordService.createRecord(record);

        assertNotNull(result);
        assertNull(result.getMotherBmi());
        verify(motherRecordRepository).save(result);
    }

    @Test
    void createRecord_ShouldNotCalculateBmi_WhenHeightIsNegative() {
        record.setMotherHeight(-160.0);
        record.setMotherWeight(60.0);

        when(motherRecordRepository.save(any(MotherRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MotherRecord result = motherRecordService.createRecord(record);

        assertNotNull(result);
        assertNull(result.getMotherBmi());
        verify(motherRecordRepository).save(result);
    }

    @Test
    void createRecord_ShouldNotCalculateBmi_WhenWeightIsNull() {
        record.setMotherHeight(160.0);
        record.setMotherWeight(null);

        when(motherRecordRepository.save(any(MotherRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MotherRecord result = motherRecordService.createRecord(record);

        assertNotNull(result);
        assertNull(result.getMotherBmi());
        verify(motherRecordRepository).save(result);
    }

    @Test
    void createRecord_ShouldNotCalculateBmi_WhenWeightIsZero() {
        record.setMotherHeight(160.0);
        record.setMotherWeight(0.0);

        when(motherRecordRepository.save(any(MotherRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MotherRecord result = motherRecordService.createRecord(record);

        assertNotNull(result);
        assertEquals(0.0, result.getMotherBmi());
        verify(motherRecordRepository).save(result);
    }

    @Test
    void createRecord_ShouldNotCalculateBmi_WhenWeightIsNegative() {
        record.setMotherHeight(160.0);
        record.setMotherWeight(-60.0);

        when(motherRecordRepository.save(any(MotherRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MotherRecord result = motherRecordService.createRecord(record);

        assertNotNull(result);
        assertEquals(-23.44, result.getMotherBmi());
        verify(motherRecordRepository).save(result);
    }
}
