package me.sathish.dbcleaner.base;

import java.time.OffsetDateTime;


public record ProjectStat(
        String service,
        String database,
        String table,
        Long pendingCount,
        OffsetDateTime lastCleaned,
        boolean available) {
}
