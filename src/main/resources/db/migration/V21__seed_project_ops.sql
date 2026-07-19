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
    VALUES
        ('karan@bajajgroup.in', 'BACKEND', 'admin@justcatering.com',
         'ABC Caterers ERP Rollout', DATE '2025-09-30', 25000.00),
        ('rahul@caterers.in', 'FRONTEND', 'admin@justcatering.com',
         'Rahul Caterers Menu Module', DATE '2025-08-15', 15000.00)
) AS v(
    client_email, department_code, user_email,
    project_name, close_date, reward_amount
)
JOIN clients c ON LOWER(c.email) = LOWER(v.client_email) AND c.deleted = FALSE
LEFT JOIN departments d ON d.code = v.department_code AND d.deleted = FALSE
LEFT JOIN users u ON LOWER(u.email) = LOWER(v.user_email) AND u.deleted = FALSE;

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
    VALUES
        ('karan@bajajgroup.in', 'BACKEND',
         DATE '2025-08-31', DATE '2025-09-30',
         'Additional GST invoice customization scope added after client review')
) AS v(
    client_email, department_code,
    current_deadline, new_deadline, reason
)
JOIN clients c ON LOWER(c.email) = LOWER(v.client_email) AND c.deleted = FALSE
LEFT JOIN departments d ON d.code = v.department_code AND d.deleted = FALSE;
