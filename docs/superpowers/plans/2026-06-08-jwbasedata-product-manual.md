# jwbasedata 产品说明书 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 为 jwbasedata 项目撰写一份综合产品说明书（Markdown 源文件），覆盖产品/业务人员、开发者、运维人员三类读者，并同步到飞书云文档。

**Architecture:** 单文件 Markdown 文档（`docs/PRODUCT_MANUAL.md`），按七个章节从概览到技术细节层层递进。内容来源为现有 CLAUDE.md、源码、配置文件、部署脚本，无需编造或猜测任何信息。

**Tech Stack:** Markdown (源文件), lark-doc skill (飞书发布)

---

### Task 1: 创建产品说明书文件并撰写第 1-2 章（产品概述 + 功能清单）

**Files:**
- Create: `docs/PRODUCT_MANUAL.md`

- [ ] **Step 1: 创建文件并写入文档头部和第 1 章 产品概述**

```markdown
# jwbasedata 产品说明书

> 版本：1.1.0 | 更新日期：2026-06-08 | 维护者：jw

## 目录

- [1. 产品概述](#1-产品概述)
- [2. 功能清单](#2-功能清单)
- [3. 技术架构](#3-技术架构)
- [4. API 参考](#4-api-参考)
- [5. 部署运维](#5-部署运维)
- [6. 开发指南](#6-开发指南)
- [7. 附录](#7-附录)

---

## 1. 产品概述

### 1.1 项目定位

jwbasedata 是一个企业级基础数据查询 REST API 服务，为前端应用和外部系统提供标准化、可扩展的基础数据访问接口。

### 1.2 解决的问题

- **统一数据入口**：基础数据分散在多个数据库表时，通过本服务提供单一、一致的 RESTful 查询接口
- **标准化响应格式**：所有 API 返回统一的 JSON 结构（code + message + data），前端无需适配不同格式
- **开箱即用的分页**：内置分页查询能力，无需各业务方各自实现
- **API 文档自动生成**：基于 Swagger 注解自动生成在线 API 文档，接口变更实时可见

### 1.3 适用场景

- 企业内部管理系统的基础数据模块（如用户查询、组织查询、字典数据）
- 微服务架构中的数据服务层（为前端 BFF 层提供数据）
- 需要快速搭建标准化 CRUD API 的新项目基础框架
```

- [ ] **Step 2: 写入第 2 章 功能清单**

```markdown
## 2. 功能清单

### 2.1 用户分页查询

- **接口**: `GET /api/users`
- **说明**: 分页查询用户列表，支持按用户名、邮箱进行模糊搜索
- **参数**: page（页码）、size（每页条数）、username（用户名，可选）、email（邮箱，可选）
- **返回**: 分页结果（数据列表、总记录数、当前页、每页大小）

### 2.2 用户按 ID 查询

- **接口**: `GET /api/users/{id}`
- **说明**: 根据主键 ID 查询单个用户的详细信息
- **参数**: id（用户 ID，路径参数）
- **返回**: 用户详情对象

### 2.3 统一 JSON 响应格式

所有 API 返回以下结构：

```json
{
  "code": 0,
  "message": "success",
  "data": {}
}
```

- `code=0` 表示成功，非 0 表示失败
- `message` 为提示信息
- `data` 为业务数据（分页查询时为分页对象）

### 2.4 Swagger UI 在线文档

基于 SpringDoc OpenAPI 自动生成。开发环境启动后访问：`http://localhost:8080/swagger-ui.html`

功能概览表：

| 功能 | 接口 | 说明 |
|------|------|------|
| 分页查询用户 | GET /api/users | 可选 username/email 模糊搜索 |
| 按 ID 查询用户 | GET /api/users/{id} | 返回单个用户详情 |
| 在线 API 文档 | /swagger-ui.html | Swagger UI 交互式文档 |
```

- [ ] **Step 3: 提交**

```bash
git -C D:/projects/claudeprojects/jwbasedata add docs/PRODUCT_MANUAL.md
git -C D:/projects/claudeprojects/jwbasedata commit -m "docs: add product manual chapters 1-2 (overview + features)"
```

---

### Task 2: 撰写第 3 章（技术架构）

**Files:**
- Modify: `docs/PRODUCT_MANUAL.md` — append content

- [ ] **Step 1: 追加第 3 章内容**

```markdown
## 3. 技术架构

### 3.1 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Java | 1.8 | 运行时环境 |
| Spring Boot | 2.7.18 | 应用框架（兼容 Java 8 的最终版本） |
| MyBatis-Plus | 3.5.3.1 | ORM 框架，数据库分页与条件查询 |
| MySQL | 5.7+ | 关系型数据库 |
| MySQL Connector | 5.1.49 | JDBC 驱动 |
| SpringDoc OpenAPI | 1.7.0 | Swagger UI / API 文档生成 |
| Log4j2 | 2.17.2 | 异步日志框架（替代 Logback） |
| Lombok | — | 编译期代码生成（getter/setter/构造器） |
| HikariCP | — | 数据库连接池（Spring Boot 内置） |
| Maven | 3.6+ | 项目构建与依赖管理 |

### 3.2 分层架构

```
┌─────────────────────────────────────────┐
│              Controller 层               │
│   REST 端点定义 · 参数校验 · 请求/响应   │
├─────────────────────────────────────────┤
│               Service 层                 │
│     业务逻辑实现 · 数据转换 · 异常处理    │
├─────────────────────────────────────────┤
│               Mapper 层                  │
│       MyBatis-Plus 数据库访问接口        │
├─────────────────────────────────────────┤
│               Entity 层                  │
│       数据库表映射（ORM 实体类）          │
└─────────────────────────────────────────┘
```

数据流：客户端 → Controller（接收请求）→ Service（业务处理）→ Mapper（数据库查询）→ Entity（表映射）→ 数据库

### 3.3 项目目录结构

```
jwbasedata/
├── pom.xml                                  # Maven 构建配置
├── src/main/java/com/jw/jwbasedata/
│   ├── JwBaseDataApplication.java           # Spring Boot 启动类
│   ├── controller/
│   │   └── UserController.java              # 用户接口控制器
│   ├── service/
│   │   ├── UserService.java                 # 用户服务接口
│   │   └── impl/
│   │       └── UserServiceImpl.java         # 用户服务实现
│   ├── mapper/
│   │   └── UserMapper.java                  # 用户 Mapper（数据访问）
│   ├── entity/
│   │   └── User.java                        # 用户实体（映射 user 表）
│   ├── dto/
│   │   └── UserQueryDTO.java                # 用户查询请求参数
│   ├── vo/
│   │   └── UserVO.java                      # 用户返回视图对象
│   ├── config/
│   │   ├── MybatisPlusConfig.java           # MyBatis-Plus 分页配置
│   │   └── SwaggerConfig.java               # Swagger / OpenAPI 配置
│   └── common/
│       ├── result/
│       │   ├── Result.java                  # 统一返回对象
│       │   └── ResultCode.java              # 返回状态码枚举
│       ├── exception/
│       │   ├── BusinessException.java       # 业务异常类
│       │   └── GlobalExceptionHandler.java  # 全局异常处理器
│       └── page/
│           ├── PageQuery.java               # 分页查询基类
│           └── PageResult.java              # 分页返回结果
├── src/main/resources/
│   ├── application.yml                      # 基础配置（JAR 内置）
│   ├── application-dev.yml                  # 开发环境配置
│   ├── log4j2-spring.xml                    # Log4j2 日志配置
│   └── db/
│       └── schema.sql                       # 数据库建表脚本
└── deploy/
    ├── config/
    │   └── application.yml                  # 外部配置模板（生产使用）
    ├── db/
    │   └── schema.sql                       # 部署用建表脚本
    └── scripts/
        ├── start.sh                         # 启动脚本
        ├── stop.sh                          # 停止脚本
        ├── restart.sh                       # 重启脚本
        └── status.sh                        # 状态检查脚本
```

### 3.4 设计理念

- **分层解耦**：Controller → Service → Mapper 三层分离，每层职责单一，可独立测试和替换
- **统一返回**：所有 API 使用 `Result<T>` 包装，前端统一处理响应（code 判断成功/失败）
- **配置外部化**：JAR 内置配置为基础默认值，部署时通过外部 `config/application.yml` 和环境变量覆盖（数据库连接、Swagger 开关等）
- **数据库分页**：分页查询 100% 走 MyBatis-Plus 的 `PaginationInnerInterceptor`，禁止内存分页
- **日志异步**：Log4j2 异步写入，避免日志 I/O 阻塞业务线程
```

- [ ] **Step 2: 提交**

```bash
git -C D:/projects/claudeprojects/jwbasedata add docs/PRODUCT_MANUAL.md
git -C D:/projects/claudeprojects/jwbasedata commit -m "docs: add chapter 3 (technical architecture)"
```

---

### Task 3: 撰写第 4 章（API 参考）

**Files:**
- Modify: `docs/PRODUCT_MANUAL.md` — append content

- [ ] **Step 1: 追加第 4 章内容**

```markdown
## 4. API 参考

### 4.1 端点总览

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/users | 分页查询用户列表 |
| GET | /api/users/{id} | 根据 ID 查询用户详情 |

### 4.2 分页查询用户列表

**请求**

```
GET /api/users?page=1&size=10&username=zhang&email=example.com
```

**参数说明**

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | Integer | 否 | 1 | 页码（从 1 开始） |
| size | Integer | 否 | 10 | 每页条数（最大 1000） |
| username | String | 否 | — | 用户名，模糊匹配 |
| email | String | 否 | — | 邮箱，模糊匹配 |

**成功响应**

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "username": "zhangsan",
        "email": "zhangsan@example.com",
        "createdAt": "2026-03-29T10:30:00"
      }
    ],
    "total": 100,
    "page": 1,
    "size": 10
  }
}
```

**响应字段说明**

| 字段 | 类型 | 说明 |
|------|------|------|
| code | Integer | 0=成功 |
| message | String | 提示信息 |
| data.records | Array | 用户数据列表 |
| data.records[].id | Long | 用户 ID |
| data.records[].username | String | 用户名 |
| data.records[].email | String | 邮箱 |
| data.records[].createdAt | String | 创建时间（ISO 8601） |
| data.total | Long | 总记录数 |
| data.page | Long | 当前页码 |
| data.size | Long | 每页条数 |

### 4.3 按 ID 查询用户

**请求**

```
GET /api/users/1
```

**成功响应**

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "id": 1,
    "username": "zhangsan",
    "email": "zhangsan@example.com",
    "createdAt": "2026-03-29T10:30:00"
  }
}
```

**错误响应 — 用户不存在**

```json
{
  "code": -1,
  "message": "用户不存在",
  "data": null
}
```

### 4.4 统一返回格式

所有 API 响应均遵循统一 JSON 结构：

```json
{
  "code": 0,
  "message": "success",
  "data": {}
}
```

`data` 字段类型因接口而异：
- 分页查询 → `PageResult` 对象（含 records, total, page, size）
- 单条查询 → 对象（如 UserVO）或 null
- 异常 → `null`

### 4.5 错误码规范

| 错误码范围 | 类型 | 说明 | 示例 |
|-----------|------|------|------|
| 0 | 成功 | 请求处理成功 | — |
| 400 | 参数错误 | 请求参数校验不通过 | `PARAM_ERROR` |
| 401 | 未授权 | 需要身份认证 | `UNAUTHORIZED` |
| 403 | 禁止访问 | 无权限 | `FORBIDDEN` |
| 404 | 资源不存在 | 查询的数据不存在 | `NOT_FOUND` |
| 500 | 服务器错误 | 系统内部异常 | `INTERNAL_SERVER_ERROR` |

自定义业务异常通过 `BusinessException` 抛出，由 `GlobalExceptionHandler` 统一拦截并转换为上述 JSON 格式。

### 4.6 Swagger UI

开发环境启动服务后，浏览器访问：

```
http://localhost:8080/swagger-ui.html
```

可在线查看所有接口的请求参数、响应格式，并直接在线调试（Try it out）。生产环境通过配置关闭 Swagger，减少安全风险。
```

- [ ] **Step 2: 提交**

```bash
git -C D:/projects/claudeprojects/jwbasedata add docs/PRODUCT_MANUAL.md
git -C D:/projects/claudeprojects/jwbasedata commit -m "docs: add chapter 4 (API reference)"
```

---

### Task 4: 撰写第 5 章（部署运维）

**Files:**
- Modify: `docs/PRODUCT_MANUAL.md` — append content

- [ ] **Step 1: 追加第 5 章内容**

```markdown
## 5. 部署运维

### 5.1 环境要求

| 组件 | 最低版本 | 说明 |
|------|---------|------|
| JDK | 1.8 | 运行时 |
| MySQL | 5.7 | 数据库 |
| Maven | 3.6 | 构建工具（仅编译打包时需要） |
| 操作系统 | Linux（推荐）/ Windows | 生产环境建议 Linux |

### 5.2 部署步骤

#### 第一步：打包

```bash
cd jwbasedata
mvn clean package -DskipTests
```

产物：`target/jwbasedata-1.0.0.jar`

#### 第二步：准备部署目录

```
deploy-release/
├── jwbasedata-1.0.0.jar      # 可执行 JAR（从 target 复制）
├── config/
│   └── application.yml        # 外部配置文件（必须）
├── scripts/
│   ├── start.sh               # 启动脚本
│   ├── stop.sh                # 停止脚本
│   ├── restart.sh             # 重启脚本
│   └── status.sh              # 状态检查脚本
└── logs/                      # 日志目录（自动创建）
```

#### 第三步：修改配置

编辑 `config/application.yml`，修改数据库连接：

```yaml
spring:
  datasource:
    url: jdbc:mysql://192.168.1.100:3306/jwbasedata?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_db_user
    password: your_db_password
```

建议数据库密码通过环境变量注入：

```bash
export DB_PASSWORD=your_secure_password
```

配置文件中的 `${DB_PASSWORD:root}` 会自动读取环境变量。

#### 第四步：初始化数据库

```bash
mysql -u root -p < deploy/db/schema.sql
```

#### 第五步：启动服务

```bash
cd deploy-release/scripts
./start.sh prod
```

启动成功输出：

```
========================================
  jwbasedata 服务启动脚本
========================================
正在启动服务...
环境: prod
JVM参数: -Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200
配置目录: ../config
服务启动成功！
PID: 12345
配置文件: ../config/application.yml
日志目录: ../logs
访问地址:
  - API: http://localhost:8080/api/users
  - Swagger: http://localhost:8080/swagger-ui.html
```

### 5.3 配置说明

#### 配置优先级（从低到高）

1. JAR 内置 `application.yml` — 基础默认值
2. `config/application.yml` — 外部配置文件（部署时使用）
3. 环境变量 — `${DB_HOST}`, `${DB_PORT}`, `${DB_NAME}`, `${DB_USERNAME}`, `${DB_PASSWORD}`
4. 命令行参数 — `--server.port=9090`

#### 关键配置项

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| server.port | 服务端口 | 8080 |
| spring.datasource.url | 数据库连接 | jdbc:mysql://localhost:3306/jwbasedata |
| spring.datasource.username | 数据库用户 | root |
| spring.datasource.password | 数据库密码 | root |
| mybatis-plus.configuration.map-underscore-to-camel-case | 下划线转驼峰 | true |
| springdoc.swagger-ui.enabled | Swagger UI 开关 | false（生产）/ true（开发） |

#### 连接池配置（HikariCP）

| 参数 | 值 | 说明 |
|------|-----|------|
| minimum-idle | 10 | 最小空闲连接 |
| maximum-pool-size | 50 | 最大连接数 |
| idle-timeout | 30000ms | 空闲超时 |
| max-lifetime | 1800000ms | 连接最大存活时间 |
| connection-timeout | 30000ms | 获取连接超时 |

### 5.4 日志

日志框架为 Log4j2，配置文件 `log4j2-spring.xml`。

| 日志文件 | 位置 | 内容 |
|---------|------|------|
| console.log | logs/console.log | 所有控制台输出 |
| jwbasedata-info.log | logs/ | INFO 级别日志 |
| jwbasedata-error.log | logs/ | ERROR 级别日志 |

日志滚动策略：按日期 + 文件大小滚动，历史日志自动压缩。

实时查看日志：

```bash
tail -f logs/console.log
tail -f logs/jwbasedata-error.log
```

### 5.5 常用运维命令

| 操作 | 命令 |
|------|------|
| 启动 | `./scripts/start.sh prod` |
| 停止 | `./scripts/stop.sh` |
| 重启 | `./scripts/restart.sh` |
| 状态 | `./scripts/status.sh` |
| 健康检查 | `curl http://localhost:8080/api/users?page=1&size=1` |
```

- [ ] **Step 2: 提交**

```bash
git -C D:/projects/claudeprojects/jwbasedata add docs/PRODUCT_MANUAL.md
git -C D:/projects/claudeprojects/jwbasedata commit -m "docs: add chapter 5 (deployment and operations)"
```

---

### Task 5: 撰写第 6-7 章（开发指南 + 附录）

**Files:**
- Modify: `docs/PRODUCT_MANUAL.md` — append content

- [ ] **Step 1: 追加第 6 章 开发指南**

```markdown
## 6. 开发指南

### 6.1 本地环境搭建

```bash
# 1. 克隆代码
git clone <repo-url>
cd jwbasedata

# 2. 初始化数据库
mysql -u root -p < src/main/resources/db/schema.sql

# 3. 修改开发环境数据库连接（按需）
# 编辑 src/main/resources/application-dev.yml，修改 username/password

# 4. 启动（dev 模式，启用 Swagger）
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 5. 访问验证
# API: http://localhost:8080/api/users?page=1&size=5
# Swagger: http://localhost:8080/swagger-ui.html
```

### 6.2 开发规范要点

**分层职责**

| 层 | 包路径 | 职责 |
|-----|--------|------|
| Controller | `controller/` | 接口定义、参数校验、请求响应，不写业务逻辑 |
| Service | `service/` + `service/impl/` | 业务逻辑、数据转换、调用 Mapper |
| Mapper | `mapper/` | 数据库访问，继承 `BaseMapper<T>` |
| Entity | `entity/` | 数据库表映射，`@TableName` + `@TableId` |
| DTO | `dto/` | 请求参数对象，继承 `PageQuery` |
| VO | `vo/` | 返回视图对象，Swagger 注解 |

**命名规范**

| 类型 | 规范 | 示例 |
|------|------|------|
| 类名 | 大驼峰 | `UserController` |
| 方法名 | 小驼峰 | `getUserById` |
| 变量名 | 小驼峰 | `userName` |
| 常量 | 大写+下划线 | `MAX_PAGE_SIZE` |
| 数据库表名 | 小写+下划线 | `user_info` |
| 数据库字段 | 小写+下划线 | `user_name` |

**依赖注入**：使用 `@RequiredArgsConstructor` + `private final`，不用 `@Autowired`。

**数据库分页**：必须使用 MyBatis-Plus 的 `Page` + `PaginationInnerInterceptor`，禁止在内存中分页。

### 6.3 添加新功能的步骤

以"添加部门查询功能"为例：

1. 创建 Entity：`Department.java`（`@TableName("department")`）
2. 创建 Mapper：`DepartmentMapper.java`（继承 `BaseMapper<Department>`）
3. 创建 DTO：`DepartmentQueryDTO.java`（继承 `PageQuery`，加查询字段）
4. 创建 VO：`DepartmentVO.java`（定义返回字段）
5. 创建 Service：`DepartmentService.java` + `DepartmentServiceImpl.java`
6. 创建 Controller：`DepartmentController.java`（`@RestController` + `@RequestMapping`）
7. 数据库添加 `department` 表并初始化数据

### 6.4 构建命令

```bash
# 编译
mvn clean compile

# 打包（跳过测试）
mvn clean package -DskipTests

# 打包并运行测试
mvn clean package

# 仅运行测试
mvn test
```

### 6.5 核心组件

| 组件 | 类名 | 作用 |
|------|------|------|
| 统一返回 | `Result.java` | 封装所有 API 响应（code + message + data） |
| 状态码枚举 | `ResultCode.java` | 定义 SUCCESS(0), PARAM_ERROR(400), NOT_FOUND(404), INTERNAL_SERVER_ERROR(500) 等 |
| 全局异常处理 | `GlobalExceptionHandler.java` | 拦截 BusinessException、参数校验异常、未知异常，统一转换为 Result |
| 业务异常 | `BusinessException.java` | 业务逻辑异常，可自定义 code 和 message |
| 分页配置 | `MybatisPlusConfig.java` | 配置 MySQL 分页插件，单页最大 1000 条 |
| Swagger 配置 | `SwaggerConfig.java` | OpenAPI 文档配置 |
| 分页查询基类 | `PageQuery.java` | DTO 基类，提供 `toPage()` 转 MyBatis-Plus Page |
| 分页结果 | `PageResult.java` | 封装分页返回（records, total, page, size） |
```

- [ ] **Step 2: 追加第 7 章 附录**

```markdown
## 7. 附录

### 7.1 数据表结构

```sql
CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_username` (`username`),
    KEY `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';
```

### 7.2 技术版本速查

| 组件 | 版本 | 选择原因 |
|------|------|---------|
| Java | 1.8 | 企业环境长期稳定版本 |
| Spring Boot | 2.7.18 | 兼容 Java 8 的最终 2.x 版本 |
| MyBatis-Plus | 3.5.3.1 | 最新稳定版，分页插件成熟 |
| MySQL | 5.7+ | 企业普遍使用的版本 |
| MySQL Connector | 5.1.49 | 兼容 MySQL 5.7 的稳定驱动 |
| SpringDoc | 1.7.0 | OpenAPI 3.0，Spring Boot 2.x 最佳兼容 |
| Log4j2 | 2.17.2 | 修复 CVE-2021-45105 的安全版本 |
| HikariCP | 内置 | Spring Boot 2.x 默认连接池 |

### 7.3 常见问题

**Q: 启动报 "Communications link failure"？**
MySQL 服务未启动或连接配置错误。检查 `config/application.yml` 中 `datasource.url`、`username`、`password`。

**Q: 分页查询怎么控制每页最大条数？**
`MybatisPlusConfig.java` 中 `setMaxLimit(1000L)`，超过 1000 会被限制。可修改该值。

**Q: 生产环境如何关闭 Swagger？**
外部配置 `config/application.yml` 中设置：
```yaml
springdoc:
  api-docs:
    enabled: false
  swagger-ui:
    enabled: false
```

**Q: 如何切换日志级别？**
修改外部配置：
```yaml
logging:
  level:
    com.jw.jwbasedata: INFO  # 改为 INFO 减少日志量
```
```

- [ ] **Step 3: 提交**

```bash
git -C D:/projects/claudeprojects/jwbasedata add docs/PRODUCT_MANUAL.md
git -C D:/projects/claudeprojects/jwbasedata commit -m "docs: add chapters 6-7 (development guide + appendix)"
```

---

### Task 6: 整体审查与润色

**Files:**
- Modify: `docs/PRODUCT_MANUAL.md` — review and fix

- [ ] **Step 1: 审阅文档** — 对照设计规格书自审清单逐项检查

对照 spec 检查：
1. 所有 7 个章节均已覆盖 ✓
2. 无占位符/TODO ✓
3. 章节间无矛盾（版本号一致为 1.1.0，技术栈版本与 pom.xml 一致）
4. 范围聚焦 jwbasedata，未涉及 DataX
5. 代码示例与源码一致（API 路径 `/api/users`、表结构为 `user` 表、配置项来自真实文件）
6. 从 CLAUDE.md 复用的数据（技术栈表、API 示例、配置说明）与实际源码文件交叉验证一致

- [ ] **Step 2: 修复发现的问题（如有）**

- [ ] **Step 3: 提交最终版本**

```bash
git -C D:/projects/claudeprojects/jwbasedata add docs/PRODUCT_MANUAL.md
git -C D:/projects/claudeprojects/jwbasedata commit -m "docs: finalize product manual (review pass)"
```

---

### Task 7: 发布到飞书云文档

**Files:**
- Read: `docs/PRODUCT_MANUAL.md` — 获取完整内容
- 使用 lark-doc skill 创建飞书文档

- [ ] **Step 1: 读取产品说明书文件**

```bash
# 确认文件存在且内容完整
wc -l D:/projects/claudeprojects/jwbasedata/docs/PRODUCT_MANUAL.md
```

- [ ] **Step 2: 使用 lark-doc 创建飞书云文档**

使用 lark-doc skill 将 `docs/PRODUCT_MANUAL.md` 的内容创建为飞书云文档。文档标题为 "jwbasedata 产品说明书 v1.1.0"。

- [ ] **Step 3: 验证飞书文档**

打开飞书文档链接，确认：
- 所有章节完整渲染
- 代码块语法高亮正常
- 表格格式正确
- 目录链接可用

- [ ] **Step 4: 记录飞书文档链接（如有）**
```
飞书文档链接: （从 lark-doc 输出获取）
```
```

### Task 8: 最终提交并收尾

- [ ] **Step 1: 查看最终文件状态**

```bash
git -C D:/projects/claudeprojects/jwbasedata status
git -C D:/projects/claudeprojects/jwbasedata log --oneline -6
```

- [ ] **Step 2: 确认所有内容已提交**

预期提交记录：
```
<hash> docs: finalize product manual (review pass)
<hash> docs: add chapters 6-7 (development guide + appendix)
<hash> docs: add chapter 5 (deployment and operations)
<hash> docs: add chapter 4 (API reference)
<hash> docs: add chapter 3 (technical architecture)
<hash> docs: add product manual chapters 1-2 (overview + features)
```
```
