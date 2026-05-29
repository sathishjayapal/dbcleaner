package me.sathish.dbcleaner.base.jobcleaner.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class JobcleanerDTO {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String runsServiceName;

    @NotNull
    private Boolean jobStatus;

    private String sourceTable;

    private Long sourceRecordId;

    private String recordSummary;

    private OffsetDateTime deletedAt;

}
