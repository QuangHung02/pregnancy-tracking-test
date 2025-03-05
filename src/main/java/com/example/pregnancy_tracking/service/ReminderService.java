package com.example.pregnancy_tracking.service;

import com.example.pregnancy_tracking.entity.Reminder;
import com.example.pregnancy_tracking.entity.ReminderHealthAlert;
import com.example.pregnancy_tracking.repository.ReminderHealthAlertRepository;
import com.example.pregnancy_tracking.repository.ReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReminderService {
    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private ReminderHealthAlertRepository reminderHealthAlertRepository;

    public Reminder createReminder(Reminder reminder) {
        return reminderRepository.save(reminder);
    }

    public List<Reminder> getRemindersByUser(Long userId) {
        return reminderRepository.findByUserUserId(userId);
    }

    public ReminderHealthAlert createHealthAlert(ReminderHealthAlert alert) {
        return reminderHealthAlertRepository.save(alert);
    }
}
