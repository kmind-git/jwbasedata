@echo off
chcp 65001 >nul
REM jwbasedata 服务停止脚本 (Windows)

set APP_NAME=jwbasedata

echo ========================================
echo   %APP_NAME% 服务停止脚本
echo ========================================

REM 查找并停止 Java 进程
echo 正在停止服务...

tasklist /FI "WINDOWTITLE eq %APP_NAME%*" 2>nul | find /I "java.exe" >nul
if %errorlevel% equ 0 (
    taskkill /FI "WINDOWTITLE eq %APP_NAME%*" /F >nul 2>&1
    echo 服务已停止
) else (
    echo 服务未运行
)

pause
