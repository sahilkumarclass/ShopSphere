# Stops the full ShopSphere Docker stack (infra + all backend services).
# Frontend (Vite) runs outside Docker - close its window manually.
#
# Usage: .\scripts\stop-all-docker.ps1            # keeps data volumes
#        .\scripts\stop-all-docker.ps1 -Wipe      # also removes MySQL/Grafana/Prometheus/Sonar volumes

param([switch]$Wipe)

$ErrorActionPreference = 'Stop'
$repo = Split-Path -Parent $PSScriptRoot
Set-Location $repo

if ($Wipe) {
    Write-Host "Stopping stack and WIPING data volumes..." -ForegroundColor Yellow
    docker compose --profile all down -v
} else {
    Write-Host "Stopping stack (data volumes preserved)..." -ForegroundColor Yellow
    docker compose --profile all down
}

Write-Host "Done. Close the frontend (Vite) window manually if it's still running." -ForegroundColor Green
