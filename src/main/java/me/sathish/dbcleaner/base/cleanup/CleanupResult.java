package me.sathish.dbcleaner.base.cleanup;

import java.io.Serializable;

public record CleanupResult(int fileImportRecordsDeleted, int domainEventsDeleted,
                            int analysisLogsDeleted) implements Serializable {

    public int total() {
        return fileImportRecordsDeleted + domainEventsDeleted + analysisLogsDeleted;
    }

}
