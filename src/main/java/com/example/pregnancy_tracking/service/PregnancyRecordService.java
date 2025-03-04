package com.example.pregnancy_tracking.service;

import com.example.pregnancy_tracking.entity.PregnancyRecord;
import com.example.pregnancy_tracking.entity.PregnancyRecordStatus;
import com.example.pregnancy_tracking.repository.PregnancyRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PregnancyRecordService {
    @Autowired
    private PregnancyRecordRepository pregnancyRecordRepository;

    public List<PregnancyRecord> getRecordsByFetusId(Long fetusId) {
        return pregnancyRecordRepository.findByFetusFetusId(fetusId);
    }

    public PregnancyRecord saveRecord(PregnancyRecord record) {
        return pregnancyRecordRepository.save(record);
    }

    public void updateRecordsStatusByFetusId(Long fetusId, PregnancyRecordStatus newStatus) {
        List<PregnancyRecord> records = pregnancyRecordRepository.findByFetusFetusId(fetusId);
        for (PregnancyRecord record : records) {
            if (record.getStatus() == PregnancyRecordStatus.ACTIVE) {
                record.setStatus(newStatus);
                pregnancyRecordRepository.save(record);
            }
        }
    }
}
