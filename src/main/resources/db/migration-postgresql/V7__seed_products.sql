-- =============================================================================
-- V7: Seed products from Figma catalog
-- =============================================================================

INSERT INTO products (name, code, product_type, description, default_reward_amount, status)
VALUES
    ('Just Catering X', 'JUST_CATERING_X', 'Just Catering', 'Just Catering X product edition', 5000.00, 'ACTIVE'),
    ('Just Catering Pro', 'JUST_CATERING_PRO', 'Just Catering', 'Just Catering Pro product edition', 5000.00, 'ACTIVE'),
    ('Just Catering ERP', 'JUST_CATERING_ERP', 'Just Catering', 'Just Catering ERP suite', 5000.00, 'ACTIVE'),
    ('Just Retail POS', 'JUST_RETAIL_POS', 'Just Retail', 'Retail point-of-sale product', 5000.00, 'ACTIVE'),
    ('Just Interior CRM', 'JUST_INTERIOR_CRM', 'Just Interior', 'Interior CRM product', 5000.00, 'ACTIVE'),
    ('Just Event Planner', 'JUST_EVENT_PLANNER', 'Just Event', 'Event planning product', 5000.00, 'ACTIVE'),
    ('JUST TAP', 'JUST_TAP', 'Just Tap', 'Just Tap product', 5000.00, 'ACTIVE'),
    ('JUST VENDOR', 'JUST_VENDOR', 'Just Vendor', 'Just Vendor product', 5000.00, 'ACTIVE'),
    ('JUST WEDDING', 'JUST_WEDDING', 'Just Wedding', 'Just Wedding product', 5000.00, 'ACTIVE');
