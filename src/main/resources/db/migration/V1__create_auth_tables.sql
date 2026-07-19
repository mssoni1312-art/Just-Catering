-- =============================================================================
-- V1: Auth & RBAC foundation tables
-- Module: Foundation + Authentication + Authorization
-- =============================================================================

-- -----------------------------------------------------------------------------
-- roles
-- -----------------------------------------------------------------------------
CREATE TABLE roles (
    id              BIGSERIAL       PRIMARY KEY,
    uuid            UUID            NOT NULL DEFAULT gen_random_uuid(),
    name            VARCHAR(100)    NOT NULL,
    code            VARCHAR(50)     NOT NULL,
    description     VARCHAR(500),
    is_system       BOOLEAN         NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         BOOLEAN         NOT NULL DEFAULT FALSE,
    status          VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version         BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_roles_uuid UNIQUE (uuid),
    CONSTRAINT uq_roles_code UNIQUE (code),
    CONSTRAINT uq_roles_name UNIQUE (name),
    CONSTRAINT chk_roles_status CHECK (status IN ('ACTIVE', 'INACTIVE'))
);

CREATE INDEX idx_roles_status ON roles (status) WHERE deleted = FALSE;
CREATE INDEX idx_roles_deleted ON roles (deleted);

COMMENT ON TABLE roles IS 'System and custom roles for RBAC';
COMMENT ON COLUMN roles.is_system IS 'System roles cannot be deleted';

-- -----------------------------------------------------------------------------
-- permissions
-- -----------------------------------------------------------------------------
CREATE TABLE permissions (
    id              BIGSERIAL       PRIMARY KEY,
    uuid            UUID            NOT NULL DEFAULT gen_random_uuid(),
    name            VARCHAR(150)    NOT NULL,
    code            VARCHAR(100)    NOT NULL,
    module          VARCHAR(100)    NOT NULL,
    description     VARCHAR(500),
    created_at      TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         BOOLEAN         NOT NULL DEFAULT FALSE,
    status          VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version         BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_permissions_uuid UNIQUE (uuid),
    CONSTRAINT uq_permissions_code UNIQUE (code),
    CONSTRAINT chk_permissions_status CHECK (status IN ('ACTIVE', 'INACTIVE'))
);

CREATE INDEX idx_permissions_module ON permissions (module) WHERE deleted = FALSE;
CREATE INDEX idx_permissions_status ON permissions (status) WHERE deleted = FALSE;
CREATE INDEX idx_permissions_deleted ON permissions (deleted);

COMMENT ON TABLE permissions IS 'Fine-grained permissions grouped by module';

-- -----------------------------------------------------------------------------
-- role_permissions (join)
-- -----------------------------------------------------------------------------
CREATE TABLE role_permissions (
    id              BIGSERIAL       PRIMARY KEY,
    uuid            UUID            NOT NULL DEFAULT gen_random_uuid(),
    role_id         BIGINT          NOT NULL,
    permission_id   BIGINT          NOT NULL,
    created_at      TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         BOOLEAN         NOT NULL DEFAULT FALSE,
    status          VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version         BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_role_permissions_uuid UNIQUE (uuid),
    CONSTRAINT uq_role_permissions_role_perm UNIQUE (role_id, permission_id),
    CONSTRAINT fk_role_permissions_role
        FOREIGN KEY (role_id) REFERENCES roles (id),
    CONSTRAINT fk_role_permissions_permission
        FOREIGN KEY (permission_id) REFERENCES permissions (id),
    CONSTRAINT chk_role_permissions_status CHECK (status IN ('ACTIVE', 'INACTIVE'))
);

CREATE INDEX idx_role_permissions_role_id ON role_permissions (role_id) WHERE deleted = FALSE;
CREATE INDEX idx_role_permissions_permission_id ON role_permissions (permission_id) WHERE deleted = FALSE;

COMMENT ON TABLE role_permissions IS 'Many-to-many mapping between roles and permissions';

-- -----------------------------------------------------------------------------
-- users
-- -----------------------------------------------------------------------------
CREATE TABLE users (
    id                      BIGSERIAL       PRIMARY KEY,
    uuid                    UUID            NOT NULL DEFAULT gen_random_uuid(),
    first_name              VARCHAR(100)    NOT NULL,
    last_name               VARCHAR(100)    NOT NULL,
    email                   VARCHAR(255)    NOT NULL,
    password_hash           VARCHAR(255)    NOT NULL,
    phone                   VARCHAR(20),
    company_name            VARCHAR(255),
    profile_image_url       VARCHAR(500),
    email_verified          BOOLEAN         NOT NULL DEFAULT FALSE,
    last_login_at           TIMESTAMPTZ,
    failed_login_attempts   INTEGER         NOT NULL DEFAULT 0,
    locked_until            TIMESTAMPTZ,
    password_changed_at     TIMESTAMPTZ,
    created_at              TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by              BIGINT,
    updated_by              BIGINT,
    deleted                 BOOLEAN         NOT NULL DEFAULT FALSE,
    status                  VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version                 BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_users_uuid UNIQUE (uuid),
    CONSTRAINT uq_users_email UNIQUE (email),
    CONSTRAINT chk_users_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'LOCKED', 'PENDING')),
    CONSTRAINT chk_users_failed_attempts CHECK (failed_login_attempts >= 0)
);

CREATE INDEX idx_users_email ON users (email) WHERE deleted = FALSE;
CREATE INDEX idx_users_status ON users (status) WHERE deleted = FALSE;
CREATE INDEX idx_users_phone ON users (phone) WHERE deleted = FALSE AND phone IS NOT NULL;
CREATE INDEX idx_users_deleted ON users (deleted);
CREATE INDEX idx_users_created_at ON users (created_at);

COMMENT ON TABLE users IS 'Application users including Super Admin and staff';
COMMENT ON COLUMN users.password_hash IS 'BCrypt hashed password';
COMMENT ON COLUMN users.locked_until IS 'Account lock expiry after failed login attempts';

-- -----------------------------------------------------------------------------
-- user_roles (join)
-- -----------------------------------------------------------------------------
CREATE TABLE user_roles (
    id              BIGSERIAL       PRIMARY KEY,
    uuid            UUID            NOT NULL DEFAULT gen_random_uuid(),
    user_id         BIGINT          NOT NULL,
    role_id         BIGINT          NOT NULL,
    created_at      TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         BOOLEAN         NOT NULL DEFAULT FALSE,
    status          VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version         BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_user_roles_uuid UNIQUE (uuid),
    CONSTRAINT uq_user_roles_user_role UNIQUE (user_id, role_id),
    CONSTRAINT fk_user_roles_user
        FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_user_roles_role
        FOREIGN KEY (role_id) REFERENCES roles (id),
    CONSTRAINT chk_user_roles_status CHECK (status IN ('ACTIVE', 'INACTIVE'))
);

CREATE INDEX idx_user_roles_user_id ON user_roles (user_id) WHERE deleted = FALSE;
CREATE INDEX idx_user_roles_role_id ON user_roles (role_id) WHERE deleted = FALSE;

COMMENT ON TABLE user_roles IS 'Many-to-many mapping between users and roles';

-- -----------------------------------------------------------------------------
-- refresh_tokens
-- -----------------------------------------------------------------------------
CREATE TABLE refresh_tokens (
    id                  BIGSERIAL       PRIMARY KEY,
    uuid                UUID            NOT NULL DEFAULT gen_random_uuid(),
    user_id             BIGINT          NOT NULL,
    token               VARCHAR(500)    NOT NULL,
    expires_at          TIMESTAMPTZ     NOT NULL,
    revoked             BOOLEAN         NOT NULL DEFAULT FALSE,
    revoked_at          TIMESTAMPTZ,
    replaced_by_token   VARCHAR(500),
    device_info         VARCHAR(255),
    ip_address          VARCHAR(45),
    created_at          TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          BIGINT,
    updated_by          BIGINT,
    deleted             BOOLEAN         NOT NULL DEFAULT FALSE,
    status              VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version             BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_refresh_tokens_uuid UNIQUE (uuid),
    CONSTRAINT uq_refresh_tokens_token UNIQUE (token),
    CONSTRAINT fk_refresh_tokens_user
        FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT chk_refresh_tokens_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'REVOKED'))
);

CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens (user_id) WHERE deleted = FALSE;
CREATE INDEX idx_refresh_tokens_expires_at ON refresh_tokens (expires_at) WHERE revoked = FALSE AND deleted = FALSE;
CREATE INDEX idx_refresh_tokens_token ON refresh_tokens (token) WHERE deleted = FALSE;

COMMENT ON TABLE refresh_tokens IS 'JWT refresh tokens for session management';
