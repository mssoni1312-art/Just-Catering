-- =============================================================================
-- V15: Seed sample client queries for ABC Caterers and Rahul Caterers
-- =============================================================================

INSERT INTO client_queries (
    client_id,
    title,
    query_type,
    assigned_user_id,
    department_id,
    priority,
    query_status,
    remarks,
    image_url,
    completed_at,
    status
)
SELECT
    c.id,
    v.title,
    v.query_type,
    u.id,
    d.id,
    v.priority,
    v.query_status,
    v.remarks,
    v.image_url,
    v.completed_at,
    'ACTIVE'
FROM (
    SELECT 'karan@bajajgroup.in' AS client_email, 'GST invoice format customization' AS title, 'REQUIREMENT' AS query_type, 'admin@justcatering.com' AS assigned_user_email, 'BACKEND' AS department_code, 'URGENT' AS priority, 'IN_PROGRESS' AS query_status, 'Client needs custom GST fields on invoices' AS remarks, NULL AS image_url, NULL AS completed_at
    UNION ALL
    SELECT 'karan@bajajgroup.in', 'FSSAI compliance documentation', 'COMPLIANCE', 'admin@justcatering.com', 'SALES', 'HIGH', 'NEEDS_ATTENTION', 'Awaiting signed compliance forms from client', NULL, NULL
    UNION ALL
    SELECT 'rahul@caterers.in', 'Menu module requirements gathering', 'REQUIREMENT', 'admin@justcatering.com', 'FRONTEND', 'HIGH', 'PENDING', 'Schedule workshop for menu and recipe workflows', NULL, NULL
    UNION ALL
    SELECT 'rahul@caterers.in', 'Signed service agreement upload', 'DOCUMENT', 'admin@justcatering.com', 'SALES', 'MEDIUM', 'COMPLETED', 'Agreement received and archived', '/uploads/queries/rahul-service-agreement.pdf', '2025-07-10 14:30:00+00'
) v
JOIN clients c ON LOWER(c.email) = LOWER(v.client_email) AND c.deleted = 0
LEFT JOIN users u ON LOWER(u.email) = LOWER(v.assigned_user_email) AND u.deleted = 0
LEFT JOIN departments d ON d.code = v.department_code AND d.deleted = 0;
