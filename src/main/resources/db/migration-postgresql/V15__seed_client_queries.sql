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
    VALUES
        ('karan@bajajgroup.in', 'GST invoice format customization', 'REQUIREMENT', 'admin@justcatering.com', 'BACKEND',
         'URGENT', 'IN_PROGRESS', 'Client needs custom GST fields on invoices', NULL, NULL),
        ('karan@bajajgroup.in', 'FSSAI compliance documentation', 'COMPLIANCE', 'admin@justcatering.com', 'SALES',
         'HIGH', 'NEEDS_ATTENTION', 'Awaiting signed compliance forms from client', NULL, NULL),
        ('rahul@caterers.in', 'Menu module requirements gathering', 'REQUIREMENT', 'admin@justcatering.com', 'FRONTEND',
         'HIGH', 'PENDING', 'Schedule workshop for menu and recipe workflows', NULL, NULL),
        ('rahul@caterers.in', 'Signed service agreement upload', 'DOCUMENT', 'admin@justcatering.com', 'SALES',
         'MEDIUM', 'COMPLETED', 'Agreement received and archived', '/uploads/queries/rahul-service-agreement.pdf',
         TIMESTAMPTZ '2025-07-10 14:30:00+00')
) AS v(
    client_email, title, query_type, assigned_user_email, department_code,
    priority, query_status, remarks, image_url, completed_at
)
JOIN clients c ON LOWER(c.email) = LOWER(v.client_email) AND c.deleted = FALSE
LEFT JOIN users u ON LOWER(u.email) = LOWER(v.assigned_user_email) AND u.deleted = FALSE
LEFT JOIN departments d ON d.code = v.department_code AND d.deleted = FALSE;
