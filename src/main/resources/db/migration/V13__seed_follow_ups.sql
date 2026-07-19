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
    v.follow_up_time::TIME,
    v.follow_up_status,
    v.expected_budget,
    v.remark,
    v.next_follow_up_date,
    v.next_follow_up_time::TIME,
    v.reminder_minutes,
    'ACTIVE'
FROM (
    VALUES
        ('rahul@caterers.in', 'ERP rollout discussion call', 'CALL', 'admin@justcatering.com',
         DATE '2025-07-20', '10:30:00', 'PENDING', 150000.00,
         'Discuss ERP modules and timeline', DATE '2025-07-27', '10:30:00', 30),
        ('hello@blueleaf.co', 'POS demo meeting', 'MEETING', 'admin@justcatering.com',
         DATE '2025-07-18', '14:00:00', 'COMPLETED', 90000.00,
         'Demo completed; awaiting decision', NULL, NULL, 60),
        ('sales@aryain.com', 'Send CRM proposal email', 'EMAIL', 'admin@justcatering.com',
         DATE '2025-07-19', '11:00:00', 'PENDING', 250000.00,
         'Include interior CRM feature list', DATE '2025-07-22', '11:00:00', 15),
        ('karan@bajajgroup.in', 'Production site visit', 'VISIT', 'admin@justcatering.com',
         DATE '2025-07-17', '09:00:00', 'NO_RESPONSE', 650000.00,
         'Client was unavailable; reschedule needed', DATE '2025-07-24', '09:00:00', 45),
        ('info@mangoevents.in', 'Event planner follow-up', 'OTHER', 'admin@justcatering.com',
         DATE '2025-07-21', '16:00:00', 'CANCELLED', 100000.00,
         'Client postponed evaluation', NULL, NULL, NULL)
) AS v(
    client_email, title, follow_up_type, assigned_user_email,
    follow_up_date, follow_up_time, follow_up_status, expected_budget,
    remark, next_follow_up_date, next_follow_up_time, reminder_minutes
)
JOIN clients c ON LOWER(c.email) = LOWER(v.client_email) AND c.deleted = FALSE
LEFT JOIN users u ON LOWER(u.email) = LOWER(v.assigned_user_email) AND u.deleted = FALSE;
