#!/usr/bin/env bash
# Import local PostgreSQL dump into Railway Postgres.
#
# Prerequisites:
#   1. Railway project linked: railway link -p <project-id> -e <environment-id>
#   2. PostgreSQL database added in Railway (NOT MySQL)
#   3. Docker installed (uses postgres:16-alpine image for psql)
#
# Usage:
#   ./scripts/import-to-railway-postgres.sh [path-to-dump.sql]
#
# Or with explicit URL:
#   DATABASE_URL='postgresql://user:pass@host:port/railway' ./scripts/import-to-railway-postgres.sh dump.sql

set -euo pipefail

DUMP_FILE="${1:-$HOME/Downloads/just_catering_superadmin_postgres_20260720_080546.sql}"

if [[ ! -f "$DUMP_FILE" ]]; then
  echo "SQL dump not found: $DUMP_FILE"
  exit 1
fi

if [[ -z "${DATABASE_URL:-}" ]]; then
  echo "Fetching DATABASE_PUBLIC_URL from Railway Postgres service..."
  DATABASE_URL="$(railway variables --service Postgres --json 2>/dev/null | python3 -c "
import json, sys
data = json.load(sys.stdin)
print(data.get('DATABASE_PUBLIC_URL') or data.get('DATABASE_URL') or '')
" || true)"
fi

if [[ -z "${DATABASE_URL:-}" ]]; then
  echo "ERROR: DATABASE_URL not set."
  echo "Add PostgreSQL in Railway dashboard, then either:"
  echo "  export DATABASE_URL='postgresql://...'  # from Railway → Postgres → Connect"
  echo "  or name the service 'Postgres' and re-run this script."
  exit 1
fi

echo "Importing $DUMP_FILE into Railway PostgreSQL..."
docker run --rm -i postgres:16-alpine psql "$DATABASE_URL" < "$DUMP_FILE"
echo "Import completed."
