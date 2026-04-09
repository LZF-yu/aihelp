# AI智能待办助手 - 后端

基于 Spring Boot 3.2 的后端服务

## 技术栈

- Spring Boot 3.2.0
- Spring Security + JWT
- MyBatis-Plus 3.5.5
- MySQL 8.0
- Redis（可选）
- Hutool 5.8.23
- Lombok

## 项目结构

```
ai-help-backend/
├── src/main/java/com/aihelp/
│   ├── AiHelpApplication.java     # 启动类
│   ├── config/                     # 配置类
│   ├── controller/                 # 控制层
│   ├── service/                    # 业务层
│   ├── mapper/                     # 数据层
│   ├── entity/                     # 实体类
│   ├── dto/                        # 数据传输对象
│   ├── security/                   # 安全认证
│   ├── ai/                         # AI能力封装
│   └── common/                     # 通用类
├── src/main/resources/
│   ├── db/schema.sql               # 数据库脚本
│   ├── mapper/                     # MyBatis XML
│   ├── application.yml             # 配置文件
│   └── application-dev.yml        # 开发环境配置
└── pom.xml
```

## 快速启动

### 1. 初始化数据库

```bash
mysql -u root -p < src/main/resources/db/schema.sql
```

### 2. 修改配置

编辑 `src/main/resources/application-dev.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    password: your_password
```

### 3. 启动服务

```bash
mvn spring-boot:run
```

服务地址：http://localhost:8080

## API接口

### 认证接口

| 接口 | 方法 | 说明 |
|-----|------|------|
| `/api/auth/register` | POST | 用户注册 |
| `/api/auth/login` | POST | 用户登录 |
| `/api/auth/me` | GET | 获取当前用户信息 |
| `/api/auth/me` | PUT | 修改个人信息 |
| `/api/auth/password` | PUT | 修改密码 |

### 任务接口

| 接口 | 方法 | 说明 |
|-----|------|------|
| `/api/tasks` | GET | 获取任务列表 |
| `/api/tasks/{id}` | GET | 获取任务详情 |
| `/api/tasks` | POST | 创建任务 |
| `/api/tasks/{id}` | PUT | 更新任务 |
| `/api/tasks/{id}` | DELETE | 删除任务 |
| `/api/tasks/{id}/status` | PATCH | 更新任务状态 |

### AI接口

| 接口 | 方法 | 说明 |
|-----|------|------|
| `/api/ai/decompose` | POST | AI任务拆解 |
| `/api/ai/priority/sort` | POST | AI优先级排序 |
| `/api/ai/plan/daily` | POST | 生成今日计划 |
| `/api/ai/plan/weekly` | POST | 生成周计划 |
| `/api/ai/report/daily` | POST | 生成日报 |
| `/api/ai/report/weekly` | POST | 生成周报 |

## 环境变量

| 变量 | 说明 | 默认值 |
|-----|------|-------|
| `spring.datasource.password` | 数据库密码 | root |
| `ai.api.key` | DashScope API Key | 用户提供的Key |
