-- =============================================================================
-- V17: Seed sample payments from Figma Payment Overview / Receipt History
-- =============================================================================

INSERT INTO payments (
    client_id,
    product_id,
    invoice_number,
    payment_date,
    amount,
    bank_type,
    payment_mode,
    remarks,
    status
)
SELECT
    c.id,
    c.product_id,
    v.invoice_number,
    v.payment_date,
    v.amount,
    v.bank_type,
    v.payment_mode,
    v.remarks,
    'ACTIVE'
FROM (
    SELECT 'rahul@caterers.in' AS client_email, 'INV-0231' AS invoice_number, '2025-07-18' AS payment_date, 50000.00 AS amount, 'GPay' AS bank_type, 'UPI' AS payment_mode, 'First installment for ERP rollout' AS remarks
    UNION ALL
    SELECT 'hello@blueleaf.co', 'INV-0232', '2025-07-17', 35000.00, 'HDFC', 'BANK_TRANSFER', 'Advance payment for POS setup'
    UNION ALL
    SELECT 'karan@bajajgroup.in', 'INV-0233', '2025-07-16', 250000.00, 'ICICI', 'BANK_TRANSFER', 'Milestone 1 payment'
    UNION ALL
    SELECT 'sales@aryain.com', 'INV-0234', '2025-07-15', 100000.00, 'Cash', 'CASH', 'On-site cash collection'
) v
JOIN clients c ON LOWER(c.email) = LOWER(v.client_email) AND c.deleted = 0;
