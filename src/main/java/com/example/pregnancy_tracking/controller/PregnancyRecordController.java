package com.example.pregnancy_tracking.controller;

import com.example.pregnancy_tracking.entity.PregnancyRecord;
import com.example.pregnancy_tracking.service.PregnancyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pregnancy-records")
public class PregnancyRecordController {
    @Autowired
    private PregnancyRecordService pregnancyRecordService;

    @GetMapping("/{fetusId}")
    public ResponseEntity<List<PregnancyRecord>> getRecordsByFetusId(@PathVariable Long fetusId) {
        List<PregnancyRecord> records = pregnancyRecordService.getRecordsByFetusId(fetusId);
        return ResponseEntity.ok(records);
    }
}
