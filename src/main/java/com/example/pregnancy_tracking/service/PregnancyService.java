package com.example.pregnancy_tracking.service;

import com.example.pregnancy_tracking.dto.PregnancyDTO;
import com.example.pregnancy_tracking.entity.*;
import com.example.pregnancy_tracking.repository.FetusRepository;
import com.example.pregnancy_tracking.repository.PregnancyRecordRepository;
import com.example.pregnancy_tracking.repository.PregnancyRepository;
import com.example.pregnancy_tracking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PregnancyService {
    @Autowired
    private PregnancyRepository pregnancyRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PregnancyRecordService pregnancyRecordService;

    public Pregnancy createPregnancy(PregnancyDTO pregnancyDTO) {
        if (pregnancyDTO.getUserId() == null) {
            throw new NullPointerException("User ID cannot be null");
        }
        if (pregnancyDTO.getExamDate() == null) {
            throw new NullPointerException("Exam date cannot be null");
        }
        if (pregnancyDTO.getGestationalWeeks() < 0) {
            throw new IllegalArgumentException("Gestational weeks must be >= 0");
        }
        if (pregnancyDTO.getGestationalWeeks() > 42) {
            throw new IllegalArgumentException("Gestational weeks must be realistic (0-42)");
        }
        if (pregnancyDTO.getGestationalDays() < 0 || pregnancyDTO.getGestationalDays() > 6) {
            throw new IllegalArgumentException("Gestational days must be between 0 and 6");
        }
        User user = userRepository.findById(pregnancyDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDate examDate = pregnancyDTO.getExamDate();
        int totalDays = (pregnancyDTO.getGestationalWeeks() * 7) + pregnancyDTO.getGestationalDays();

        LocalDate startDate = examDate.minusDays(totalDays);
        LocalDate dueDate = startDate.plusDays(280);

        user.setTotalPregnancies(user.getTotalPregnancies() + 1);
        userRepository.save(user);

        Pregnancy pregnancy = new Pregnancy();
        pregnancy.setUser(user);
        pregnancy.setStartDate(startDate);
        pregnancy.setDueDate(dueDate);
        pregnancy.setGestationalWeeks(pregnancyDTO.getGestationalWeeks());
        pregnancy.setGestationalDays(pregnancyDTO.getGestationalDays());
        pregnancy.setStatus(PregnancyStatus.ONGOING);

        LocalDateTime now = LocalDateTime.now();
        pregnancy.setCreatedAt(now);
        pregnancy.setLastUpdatedAt(now);

        return pregnancyRepository.save(pregnancy);
    }

    public Pregnancy getPregnancyById(Long pregnancyId) {
        return pregnancyRepository.findById(pregnancyId)
                .orElseThrow(() -> new RuntimeException("Pregnancy not found"));
    }

    public Pregnancy updatePregnancy(Long pregnancyId, PregnancyDTO pregnancyDTO) {
        Pregnancy pregnancy = pregnancyRepository.findById(pregnancyId)
                .orElseThrow(() -> new RuntimeException("Pregnancy not found"));
        if (pregnancyDTO == null) {
            throw new NullPointerException("PregnancyDTO cannot be null");
        }

        pregnancyRepository.findById(pregnancyId)
                .orElseThrow(() -> new IllegalArgumentException("Pregnancy not found"));

        if (pregnancyDTO.getExamDate() == null) {
            throw new NullPointerException("Exam date cannot be null");
        }

        if (pregnancyDTO.getGestationalWeeks() < 0 || pregnancyDTO.getGestationalWeeks() > 42) {
            throw new IllegalArgumentException("Gestational weeks must be between 0 and 42");
        }

        if (pregnancyDTO.getGestationalDays() < 0 || pregnancyDTO.getGestationalDays() >= 7) {
            throw new IllegalArgumentException("Gestational days must be between 0 and 6");
        }
        LocalDate examDate = pregnancyDTO.getExamDate();
        int totalDays = (pregnancyDTO.getGestationalWeeks() * 7) + pregnancyDTO.getGestationalDays();

        LocalDate startDate = examDate.minusDays(totalDays);
        LocalDate dueDate = startDate.plusDays(280);

        pregnancy.setExamDate(examDate);
        pregnancy.setStartDate(startDate);
        pregnancy.setDueDate(dueDate);
        pregnancy.setGestationalWeeks(pregnancyDTO.getGestationalWeeks());
        pregnancy.setGestationalDays(pregnancyDTO.getGestationalDays());
        pregnancy.setLastUpdatedAt(LocalDateTime.now());

        pregnancyRepository.save(pregnancy);

        pregnancyRecordService.updateRecordsForPregnancy(pregnancyId, pregnancy.getGestationalWeeks());

        return pregnancy;
    }

    @Autowired
    private PregnancyRecordRepository pregnancyRecordRepository;
    @Autowired
    private FetusRepository fetusRepository;

    public Pregnancy updatePregnancyStatus(Long pregnancyId, PregnancyStatus newStatus) {
        Pregnancy pregnancy = pregnancyRepository.findById(pregnancyId)
                .orElseThrow(() -> new RuntimeException("Pregnancy not found"));

        pregnancy.setStatus(newStatus);
        pregnancy.setLastUpdatedAt(LocalDateTime.now());

        if (newStatus == PregnancyStatus.COMPLETED) {
            List<Fetus> fetuses = fetusRepository.findByPregnancyPregnancyId(pregnancyId);
            for (Fetus fetus : fetuses) {
                List<PregnancyRecord> records = pregnancyRecordRepository.findByFetusFetusId(fetus.getFetusId());
                for (PregnancyRecord record : records) {
                    if (record.getStatus() == PregnancyRecordStatus.ACTIVE) {
                        record.setStatus(PregnancyRecordStatus.COMPLETED);
                        pregnancyRecordRepository.save(record);
                    }
                }
            }
        }

        return pregnancyRepository.save(pregnancy);
    }

}
