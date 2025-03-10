package com.example.pregnancy_tracking.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "PregnancyRecords")
@Getter
@Setter
public class PregnancyRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;

    @ManyToOne
    @JoinColumn(name = "fetus_id", nullable = false)
    private Fetus fetus;

    @ManyToOne
    @JoinColumn(name = "pregnancy_id", nullable = false)
    private Pregnancy pregnancy;


    private Integer week;
    private Double fetalWeight;
    private Double crownHeelLength;
    private Double headCircumference;

    @Enumerated(EnumType.STRING)
    private PregnancyRecordStatus status;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
