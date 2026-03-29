# jwbasedata 部署包

此目录包含 jwbasedata 服务的部署资源。

## 部署包结构

此目录为完整的部署包，包含运行 jwbasedata 服务所需的所有文件。

```
deploy/
├── jwbasedata-1.0.0.jar     # 可执行 JAR 文件
├── config/                  # 外部配置文件
│   └── application.yml      # 配置文件模板，部署时需修改
├── scripts/                 # 启动脚本
│   ├── start.sh             # Linux 启动脚本
│   ├── stop.sh              # Linux 停止脚本
│   ├── restart.sh           # Linux 重启脚本
│   ├── status.sh            # Linux 状态查看脚本
│   ├── start.bat            # Windows 启动脚本
│   ├── stop.bat             # Windows 停止脚本
│   └── status.bat           # Windows 状态查看脚本
├── db/                      # 数据库脚本
│   └── schema.sql           # 数据库表结构
├── logs/                    # 日志目录 (运行时自动创建)
│   └── .gitkeep             # 保持目录结构
└── README.md                # 本文件
```

## 快速部署步骤

### 1. 压缩部署包

将此 `deploy/` 目录压缩为 `jwbasedata-deploy.tar.gz`：

```bash
tar -czf jwbasedata-deploy.tar.gz deploy/
```

### 2. 上传到 Linux 服务器

使用 scp 或 sftp 将压缩包上传到目标服务器：

```bash
scp jwbasedata-deploy.tar.gz user@server:/opt/
```

### 3. 解压部署包

在服务器上解压到目标目录：

```bash
cd /opt
tar -xzf jwbasedata-deploy.tar.gz
mv deploy jwbasedata
cd jwbasedata
```

### 4. 数据库初始化

创建数据库并导入表结构：

```bash
mysql -u root -p
# 在 MySQL 中执行
CREATE DATABASE jwbasedata CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE jwbasedata;
SOURCE db/schema.sql;
```

### 5. 修改配置文件

编辑 `config/application.yml`，设置数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://数据库地址:3306/jwbasedata?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: 用户名
    password: 密码
```

### 6. 启动服务

```bash
# 赋予执行权限
chmod +x scripts/*.sh

# 启动服务
./scripts/start.sh prod
```

### 7. 停止服务

```bash
./scripts/stop.sh
```

## 配置加载顺序

1. JAR 内置 `application.yml`（默认配置）
2. `config/application.yml`（外部配置，推荐）
3. 环境变量（DB_HOST, DB_PORT 等）
4. 命令行参数

## 详细文档

完整部署文档请参阅项目根目录的 [DEPLOY.md](../DEPLOY.md)。