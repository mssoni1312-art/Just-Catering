-- =============================================================================
-- V33: Seed requirement demo data for existing clients
-- =============================================================================

INSERT INTO client_queries (
    client_id,
    title,
    description,
    query_type,
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
    v.priority,
    v.query_status,
    v.remarks,
    v.scheduled_at::timestamptz,
    v.has_check_in_out,
    v.voice_url,
    v.voice_duration_seconds,
    v.document_url,
    v.document_name,
    v.document_size_bytes,
    v.document_content_type,
    'ACTIVE'
FROM clients c
CROSS JOIN (
    VALUES
        (
            'Venue Inspection',
            'Inspect the venue and ensure all setup is complete.',
            'HIGH',
            'PENDING',
            'Check seating arrangement, lighting & parking.',
            '2025-07-13T14:15:00Z',
            TRUE,
            '/api/files/venue-inspection-voice.m4a',
            80,
            '/api/files/venue-report-13072025.pdf',
            'Venue_Report_13072025.pdf',
            1258291,
            'application/pdf'
        ),
        (
            'Client Meeting',
            'Discuss final menu and service flow with client.',
            'MEDIUM',
            'IN_PROGRESS',
            'Client prefers Jain menu with live counters.',
            '2025-07-12T11:00:00Z',
            FALSE,
            NULL,
            NULL,
            '/api/files/meeting-notes-12072025.docx',
            'Meeting_Notes_12072025.docx',
            552960,
            'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
        ),
        (
            'Staff Briefing',
            'Brief the serving team on event timeline.',
            'MEDIUM',
            'PENDING',
            'Ensure all staff are aware of dress code.',
            '2025-07-11T09:30:00Z',
            FALSE,
            NULL,
            NULL,
            NULL,
            NULL,
            NULL,
            NULL
        )
) AS v(
    title,
    description,
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
    document_content_type
)
WHERE c.deleted = FALSE
  AND NOT EXISTS (
      SELECT 1
      FROM client_queries q
      WHERE q.client_id = c.id
        AND q.title = v.title
        AND q.deleted = FALSE
  );
