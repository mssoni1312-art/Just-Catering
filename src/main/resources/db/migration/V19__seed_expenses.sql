-- =============================================================================
-- V19: Seed sample travel and office expenses
-- =============================================================================

INSERT INTO expenses (
    client_id,
    title,
    expense_type,
    member_user_id,
    expense_date,
    paid_date,
    due_date,
    amount,
    payment_mode,
    from_city,
    to_city,
    from_date,
    to_date,
    km,
    account_contact,
    remarks,
    bill_url,
    status
)
SELECT
    c.id,
    v.title,
    v.expense_type,
    u.id,
    v.expense_date,
    v.paid_date,
    v.due_date,
    v.amount,
    v.payment_mode,
    v.from_city,
    v.to_city,
    v.from_date,
    v.to_date,
    v.km,
    v.account_contact,
    v.remarks,
    v.bill_url,
    'ACTIVE'
FROM (
    SELECT 'karan@bajajgroup.in' AS client_email, 'Pune site visit travel' AS title, 'TRAVEL' AS expense_type, 'admin@justcatering.com' AS member_user_email, '2025-07-10' AS expense_date, '2025-07-12' AS paid_date, '2025-07-15' AS due_date, 4850.00 AS amount, 'UPI' AS payment_mode, 'Mumbai' AS from_city, 'Pune' AS to_city, '2025-07-10' AS from_date, '2025-07-11' AS to_date, 148.50 AS km, 'Karan Bajaj' AS account_contact, 'Client onboarding site visit' AS remarks, NULL AS bill_url
    UNION ALL
    SELECT 'rahul@caterers.in', 'Ahmedabad client meeting', 'TRAVEL', 'admin@justcatering.com', '2025-07-08', '2025-07-09', NULL, 6200.00, 'Bank Transfer', 'Mumbai', 'Ahmedabad', '2025-07-08', '2025-07-08', 524.00, 'Rahul Sharma', 'Flight and cab for ERP demo', NULL
    UNION ALL
    SELECT NULL, 'Office stationery purchase', 'OFFICE', 'admin@justcatering.com', '2025-07-05', '2025-07-06', NULL, 3250.00, 'Cash', NULL, NULL, NULL, NULL, NULL, 'Accounts Team', 'Printer paper, pens, and folders', NULL
    UNION ALL
    SELECT NULL, 'Monthly office internet bill', 'OFFICE', 'admin@justcatering.com', '2025-07-01', '2025-07-03', '2025-07-05', 1899.00, 'Auto Debit', NULL, NULL, NULL, NULL, NULL, 'ISP Billing', 'Broadband subscription for HQ', NULL
    UNION ALL
    SELECT 'hello@blueleaf.co', 'Client lunch meeting', 'FOOD', 'admin@justcatering.com', '2025-07-16', '2025-07-16', NULL, 1450.00, 'Card', NULL, NULL, NULL, NULL, NULL, 'Vivek Soni', 'POS demo follow-up lunch', NULL
) v
LEFT JOIN clients c ON v.client_email IS NOT NULL
    AND LOWER(c.email) = LOWER(v.client_email)
    AND c.deleted = 0
LEFT JOIN users u ON LOWER(u.email) = LOWER(v.member_user_email) AND u.deleted = 0;
