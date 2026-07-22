-- =============================================================================
-- V34: On-site requirements tables
-- =============================================================================

CREATE TABLE requirements (
    id                      INTEGER         PRIMARY KEY AUTOINCREMENT,
    uuid                    TEXT            NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
    client_id               BIGINT          NOT NULL,
    title                   VARCHAR(200)    NOT NULL,
    description             TEXT,
    notes                   VARCHAR(1000),
    assigned_user_id        BIGINT,
    check_in_enabled        INTEGER         NOT NULL DEFAULT 0,
    check_in_at             TEXT,
    check_in_latitude       NUMERIC(10, 7),
    check_in_longitude      NUMERIC(10, 7),
    check_in_address        VARCHAR(500),
    check_out_enabled       INTEGER         NOT NULL DEFAULT 0,
    check_out_at            TEXT,
    check_out_latitude      NUMERIC(10, 7),
    check_out_longitude     NUMERIC(10, 7),
    check_out_address       VARCHAR(500),
    voice_url               VARCHAR(500),
    voice_duration_seconds  INTEGER,
    created_at              TEXT            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              TEXT            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by              BIGINT,
    updated_by              BIGINT,
    deleted                 INTEGER         NOT NULL DEFAULT 0,
    status                  VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version                 BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_requirements_uuid UNIQUE (uuid),
    CONSTRAINT fk_requirements_client
        FOREIGN KEY (client_id) REFERENCES clients (id),
    CONSTRAINT fk_requirements_assigned_user
        FOREIGN KEY (assigned_user_id) REFERENCES users (id),
    CONSTRAINT chk_requirements_status CHECK (status IN ('ACTIVE', 'INACTIVE'))
);

CREATE TABLE requirement_documents (
    id                  INTEGER         PRIMARY KEY AUTOINCREMENT,
    uuid                TEXT            NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
    requirement_id      BIGINT          NOT NULL,
    file_name           VARCHAR(255)    NOT NULL,
    file_url            VARCHAR(500)    NOT NULL,
    file_size_bytes     BIGINT,
    content_type        VARCHAR(100),
    created_at          TEXT            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TEXT            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          BIGINT,
    updated_by          BIGINT,
    deleted             INTEGER         NOT NULL DEFAULT 0,
    status              VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version             BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_requirement_documents_uuid UNIQUE (uuid),
    CONSTRAINT fk_requirement_documents_requirement
        FOREIGN KEY (requirement_id) REFERENCES requirements (id)
);

CREATE INDEX idx_requirements_status ON requirements (status) WHERE deleted = 0;
CREATE INDEX idx_requirements_client_id ON requirements (client_id) WHERE deleted = 0;
CREATE INDEX idx_requirements_assigned_user_id ON requirements (assigned_user_id) WHERE deleted = 0;
CREATE INDEX idx_requirements_check_in_at ON requirements (check_in_at) WHERE deleted = 0;
CREATE INDEX idx_requirements_deleted ON requirements (deleted);
CREATE INDEX idx_requirements_created_at ON requirements (created_at);
CREATE INDEX idx_requirement_documents_requirement_id
    ON requirement_documents (requirement_id)
    WHERE deleted = 0;
