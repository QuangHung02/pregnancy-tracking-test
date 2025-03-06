package com.example.pregnancy_tracking.service;

import com.example.pregnancy_tracking.entity.*;
import com.example.pregnancy_tracking.repository.MotherRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class CheckMotherHealthServiceTest {
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
        // Thiết lập tiêu chuẩn BMI
        when(standardService.getMomStandard(20)).thenReturn(Optional.of(standard));

        // Đặt BMI của mẹ cao hơn tiêu chuẩn
        record.setMotherBmi(26.0);
        motherRecordService.checkMotherHealth(record);

        // Kiểm tra xem nhắc nhở và cảnh báo sức khỏe có được tạo ra không
        verify(reminderService).createReminder(any(Reminder.class));
        verify(reminderService).createHealthAlert(any(ReminderHealthAlert.class));
    }

    @Test
    void checkMotherHealth_ShouldCreateHealthAlert_WhenBmiIsBelowStandard() {
        // Thiết lập tiêu chuẩn BMI
        when(standardService.getMomStandard(20)).thenReturn(Optional.of(standard));

        // Đặt BMI của mẹ thấp hơn tiêu chuẩn
        record.setMotherBmi(17.0);
        motherRecordService.checkMotherHealth(record);

        // Kiểm tra xem nhắc nhở và cảnh báo sức khỏe có được tạo ra không
        verify(reminderService).createReminder(any(Reminder.class));
        verify(reminderService).createHealthAlert(any(ReminderHealthAlert.class));
    }

    @Test
    void checkMotherHealth_ShouldNotCreateHealthAlert_WhenBmiIsWithinStandard() {
        // Thiết lập tiêu chuẩn BMI
        when(standardService.getMomStandard(20)).thenReturn(Optional.of(standard));

        // Đặt BMI của mẹ trong phạm vi tiêu chuẩn
        record.setMotherBmi(22.0);
        motherRecordService.checkMotherHealth(record);

        // Kiểm tra xem nhắc nhở và cảnh báo sức khỏe không được tạo ra
        verify(reminderService, never()).createReminder(any(Reminder.class));
        verify(reminderService, never()).createHealthAlert(any(ReminderHealthAlert.class));
    }

    @Test
    void checkMotherHealth_ShouldNotCreateHealthAlert_WhenStandardIsNotFound() {
        // Không tìm thấy tiêu chuẩn BMI
        when(standardService.getMomStandard(20)).thenReturn(Optional.empty());

        motherRecordService.checkMotherHealth(record);

        // Kiểm tra xem nhắc nhở và cảnh báo sức khỏe không được tạo ra
        verify(reminderService, never()).createReminder(any(Reminder.class));
        verify(reminderService, never()).createHealthAlert(any(ReminderHealthAlert.class));
    }

    @Test
    void checkMotherHealth_ShouldCreateHealthAlertWithCorrectDetails() {
        // Thiết lập tiêu chuẩn BMI
        when(standardService.getMomStandard(20)).thenReturn(Optional.of(standard));

        // Đặt BMI của mẹ cao hơn tiêu chuẩn
        record.setMotherBmi(26.0);
        motherRecordService.checkMotherHealth(record);

        // Kiểm tra xem nhắc nhở và cảnh báo sức khỏe có chi tiết chính xác không
        verify(reminderService).createReminder(argThat(reminder ->
                reminder.getType() == ReminderType.HEALTH_ALERT &&
                        reminder.getStatus() == ReminderStatus.NOT_YET
        ));
        verify(reminderService).createHealthAlert(argThat(alert ->
                alert.getHealthType() == HealthType.HIGH_BMI &&
                        alert.getSeverity() == SeverityLevel.MODERATE &&
                        alert.getSource() == AlertSource.MOTHER_RECORDS
        ));
    }


    @Test
    void checkMotherHealth_ShouldNotCreateHealthAlert_WhenBmiIsAtMinStandard() {
        when(standardService.getMomStandard(20)).thenReturn(Optional.of(standard));

        record.setMotherBmi(18.5);
        motherRecordService.checkMotherHealth(record);

        verify(reminderService, never()).createReminder(any(Reminder.class));
        verify(reminderService, never()).createHealthAlert(any(ReminderHealthAlert.class));
    }

    @Test
    void checkMotherHealth_ShouldNotCreateHealthAlert_WhenBmiIsAtMaxStandard() {
        when(standardService.getMomStandard(20)).thenReturn(Optional.of(standard));

        record.setMotherBmi(24.9);
        motherRecordService.checkMotherHealth(record);

        verify(reminderService, never()).createReminder(any(Reminder.class));
        verify(reminderService, never()).createHealthAlert(any(ReminderHealthAlert.class));
    }

    @Test
    void checkMotherHealth_ShouldNotCreateHealthAlert_WhenBmiIsNullAndStandardNotFound() {
        when(standardService.getMomStandard(20)).thenReturn(Optional.empty());

        record.setMotherBmi(null);
        motherRecordService.checkMotherHealth(record);

        verify(reminderService, never()).createReminder(any(Reminder.class));
        verify(reminderService, never()).createHealthAlert(any(ReminderHealthAlert.class));
    }
}


