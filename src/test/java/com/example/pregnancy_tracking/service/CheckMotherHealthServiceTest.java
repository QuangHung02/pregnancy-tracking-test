package com.example.pregnancy_tracking.service;

import com.example.pregnancy_tracking.entity.*;
import com.example.pregnancy_tracking.repository.MotherRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class CheckMotherHealthServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(CheckMotherHealthServiceTest.class);

    @Mock
    private MotherRecordRepository motherRecordRepository;

    @Mock
    private StandardService standardService;

    @Mock
    private ReminderService reminderService;

    @InjectMocks
    private MotherRecordService motherRecordService;

    private MotherRecord record;
    private MomStandard standard;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        record = new MotherRecord();
        record.setPregnancy(new Pregnancy());
        record.getPregnancy().setUser(new User());
        record.setWeek(20);
        record.setMotherBmi(25.0);

        standard = new MomStandard();
        standard.setMinBmi(18.5);
        standard.setMaxBmi(24.9);
    }

    @Test
    void checkMotherHealth_ShouldCreateHealthAlert_WhenBmiIsAboveStandard() {
        try {
            when(standardService.getMomStandard(20)).thenReturn(Optional.of(standard));
            record.setMotherBmi(26.0);
            motherRecordService.checkMotherHealth(record);
            verify(reminderService).createReminder(any(Reminder.class));
            verify(reminderService).createHealthAlert(any(ReminderHealthAlert.class));
        } catch (AssertionError e) {
            logger.error("Test failed: checkMotherHealth_ShouldCreateHealthAlert_WhenBmiIsAboveStandard", e);
            throw e;
        }
    }

    @Test
    void checkMotherHealth_ShouldCreateHealthAlert_WhenBmiIsBelowStandard() {
        try {
            when(standardService.getMomStandard(20)).thenReturn(Optional.of(standard));
            record.setMotherBmi(17.0);
            motherRecordService.checkMotherHealth(record);
            verify(reminderService).createReminder(any(Reminder.class));
            verify(reminderService).createHealthAlert(any(ReminderHealthAlert.class));
        } catch (AssertionError e) {
            logger.error("Test failed: checkMotherHealth_ShouldCreateHealthAlert_WhenBmiIsBelowStandard", e);
            throw e;
        }
    }

    @Test
    void checkMotherHealth_ShouldNotCreateHealthAlert_WhenBmiIsWithinStandard() {
        try {
            when(standardService.getMomStandard(20)).thenReturn(Optional.of(standard));
            record.setMotherBmi(22.0);
            motherRecordService.checkMotherHealth(record);
            verify(reminderService, never()).createReminder(any(Reminder.class));
            verify(reminderService, never()).createHealthAlert(any(ReminderHealthAlert.class));
        } catch (AssertionError e) {
            logger.error("Test failed: checkMotherHealth_ShouldNotCreateHealthAlert_WhenBmiIsWithinStandard", e);
            throw e;
        }
    }

    @Test
    void checkMotherHealth_ShouldNotCreateHealthAlert_WhenStandardIsNotFound() {
        try {
            when(standardService.getMomStandard(20)).thenReturn(Optional.empty());
            motherRecordService.checkMotherHealth(record);
            verify(reminderService, never()).createReminder(any(Reminder.class));
            verify(reminderService, never()).createHealthAlert(any(ReminderHealthAlert.class));
        } catch (AssertionError e) {
            logger.error("Test failed: checkMotherHealth_ShouldNotCreateHealthAlert_WhenStandardIsNotFound", e);
            throw e;
        }
    }

    @Test
    void checkMotherHealth_ShouldCreateHealthAlertWithCorrectDetails() {
        try {
            when(standardService.getMomStandard(20)).thenReturn(Optional.of(standard));
            record.setMotherBmi(26.0);
            motherRecordService.checkMotherHealth(record);
            verify(reminderService).createReminder(argThat(reminder ->
                    reminder.getType() == ReminderType.HEALTH_ALERT &&
                            reminder.getStatus() == ReminderStatus.NOT_YET
            ));
            verify(reminderService).createHealthAlert(argThat(alert ->
                    alert.getHealthType() == HealthType.HIGH_BMI &&
                            alert.getSeverity() == SeverityLevel.MODERATE &&
                            alert.getSource() == AlertSource.MOTHER_RECORDS
            ));
        } catch (AssertionError e) {
            logger.error("Test failed: checkMotherHealth_ShouldCreateHealthAlertWithCorrectDetails", e);
            throw e;
        }
    }

    @Test
    void checkMotherHealth_ShouldNotCreateHealthAlert_WhenBmiIsAtMinStandard() {
        try {
            when(standardService.getMomStandard(20)).thenReturn(Optional.of(standard));
            record.setMotherBmi(18.5);
            motherRecordService.checkMotherHealth(record);
            verify(reminderService, never()).createReminder(any(Reminder.class));
            verify(reminderService, never()).createHealthAlert(any(ReminderHealthAlert.class));
        } catch (AssertionError e) {
            logger.error("Test failed: checkMotherHealth_ShouldNotCreateHealthAlert_WhenBmiIsAtMinStandard", e);
            throw e;
        }
    }

    @Test
    void checkMotherHealth_ShouldNotCreateHealthAlert_WhenBmiIsAtMaxStandard() {
        try {
            when(standardService.getMomStandard(20)).thenReturn(Optional.of(standard));
            record.setMotherBmi(24.9);
            motherRecordService.checkMotherHealth(record);
            verify(reminderService, never()).createReminder(any(Reminder.class));
            verify(reminderService, never()).createHealthAlert(any(ReminderHealthAlert.class));
        } catch (AssertionError e) {
            logger.error("Test failed: checkMotherHealth_ShouldNotCreateHealthAlert_WhenBmiIsAtMaxStandard", e);
            throw e;
        }
    }

    @Test
    void checkMotherHealth_ShouldNotCreateHealthAlert_WhenBmiIsNullAndStandardNotFound() {
        try {
            when(standardService.getMomStandard(20)).thenReturn(Optional.empty());
            record.setMotherBmi(null);
            motherRecordService.checkMotherHealth(record);
            verify(reminderService, never()).createReminder(any(Reminder.class));
            verify(reminderService, never()).createHealthAlert(any(ReminderHealthAlert.class));
        } catch (AssertionError e) {
            logger.error("Test failed: checkMotherHealth_ShouldNotCreateHealthAlert_WhenBmiIsNullAndStandardNotFound", e);
            throw e;
        }
    }
}