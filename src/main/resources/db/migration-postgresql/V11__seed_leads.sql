-- =============================================================================
-- V11: Seed sample meeting leads from Figma
-- =============================================================================

INSERT INTO leads (
    first_name,
    last_name,
    email,
    company_name,
    phone,
    state,
    city,
    approx_budget,
    product_id,
    notes,
    lead_stage,
    status
)
SELECT
    v.first_name,
    v.last_name,
    v.email,
    v.company_name,
    v.phone,
    v.state,
    v.city,
    v.approx_budget,
    p.id,
    v.notes,
    v.lead_stage,
    'ACTIVE'
FROM (
    VALUES
        ('Rahul', 'Sharma', 'rahul.sharma@lead.in', 'Rahul Caterers', '+919822011223',
         'Maharashtra', 'Mumbai', 150000.00, 'JUST_CATERING_ERP',
         'Interested in ERP rollout for catering operations', 'NEW'),
        ('Vivek', 'Soni', 'vivek.soni@lead.in', 'Blueleaf Foods', '+919811055221',
         'Delhi', 'Delhi', 90000.00, 'JUST_RETAIL_POS',
         'Exploring POS for retail outlets', 'CONTACTED'),
        ('Ritesh', 'Sharma', 'ritesh.sharma@lead.in', 'Arya Interiors', '+919009071120',
         'Gujarat', 'Ahmedabad', 250000.00, 'JUST_INTERIOR_CRM',
         NULL, 'QUALIFIED'),
        ('Pradeep', 'Patel', 'pradeep.patel@lead.in', 'Mango Event', '+919845520011',
         'Karnataka', 'Bangalore', 100000.00, 'JUST_EVENT_PLANNER',
         'Event planning demo requested', 'CONVERTED'),
        ('Suraj', 'Patel', 'suraj.patel@lead.in', 'Suryodaya Textiles', '+919004033212',
         'Gujarat', 'Surat', 60000.00, 'JUST_CATERING_X',
         'Budget constraints; follow up next quarter', 'LOST')
) AS v(
    first_name, last_name, email, company_name, phone, state, city,
    approx_budget, product_code, notes, lead_stage
)
LEFT JOIN products p ON p.code = v.product_code AND p.deleted = FALSE;
