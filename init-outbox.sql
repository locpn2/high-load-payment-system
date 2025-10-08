-- Create outbox table for Transactional Outbox Pattern
CREATE TABLE IF NOT EXISTS outbox (
    id BIGSERIAL PRIMARY KEY,
    aggregate_type VARCHAR(255) NOT NULL,
    aggregate_id VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    payload JSONB NOT NULL,
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) DEFAULT 'PENDING'
);

-- Create index for efficient querying
CREATE INDEX IF NOT EXISTS idx_outbox_status_timestamp ON outbox (status, timestamp);
CREATE INDEX IF NOT EXISTS idx_outbox_aggregate_type_id ON outbox (aggregate_type, aggregate_id);

-- Grant permissions to payment_worker user
GRANT ALL PRIVILEGES ON TABLE outbox TO payment_worker;
GRANT USAGE, SELECT ON SEQUENCE outbox_id_seq TO payment_worker;
