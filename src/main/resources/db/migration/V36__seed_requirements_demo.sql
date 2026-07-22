-- =============================================================================
-- V36: Seed on-site requirement demo data
-- =============================================================================

INSERT INTO requirements (
    client_id,
    title,
    description,
    notes,
    check_in_enabled,
    check_in_at,
    check_in_latitude,
    check_in_longitude,
    check_in_address,
    check_out_enabled,
    voice_url,
    voice_duration_seconds,
    status
)
SELECT
    c.id,
    v.title,
    v.description,
    v.notes,
    v.check_in_enabled,
    v.check_in_at,
    v.check_in_latitude,
    v.check_in_longitude,
    v.check_in_address,
    v.check_out_enabled,
    v.voice_url,
    v.voice_duration_seconds,
    'ACTIVE'
FROM clients c
CROSS JOIN (
    SELECT
        'Venue Inspection' AS title,
        'Inspect the venue and ensure all setup is complete.' AS description,
        'Check seating arrangement, lighting & parking.' AS notes,
        1 AS check_in_enabled,
        '2025-07-13T14:15:00Z' AS check_in_at,
        23.0225 AS check_in_latitude,
        72.5714 AS check_in_longitude,
        'Ahmedabad, Gujarat, India; CG Road, Navrangpura, Ahmedabad — 380009' AS check_in_address,
        1 AS check_out_enabled,
        '/api/files/venue-inspection-voice.m4a' AS voice_url,
        80 AS voice_duration_seconds
    UNION ALL
    SELECT
        'Client Meeting',
        'Discuss final menu and service flow with client.',
        'Client prefers Jain menu with live counters.',
        0,
        '2025-07-12T11:00:00Z',
        NULL,
        NULL,
        NULL,
        0,
        NULL,
        NULL
    UNION ALL
    SELECT
        'Staff Briefing',
        'Brief the serving team on event timeline.',
        'Ensure all staff are aware of dress code.',
        0,
        '2025-07-11T09:30:00Z',
        NULL,
        NULL,
        NULL,
        0,
        NULL,
        NULL
) v
WHERE c.deleted = 0
  AND NOT EXISTS (
      SELECT 1
      FROM requirements r
      WHERE r.client_id = c.id
        AND r.title = v.title
        AND r.deleted = 0
  );

INSERT INTO requirement_documents (
    requirement_id,
    file_name,
    file_url,
    file_size_bytes,
    content_type,
    status
)
SELECT
    r.id,
    v.file_name,
    v.file_url,
    v.file_size_bytes,
    v.content_type,
    'ACTIVE'
FROM requirements r
JOIN (
    SELECT 'Venue Inspection' AS title, 'Venue_Report_13072025.pdf' AS file_name,
           '/api/files/venue-report-13072025.pdf' AS file_url,
           1258291 AS file_size_bytes, 'application/pdf' AS content_type
    UNION ALL
    SELECT 'Client Meeting', 'Meeting_Notes_12072025.docx',
           '/api/files/meeting-notes-12072025.docx',
           552960, 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
) v ON v.title = r.title
WHERE r.deleted = 0
  AND NOT EXISTS (
      SELECT 1
      FROM requirement_documents d
      WHERE d.requirement_id = r.id
        AND d.file_name = v.file_name
        AND d.deleted = 0
  );
