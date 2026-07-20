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
    VALUES
        ('rahul@caterers.in', 'INV-0231', DATE '2025-07-18', 50000.00, 'GPay', 'UPI',
         'First installment for ERP rollout'),
        ('hello@blueleaf.co', 'INV-0232', DATE '2025-07-17', 35000.00, 'HDFC', 'BANK_TRANSFER',
         'Advance payment for POS setup'),
        ('karan@bajajgroup.in', 'INV-0233', DATE '2025-07-16', 250000.00, 'ICICI', 'BANK_TRANSFER',
         'Milestone 1 payment'),
        ('sales@aryain.com', 'INV-0234', DATE '2025-07-15', 100000.00, 'Cash', 'CASH',
         'On-site cash collection')
) AS v(client_email, invoice_number, payment_date, amount, bank_type, payment_mode, remarks)
JOIN clients c ON LOWER(c.email) = LOWER(v.client_email) AND c.deleted = FALSE;
