# jwbasedata - 基础数据查询服务

## 项目概述

jwbasedata 是一个基于 Spring Boot 2.7.x 的企业级基础数据查询服务，主要功能是从 MySQL 数据库读取数据并提供 RESTful API 接口供前端调用。

### 核心目的
- 提供标准化的基础数据查询服务
- 实现用户数据的分页查询和按ID查询功能
- 构建可扩展、可维护的企业级应用架构

## 技术栈规范

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 1.8 | JDK 版本 |
| Spring Boot | 2.7.18 | 基础框架，兼容 Java 8 |
| MyBatis-Plus | 3.5.3.1 | ORM 框架，简化数据库操作 |
| MySQL | 5.7+ | 数据库 |
| SpringDoc OpenAPI | 1.7.0 | API 文档生成 (Swagger) |
| Log4j2 | 2.17.2 | 日志框架（异步日志、滚动策略） |
| Lombok | - | 简化代码，自动生成getter/setter等 |
| Maven | 3.6+ | 构建工具 |

## 项目结构规范

```
src/main/java/com/jw/jwbasedata/
├── controller/          # 控制器层 - 只负责接口定义和请求响应
├── service/            # 服务层 - 业务逻辑实现
│   └── impl/           # 服务实现类
├── mapper/             # Mapper 接口 - 数据库访问
├── entity/             # 实体类 - 数据库表映射
├── dto/                # 数据传输对象 - 请求参数
├── vo/                 # 视图对象 - 响应数据
├── config/             # 配置类
│   ├── MyBatisPlusConfig.java    # MyBatis-Plus 分页配置
│   ├── SwaggerConfig.java        # Swagger 配置
│   └── WebMvcConfig.java         # Web MVC 配置
└── common/             # 公共模块
    ├── Result.java                # 统一返回对象
    └── GlobalExceptionHandler.java # 全局异常处理
```

## 开发规范

### 1. 分层架构
- **Controller 层**：只负责接口定义、参数校验、请求响应
- **Service 层**：负责业务逻辑实现，一个 Service 对应一个业务模块
- **Mapper 层**：负责数据库访问，使用 MyBatis-Plus 提供的基础方法
- **Entity 层**：使用 Lombok 注解，对应数据库表结构
- **DTO 层**：接收前端请求参数，使用验证注解
- **VO 层**：返回给前端的数据对象，可包含计算字段

### 2. 代码规范
- 使用 Lombok 减少样板代码
- 类名使用大驼峰命名法（UserController）
- 方法名使用小驼峰命名法（getUserById）
- 变量名使用小驼峰命名法（userName）
- 常量名使用大写+下划线（MAX_PAGE_SIZE）

### 3. 数据库规范
- 表名使用小写+下划线（user_info）
- 字段名使用小写+下划线（user_name）
- 主键使用 BIGINT 自增类型
- 时间字段使用 DATETIME 类型
- 逻辑删除使用 deleted 字段（0-未删除，1-已删除）

## API 规范

### 统一返回结构
```json
{
  "code": 0,
  "message": "success",
  "data": {}
}
```

### 分页返回结构
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "records": [],
    "total": 100,
    "page": 1,
    "size": 10
  }
}
```

### 错误码规范
- `0`：成功
- `1000-1999`：参数错误
- `2000-2999`：业务错误
- `3000-3999`：系统错误
- `4000-4999`：数据库错误

### API 接口示例
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/users | 分页查询用户列表 |
| GET | /api/users/{id} | 根据 ID 查询用户 |

## 配置规范

### 1. 配置文件结构
- `application.yml`：JAR 内置基础配置（打包时固定）
- `config/application.yml`：外部配置文件（部署时修改，优先级更高）
- `application-dev.yml`：开发环境配置
- `log4j2-spring.xml`：Log4j2 日志配置

### 2. 配置加载优先级（从低到高）
1. JAR 内置 `application.yml`
2. `config/application.yml`（外部配置）
3. 环境变量（DB_HOST, DB_PORT 等）
4. 命令行参数

### 3. 日志规范
- 使用 Log4j2 替代 Logback
- 异步日志提升性能
- 按级别分离日志文件（info.log, error.log）
- 按日期和大小滚动日志文件
- 项目包日志级别设置为 DEBUG

## 部署规范

### 1. 打包方式
```bash
mvn clean package -DskipTests
```

### 2. 部署目录结构
```
deploy-release/
├── jwbasedata-1.0.0.jar        # 可执行 JAR
├── config/                     # 外部配置目录
│   └── application.yml         # 生产环境配置
├── scripts/                    # 启动脚本
│   ├── start.sh                # Linux 启动
│   ├── stop.sh                 # Linux 停止
│   └── ...
├── logs/                       # 日志目录
└── jwbasedata.pid              # PID 文件
```

### 3. 启动方式
```bash
# 开发环境
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 生产环境
./scripts/start.sh prod
```

### 4. 数据库初始化
```sql
-- 执行 db/schema.sql
mysql -u root -p < src/main/resources/db/schema.sql
```

## 基础组件要求

### 必须实现的基础组件
1. **统一返回对象**：`Result.java` - 封装所有 API 响应
2. **全局异常处理**：`GlobalExceptionHandler.java` - 统一异常处理
3. **MyBatis-Plus 分页配置**：`MyBatisPlusConfig.java` - 分页插件配置
4. **Swagger 配置**：`SwaggerConfig.java` - API 文档配置
5. **数据库连接配置**：通过 `application.yml` 配置
6. **日志配置**：`log4j2-spring.xml` - Log4j2 配置

### 数据表结构示例
```sql
CREATE TABLE user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
);
```

## 质量要求

### 1. 代码质量
- 编译必须通过：`mvn clean compile`
- 代码风格统一
- 注释清晰，关键业务逻辑必须有注释
- 异常处理完善

### 2. 安全要求
- 数据库密码等敏感信息通过环境变量或外部配置注入
- 生产环境关闭 Swagger
- 输入参数必须验证
- 防止 SQL 注入（使用 MyBatis-Plus 参数化查询）

### 3. 性能要求
- 数据库连接池配置合理（HikariCP）
- 日志使用异步写入
- 分页查询必须使用数据库分页，不能内存分页

## 维护说明

### 1. 版本管理
- 使用语义化版本：主版本.次版本.修订版本
- 每次发布更新 `pom.xml` 中的版本号
- 保持向后兼容性

### 2. 文档维护
- 更新 `README.md`：项目说明和快速开始
- 更新 `DEPLOY.md`：部署文档
- 更新 `CLAUDE.md`：项目规范和上下文
- 保持 API 文档（Swagger）与实际接口一致

### 3. 监控与日志
- 监控服务状态：`./scripts/status.sh`
- 查看实时日志：`tail -f logs/console.log`
- 查看错误日志：`tail -f logs/jwbasedata-error.log`
- 定期清理历史日志文件

---
*最后更新：2026-03-29*
*维护者：jw*