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
    SELECT 'Rahul Caterers' AS client_name, 'Rahul Sharma' AS contact_person, '+919822011223' AS mobile, 'rahul@caterers.in' AS email, NULL AS gst_number, 'Catering Services' AS client_type, 'JUST_CATERING_ERP' AS product_code, '2025-07-17' AS deal_date, 125000.00 AS total_amount, 150000.00 AS budget, 'Interested in ERP rollout' AS notes, 'INTERESTED' AS client_stage, 'HIGH' AS priority, 25.00 AS requirements_completion_percentage, 'Mumbai' AS city, 'Maharashtra' AS state
    UNION ALL
    SELECT 'Blueleaf Foods', 'Vivek Soni', '+919811055221', 'hello@blueleaf.co', NULL, 'Catering Services', 'JUST_RETAIL_POS', '2025-07-16', 85000.00, 90000.00, NULL, 'IN_PROGRESS', 'MEDIUM', 48.00, 'Delhi', 'Delhi'
    UNION ALL
    SELECT 'Arya Interiors', 'Ritesh Sharma', '+919009071120', 'sales@aryain.com', NULL, 'Interior Services', 'JUST_INTERIOR_CRM', '2025-07-15', 210000.00, 250000.00, NULL, 'ACTIVE', 'HIGH', 72.00, 'Ahmedabad', 'Gujarat'
    UNION ALL
    SELECT 'Mango Event', 'Pradeep Patel', '+919845520011', 'info@mangoevents.in', NULL, 'Event Services', 'JUST_EVENT_PLANNER', '2025-07-14', 95000.00, 100000.00, NULL, 'ACTIVE', 'MEDIUM', 60.00, 'Bangalore', 'Karnataka'
    UNION ALL
    SELECT 'Suryodaya Textiles', 'Suraj Patel', '+919004033212', 'ops@suryodaya.co', NULL, 'Textile Services', 'JUST_CATERING_X', '2025-07-12', 55000.00, 60000.00, NULL, 'ON_HOLD', 'LOW', 15.00, 'Surat', 'Gujarat'
    UNION ALL
    SELECT 'ABC Caterers Pvt Ltd', 'Karan Bajaj', '+919030088776', 'karan@bajajgroup.in', '27AABCU9603R1ZM', 'Catering Services', 'JUST_CATERING_PRO', '2025-05-10', 615000.00, 650000.00, 'Deployed production client', 'ACTIVE', 'URGENT', 85.00, 'Pune', 'Maharashtra'
) v
JOIN products p ON p.code = v.product_code AND p.deleted = 0;
