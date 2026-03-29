#!/bin/bash
#
# jwbasedata 服务停止脚本
#

APP_NAME="jwbasedata"
PID_FILE="../${APP_NAME}.pid"

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}  ${APP_NAME} 服务停止脚本${NC}"
echo -e "${GREEN}========================================${NC}"

# 检查 PID 文件是否存在
if [ ! -f "${PID_FILE}" ]; then
    echo -e "${YELLOW}服务未运行或 PID 文件不存在${NC}"
    exit 0
fi

PID=$(cat ${PID_FILE})

# 检查进程是否存在
if ! ps -p ${PID} > /dev/null 2>&1; then
    echo -e "${YELLOW}服务未运行，清理 PID 文件${NC}"
    rm -f ${PID_FILE}
    exit 0
fi

# 停止服务
echo -e "${GREEN}正在停止服务，PID: ${PID}${NC}"
kill ${PID}

# 等待服务停止
COUNT=0
while ps -p ${PID} > /dev/null 2>&1; do
    sleep 1
    COUNT=$((COUNT + 1))
    if [ ${COUNT} -ge 30 ]; then
        echo -e "${YELLOW}服务未响应，强制停止...${NC}"
        kill -9 ${PID}
        break
    fi
done

# 清理 PID 文件
rm -f ${PID_FILE}

echo -e "${GREEN}服务已停止${NC}"
