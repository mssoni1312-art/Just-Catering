#!/usr/bin/env python3
"""Convert PostgreSQL Flyway migrations to SQLite-compatible SQL."""

from __future__ import annotations

import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SRC = ROOT / "src/main/resources/db/migration-postgresql"
DST = ROOT / "src/main/resources/db/migration"

SQLITE_UUID_DEFAULT = (
    "(lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4'"
    " || substr(lower(hex(randomblob(2))),2) || '-'"
    " || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2)"
    " || '-' || lower(hex(randomblob(6))))"
)


def split_sql_row(row: str) -> list[str]:
    parts: list[str] = []
    current: list[str] = []
    in_string = False
    i = 0
    while i < len(row):
        ch = row[i]
        if ch == "'" and not in_string:
            in_string = True
            current.append(ch)
        elif ch == "'" and in_string:
            if i + 1 < len(row) and row[i + 1] == "'":
                current.append("''")
                i += 1
            else:
                in_string = False
                current.append(ch)
        elif ch == "," and not in_string:
            parts.append("".join(current).strip())
            current = []
        else:
            current.append(ch)
        i += 1
    if current:
        parts.append("".join(current).strip())
    return parts


def parse_values_rows(rows_part: str) -> list[str]:
    rows: list[str] = []
    depth = 0
    current: list[str] = []
    for ch in rows_part:
        if ch == "(":
            depth += 1
            if depth == 1:
                current = []
                continue
        elif ch == ")":
            depth -= 1
            if depth == 0:
                rows.append("".join(current).strip())
                continue
        if depth >= 1:
            current.append(ch)
    return rows


def convert_values_blocks(content: str) -> str:
    pattern = re.compile(
        r"FROM\s*\(\s*VALUES\s*(?P<rows>.*?)\)\s*AS\s*v\(\s*(?P<cols>[^)]+)\)",
        re.DOTALL | re.IGNORECASE,
    )

    def convert_block(match: re.Match[str]) -> str:
        cols = [c.strip() for c in match.group("cols").split(",")]
        rows = parse_values_rows(match.group("rows"))
        selects: list[str] = []
        for index, row in enumerate(rows):
            values = split_sql_row(row)
            if len(values) != len(cols):
                raise ValueError(f"Column/value count mismatch: {cols} vs {values}")
            if index == 0:
                select = "SELECT " + ", ".join(f"{value} AS {col}" for value, col in zip(values, cols))
            else:
                select = "SELECT " + ", ".join(values)
            selects.append(select)
        return "FROM (\n    " + "\n    UNION ALL\n    ".join(selects) + "\n) v"

    return pattern.sub(convert_block, content)


def convert_base(content: str) -> str:
    lines = []
    for line in content.splitlines():
        if line.strip().startswith("COMMENT ON "):
            continue
        lines.append(line)
    content = "\n".join(lines)

    replacements = [
        (r"\bBIGSERIAL\b", "INTEGER"),
        (r"\bUUID\b", "TEXT"),
        (r"TIMESTAMPTZ '([^']+)'", r"'\1'"),
        (r"\bTIMESTAMPTZ\b", "TEXT"),
        (r"\bBOOLEAN\b", "INTEGER"),
        (r"\bDEFAULT gen_random_uuid\(\)", ""),
        (r"\bDEFAULT FALSE\b", "DEFAULT 0"),
        (r"\bDEFAULT TRUE\b", "DEFAULT 1"),
        (r"::TIME\b", ""),
        (r"DATE '([^']+)'", r"'\1'"),
        (r"deleted = FALSE", "deleted = 0"),
        (r"revoked = FALSE", "revoked = 0"),
        (r"\bTRUE\b", "1"),
        (r"\bFALSE\b", "0"),
    ]
    for pattern, repl in replacements:
        content = re.sub(pattern, repl, content)

    content = re.sub(
        r"uuid\s+TEXT\s+NOT NULL\s*,",
        f"uuid            TEXT            NOT NULL DEFAULT {SQLITE_UUID_DEFAULT},",
        content,
    )
    content = content.replace(
        "INTEGER       PRIMARY KEY,",
        "INTEGER       PRIMARY KEY AUTOINCREMENT,",
    )
    content = convert_values_blocks(content)

    return content.strip() + "\n"


def convert_v1(content: str) -> str:
    content = convert_base(content)
    content = content.replace(
        "    CONSTRAINT uq_role_permissions_role_perm UNIQUE (role_id, permission_id),\n", ""
    )
    content = content.replace(
        "    CONSTRAINT uq_user_roles_user_role UNIQUE (user_id, role_id),\n", ""
    )
    return content


def convert_v3(content: str) -> str:
    return """-- =============================================================================
-- V3: Partial unique indexes for soft-deleted join tables
-- =============================================================================

CREATE UNIQUE INDEX IF NOT EXISTS uq_role_permissions_role_perm_active
    ON role_permissions (role_id, permission_id)
    WHERE deleted = 0;

CREATE UNIQUE INDEX IF NOT EXISTS uq_user_roles_user_role_active
    ON user_roles (user_id, role_id)
    WHERE deleted = 0;
"""


def convert_v8(content: str) -> str:
    content = convert_base(content)
    content = content.replace(
        """    CONSTRAINT chk_clients_stage CHECK (client_stage IN (
        'INTERESTED', 'IN_PROGRESS', 'ACTIVE', 'ON_HOLD', 'COMPLETED', 'CHURNED'
    )),""",
        """    CONSTRAINT chk_clients_stage CHECK (client_stage IN (
        'NEW', 'INTERESTED', 'IN_PROGRESS', 'ACTIVE', 'ON_HOLD', 'COMPLETED', 'CHURNED'
    )),""",
    )
    content = content.replace("notes                               VARCHAR(1000),", "notes                               VARCHAR(300),")
    return content


def convert_v12(content: str) -> str:
    content = convert_base(content)
    content = re.sub(
        r"client_id\s+BIGINT\s+NOT NULL,",
        "client_id               BIGINT,\n    lead_id                 BIGINT,",
        content,
    )
    content = content.replace(
        "    CONSTRAINT uq_follow_ups_uuid UNIQUE (uuid),\n    lead_id                 BIGINT,\n    CONSTRAINT fk_follow_ups_client",
        "    CONSTRAINT uq_follow_ups_uuid UNIQUE (uuid),\n    CONSTRAINT fk_follow_ups_client",
    )
    if "CONSTRAINT fk_follow_ups_lead" not in content:
        content = content.replace(
            """    CONSTRAINT fk_follow_ups_client
        FOREIGN KEY (client_id) REFERENCES clients (id),""",
            """    CONSTRAINT fk_follow_ups_client
        FOREIGN KEY (client_id) REFERENCES clients (id),
    CONSTRAINT fk_follow_ups_lead
        FOREIGN KEY (lead_id) REFERENCES leads (id),""",
        )
    if "CONSTRAINT chk_follow_ups_client_or_lead" not in content:
        content = content.replace(
            """    CONSTRAINT chk_follow_ups_reminder_minutes CHECK (
        reminder_minutes IS NULL OR reminder_minutes >= 0
    )
);""",
            """    CONSTRAINT chk_follow_ups_reminder_minutes CHECK (
        reminder_minutes IS NULL OR reminder_minutes >= 0
    ),
    CONSTRAINT chk_follow_ups_client_or_lead CHECK (
        (client_id IS NOT NULL AND lead_id IS NULL)
        OR (client_id IS NULL AND lead_id IS NOT NULL)
    )
);""",
        )
    if "idx_follow_ups_lead_id" not in content:
        content = content.replace(
            "CREATE INDEX idx_follow_ups_client_id ON follow_ups (client_id) WHERE deleted = 0;",
            "CREATE INDEX idx_follow_ups_client_id ON follow_ups (client_id) WHERE deleted = 0;\n"
            "CREATE INDEX idx_follow_ups_lead_id ON follow_ups (lead_id) WHERE deleted = 0;",
        )
    return content


def convert_v22(_: str) -> str:
    return """-- V22 merged into V8 for SQLite (client_stage includes NEW).
"""


def convert_v23(_: str) -> str:
    return """-- V23 merged into V8 for SQLite (notes length 300).
"""


def convert_v25(_: str) -> str:
    return """-- =============================================================================
-- V25: Clear all sample/business data for a fresh start
-- =============================================================================

UPDATE departments SET parent_id = NULL WHERE parent_id IS NOT NULL;

DELETE FROM client_deadlines;
DELETE FROM client_manager_assignments;
DELETE FROM payments;
DELETE FROM expenses;
DELETE FROM client_queries;
DELETE FROM follow_ups;
DELETE FROM leads;
DELETE FROM clients;
DELETE FROM department_members;
DELETE FROM departments;
DELETE FROM products;
DELETE FROM refresh_tokens;
DELETE FROM user_roles;
DELETE FROM users;

DELETE FROM sqlite_sequence WHERE name IN (
    'client_deadlines',
    'client_manager_assignments',
    'payments',
    'expenses',
    'client_queries',
    'follow_ups',
    'leads',
    'clients',
    'department_members',
    'departments',
    'products',
    'refresh_tokens',
    'user_roles',
    'users'
);
"""


def convert_v27(_: str) -> str:
    return """-- V27 merged into V12 for SQLite (follow_ups supports leads).
"""


def convert_v21(content: str) -> str:
    content = convert_base(content)
    content = content.replace(
        "LEFT JOIN users u ON LOWER(u.email) = LOWER(v.user_email) AND u.deleted = 0;",
        "LEFT JOIN users u ON LOWER(u.email) = LOWER(v.user_email) AND u.deleted = 0\nWHERE u.id IS NOT NULL;",
    )
    return content


CONVERTERS = {
    "V1__create_auth_tables.sql": convert_v1,
    "V3__partial_unique_join_indexes.sql": convert_v3,
    "V8__create_client_tables.sql": convert_v8,
    "V12__create_follow_up_tables.sql": convert_v12,
    "V21__seed_project_ops.sql": convert_v21,
    "V22__add_new_client_stage.sql": convert_v22,
    "V23__alter_client_notes_length.sql": convert_v23,
    "V25__clear_sample_data.sql": convert_v25,
    "V27__follow_ups_support_leads.sql": convert_v27,
}


def main() -> None:
    DST.mkdir(parents=True, exist_ok=True)
    for src_file in sorted(SRC.glob("V*.sql")):
        raw = src_file.read_text(encoding="utf-8")
        converter = CONVERTERS.get(src_file.name, convert_base)
        converted = converter(raw)
        (DST / src_file.name).write_text(converted, encoding="utf-8")
        print(f"converted {src_file.name}")


if __name__ == "__main__":
    main()
