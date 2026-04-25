# One-click launcher for the entire ShopSphere stack via Docker.
# - Ensures Docker Desktop is running
# - Builds all Java JARs once (mvnw package)
# - Brings up infra + all 8 backend services with `docker compose --profile all up -d --build`
# - Starts the React frontend in a new window
# - Opens the browser when the gateway is ready
#
# Usage: right-click -> Run with PowerShell, or `.\scripts\run-all-docker.ps1`

$ErrorActionPreference = 'Stop'
$repo = Split-Path -Parent $PSScriptRoot
Set-Location $repo

Write-Host ""
Write-Host "==============================================" -ForegroundColor Cyan
Write-Host "  ShopSphere - One-click Docker launcher"      -ForegroundColor Cyan
Write-Host "==============================================" -ForegroundColor Cyan
Write-Host ""

# ---------- 1. Ensure Docker Desktop is running ----------
function Test-DockerRunning {
    try {
        docker info --format '{{.ServerVersion}}' 2>$null | Out-Null
        return $LASTEXITCODE -eq 0
    } catch { return $false }
}

if (-not (Test-DockerRunning)) {
    Write-Host "[1/5] Docker Desktop is not running. Starting it..." -ForegroundColor Yellow
    $dockerExe = "$env:ProgramFiles\Docker\Docker\Docker Desktop.exe"
    if (-not (Test-Path $dockerExe)) {
        Write-Host "ERROR: Could not find Docker Desktop at $dockerExe" -ForegroundColor Red
        Write-Host "Please start Docker Desktop manually and re-run this script." -ForegroundColor Red
        exit 1
    }
    Start-Process -FilePath $dockerExe | Out-Null

    Write-Host "      Waiting for Docker engine to become ready (this can take ~60s)..." -NoNewline
    $maxWait = 180
    $waited = 0
    while (-not (Test-DockerRunning)) {
        if ($waited -ge $maxWait) {
            Write-Host ""
            Write-Host "ERROR: Docker engine did not become ready within ${maxWait}s." -ForegroundColor Red
            exit 1
        }
        Start-Sleep -Seconds 3
        $waited += 3
        Write-Host "." -NoNewline
    }
    Write-Host " ready." -ForegroundColor Green
} else {
    Write-Host "[1/5] Docker Desktop is already running." -ForegroundColor Green
}

# ---------- 2. Build all Java modules ----------
Write-Host ""
Write-Host "[2/5] Building all Java modules (mvnw -DskipTests package)..." -ForegroundColor Yellow
Write-Host "      First run downloads dependencies and may take several minutes."
& .\mvnw.cmd -DskipTests package
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Maven build failed. Aborting." -ForegroundColor Red
    exit 1
}
Write-Host "      Build complete." -ForegroundColor Green

# ---------- 3. docker compose up (infra + services) ----------
Write-Host ""
Write-Host "[3/5] Pulling images and starting full stack (docker compose --profile all up -d --build)..." -ForegroundColor Yellow
docker compose --profile all up -d --build
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: docker compose failed. Check 'docker compose logs' for details." -ForegroundColor Red
    exit 1
}
Write-Host "      All containers started." -ForegroundColor Green

# ---------- 4. Wait for the API gateway to be ready ----------
Write-Host ""
Write-Host "[4/5] Waiting for API gateway (http://localhost:8080) to be ready..." -NoNewline
$gatewayReady = $false
$maxWait = 240
$waited = 0
while (-not $gatewayReady) {
    if ($waited -ge $maxWait) {
        Write-Host ""
        Write-Host "WARNING: Gateway did not respond within ${maxWait}s. Continuing anyway." -ForegroundColor Yellow
        Write-Host "         Check: docker compose ps  /  docker compose logs api-gateway" -ForegroundColor Yellow
        break
    }
    try {
        $resp = Invoke-WebRequest -Uri 'http://localhost:8080/actuator/health' -UseBasicParsing -TimeoutSec 3 -ErrorAction Stop
        if ($resp.StatusCode -eq 200) { $gatewayReady = $true; break }
    } catch { }
    Start-Sleep -Seconds 5
    $waited += 5
    Write-Host "." -NoNewline
}
if ($gatewayReady) { Write-Host " ready." -ForegroundColor Green }

# ---------- 5. Start the frontend in a new window ----------
Write-Host ""
Write-Host "[5/5] Starting frontend (Vite dev server) in a new window..." -ForegroundColor Yellow
$frontendDir = Join-Path $repo 'shopsphere-frontend'
if (-not (Test-Path (Join-Path $frontendDir '.env'))) {
    if (Test-Path (Join-Path $frontendDir '.env.example')) {
        Copy-Item (Join-Path $frontendDir '.env.example') (Join-Path $frontendDir '.env')
        Write-Host "      Created shopsphere-frontend\.env from .env.example" -ForegroundColor Green
    }
}

$frontendCmd = @"
cd `"$frontendDir`"
`$Host.UI.RawUI.WindowTitle = 'ShopSphere :: frontend (Vite)'
if (-not (Test-Path 'node_modules')) { Write-Host 'Installing npm dependencies...'; npm install }
npm run dev
"@

Start-Process powershell -ArgumentList @('-NoExit', '-Command', $frontendCmd) | Out-Null
Write-Host "      Frontend window launched." -ForegroundColor Green

# ---------- Done ----------
Write-Host ""
Write-Host "==============================================" -ForegroundColor Cyan
Write-Host "  ShopSphere is up!"                            -ForegroundColor Cyan
Write-Host "==============================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "  Frontend       http://localhost:5173"
Write-Host "  API Gateway    http://localhost:8080"
Write-Host "  Eureka         http://localhost:8761"
Write-Host "  Grafana        http://localhost:3000   (admin/admin)"
Write-Host "  Prometheus     http://localhost:9090"
Write-Host "  Zipkin         http://localhost:9411"
Write-Host "  MailHog UI     http://localhost:8025"
Write-Host "  SonarQube      http://localhost:9000   (admin/admin)"
Write-Host ""
Write-Host "  View logs:     docker compose logs -f <service>"
Write-Host "  Stop stack:    docker compose --profile all down"
Write-Host ""

Start-Sleep -Seconds 5
Start-Process 'http://localhost:5173'
