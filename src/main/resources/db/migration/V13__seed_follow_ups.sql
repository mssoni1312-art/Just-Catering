-- =============================================================================
-- V13: Seed sample follow-ups for existing clients
-- =============================================================================

INSERT INTO follow_ups (
    client_id,
    title,
    follow_up_type,
    assigned_user_id,
    follow_up_date,
    follow_up_time,
    follow_up_status,
    expected_budget,
    remark,
    next_follow_up_date,
    next_follow_up_time,
    reminder_minutes,
    status
)
SELECT
    c.id,
    v.title,
    v.follow_up_type,
    u.id,
    v.follow_up_date,
    v.follow_up_time,
    v.follow_up_status,
    v.expected_budget,
    v.remark,
    v.next_follow_up_date,
    v.next_follow_up_time,
    v.reminder_minutes,
    'ACTIVE'
FROM (
    SELECT 'rahul@caterers.in' AS client_email, 'ERP rollout discussion call' AS title, 'CALL' AS follow_up_type, 'admin@justcatering.com' AS assigned_user_email, '2025-07-20' AS follow_up_date, '10:30:00' AS follow_up_time, 'PENDING' AS follow_up_status, 150000.00 AS expected_budget, 'Discuss ERP modules and timeline' AS remark, '2025-07-27' AS next_follow_up_date, '10:30:00' AS next_follow_up_time, 30 AS reminder_minutes
    UNION ALL
    SELECT 'hello@blueleaf.co', 'POS demo meeting', 'MEETING', 'admin@justcatering.com', '2025-07-18', '14:00:00', 'COMPLETED', 90000.00, 'Demo completed; awaiting decision', NULL, NULL, 60
    UNION ALL
    SELECT 'sales@aryain.com', 'Send CRM proposal email', 'EMAIL', 'admin@justcatering.com', '2025-07-19', '11:00:00', 'PENDING', 250000.00, 'Include interior CRM feature list', '2025-07-22', '11:00:00', 15
    UNION ALL
    SELECT 'karan@bajajgroup.in', 'Production site visit', 'VISIT', 'admin@justcatering.com', '2025-07-17', '09:00:00', 'NO_RESPONSE', 650000.00, 'Client was unavailable; reschedule needed', '2025-07-24', '09:00:00', 45
    UNION ALL
    SELECT 'info@mangoevents.in', 'Event planner follow-up', 'OTHER', 'admin@justcatering.com', '2025-07-21', '16:00:00', 'CANCELLED', 100000.00, 'Client postponed evaluation', NULL, NULL, NULL
) v
JOIN clients c ON LOWER(c.email) = LOWER(v.client_email) AND c.deleted = 0
LEFT JOIN users u ON LOWER(u.email) = LOWER(v.assigned_user_email) AND u.deleted = 0;
