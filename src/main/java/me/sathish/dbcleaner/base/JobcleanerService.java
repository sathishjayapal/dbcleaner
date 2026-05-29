package me.sathish.dbcleaner.base;

import java.time.OffsetDateTime;
import me.sathish.dbcleaner.base.jobcleaner.domain.Jobcleaner;
import me.sathish.dbcleaner.base.jobcleaner.model.JobcleanerDTO;
import me.sathish.dbcleaner.base.jobcleaner.repos.JobcleanerRepository;
import me.sathish.dbcleaner.base.util.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class JobcleanerService {

    private final JobcleanerRepository jobcleanerRepository;

    public JobcleanerService(final JobcleanerRepository jobcleanerRepository) {
        this.jobcleanerRepository = jobcleanerRepository;
    }

    public Page<JobcleanerDTO> findAll(final String filter, final Pageable pageable) {
        Page<Jobcleaner> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = jobcleanerRepository.findAllById(longFilter, pageable);
        } else {
            page = jobcleanerRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(jobcleaner -> mapToDTO(jobcleaner, new JobcleanerDTO()))
                .toList(),
                pageable, page.getTotalElements());
    }

    public JobcleanerDTO get(final Long id) {
        return jobcleanerRepository.findById(id)
                .map(jobcleaner -> mapToDTO(jobcleaner, new JobcleanerDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final JobcleanerDTO jobcleanerDTO) {
        final Jobcleaner jobcleaner = new Jobcleaner();
        mapToEntity(jobcleanerDTO, jobcleaner);
        return jobcleanerRepository.save(jobcleaner).getId();
    }

    public void update(final Long id, final JobcleanerDTO jobcleanerDTO) {
        final Jobcleaner jobcleaner = jobcleanerRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(jobcleanerDTO, jobcleaner);
        jobcleanerRepository.save(jobcleaner);
    }

    public void delete(final Long id) {
        final Jobcleaner jobcleaner = jobcleanerRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        jobcleanerRepository.delete(jobcleaner);
    }

    public void saveAuditEntry(final String serviceName, final String table,
            final Long recordId, final String summary, final boolean success) {
        final Jobcleaner entry = new Jobcleaner();
        entry.setRunsServiceName(serviceName);
        entry.setJobStatus(success);
        entry.setSourceTable(table);
        entry.setSourceRecordId(recordId);
        entry.setRecordSummary(summary);
        entry.setDeletedAt(OffsetDateTime.now());
        jobcleanerRepository.save(entry);
    }

    public Page<JobcleanerDTO> findAllAuditEntries(final Pageable pageable) {
        final Page<Jobcleaner> page = jobcleanerRepository.findAllByOrderByDeletedAtDesc(pageable);
        return new PageImpl<>(page.getContent()
                .stream()
                .map(jobcleaner -> mapToDTO(jobcleaner, new JobcleanerDTO()))
                .toList(),
                pageable, page.getTotalElements());
    }

    private JobcleanerDTO mapToDTO(final Jobcleaner jobcleaner, final JobcleanerDTO jobcleanerDTO) {
        jobcleanerDTO.setId(jobcleaner.getId());
        jobcleanerDTO.setRunsServiceName(jobcleaner.getRunsServiceName());
        jobcleanerDTO.setJobStatus(jobcleaner.getJobStatus());
        jobcleanerDTO.setSourceTable(jobcleaner.getSourceTable());
        jobcleanerDTO.setSourceRecordId(jobcleaner.getSourceRecordId());
        jobcleanerDTO.setRecordSummary(jobcleaner.getRecordSummary());
        jobcleanerDTO.setDeletedAt(jobcleaner.getDeletedAt());
        return jobcleanerDTO;
    }

    private Jobcleaner mapToEntity(final JobcleanerDTO jobcleanerDTO, final Jobcleaner jobcleaner) {
        jobcleaner.setRunsServiceName(jobcleanerDTO.getRunsServiceName());
        jobcleaner.setJobStatus(jobcleanerDTO.getJobStatus());
        return jobcleaner;
    }

    public boolean runsServiceNameExists(final String runsServiceName) {
        return jobcleanerRepository.existsByRunsServiceNameIgnoreCase(runsServiceName);
    }

}
