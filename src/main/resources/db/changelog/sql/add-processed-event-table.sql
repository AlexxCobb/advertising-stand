create TABLE processed_event (
    event_id VARCHAR(255) PRIMARY KEY,
    processed_at TIMESTAMP NOT NULL,
    event_type VARCHAR(50) NOT NULL
);