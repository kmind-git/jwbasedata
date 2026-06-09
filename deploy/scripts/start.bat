@echo off
chcp 65001 >nul
REM jwbasedata 服务启动脚本 (Windows)
REM 配置加载: JAR内置 -> config/application.yml -> 环境变量

set APP_NAME=jwbasedata
set JAR_NAME=jwbasedata-1.0.0.jar
set JAR_PATH=..\%JAR_NAME%
set CONFIG_PATH=..\config
set LOG_PATH=..\logs
set ENV=%1
if "%ENV%"=="" set ENV=prod

REM JVM 参数
set JVM_OPTS=-Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200

echo ========================================
echo   %APP_NAME% 服务启动脚本
echo ========================================

REM 检查 JAR 文件
if not exist "%JAR_PATH%" (
    echo 错误: 找不到 JAR 文件 %JAR_PATH%
    pause
    exit /b 1
)

REM 检查外部配置文件
if not exist "%CONFIG_PATH%\application.yml" (
    echo 警告: 未找到外部配置文件 %CONFIG_PATH%\application.yml
    echo 将使用 JAR 内置配置或环境变量
)

REM 创建必要目录
if not exist "%LOG_PATH%" mkdir "%LOG_PATH%"
if not exist "%CONFIG_PATH%" mkdir "%CONFIG_PATH%"

REM 检查服务是否已运行
tasklist /FI "IMAGENAME eq java.exe" /FI "WINDOWTITLE eq %APP_NAME%*" 2>nul | find /I "java.exe" >nul
if %errorlevel% equ 0 (
    echo 服务已在运行中
    pause
    exit /b 0
)

REM 启动服务
echo 正在启动服务...
echo 环境: %ENV%
echo JVM参数: %JVM_OPTS%
echo 配置目录: %CONFIG_PATH%

start "%APP_NAME%" java %JVM_OPTS% -jar "%JAR_PATH%" --spring.profiles.active=%ENV% --spring.config.additional-location=file:%CONFIG_PATH%/

echo.
echo 服务启动中，请稍候...
timeout /t 3 /nobreak >nul

echo.
echo 服务启动完成！
echo.
echo 配置文件: %CONFIG_PATH%\application.yml
echo 日志目录: %LOG_PATH%
echo.
echo 访问地址:
echo   - API: http://localhost:8080/api/users
echo   - Swagger: http://localhost:8080/swagger-ui.html
echo.
pause
