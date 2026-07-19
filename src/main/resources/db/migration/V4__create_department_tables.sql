-- =============================================================================
-- V4: Department and department member tables
-- Module: Department / Member (Team)
-- =============================================================================

CREATE TABLE departments (
    id              BIGSERIAL       PRIMARY KEY,
    uuid            UUID            NOT NULL DEFAULT gen_random_uuid(),
    name            VARCHAR(150)    NOT NULL,
    code            VARCHAR(50)     NOT NULL,
    description     VARCHAR(500),
    parent_id       BIGINT,
    created_at      TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         BOOLEAN         NOT NULL DEFAULT FALSE,
    status          VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version         BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_departments_uuid UNIQUE (uuid),
    CONSTRAINT uq_departments_code UNIQUE (code),
    CONSTRAINT fk_departments_parent
        FOREIGN KEY (parent_id) REFERENCES departments (id),
    CONSTRAINT chk_departments_status CHECK (status IN ('ACTIVE', 'INACTIVE'))
);

CREATE UNIQUE INDEX uq_departments_name_active
    ON departments (LOWER(name))
    WHERE deleted = FALSE;

CREATE INDEX idx_departments_status ON departments (status) WHERE deleted = FALSE;
CREATE INDEX idx_departments_parent_id ON departments (parent_id) WHERE deleted = FALSE;
CREATE INDEX idx_departments_deleted ON departments (deleted);
CREATE INDEX idx_departments_name ON departments (name) WHERE deleted = FALSE;

COMMENT ON TABLE departments IS 'Organizational departments / teams';
COMMENT ON COLUMN departments.parent_id IS 'Optional parent department for hierarchy';

CREATE TABLE department_members (
    id              BIGSERIAL       PRIMARY KEY,
    uuid            UUID            NOT NULL DEFAULT gen_random_uuid(),
    department_id   BIGINT          NOT NULL,
    user_id         BIGINT          NOT NULL,
    designation     VARCHAR(150)    NOT NULL,
    is_lead         BOOLEAN         NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         BOOLEAN         NOT NULL DEFAULT FALSE,
    status          VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version         BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_department_members_uuid UNIQUE (uuid),
    CONSTRAINT fk_department_members_department
        FOREIGN KEY (department_id) REFERENCES departments (id),
    CONSTRAINT fk_department_members_user
        FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT chk_department_members_status CHECK (status IN ('ACTIVE', 'INACTIVE'))
);

CREATE UNIQUE INDEX uq_department_members_dept_user_active
    ON department_members (department_id, user_id)
    WHERE deleted = FALSE;

CREATE INDEX idx_department_members_department_id
    ON department_members (department_id) WHERE deleted = FALSE;
CREATE INDEX idx_department_members_user_id
    ON department_members (user_id) WHERE deleted = FALSE;
CREATE INDEX idx_department_members_deleted ON department_members (deleted);

COMMENT ON TABLE department_members IS 'Users assigned to departments with job designation';
COMMENT ON COLUMN department_members.designation IS 'Job title within the department (e.g. Sales Manager)';
COMMENT ON COLUMN department_members.is_lead IS 'Whether the member leads the department';
