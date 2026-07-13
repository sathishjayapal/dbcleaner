package me.sathish.dbcleaner.base;

import me.sathish.dbcleaner.base.jobcleaner.domain.Jobcleaner;
import me.sathish.dbcleaner.base.jobcleaner.repos.JobcleanerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;


@Service
public class HomeStatsService {

    private static final Logger log = LoggerFactory.getLogger(HomeStatsService.class);

    private final JdbcTemplate runsAppJdbc;
    private final JdbcTemplate eventsTrackerJdbc;
    private final JdbcTemplate runsAiJdbc;
    private final JobcleanerRepository jobcleanerRepository;

    public HomeStatsService(
            @Qualifier("runsAppJdbc") final JdbcTemplate runsAppJdbc,
            @Qualifier("eventsTrackerJdbc") final JdbcTemplate eventsTrackerJdbc,
            @Qualifier("runsAiJdbc") final JdbcTemplate runsAiJdbc,
            final JobcleanerRepository jobcleanerRepository) {
        this.runsAppJdbc = runsAppJdbc;
        this.eventsTrackerJdbc = eventsTrackerJdbc;
        this.runsAiJdbc = runsAiJdbc;
        this.jobcleanerRepository = jobcleanerRepository;
    }

    public List<ProjectStat> projectStats() {
        // TEMP: runs-app and runs-ai disabled while testing eventstracker cleanup in isolation
        return List.of(
                buildStat(eventsTrackerJdbc, "eventstracker", "eventstracker", "domain_event"));
    }

    private ProjectStat buildStat(final JdbcTemplate jdbc, final String service,
                                  final String database, final String table) {
        Long pendingCount = null;
        boolean available = true;
        try {
            pendingCount = jdbc.queryForObject("SELECT count(*) FROM " + table, Long.class);
        } catch (final Exception e) {
            available = false;
            log.warn("Could not read pending count for {}.{}: {}", database, table, e.getMessage());
        }
        final OffsetDateTime lastCleaned = jobcleanerRepository
                .findFirstBySourceTableOrderByDeletedAtDesc(table)
                .map(Jobcleaner::getDeletedAt)
                .orElse(null);
        return new ProjectStat(service, database, table, pendingCount, lastCleaned, available);
    }

}
