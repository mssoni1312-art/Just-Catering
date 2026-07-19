-- =============================================================================
-- V26: Product type and product name lookup tables
-- Module: Product (Add Product modal dropdowns)
-- =============================================================================

CREATE TABLE product_types (
    id              BIGSERIAL       PRIMARY KEY,
    uuid            UUID            NOT NULL DEFAULT gen_random_uuid(),
    name            VARCHAR(100)    NOT NULL,
    created_at      TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         BOOLEAN         NOT NULL DEFAULT FALSE,
    status          VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version         BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_product_types_uuid UNIQUE (uuid),
    CONSTRAINT chk_product_types_status CHECK (status IN ('ACTIVE', 'INACTIVE'))
);

CREATE UNIQUE INDEX uq_product_types_name_active
    ON product_types (LOWER(name))
    WHERE deleted = FALSE;

CREATE INDEX idx_product_types_status ON product_types (status) WHERE deleted = FALSE;
CREATE INDEX idx_product_types_deleted ON product_types (deleted);

COMMENT ON TABLE product_types IS 'Product families/types for the Add Product Type dropdown';

CREATE TABLE product_names (
    id              BIGSERIAL       PRIMARY KEY,
    uuid            UUID            NOT NULL DEFAULT gen_random_uuid(),
    product_type_id BIGINT          NOT NULL,
    name            VARCHAR(150)    NOT NULL,
    created_at      TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         BOOLEAN         NOT NULL DEFAULT FALSE,
    status          VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version         BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_product_names_uuid UNIQUE (uuid),
    CONSTRAINT fk_product_names_type
        FOREIGN KEY (product_type_id) REFERENCES product_types (id),
    CONSTRAINT chk_product_names_status CHECK (status IN ('ACTIVE', 'INACTIVE'))
);

CREATE UNIQUE INDEX uq_product_names_type_name_active
    ON product_names (product_type_id, LOWER(name))
    WHERE deleted = FALSE;

CREATE INDEX idx_product_names_type_id ON product_names (product_type_id) WHERE deleted = FALSE;
CREATE INDEX idx_product_names_status ON product_names (status) WHERE deleted = FALSE;
CREATE INDEX idx_product_names_deleted ON product_names (deleted);

COMMENT ON TABLE product_names IS 'Product names scoped to a product type for the Add Product Name dropdown';
