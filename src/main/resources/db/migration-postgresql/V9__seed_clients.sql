-- =============================================================================
-- V9: Seed sample clients from Figma
-- =============================================================================

INSERT INTO clients (
    client_name,
    contact_person,
    mobile,
    email,
    gst_number,
    client_type,
    product_id,
    deal_date,
    total_amount,
    budget,
    notes,
    client_stage,
    priority,
    requirements_completion_percentage,
    city,
    state,
    status
)
SELECT
    v.client_name,
    v.contact_person,
    v.mobile,
    v.email,
    v.gst_number,
    v.client_type,
    p.id,
    v.deal_date,
    v.total_amount,
    v.budget,
    v.notes,
    v.client_stage,
    v.priority,
    v.requirements_completion_percentage,
    v.city,
    v.state,
    'ACTIVE'
FROM (
    VALUES
        ('Rahul Caterers', 'Rahul Sharma', '+919822011223', 'rahul@caterers.in', NULL,
         'Catering Services', 'JUST_CATERING_ERP', DATE '2025-07-17', 125000.00, 150000.00,
         'Interested in ERP rollout', 'INTERESTED', 'HIGH', 25.00, 'Mumbai', 'Maharashtra'),
        ('Blueleaf Foods', 'Vivek Soni', '+919811055221', 'hello@blueleaf.co', NULL,
         'Catering Services', 'JUST_RETAIL_POS', DATE '2025-07-16', 85000.00, 90000.00,
         NULL, 'IN_PROGRESS', 'MEDIUM', 48.00, 'Delhi', 'Delhi'),
        ('Arya Interiors', 'Ritesh Sharma', '+919009071120', 'sales@aryain.com', NULL,
         'Interior Services', 'JUST_INTERIOR_CRM', DATE '2025-07-15', 210000.00, 250000.00,
         NULL, 'ACTIVE', 'HIGH', 72.00, 'Ahmedabad', 'Gujarat'),
        ('Mango Event', 'Pradeep Patel', '+919845520011', 'info@mangoevents.in', NULL,
         'Event Services', 'JUST_EVENT_PLANNER', DATE '2025-07-14', 95000.00, 100000.00,
         NULL, 'ACTIVE', 'MEDIUM', 60.00, 'Bangalore', 'Karnataka'),
        ('Suryodaya Textiles', 'Suraj Patel', '+919004033212', 'ops@suryodaya.co', NULL,
         'Textile Services', 'JUST_CATERING_X', DATE '2025-07-12', 55000.00, 60000.00,
         NULL, 'ON_HOLD', 'LOW', 15.00, 'Surat', 'Gujarat'),
        ('ABC Caterers Pvt Ltd', 'Karan Bajaj', '+919030088776', 'karan@bajajgroup.in', '27AABCU9603R1ZM',
         'Catering Services', 'JUST_CATERING_PRO', DATE '2025-05-10', 615000.00, 650000.00,
         'Deployed production client', 'ACTIVE', 'URGENT', 85.00, 'Pune', 'Maharashtra')
) AS v(
    client_name, contact_person, mobile, email, gst_number, client_type, product_code,
    deal_date, total_amount, budget, notes, client_stage, priority,
    requirements_completion_percentage, city, state
)
JOIN products p ON p.code = v.product_code AND p.deleted = FALSE;
