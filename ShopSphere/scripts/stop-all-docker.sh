#!/usr/bin/env bash
# Stops the full ShopSphere Docker stack (infra + all backend services).
# Frontend (Vite) runs outside Docker - close its window manually (or `pkill -f "vite"`).
#
# Usage:  ./scripts/stop-all-docker.sh           # keeps data volumes
#         ./scripts/stop-all-docker.sh --wipe    # also removes MySQL/Grafana/Prometheus/Sonar volumes

set -e
REPO_DIR="$(cd "$(dirname "$0")/.." && pwd)"
cd "$REPO_DIR"

if [ "${1:-}" = "--wipe" ]; then
  echo "Stopping stack and WIPING data volumes..."
  docker compose --profile all down -v
else
  echo "Stopping stack (data volumes preserved)..."
  docker compose --profile all down
fi

echo "Done. Close the frontend (Vite) window manually if it's still running."
