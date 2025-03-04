package com.example.pregnancy_tracking.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "PregnancyRecords")
@Getter
@Setter
public class PregnancyRecord {  // ĐÚNG: Không có "s"
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;

    @ManyToOne
    @JoinColumn(name = "fetus_id", nullable = false)
    private Fetus fetus;

    private Integer week;
    private Double fetalWeight;
    private Double crownHeelLength;
    private Double headCircumference;

    @Enumerated(EnumType.STRING)
    private PregnancyRecordStatus status;  // Đảm bảo enum này tồn tại

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
