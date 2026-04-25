@echo off
REM Double-click launcher for ShopSphere. Forwards to the PowerShell script.
powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%~dp0run-all-docker.ps1"
pause
