-- =============================================================================
-- V20: Project Ops tables (manager assignments, deadline management)
-- Module: Project Ops
-- =============================================================================

CREATE TABLE client_manager_assignments (
    id                  INTEGER       PRIMARY KEY AUTOINCREMENT,
    uuid            TEXT            NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
    client_id           BIGINT          NOT NULL,
    department_id       BIGINT,
    user_id             BIGINT          NOT NULL,
    project_name        VARCHAR(200),
    close_date          DATE,
    reward_amount       NUMERIC(14, 2),
    created_at          TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          BIGINT,
    updated_by          BIGINT,
    deleted             INTEGER         NOT NULL DEFAULT 0,
    status              VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version             BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_client_manager_assignments_uuid UNIQUE (uuid),
    CONSTRAINT fk_client_manager_assignments_client
        FOREIGN KEY (client_id) REFERENCES clients (id),
    CONSTRAINT fk_client_manager_assignments_department
        FOREIGN KEY (department_id) REFERENCES departments (id),
    CONSTRAINT fk_client_manager_assignments_user
        FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT chk_client_manager_assignments_status CHECK (status IN ('ACTIVE', 'INACTIVE')),
    CONSTRAINT chk_client_manager_assignments_reward CHECK (reward_amount IS NULL OR reward_amount >= 0)
);

CREATE UNIQUE INDEX uq_client_manager_assignments_client_user_active
    ON client_manager_assignments (client_id, user_id)
    WHERE deleted = 0;

CREATE INDEX idx_client_manager_assignments_status ON client_manager_assignments (status) WHERE deleted = 0;
CREATE INDEX idx_client_manager_assignments_client_id ON client_manager_assignments (client_id) WHERE deleted = 0;
CREATE INDEX idx_client_manager_assignments_department_id ON client_manager_assignments (department_id) WHERE deleted = 0;
CREATE INDEX idx_client_manager_assignments_user_id ON client_manager_assignments (user_id) WHERE deleted = 0;
CREATE INDEX idx_client_manager_assignments_close_date ON client_manager_assignments (close_date) WHERE deleted = 0;
CREATE INDEX idx_client_manager_assignments_deleted ON client_manager_assignments (deleted);
CREATE INDEX idx_client_manager_assignments_created_at ON client_manager_assignments (created_at);


CREATE TABLE client_deadlines (
    id                  INTEGER       PRIMARY KEY AUTOINCREMENT,
    uuid            TEXT            NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
    client_id           BIGINT          NOT NULL,
    department_id       BIGINT,
    current_deadline    DATE            NOT NULL,
    new_deadline        DATE            NOT NULL,
    reason              VARCHAR(1000)   NOT NULL,
    created_at          TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          BIGINT,
    updated_by          BIGINT,
    deleted             INTEGER         NOT NULL DEFAULT 0,
    status              VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version             BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_client_deadlines_uuid UNIQUE (uuid),
    CONSTRAINT fk_client_deadlines_client
        FOREIGN KEY (client_id) REFERENCES clients (id),
    CONSTRAINT fk_client_deadlines_department
        FOREIGN KEY (department_id) REFERENCES departments (id),
    CONSTRAINT chk_client_deadlines_status CHECK (status IN ('ACTIVE', 'INACTIVE'))
);

CREATE INDEX idx_client_deadlines_status ON client_deadlines (status) WHERE deleted = 0;
CREATE INDEX idx_client_deadlines_client_id ON client_deadlines (client_id) WHERE deleted = 0;
CREATE INDEX idx_client_deadlines_department_id ON client_deadlines (department_id) WHERE deleted = 0;
CREATE INDEX idx_client_deadlines_current_deadline ON client_deadlines (current_deadline) WHERE deleted = 0;
CREATE INDEX idx_client_deadlines_new_deadline ON client_deadlines (new_deadline) WHERE deleted = 0;
CREATE INDEX idx_client_deadlines_deleted ON client_deadlines (deleted);
CREATE INDEX idx_client_deadlines_created_at ON client_deadlines (created_at);
