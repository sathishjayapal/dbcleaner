package me.sathish.dbcleaner.base.cleanup;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import me.sathish.dbcleaner.base.JobcleanerService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CleanupService {

    private final JdbcTemplate runsAppJdbc;
    private final JdbcTemplate eventsTrackerJdbc;
    private final JdbcTemplate runsAiJdbc;
    private final JobcleanerService jobcleanerService;

    public CleanupService(
            @Qualifier("runsAppJdbc") final JdbcTemplate runsAppJdbc,
            @Qualifier("eventsTrackerJdbc") final JdbcTemplate eventsTrackerJdbc,
            @Qualifier("runsAiJdbc") final JdbcTemplate runsAiJdbc,
            final JobcleanerService jobcleanerService) {
        this.runsAppJdbc = runsAppJdbc;
        this.eventsTrackerJdbc = eventsTrackerJdbc;
        this.runsAiJdbc = runsAiJdbc;
        this.jobcleanerService = jobcleanerService;
    }

    @Transactional
    public CleanupResult cleanAll() {
        int fileImportCount = cleanFileImportRecords();
        int domainEventCount = cleanDomainEvents();
        int analysisLogCount = cleanAnalysisProcessingLogs();
        return new CleanupResult(fileImportCount, domainEventCount, analysisLogCount);
    }

    private int cleanFileImportRecords() {
        List<Map<String, Object>> rows = runsAppJdbc.queryForList(
                "SELECT id, file_name, status, processed_at FROM file_import_record");
        for (Map<String, Object> row : rows) {
            Long id = toLong(row.get("id"));
            String summary = buildSummary(row, "file_name", "status", "processed_at");
            jobcleanerService.saveAuditEntry("runs-app", "file_import_record", id, summary, true);
        }
        runsAppJdbc.update("DELETE FROM file_import_record");
        return rows.size();
    }

    private int cleanDomainEvents() {
        List<Map<String, Object>> rows = eventsTrackerJdbc.queryForList(
                "SELECT id, event_id, event_type, domain_id, date_created FROM domain_event");
        for (Map<String, Object> row : rows) {
            Long id = toLong(row.get("id"));
            String summary = buildSummary(row, "event_id", "event_type", "domain_id", "date_created");
            jobcleanerService.saveAuditEntry("eventstracker", "domain_event", id, summary, true);
        }
        eventsTrackerJdbc.update("DELETE FROM domain_event");
        return rows.size();
    }

    private int cleanAnalysisProcessingLogs() {
        List<Map<String, Object>> rows = runsAiJdbc.queryForList(
                "SELECT id, activity_id, processing_status, event_type, created_at FROM analysis_processing_log");
        for (Map<String, Object> row : rows) {
            Long id = toLong(row.get("id"));
            String summary = buildSummary(row, "activity_id", "processing_status", "event_type", "created_at");
            jobcleanerService.saveAuditEntry("runs-ai-analyzer", "analysis_processing_log", id, summary, true);
        }
        runsAiJdbc.update("DELETE FROM analysis_processing_log");
        return rows.size();
    }

    private String buildSummary(Map<String, Object> row, String... keys) {
        return java.util.Arrays.stream(keys)
                .filter(row::containsKey)
                .map(k -> k + "=" + row.get(k))
                .collect(Collectors.joining(", "));
    }

    private Long toLong(Object value) {
        if (value instanceof Number n) {
            return n.longValue();
        }
        return null;
    }

}
