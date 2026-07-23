-- =============================================================================
-- V41: Expense categories for Overall Expenses / Add Category
-- =============================================================================

CREATE TABLE expense_categories (
    id              BIGSERIAL       PRIMARY KEY,
    uuid            UUID            NOT NULL DEFAULT gen_random_uuid(),
    name            VARCHAR(100)    NOT NULL,
    code            VARCHAR(30),
    icon_key        VARCHAR(50),
    sort_order      INTEGER         NOT NULL DEFAULT 0,
    created_at      TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         BOOLEAN         NOT NULL DEFAULT FALSE,
    status          VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version         BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_expense_categories_uuid UNIQUE (uuid),
    CONSTRAINT chk_expense_categories_status CHECK (status IN ('ACTIVE', 'INACTIVE'))
);

CREATE UNIQUE INDEX uq_expense_categories_name_active
    ON expense_categories (LOWER(name))
    WHERE deleted = FALSE;

CREATE UNIQUE INDEX uq_expense_categories_code_active
    ON expense_categories (UPPER(code))
    WHERE deleted = FALSE AND code IS NOT NULL;

CREATE INDEX idx_expense_categories_status ON expense_categories (status) WHERE deleted = FALSE;
CREATE INDEX idx_expense_categories_deleted ON expense_categories (deleted);
CREATE INDEX idx_expense_categories_sort_order ON expense_categories (sort_order) WHERE deleted = FALSE;

INSERT INTO expense_categories (name, code, icon_key, sort_order, status, deleted, version)
VALUES
    ('Travel Expense', 'TRAVEL', 'travel', 1, 'ACTIVE', FALSE, 0),
    ('Food Expense', 'FOOD', 'food', 2, 'ACTIVE', FALSE, 0),
    ('Office Expenses', 'OFFICE', 'office', 3, 'ACTIVE', FALSE, 0),
    ('Other', 'OTHER', 'other', 4, 'ACTIVE', FALSE, 0);
