# jwbasedata - 基础数据查询服务

基于 Spring Boot 2.7.x 的企业级基础数据查询服务，提供 RESTful API 接口供前端调用。

## 技术栈

| 技术 | 版本 | 说明 |
|-----|------|-----|
| Java | 1.8 | JDK 版本 |
| Spring Boot | 2.7.18 | 基础框架 |
| MyBatis-Plus | 3.5.3.1 | ORM 框架 |
| MySQL | 5.7+ | 数据库 |
| SpringDoc | 1.7.0 | API 文档 |
| Log4j2 | 2.17.2 | 日志框架 |
| Lombok | - | 简化代码 |

## 项目结构

```
jwbasedata/
├── pom.xml
├── src/main/
│   ├── java/com/jw/jwbasedata/
│   │   ├── controller/          # 控制器层
│   │   ├── service/             # 服务层
│   │   ├── mapper/              # Mapper 接口
│   │   ├── entity/              # 实体类
│   │   ├── dto/                 # 数据传输对象
│   │   ├── vo/                  # 视图对象
│   │   ├── config/              # 配置类
│   │   └── common/              # 公共模块
│   └── resources/
│       ├── application.yml      # 默认配置
│       ├── application-dev.yml  # 开发环境配置
│       ├── mapper/              # Mapper XML
│       ├── db/                  # SQL 脚本
│       └── log4j2-spring.xml    # 日志配置
├── deploy/                      # 部署脚本
│   ├── start.sh / start.bat     # 启动脚本
│   ├── stop.sh / stop.bat       # 停止脚本
│   ├── restart.sh               # 重启脚本
│   ├── status.sh / status.bat   # 状态查看
│   └── config/
│       └── application.yml      # 外部配置模板
├── DEPLOY.md                    # 部署文档
└── README.md
```

## 快速开始

### 1. 环境准备

- JDK 1.8+
- Maven 3.6+
- MySQL 5.7+

### 2. 数据库初始化

```bash
mysql -u root -p < src/main/resources/db/schema.sql
```

### 3. 本地开发运行

```bash
# 使用开发配置运行
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 4. 打包

```bash
mvn clean package -DskipTests
```

---

## 部署方式

### 配置加载顺序

```
优先级从低到高：
1. JAR 内置 application.yml (默认配置)
2. config/application.yml (外部配置)
3. 环境变量 (DB_HOST, DB_PORT 等)
4. 命令行参数
```

### 快速部署

```bash
# 1. 创建部署目录
mkdir -p release/{config,logs}
cp target/jwbasedata-1.0.0.jar release/
cp deploy/config/application.yml release/config/
cp deploy/*.sh release/ && chmod +x release/*.sh

# 2. 修改外部配置
vim release/config/application.yml

# 3. 启动服务
cd release && ./start.sh prod
```

### 环境变量配置

```bash
export DB_HOST=192.168.1.100
export DB_PORT=3306
export DB_NAME=jwbasedata
export DB_USERNAME=app_user
export DB_PASSWORD=secure_password

java -jar jwbasedata-1.0.0.jar
```

详细部署说明请查看 [DEPLOY.md](DEPLOY.md)

---

## API 接口

| 方法 | 路径 | 说明 |
|-----|------|-----|
| GET | /api/users | 分页查询用户列表 |
| GET | /api/users/{id} | 根据 ID 查询用户 |

### 接口示例

**分页查询**
```bash
GET /api/users?page=1&size=10&username=zhang
```

**响应**
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "records": [...],
    "total": 100,
    "page": 1,
    "size": 10
  }
}
```

## API 文档

- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/v3/api-docs

## License

Apache License 2.0
