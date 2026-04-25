#!/usr/bin/env bash
# One-click launcher for the entire ShopSphere stack via Docker (macOS / Linux).
# - Ensures Docker Desktop is running (macOS: launches Docker.app)
# - Builds all Java JARs once (mvnw package)
# - Brings up infra + all 8 backend services with `docker compose --profile all up -d --build`
# - Starts the React frontend in a new Terminal window (macOS) or background (Linux)
# - Opens the browser when the gateway is ready
#
# Usage:  ./scripts/run-all-docker.sh
# (first time: chmod +x scripts/run-all-docker.sh)

set -e

REPO_DIR="$(cd "$(dirname "$0")/.." && pwd)"
cd "$REPO_DIR"

OS="$(uname -s)"

cyan()  { printf "\033[36m%s\033[0m\n" "$1"; }
green() { printf "\033[32m%s\033[0m\n" "$1"; }
yellow(){ printf "\033[33m%s\033[0m\n" "$1"; }
red()   { printf "\033[31m%s\033[0m\n" "$1"; }

echo
cyan "=============================================="
cyan "  ShopSphere - One-click Docker launcher"
cyan "=============================================="
echo

# ---------- 1. Ensure Docker is running ----------
docker_running() { docker info >/dev/null 2>&1; }

if ! docker_running; then
  yellow "[1/5] Docker is not running. Starting it..."
  if [ "$OS" = "Darwin" ]; then
    open -a Docker || { red "ERROR: Could not launch Docker Desktop (Docker.app)."; exit 1; }
  else
    red "ERROR: Docker daemon is not running. Start it with: sudo systemctl start docker"
    exit 1
  fi

  printf "      Waiting for Docker engine to become ready (this can take ~60s)..."
  waited=0; max=180
  while ! docker_running; do
    if [ $waited -ge $max ]; then
      echo; red "ERROR: Docker engine did not become ready within ${max}s."; exit 1
    fi
    sleep 3; waited=$((waited+3)); printf "."
  done
  green " ready."
else
  green "[1/5] Docker is already running."
fi

# ---------- 2. Build all Java modules ----------
echo
yellow "[2/5] Building all Java modules (./mvnw -DskipTests package)..."
echo   "      First run downloads dependencies and may take several minutes."
chmod +x ./mvnw 2>/dev/null || true
./mvnw -DskipTests package
green "      Build complete."

# ---------- 3. docker compose up (infra + services) ----------
echo
yellow "[3/5] Pulling images and starting full stack (docker compose --profile all up -d --build)..."
docker compose --profile all up -d --build
green "      All containers started."

# ---------- 4. Wait for the API gateway to be ready ----------
echo
printf "[4/5] Waiting for API gateway (http://localhost:8080) to be ready..."
waited=0; max=240; ready=0
while [ $waited -lt $max ]; do
  if curl -fs -m 3 http://localhost:8080/actuator/health >/dev/null 2>&1; then
    ready=1; break
  fi
  sleep 5; waited=$((waited+5)); printf "."
done
if [ $ready -eq 1 ]; then
  green " ready."
else
  echo
  yellow "WARNING: Gateway did not respond within ${max}s. Continuing anyway."
  yellow "         Check: docker compose ps  /  docker compose logs api-gateway"
fi

# ---------- 5. Start the frontend ----------
echo
yellow "[5/5] Starting frontend (Vite dev server)..."
FRONTEND_DIR="$REPO_DIR/shopsphere-frontend"
if [ ! -f "$FRONTEND_DIR/.env" ] && [ -f "$FRONTEND_DIR/.env.example" ]; then
  cp "$FRONTEND_DIR/.env.example" "$FRONTEND_DIR/.env"
  green "      Created shopsphere-frontend/.env from .env.example"
fi

if [ "$OS" = "Darwin" ]; then
  # Open a new Terminal window running the dev server
  osascript <<EOF
tell application "Terminal"
    activate
    do script "cd '$FRONTEND_DIR' && [ -d node_modules ] || npm install && npm run dev"
end tell
EOF
  green "      Frontend launched in a new Terminal window."
else
  # Linux: run in background, log to file
  ( cd "$FRONTEND_DIR" && \
    { [ -d node_modules ] || npm install; } && \
    nohup npm run dev > "$REPO_DIR/frontend.log" 2>&1 & )
  green "      Frontend started in background (logs: frontend.log)."
fi

# ---------- Done ----------
echo
cyan "=============================================="
cyan "  ShopSphere is up!"
cyan "=============================================="
echo
echo "  Frontend       http://localhost:5173"
echo "  API Gateway    http://localhost:8080"
echo "  Eureka         http://localhost:8761"
echo "  Grafana        http://localhost:3000   (admin/admin)"
echo "  Prometheus     http://localhost:9090"
echo "  Zipkin         http://localhost:9411"
echo "  MailHog UI     http://localhost:8025"
echo "  SonarQube      http://localhost:9000   (admin/admin)"
echo
echo "  View logs:     docker compose logs -f <service>"
echo "  Stop stack:    ./scripts/stop-all-docker.sh"
echo

sleep 5
if [ "$OS" = "Darwin" ]; then
  open http://localhost:5173 || true
else
  xdg-open http://localhost:5173 >/dev/null 2>&1 || true
fi
