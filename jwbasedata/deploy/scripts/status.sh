#!/bin/bash
#
# jwbasedata 服务状态查看脚本
#

APP_NAME="jwbasedata"
PID_FILE="../${APP_NAME}.pid"

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}  ${APP_NAME} 服务状态${NC}"
echo -e "${GREEN}========================================${NC}"

if [ -f "${PID_FILE}" ]; then
    PID=$(cat ${PID_FILE})
    if ps -p ${PID} > /dev/null 2>&1; then
        echo -e "状态: ${GREEN}运行中${NC}"
        echo -e "PID: ${PID}"

        # 显示进程信息
        ps -p ${PID} -o pid,ppid,cmd,%mem,%cpu,etime

        # 显示端口占用
        echo ""
        echo -e "${GREEN}端口占用:${NC}"
        netstat -tlnp 2>/dev/null | grep ${PID} || ss -tlnp 2>/dev/null | grep ${PID}
    else
        echo -e "状态: ${RED}已停止${NC}"
        echo -e "${YELLOW}PID 文件存在但进程不存在，建议清理${NC}"
        rm -f ${PID_FILE}
    fi
else
    echo -e "状态: ${RED}已停止${NC}"
    echo -e "PID 文件不存在"
fi
