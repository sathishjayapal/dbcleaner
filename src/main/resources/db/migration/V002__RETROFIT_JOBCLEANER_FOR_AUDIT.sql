ALTER TABLE jobcleaner ALTER COLUMN runs_service_name TYPE VARCHAR(100);
ALTER TABLE jobcleaner DROP CONSTRAINT unique_jobcleaner_runs_service_name;

ALTER TABLE jobcleaner ADD COLUMN source_table     VARCHAR(100);
ALTER TABLE jobcleaner ADD COLUMN source_record_id BIGINT;
ALTER TABLE jobcleaner ADD COLUMN record_summary   TEXT;
ALTER TABLE jobcleaner ADD COLUMN deleted_at       TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP;

CREATE INDEX idx_jobcleaner_runs_service_name ON jobcleaner(runs_service_name);
CREATE INDEX idx_jobcleaner_deleted_at ON jobcleaner(deleted_at DESC);
