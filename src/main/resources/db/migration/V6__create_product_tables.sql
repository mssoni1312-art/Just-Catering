-- =============================================================================
-- V6: Product catalog tables
-- Module: Product
-- =============================================================================

CREATE TABLE products (
    id                      INTEGER       PRIMARY KEY AUTOINCREMENT,
    uuid            TEXT            NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
    name                    VARCHAR(150)    NOT NULL,
    code                    VARCHAR(50)     NOT NULL,
    product_type            VARCHAR(100)    NOT NULL,
    description             VARCHAR(500),
    default_reward_amount   NUMERIC(12, 2)  NOT NULL DEFAULT 0.00,
    created_at              TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by              BIGINT,
    updated_by              BIGINT,
    deleted                 INTEGER         NOT NULL DEFAULT 0,
    status                  VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version                 BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_products_uuid UNIQUE (uuid),
    CONSTRAINT uq_products_code UNIQUE (code),
    CONSTRAINT chk_products_status CHECK (status IN ('ACTIVE', 'INACTIVE')),
    CONSTRAINT chk_products_reward_amount CHECK (default_reward_amount >= 0)
);

CREATE UNIQUE INDEX uq_products_name_type_active
    ON products (LOWER(name), LOWER(product_type))
    WHERE deleted = 0;

CREATE INDEX idx_products_status ON products (status) WHERE deleted = 0;
CREATE INDEX idx_products_type ON products (product_type) WHERE deleted = 0;
CREATE INDEX idx_products_name ON products (name) WHERE deleted = 0;
CREATE INDEX idx_products_deleted ON products (deleted);
