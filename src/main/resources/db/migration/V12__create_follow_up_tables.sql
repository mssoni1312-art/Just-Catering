-- =============================================================================
-- V12: Follow-up tables
-- Module: Follow-up
-- =============================================================================

CREATE TABLE follow_ups (
    id                      INTEGER       PRIMARY KEY AUTOINCREMENT,
    uuid            TEXT            NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
    client_id               BIGINT,
    lead_id                 BIGINT,
    title                   VARCHAR(200)    NOT NULL,
    follow_up_type          VARCHAR(30)     NOT NULL DEFAULT 'CALL',
    assigned_user_id        BIGINT,
    follow_up_date          DATE            NOT NULL,
    follow_up_time          TIME,
    follow_up_status        VARCHAR(30)     NOT NULL DEFAULT 'PENDING',
    expected_budget         NUMERIC(14, 2),
    remark                  TEXT,
    next_follow_up_date     DATE,
    next_follow_up_time     TIME,
    reminder_minutes        INTEGER,
    created_at              TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by              BIGINT,
    updated_by              BIGINT,
    deleted                 INTEGER         NOT NULL DEFAULT 0,
    status                  VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version                 BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_follow_ups_uuid UNIQUE (uuid),
    CONSTRAINT fk_follow_ups_client
        FOREIGN KEY (client_id) REFERENCES clients (id),
    CONSTRAINT fk_follow_ups_lead
        FOREIGN KEY (lead_id) REFERENCES leads (id),
    CONSTRAINT fk_follow_ups_assigned_user
        FOREIGN KEY (assigned_user_id) REFERENCES users (id),
    CONSTRAINT chk_follow_ups_status CHECK (status IN ('ACTIVE', 'INACTIVE')),
    CONSTRAINT chk_follow_ups_type CHECK (follow_up_type IN (
        'CALL', 'MEETING', 'EMAIL', 'VISIT', 'OTHER'
    )),
    CONSTRAINT chk_follow_ups_follow_up_status CHECK (follow_up_status IN (
        'PENDING', 'COMPLETED', 'CANCELLED', 'NO_RESPONSE'
    )),
    CONSTRAINT chk_follow_ups_expected_budget CHECK (
        expected_budget IS NULL OR expected_budget >= 0
    ),
    CONSTRAINT chk_follow_ups_reminder_minutes CHECK (
        reminder_minutes IS NULL OR reminder_minutes >= 0
    ),
    CONSTRAINT chk_follow_ups_client_or_lead CHECK (
        (client_id IS NOT NULL AND lead_id IS NULL)
        OR (client_id IS NULL AND lead_id IS NOT NULL)
    )
);

CREATE INDEX idx_follow_ups_status ON follow_ups (status) WHERE deleted = 0;
CREATE INDEX idx_follow_ups_client_id ON follow_ups (client_id) WHERE deleted = 0;
CREATE INDEX idx_follow_ups_lead_id ON follow_ups (lead_id) WHERE deleted = 0;
CREATE INDEX idx_follow_ups_assigned_user_id ON follow_ups (assigned_user_id) WHERE deleted = 0;
CREATE INDEX idx_follow_ups_type ON follow_ups (follow_up_type) WHERE deleted = 0;
CREATE INDEX idx_follow_ups_follow_up_status ON follow_ups (follow_up_status) WHERE deleted = 0;
CREATE INDEX idx_follow_ups_follow_up_date ON follow_ups (follow_up_date) WHERE deleted = 0;
CREATE INDEX idx_follow_ups_deleted ON follow_ups (deleted);
CREATE INDEX idx_follow_ups_created_at ON follow_ups (created_at);
