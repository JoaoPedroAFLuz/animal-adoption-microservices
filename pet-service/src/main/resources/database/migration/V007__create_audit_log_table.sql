CREATE TABLE audit_log (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    action VARCHAR(20) NOT NULL,
    entity_id UUID NOT NULL,
    user_id VARCHAR(255),
    details TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);
