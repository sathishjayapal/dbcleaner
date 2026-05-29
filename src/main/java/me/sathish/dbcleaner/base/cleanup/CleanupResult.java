package me.sathish.dbcleaner.base.cleanup;

public record CleanupResult(int fileImportRecordsDeleted, int domainEventsDeleted, int analysisLogsDeleted) {

    public int total() {
        return fileImportRecordsDeleted + domainEventsDeleted + analysisLogsDeleted;
    }

}
