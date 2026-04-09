# AI智能待办助手 - 从0到1开发计划

## 项目概述

本项目是一款面向个人用户的AI智能待办助手，以"基础待办+AI增强"为核心，主打"简单、高效、智能"，帮助用户快速拆解任务、规划时间、跟踪进度。

## 技术栈

| 层级 | 技术选型 |
|-----|---------|
| 前端 | Vue3 + Vite + TypeScript + Element Plus + ECharts |
| 后端 | Spring Boot 3.2 + MyBatis-Plus + Spring Security |
| 数据库 | MySQL 8.0 |
| 缓存 | Redis（可选） |
| AI | 阿里云 DashScope（通义千问） |
| 认证 | JWT |

## 系统架构

```
用户层 (浏览器)
  └─ Vue3 + Element Plus + ECharts
      │
      ▼ HTTP/REST
应用层 (Spring Boot)
  ├─ 用户模块
  ├─ 任务模块
  ├─ 分类模块
  ├─ 仪表盘统计模块
  └─ AI能力层（DashScope API）
      │
      ▼
数据层 (MySQL + Redis)
```

## 项目结构

```
e:\aihelp\
├── prds/                    # 产品需求文档
│   ├── dp.md               # 需求文档
│   ├── ui.md               # UI设计文档
│   └── tech.md             # 技术架构文档
│
├── ai-help-backend/         # 后端项目（Spring Boot）
│   └── src/main/java/com/aihelp/
│       ├── controller/      # 控制层
│       ├── service/        # 业务层
│       ├── mapper/          # 数据层
│       ├── entity/          # 实体类
│       ├── dto/             # 数据传输对象
│       ├── security/        # 安全认证
│       ├── ai/              # AI能力封装
│       └── common/          # 通用类
│
└── ai-help-frontend/         # 前端项目（Vue3）
    └── src/
        ├── api/             # API请求
        ├── stores/          # 状态管理
        ├── views/           # 页面组件
        ├── router/          # 路由配置
        └── types/           # 类型定义
```

## 开发环境状态

| 组件 | 状态 |
|-----|------|
| JDK | ✅ 已安装 |
| Node.js | ✅ 已安装 |
| MySQL | ✅ 已安装 |
| Redis | ✅ 已安装 |
| Maven | ✅ 已安装 |
| AI API Key | ✅ 已获取 |

## 开发阶段

| 阶段 | 内容 | 时间 |
|-----|------|------|
| 阶段1 | 项目初始化（脚手架搭建） | 第1天 |
| 阶段2 | 数据库设计与后端基础开发 | 第2-5天 |
| 阶段3 | 前端基础页面开发 | 第6-9天 |
| 阶段4 | AI能力集成 | 第10-12天 |
| 阶段5 | 前后端联调与优化 | 第13-15天 |
| 阶段6 | 部署与文档整理 | 第16-17天 |

## 快速启动

### 后端启动

```bash
cd ai-help-backend

# 初始化数据库
mysql -u root -p < src/main/resources/db/schema.sql

# 启动后端
mvn spring-boot:run
```

后端地址：http://localhost:8080

### 前端启动

```bash
cd ai-help-frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

前端地址：http://localhost:5173

## 功能模块

### 基础待办模块
- 用户登录/注册
- 待办任务CRUD
- 任务分类管理
- 任务状态管理
- 仪表盘展示

### AI增强模块
- 任务自动拆解
- 智能优先级排序
- 每日/每周计划生成
- 任务进度总结
- AI智能对话

## 参考文档

| 文档 | 说明 |
|-----|------|
| `prds/dp.md` | 产品需求文档 |
| `prds/ui.md` | UI设计文档 |
| `prds/tech.md` | 技术架构文档 |

## 联系方式

开发过程中如有疑问，欢迎随时沟通。
