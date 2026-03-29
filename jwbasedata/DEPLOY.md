# 部署文档

## 部署架构

```
┌─────────────────────────────────────────────────────────┐
│  配置加载优先级 (从低到高)                                │
├─────────────────────────────────────────────────────────┤
│  1. JAR 内置 application.yml (默认配置)                  │
│  2. config/application.yml (外部配置，推荐)              │
│  3. 环境变量 (DB_HOST, DB_PORT 等)                       │
│  4. 命令行参数 (--server.port=8081)                      │
└─────────────────────────────────────────────────────────┘
```

## 目录结构

### 项目源代码结构
```
jwbasedata/
├── src/                         # 源代码
├── target/                      # 构建输出
├── deploy/                      # 部署资源 (配置模板、脚本、说明)
│   ├── config/                  # 外部配置模板
│   │   └── application.yml      # 配置文件模板 (部署时修改)
│   ├── scripts/                 # 启动脚本
│   │   ├── start.sh             # Linux 启动
│   │   ├── stop.sh              # Linux 停止
│   │   ├── restart.sh           # Linux 重启
│   │   ├── status.sh            # Linux 状态
│   │   ├── start.bat            # Windows 启动
│   │   ├── stop.bat             # Windows 停止
│   │   └── status.bat           # Windows 状态
│   └── README.md                # 部署说明 (可选)
├── config/                      # 开发环境外部配置 (运行时加载)
├── logs/                        # 开发环境日志
├── pom.xml
├── README.md
└── DEPLOY.md
```

### 生产环境部署目录结构
```
deploy-release/                  # 部署包目录 (用户创建)
├── jwbasedata-1.0.0.jar        # 可执行 JAR
├── config/                      # 外部配置目录
│   └── application.yml          # 生产环境配置文件
├── scripts/                     # 启动脚本
│   ├── start.sh
│   ├── stop.sh
│   └── ...
├── logs/                        # 日志目录
└── jwbasedata.pid               # PID 文件
```

---

## 环境要求

| 软件 | 版本要求 |
|-----|---------|
| JDK | 1.8+ |
| MySQL | 5.7+ |
| 内存 | 最小 512MB，推荐 1GB+ |

---

## 一、快速部署

### 1.1 打包项目

```bash
mvn clean package -DskipTests
```

### 1.2 创建部署目录

```bash
mkdir -p deploy-release/{config,logs,scripts}
cp target/jwbasedata-1.0.0.jar deploy-release/
cp deploy/config/application.yml deploy-release/config/
cp deploy/scripts/*.sh deploy-release/scripts/
cp deploy/scripts/*.bat deploy-release/scripts/
chmod +x deploy-release/scripts/*.sh
```

### 1.3 修改配置

编辑 `config/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://192.168.1.100:3306/jwbasedata?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: app_user
    password: your_secure_password
```

### 1.4 启动服务

```bash
cd deploy-release
./scripts/start.sh prod
```

---

## 二、配置方式详解

### 2.1 方式一：外部配置文件 (推荐)

```bash
# 目录结构
deploy-release/
├── jwbasedata-1.0.0.jar
├── config/
│   └── application.yml    # 修改此文件
├── scripts/               # 启动脚本
│   ├── start.sh
│   ├── stop.sh
│   └── ...
├── logs/
└── jwbasedata.pid
```

启动时自动加载 `config/application.yml`。

### 2.2 方式二：环境变量

```bash
# 设置环境变量
export DB_HOST=192.168.1.100
export DB_PORT=3306
export DB_NAME=jwbasedata
export DB_USERNAME=app_user
export DB_PASSWORD=secure_password

# 启动服务
java -jar jwbasedata-1.0.0.jar
```

### 2.3 方式三：命令行参数

```bash
java -jar jwbasedata-1.0.0.jar \
  --spring.datasource.url="jdbc:mysql://192.168.1.100:3306/jwbasedata" \
  --spring.datasource.username=app_user \
  --spring.datasource.password=secure_password \
  --server.port=8081
```

### 2.4 方式四：指定配置文件路径

```bash
java -jar jwbasedata-1.0.0.jar \
  --spring.config.location=file:/etc/jwbasedata/application.yml
```

---

## 三、Linux 部署

### 3.1 赋予执行权限

```bash
chmod +x *.sh
```

### 3.2 启动服务

```bash
./scripts/start.sh prod
```

### 3.3 停止服务

```bash
./scripts/stop.sh
```

### 3.4 重启服务

```bash
./scripts/restart.sh prod
```

### 3.5 查看状态

```bash
./scripts/status.sh
```

---

## 四、Windows 部署

### 4.1 启动服务

双击 `scripts/start.bat` 或：

```cmd
scripts\start.bat
```

### 4.2 停止服务

双击 `scripts/stop.bat`

---

## 五、Docker 部署

### 5.1 Dockerfile

```dockerfile
FROM openjdk:8-jdk-alpine

LABEL maintainer="jw"

WORKDIR /app

# 复制 JAR
COPY target/jwbasedata-1.0.0.jar app.jar

# 创建配置和日志目录
RUN mkdir -p /app/config /app/logs

ENV TZ=Asia/Shanghai
ENV JVM_OPTS="-Xms512m -Xmx1024m"

EXPOSE 8080

# 使用外部配置目录
ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar app.jar --spring.config.additional-location=file:/app/config/"]
```

### 5.2 构建镜像

```bash
docker build -t jwbasedata:1.0.0 .
```

### 5.3 运行容器

```bash
docker run -d \
  --name jwbasedata \
  -p 8080:8080 \
  -v $(pwd)/config:/app/config \
  -v $(pwd)/logs:/app/logs \
  -e DB_HOST=host.docker.internal \
  -e DB_USERNAME=root \
  -e DB_PASSWORD=root \
  jwbasedata:1.0.0
```

### 5.4 Docker Compose

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:5.7
    container_name: jwbasedata-mysql
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: jwbasedata
    volumes:
      - mysql_data:/var/lib/mysql
      - ./src/main/resources/db/schema.sql:/docker-entrypoint-initdb.d/schema.sql

  app:
    image: jwbasedata:1.0.0
    container_name: jwbasedata-app
    depends_on:
      - mysql
    environment:
      DB_HOST: mysql
      DB_PORT: 3306
      DB_NAME: jwbasedata
      DB_USERNAME: root
      DB_PASSWORD: root
    ports:
      - "8080:8080"
    volumes:
      - ./config:/app/config
      - ./logs:/app/logs

volumes:
  mysql_data:
```

启动：

```bash
docker-compose up -d
```

---

## 六、生产环境配置示例

### 6.1 config/application.yml

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://${DB_HOST:prod-db.internal}:${DB_PORT:3306}/${DB_NAME:jwbasedata}?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    driver-class-name: com.mysql.cj.jdbc.Driver
    hikari:
      minimum-idle: 10
      maximum-pool-size: 50
      idle-timeout: 30000
      max-lifetime: 1800000
      connection-timeout: 30000

# MyBatis-Plus 配置
mybatis-plus:
  mapper-locations: classpath:/mapper/*.xml
  type-aliases-package: com.jw.jwbasedata.entity
  configuration:
    map-underscore-to-camel-case: true
  global-config:
    db-config:
      id-type: auto

# 生产环境关闭 Swagger
springdoc:
  api-docs:
    enabled: false
  swagger-ui:
    enabled: false

# 日志级别配置
logging:
  level:
    root: INFO
    com.jw.jwbasedata: DEBUG
  file:
    path: ./logs
  log4j2:
    config-file: classpath:log4j2-spring.xml
```

### 6.2 JVM 调优

```bash
# 修改 start.sh 中的 JVM_OPTS
JVM_OPTS="-Xms1g -Xmx2g -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=../logs/"
```

---

## 七、配置项说明

| 配置项 | 环境变量 | 默认值 | 说明 |
|-------|---------|-------|------|
| server.port | SERVER_PORT | 8080 | 服务端口 |
| 数据库地址 | DB_HOST | localhost | MySQL 地址 |
| 数据库端口 | DB_PORT | 3306 | MySQL 端口 |
| 数据库名称 | DB_NAME | jwbasedata | 数据库名 |
| 数据库用户 | DB_USERNAME | root | 用户名 |
| 数据库密码 | DB_PASSWORD | root | 密码 |

---

## 八、健康检查

```bash
# 接口测试
curl http://localhost:8080/api/users?page=1&size=10

# 预期返回
{"code":0,"message":"success","data":{...}}
```

---

## 九、常见问题

### 9.1 配置未生效

检查配置加载顺序：
1. 确认 `config/application.yml` 文件存在
2. 检查 YAML 格式是否正确
3. 查看启动日志确认配置加载路径

### 9.2 数据库连接失败

```bash
# 测试数据库连接
mysql -h <host> -P <port> -u <user> -p

# 检查环境变量
echo $DB_HOST $DB_USERNAME
```

### 9.3 端口冲突

```bash
# 修改配置或使用命令行参数
java -jar jwbasedata-1.0.0.jar --server.port=8081
```

---

## 十、日志查看

```bash
# 实时日志
tail -f logs/console.log

# INFO 日志
tail -f logs/jwbasedata-info.log

# ERROR 日志
tail -f logs/jwbasedata-error.log
```
