-- =============================================================================
-- V39: On-site manager task plan tables
-- =============================================================================

CREATE TABLE onsite_task_plans (
    id                  BIGSERIAL       PRIMARY KEY,
    uuid                UUID            NOT NULL DEFAULT gen_random_uuid(),
    client_id           BIGINT,
    manager_id          BIGINT          NOT NULL,
    additional_notes    VARCHAR(500),
    number_of_days      INTEGER         NOT NULL,
    created_at          TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          BIGINT,
    updated_by          BIGINT,
    deleted             BOOLEAN         NOT NULL DEFAULT FALSE,
    status              VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version             BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_onsite_task_plans_uuid UNIQUE (uuid),
    CONSTRAINT fk_onsite_task_plans_client
        FOREIGN KEY (client_id) REFERENCES clients (id),
    CONSTRAINT fk_onsite_task_plans_manager
        FOREIGN KEY (manager_id) REFERENCES users (id),
    CONSTRAINT chk_onsite_task_plans_status CHECK (status IN ('ACTIVE', 'INACTIVE')),
    CONSTRAINT chk_onsite_task_plans_days CHECK (number_of_days >= 1)
);

CREATE TABLE onsite_task_plan_days (
    id                  BIGSERIAL       PRIMARY KEY,
    uuid                UUID            NOT NULL DEFAULT gen_random_uuid(),
    task_plan_id        BIGINT          NOT NULL,
    day_number          INTEGER         NOT NULL,
    task_date           DATE            NOT NULL,
    task_description    VARCHAR(300)    NOT NULL,
    created_at          TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          BIGINT,
    updated_by          BIGINT,
    deleted             BOOLEAN         NOT NULL DEFAULT FALSE,
    status              VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version             BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_onsite_task_plan_days_uuid UNIQUE (uuid),
    CONSTRAINT uq_onsite_task_plan_days_plan_day UNIQUE (task_plan_id, day_number),
    CONSTRAINT fk_onsite_task_plan_days_plan
        FOREIGN KEY (task_plan_id) REFERENCES onsite_task_plans (id),
    CONSTRAINT chk_onsite_task_plan_days_day_number CHECK (day_number >= 1)
);

CREATE INDEX idx_onsite_task_plans_client_id ON onsite_task_plans (client_id) WHERE deleted = FALSE;
CREATE INDEX idx_onsite_task_plans_manager_id ON onsite_task_plans (manager_id) WHERE deleted = FALSE;
CREATE INDEX idx_onsite_task_plans_status ON onsite_task_plans (status) WHERE deleted = FALSE;
CREATE INDEX idx_onsite_task_plans_deleted ON onsite_task_plans (deleted);
CREATE INDEX idx_onsite_task_plans_created_at ON onsite_task_plans (created_at);
CREATE INDEX idx_onsite_task_plan_days_plan_id ON onsite_task_plan_days (task_plan_id) WHERE deleted = FALSE;
