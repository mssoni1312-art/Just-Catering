-- =============================================================================
-- V14: Client query / requirement tables
-- Module: Query
-- =============================================================================

CREATE TABLE client_queries (
    id                  INTEGER       PRIMARY KEY AUTOINCREMENT,
    uuid            TEXT            NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
    client_id           BIGINT          NOT NULL,
    title               VARCHAR(200)    NOT NULL,
    query_type          VARCHAR(100),
    assigned_user_id    BIGINT,
    department_id       BIGINT,
    priority            VARCHAR(20)     NOT NULL DEFAULT 'MEDIUM',
    query_status        VARCHAR(30)     NOT NULL DEFAULT 'PENDING',
    remarks             TEXT,
    image_url           VARCHAR(500),
    completed_at        TEXT,
    created_at          TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          BIGINT,
    updated_by          BIGINT,
    deleted             INTEGER         NOT NULL DEFAULT 0,
    status              VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version             BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_client_queries_uuid UNIQUE (uuid),
    CONSTRAINT fk_client_queries_client
        FOREIGN KEY (client_id) REFERENCES clients (id),
    CONSTRAINT fk_client_queries_assigned_user
        FOREIGN KEY (assigned_user_id) REFERENCES users (id),
    CONSTRAINT fk_client_queries_department
        FOREIGN KEY (department_id) REFERENCES departments (id),
    CONSTRAINT chk_client_queries_status CHECK (status IN ('ACTIVE', 'INACTIVE')),
    CONSTRAINT chk_client_queries_priority CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH', 'URGENT')),
    CONSTRAINT chk_client_queries_query_status CHECK (query_status IN (
        'PENDING', 'IN_PROGRESS', 'NEEDS_ATTENTION', 'COMPLETED', 'SOLVED'
    ))
);

CREATE INDEX idx_client_queries_status ON client_queries (status) WHERE deleted = 0;
CREATE INDEX idx_client_queries_client_id ON client_queries (client_id) WHERE deleted = 0;
CREATE INDEX idx_client_queries_assigned_user_id ON client_queries (assigned_user_id) WHERE deleted = 0;
CREATE INDEX idx_client_queries_department_id ON client_queries (department_id) WHERE deleted = 0;
CREATE INDEX idx_client_queries_priority ON client_queries (priority) WHERE deleted = 0;
CREATE INDEX idx_client_queries_query_status ON client_queries (query_status) WHERE deleted = 0;
CREATE INDEX idx_client_queries_query_type ON client_queries (query_type) WHERE deleted = 0;
CREATE INDEX idx_client_queries_completed_at ON client_queries (completed_at) WHERE deleted = 0;
CREATE INDEX idx_client_queries_deleted ON client_queries (deleted);
CREATE INDEX idx_client_queries_created_at ON client_queries (created_at);
