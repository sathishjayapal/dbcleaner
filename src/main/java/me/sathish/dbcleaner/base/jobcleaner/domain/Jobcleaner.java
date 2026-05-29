package me.sathish.dbcleaner.base.jobcleaner.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Jobcleaner {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long id;

    @Column(nullable = false, length = 100)
    private String runsServiceName;

    @Column(nullable = false)
    private Boolean jobStatus;

    @Column(length = 100)
    private String sourceTable;

    @Column
    private Long sourceRecordId;

    @Column(columnDefinition = "TEXT")
    private String recordSummary;

    @Column
    private OffsetDateTime deletedAt;

}
