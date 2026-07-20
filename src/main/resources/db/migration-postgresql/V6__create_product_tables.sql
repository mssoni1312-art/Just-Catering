-- =============================================================================
-- V6: Product catalog tables
-- Module: Product
-- =============================================================================

CREATE TABLE products (
    id                      BIGSERIAL       PRIMARY KEY,
    uuid                    UUID            NOT NULL DEFAULT gen_random_uuid(),
    name                    VARCHAR(150)    NOT NULL,
    code                    VARCHAR(50)     NOT NULL,
    product_type            VARCHAR(100)    NOT NULL,
    description             VARCHAR(500),
    default_reward_amount   NUMERIC(12, 2)  NOT NULL DEFAULT 0.00,
    created_at              TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by              BIGINT,
    updated_by              BIGINT,
    deleted                 BOOLEAN         NOT NULL DEFAULT FALSE,
    status                  VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version                 BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_products_uuid UNIQUE (uuid),
    CONSTRAINT uq_products_code UNIQUE (code),
    CONSTRAINT chk_products_status CHECK (status IN ('ACTIVE', 'INACTIVE')),
    CONSTRAINT chk_products_reward_amount CHECK (default_reward_amount >= 0)
);

CREATE UNIQUE INDEX uq_products_name_type_active
    ON products (LOWER(name), LOWER(product_type))
    WHERE deleted = FALSE;

CREATE INDEX idx_products_status ON products (status) WHERE deleted = FALSE;
CREATE INDEX idx_products_type ON products (product_type) WHERE deleted = FALSE;
CREATE INDEX idx_products_name ON products (name) WHERE deleted = FALSE;
CREATE INDEX idx_products_deleted ON products (deleted);

COMMENT ON TABLE products IS 'Sellable product catalog (Just Catering X, Pro, etc.)';
COMMENT ON COLUMN products.product_type IS 'Product family/type shown in Figma Type field';
COMMENT ON COLUMN products.default_reward_amount IS 'Default reward amount from Settings reward rules';
