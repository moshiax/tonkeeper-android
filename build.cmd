@echo off
cd /d %~dp0

echo Building APK...
call gradlew assembleDebug

if %ERRORLEVEL% neq 0 (
    echo Build failed!
    pause
    exit /b %ERRORLEVEL%
)

echo Build successful!
explorer "%CD%\apps\wallet\instance\main\build\outputs\apk\default\debug"