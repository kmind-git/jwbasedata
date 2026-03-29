#!/bin/bash
#
# jwbasedata 服务重启脚本
#

SCRIPT_DIR=$(dirname "$0")

echo "正在重启服务..."
${SCRIPT_DIR}/stop.sh
sleep 2
${SCRIPT_DIR}/start.sh $1
