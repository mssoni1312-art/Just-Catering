-- =============================================================================
-- V32: Seed requirement list demo data matching mobile design
-- =============================================================================

INSERT INTO client_queries (
    client_id,
    title,
    description,
    query_type,
    assigned_user_id,
    department_id,
    priority,
    query_status,
    remarks,
    scheduled_at,
    has_check_in_out,
    voice_url,
    voice_duration_seconds,
    document_url,
    document_name,
    document_size_bytes,
    document_content_type,
    status
)
SELECT
    c.id,
    v.title,
    v.description,
    'REQUIREMENT',
    u.id,
    d.id,
    v.priority,
    v.query_status,
    v.remarks,
    v.scheduled_at,
    v.has_check_in_out,
    v.voice_url,
    v.voice_duration_seconds,
    v.document_url,
    v.document_name,
    v.document_size_bytes,
    v.document_content_type,
    'ACTIVE'
FROM (
    SELECT
        'rahul@caterers.in' AS client_email,
        'Venue Inspection' AS title,
        'Inspect the venue and ensure all setup is complete.' AS description,
        'admin@justcatering.com' AS assigned_user_email,
        'SALES' AS department_code,
        'HIGH' AS priority,
        'PENDING' AS query_status,
        'Check seating arrangement, lighting & parking.' AS remarks,
        '2025-07-13T14:15:00Z' AS scheduled_at,
        1 AS has_check_in_out,
        '/api/files/venue-inspection-voice.m4a' AS voice_url,
        80 AS voice_duration_seconds,
        '/api/files/venue-report-13072025.pdf' AS document_url,
        'Venue_Report_13072025.pdf' AS document_name,
        1258291 AS document_size_bytes,
        'application/pdf' AS document_content_type
    UNION ALL
    SELECT
        'rahul@caterers.in',
        'Client Meeting',
        'Discuss final menu and service flow with client.',
        'admin@justcatering.com',
        'SALES',
        'MEDIUM',
        'IN_PROGRESS',
        'Client prefers Jain menu with live counters.',
        '2025-07-12T11:00:00Z',
        0,
        NULL,
        NULL,
        '/api/files/meeting-notes-12072025.docx',
        'Meeting_Notes_12072025.docx',
        552960,
        'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
    UNION ALL
    SELECT
        'rahul@caterers.in',
        'Staff Briefing',
        'Brief the serving team on event timeline.',
        'admin@justcatering.com',
        'SALES',
        'MEDIUM',
        'PENDING',
        'Ensure all staff are aware of dress code.',
        '2025-07-11T09:30:00Z',
        0,
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        NULL
) v
JOIN clients c ON LOWER(c.email) = LOWER(v.client_email) AND c.deleted = 0
LEFT JOIN users u ON LOWER(u.email) = LOWER(v.assigned_user_email) AND u.deleted = 0
LEFT JOIN departments d ON d.code = v.department_code AND d.deleted = 0
WHERE NOT EXISTS (
    SELECT 1
    FROM client_queries q
    WHERE q.client_id = c.id
      AND q.title = v.title
      AND q.deleted = 0
);
