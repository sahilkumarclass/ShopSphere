# Launches each ShopSphere backend service in its own PowerShell window.
# Run from anywhere. Assumes mvnw is at the repo root.

$ErrorActionPreference = 'Stop'
$repo = Split-Path -Parent $PSScriptRoot

function Start-Service($name, $delaySeconds) {
    $title = "ShopSphere :: $name"
    Write-Host "Starting $name..."
    Start-Process powershell -ArgumentList @(
        '-NoExit', '-Command',
        "cd `"$repo`"; `$Host.UI.RawUI.WindowTitle = '$title'; .\mvnw -pl $name spring-boot:run"
    )
    if ($delaySeconds -gt 0) { Start-Sleep -Seconds $delaySeconds }
}

# Start order matters — Eureka first so others can register.
Start-Service 'eureka-server'        20
Start-Service 'config-server'        15
Start-Service 'api-gateway'          10
Start-Service 'auth-service'         5
Start-Service 'catalog-service'      5
Start-Service 'order-service'        5
Start-Service 'admin-service'        5
Start-Service 'notification-service' 0

Write-Host ""
Write-Host "All services launching. Eureka: http://localhost:8761  |  Gateway: http://localhost:8080"
Write-Host "Frontend: cd shopsphere-frontend ; npm install ; npm run dev"
