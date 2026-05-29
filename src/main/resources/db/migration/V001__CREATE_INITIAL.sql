CREATE SEQUENCE  IF NOT EXISTS primary_sequence START WITH 10000 INCREMENT BY 1;

CREATE TABLE jobcleaner (
    id BIGINT NOT NULL,
    runs_service_name VARCHAR(16) NOT NULL,
    job_status BOOLEAN NOT NULL,
    CONSTRAINT jobcleaner_pkey PRIMARY KEY (id)
);

ALTER TABLE jobcleaner ADD CONSTRAINT unique_jobcleaner_runs_service_name UNIQUE (runs_service_name);
