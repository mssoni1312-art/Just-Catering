-- =============================================================================
-- V21: Seed sample project ops data for ABC Caterers and Rahul Caterers
-- =============================================================================

INSERT INTO client_manager_assignments (
    client_id,
    department_id,
    user_id,
    project_name,
    close_date,
    reward_amount,
    status
)
SELECT
    c.id,
    d.id,
    u.id,
    v.project_name,
    v.close_date,
    v.reward_amount,
    'ACTIVE'
FROM (
    SELECT 'karan@bajajgroup.in' AS client_email, 'BACKEND' AS department_code, 'admin@justcatering.com' AS user_email, 'ABC Caterers ERP Rollout' AS project_name, '2025-09-30' AS close_date, 25000.00 AS reward_amount
    UNION ALL
    SELECT 'rahul@caterers.in', 'FRONTEND', 'admin@justcatering.com', 'Rahul Caterers Menu Module', '2025-08-15', 15000.00
) v
JOIN clients c ON LOWER(c.email) = LOWER(v.client_email) AND c.deleted = 0
LEFT JOIN departments d ON d.code = v.department_code AND d.deleted = 0
LEFT JOIN users u ON LOWER(u.email) = LOWER(v.user_email) AND u.deleted = 0
WHERE u.id IS NOT NULL;

INSERT INTO client_deadlines (
    client_id,
    department_id,
    current_deadline,
    new_deadline,
    reason,
    status
)
SELECT
    c.id,
    d.id,
    v.current_deadline,
    v.new_deadline,
    v.reason,
    'ACTIVE'
FROM (
    SELECT 'karan@bajajgroup.in' AS client_email, 'BACKEND' AS department_code, '2025-08-31' AS current_deadline, '2025-09-30' AS new_deadline, 'Additional GST invoice customization scope added after client review' AS reason
) v
JOIN clients c ON LOWER(c.email) = LOWER(v.client_email) AND c.deleted = 0
LEFT JOIN departments d ON d.code = v.department_code AND d.deleted = 0;
