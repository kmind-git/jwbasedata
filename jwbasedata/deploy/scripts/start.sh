#!/bin/bash
#
# jwbasedata 服务启动脚本
# 用法: ./start.sh [环境名称，默认prod]
#
# 配置加载顺序：
# 1. JAR内置 application.yml
# 2. config/application.yml (外部配置，优先级更高)
# 3. 环境变量覆盖
#

APP_NAME="jwbasedata"
JAR_NAME="jwbasedata-1.0.0.jar"
JAR_PATH="../${JAR_NAME}"
CONFIG_PATH="../config"
LOG_PATH="../logs"
PID_FILE="../${APP_NAME}.pid"

# JVM 参数
JVM_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# 环境参数
ENV=${1:-prod}

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}  ${APP_NAME} 服务启动脚本${NC}"
echo -e "${GREEN}========================================${NC}"

# 检查 JAR 文件是否存在
if [ ! -f "${JAR_PATH}" ]; then
    echo -e "${RED}错误: 找不到 JAR 文件 ${JAR_PATH}${NC}"
    exit 1
fi

# 检查外部配置文件
if [ ! -f "${CONFIG_PATH}/application.yml" ]; then
    echo -e "${YELLOW}警告: 未找到外部配置文件 ${CONFIG_PATH}/application.yml${NC}"
    echo -e "${YELLOW}将使用 JAR 内置配置或环境变量${NC}"
fi

# 创建必要目录
mkdir -p ${LOG_PATH}
mkdir -p ${CONFIG_PATH}

# 检查服务是否已运行
if [ -f "${PID_FILE}" ]; then
    PID=$(cat ${PID_FILE})
    if ps -p ${PID} > /dev/null 2>&1; then
        echo -e "${YELLOW}服务已在运行中，PID: ${PID}${NC}"
        exit 0
    else
        rm -f ${PID_FILE}
    fi
fi

# 启动服务
echo -e "${GREEN}正在启动服务...${NC}"
echo -e "环境: ${ENV}"
echo -e "JVM参数: ${JVM_OPTS}"
echo -e "配置目录: ${CONFIG_PATH}"

# 构建启动命令
START_CMD="java ${JVM_OPTS} \
    -jar ${JAR_PATH} \
    --spring.profiles.active=${ENV} \
    --spring.config.additional-location=file:${CONFIG_PATH}/"

echo -e "启动命令: ${START_CMD}"

nohup ${START_CMD} > ${LOG_PATH}/console.log 2>&1 &

echo $! > ${PID_FILE}

# 等待服务启动
sleep 3

# 检查服务是否启动成功
if ps -p $(cat ${PID_FILE}) > /dev/null 2>&1; then
    echo -e "${GREEN}服务启动成功！${NC}"
    echo -e "PID: $(cat ${PID_FILE})"
    echo -e "配置文件: ${CONFIG_PATH}/application.yml"
    echo -e "日志目录: ${LOG_PATH}"
    echo -e "控制台日志: ${LOG_PATH}/console.log"
    echo ""
    echo -e "访问地址:"
    echo -e "  - API: http://localhost:8080/api/users"
    echo -e "  - Swagger: http://localhost:8080/swagger-ui.html"
else
    echo -e "${RED}服务启动失败，请检查日志${NC}"
    echo -e "${YELLOW}日志内容:${NC}"
    tail -50 ${LOG_PATH}/console.log
    rm -f ${PID_FILE}
    exit 1
fi
