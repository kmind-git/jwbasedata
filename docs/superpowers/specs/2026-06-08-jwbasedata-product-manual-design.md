# Design Spec: jwbasedata 产品说明书

**日期**: 2026-06-08  
**状态**: 待审批  
**版本**: 1.0

## 目标

为 jwbasedata 项目编写一份综合产品说明书，覆盖产品/业务人员、开发者、运维人员三类读者的需求。

## 输出物

1. **Markdown 源文件**: `docs/PRODUCT_MANUAL.md`
2. **飞书云文档**: 内容同 Markdown 源，通过 lark-doc 发布

## 文档结构

### 1. 产品概述
- 项目定位：企业级基础数据查询 REST API 服务
- 解决什么问题：统一的基础数据访问入口，标准化 API 响应格式
- 适用场景：企业系统中对基础数据（如用户信息）的查询需求

### 2. 功能清单
- 用户分页查询（支持按用户名、邮箱模糊搜索）
- 用户按 ID 查询
- 统一 JSON 响应格式
- Swagger UI 在线 API 文档

### 3. 技术架构
- 技术栈总览表（Java 8 / Spring Boot 2.7.18 / MyBatis-Plus 3.5.3.1 / MySQL 5.7+）
- 分层架构说明与图示（Controller → Service → Mapper → DB）
- 项目目录结构
- 设计理念：分层解耦、统一返回、配置外部化

### 4. API 参考
- 端点列表与说明
  - `GET /api/users` — 分页查询（参数: page, size, username?, email?）
  - `GET /api/users/{id}` — 按 ID 查询
- 统一返回格式说明（Result<T> + 示例 JSON）
- 分页返回格式说明（PageResult<T> + 示例 JSON）
- 错误码规范（0=成功, 1000-1999=参数, 2000-2999=业务, 3000-3999=系统, 4000-4999=数据库）
- Swagger UI 访问方式（/swagger-ui.html）

### 5. 部署运维
- 环境要求（JDK 8+, MySQL 5.7+, Maven）
- 部署步骤（打包 → 配置 → 启动）
- 配置文件说明（优先级、外部配置、环境变量）
- 启动/停止命令
- 日志文件说明（info.log / error.log 位置与格式）
- 监控与状态检查

### 6. 开发指南
- 本地环境搭建流程
- 项目结构说明
- 核心组件说明（Result, GlobalExceptionHandler, MybatisPlusConfig, SwaggerConfig）
- 开发规范要点（分层、命名、数据库规范）
- 数据库初始化（schema.sql）
- 构建命令

### 7. 附录
- 数据表结构（user 表 DDL）
- 配置项速查表
- 技术版本清单

## 格式规范

- 使用中文撰写
- 代码块使用语法高亮（json, sql, bash, yaml）
- 表格用于对比性信息
- 目录使用 Markdown TOC
- 每个章节控制在 100-300 字，代码示例除外

## 发布流程

1. 在 `docs/PRODUCT_MANUAL.md` 写入完整内容
2. 使用 lark-doc 创建飞书云文档
3. 将 Markdown 内容同步到飞书文档
4. 提交 Markdown 文件到 git

## 自审清单

- [ ] 无占位符/TODO
- [ ] 章节间无矛盾
- [ ] 范围聚焦 jwbasedata，不涉及 DataX
- [ ] 无歧义表述
