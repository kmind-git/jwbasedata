@echo off
chcp 65001 >nul
REM jwbasedata 服务状态查看脚本 (Windows)

set APP_NAME=jwbasedata

echo ========================================
echo   %APP_NAME% 服务状态
echo ========================================

tasklist /FI "WINDOWTITLE eq %APP_NAME%*" 2>nul | find /I "java.exe" >nul
if %errorlevel% equ 0 (
    echo 状态: 运行中
    echo.
    echo 进程信息:
    tasklist /FI "WINDOWTITLE eq %APP_NAME%*"
    echo.
    echo 端口占用:
    netstat -ano | findstr ":8080"
) else (
    echo 状态: 已停止
)

pause
