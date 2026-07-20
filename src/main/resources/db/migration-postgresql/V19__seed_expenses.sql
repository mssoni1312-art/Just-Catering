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
    VALUES
        ('karan@bajajgroup.in', 'Pune site visit travel', 'TRAVEL', 'admin@justcatering.com',
         DATE '2025-07-10', DATE '2025-07-12', DATE '2025-07-15', 4850.00, 'UPI',
         'Mumbai', 'Pune', DATE '2025-07-10', DATE '2025-07-11', 148.50,
         'Karan Bajaj', 'Client onboarding site visit', NULL),
        ('rahul@caterers.in', 'Ahmedabad client meeting', 'TRAVEL', 'admin@justcatering.com',
         DATE '2025-07-08', DATE '2025-07-09', NULL, 6200.00, 'Bank Transfer',
         'Mumbai', 'Ahmedabad', DATE '2025-07-08', DATE '2025-07-08', 524.00,
         'Rahul Sharma', 'Flight and cab for ERP demo', NULL),
        (NULL, 'Office stationery purchase', 'OFFICE', 'admin@justcatering.com',
         DATE '2025-07-05', DATE '2025-07-06', NULL, 3250.00, 'Cash',
         NULL, NULL, NULL, NULL, NULL,
         'Accounts Team', 'Printer paper, pens, and folders', NULL),
        (NULL, 'Monthly office internet bill', 'OFFICE', 'admin@justcatering.com',
         DATE '2025-07-01', DATE '2025-07-03', DATE '2025-07-05', 1899.00, 'Auto Debit',
         NULL, NULL, NULL, NULL, NULL,
         'ISP Billing', 'Broadband subscription for HQ', NULL),
        ('hello@blueleaf.co', 'Client lunch meeting', 'FOOD', 'admin@justcatering.com',
         DATE '2025-07-16', DATE '2025-07-16', NULL, 1450.00, 'Card',
         NULL, NULL, NULL, NULL, NULL,
         'Vivek Soni', 'POS demo follow-up lunch', NULL)
) AS v(
    client_email, title, expense_type, member_user_email,
    expense_date, paid_date, due_date, amount, payment_mode,
    from_city, to_city, from_date, to_date, km,
    account_contact, remarks, bill_url
)
LEFT JOIN clients c ON v.client_email IS NOT NULL
    AND LOWER(c.email) = LOWER(v.client_email)
    AND c.deleted = FALSE
LEFT JOIN users u ON LOWER(u.email) = LOWER(v.member_user_email) AND u.deleted = FALSE;
