-- =============================================================================
-- V4: Department and department member tables
-- Module: Department / Member (Team)
-- =============================================================================

CREATE TABLE departments (
    id              INTEGER       PRIMARY KEY AUTOINCREMENT,
    uuid            TEXT            NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
    name            VARCHAR(150)    NOT NULL,
    code            VARCHAR(50)     NOT NULL,
    description     VARCHAR(500),
    parent_id       BIGINT,
    created_at      TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         INTEGER         NOT NULL DEFAULT 0,
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
    WHERE deleted = 0;

CREATE INDEX idx_departments_status ON departments (status) WHERE deleted = 0;
CREATE INDEX idx_departments_parent_id ON departments (parent_id) WHERE deleted = 0;
CREATE INDEX idx_departments_deleted ON departments (deleted);
CREATE INDEX idx_departments_name ON departments (name) WHERE deleted = 0;


CREATE TABLE department_members (
    id              INTEGER       PRIMARY KEY AUTOINCREMENT,
    uuid            TEXT            NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
    department_id   BIGINT          NOT NULL,
    user_id         BIGINT          NOT NULL,
    designation     VARCHAR(150)    NOT NULL,
    is_lead         INTEGER         NOT NULL DEFAULT 0,
    created_at      TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         INTEGER         NOT NULL DEFAULT 0,
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
    WHERE deleted = 0;

CREATE INDEX idx_department_members_department_id
    ON department_members (department_id) WHERE deleted = 0;
CREATE INDEX idx_department_members_user_id
    ON department_members (user_id) WHERE deleted = 0;
CREATE INDEX idx_department_members_deleted ON department_members (deleted);
