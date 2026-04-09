# AI 智能待办助手

<div align="center">

<!-- 徽章区 -->
![Vue](https://img.shields.io/badge/Vue-3.4-green?style=flat-square&logo=vue.js)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-brightgreen?style=flat-square&logo=springboot)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=flat-square&logo=mysql)
![TypeScript](https://img.shields.io/badge/TypeScript-5.0-blue?style=flat-square&logo=typescript)
![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)

**一个融合 AI 能力的智能待办管理工具，让任务拆解、规划和进度追踪更加高效。**

[功能介绍](#功能介绍) · [快速开始](#快速开始) · [技术架构](#技术架构) · [项目结构](#项目结构) · [开发文档](#参考文档)

</div>

---

## 项目简介

AI 智能待办助手是一款面向个人用户的任务管理工具，以 **"基础待办 + AI 增强"** 为核心模式。它不仅提供完整的任务 CRUD、分类管理和进度追踪能力，还接入了阿里云通义千问（DashScope）AI 大模型，能够帮助用户：

- 🤖 **自动拆解复杂任务** — 上传一个大目标，AI 帮你拆解成可执行的子任务
- ⚡ **智能优先级排序** — 根据任务紧急程度和时间自动排序
- 📅 **一键生成计划** — 输入目标，自动生成每日/每周行动计划
- 📊 **智能进度总结** — 自动生成任务完成报告和进度图表

---

## 功能介绍

### 基础待办模块

| 功能 | 说明 |
|------|------|
| 用户认证 | 支持登录、注册，采用 JWT 无状态认证 |
| 任务管理 | 完整的增删改查，支持标题、描述、优先级、截止日期 |
| 分类管理 | 灵活分类体系（工作、学习、生活、其他），支持多维度筛选 |
| 状态流转 | 任务状态可视化（待办 → 进行中 → 已完成） |
| 仪表盘 | 统计概览，进度图表，分类分布一目了然 |

### AI 增强模块

| 功能 | 说明 |
|------|------|
| 任务拆解 | 输入模糊目标，AI 自动拆解为可执行的子任务清单 |
| 优先级排序 | 结合紧迫性和重要性，智能推荐任务执行顺序 |
| 计划生成 | 输入时间段目标，AI 生成详细的每日/每周执行计划 |
| 进度总结 | 自动分析任务完成情况，生成总结报告 |
| 智能对话 | 内置 AI 助手，随时解答疑问并给出建议 |

---

## 快速开始

### 环境要求

| 依赖 | 版本要求 | 说明 |
|------|---------|------|
| JDK | 17+ | 建议 JDK 21 |
| Node.js | 18+ | 建议 Node 20 LTS |
| MySQL | 8.0+ | 需提前安装并创建数据库 |
| Maven | 3.6+ | 后端构建工具 |
| Redis | 6.0+ | 可选，用于缓存加速 |

### 1. 克隆项目

```bash
git clone <项目地址>
cd aihelp
```

### 2. 初始化数据库

```bash
mysql -u root -p123456
```

```sql
CREATE DATABASE IF NOT EXISTS ai_help CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE ai_help;
SOURCE ai-help-backend/src/main/resources/db/schema.sql;
EXIT;
```

### 3. 配置 AI API Key

> 前往 [阿里云 DashScope](https://dashscope.console.aliyun.com/) 申请 API Key。

编辑 `ai-help-backend/src/main/resources/application.yml`，填入你的 API Key：

```yaml
ai:
  dashscope:
    api-key: your-api-key-here
```

### 4. 启动后端

```bash
cd ai-help-backend
mvn spring-boot:run
```

后端启动完成 → http://localhost:8080

### 5. 启动前端

```bash
cd ai-help-frontend
npm install
npm run dev
```

前端启动完成 → http://localhost:5173

### 6. 访问项目

打开浏览器访问 **http://localhost:5173**，注册账号后即可使用全部功能。

---

## 技术架构

### 技术选型

| 层级 | 技术选型 | 说明 |
|------|---------|------|
| 前端 | Vue 3 + Vite + TypeScript | 组合式 API，性能优异 |
| UI 框架 | Element Plus | 企业级 Vue 3 组件库 |
| 图表 | ECharts | 丰富的数据可视化图表 |
| 状态管理 | Pinia | Vue 3 专属状态管理 |
| 后端 | Spring Boot 3.2 + MyBatis-Plus | 快速 CRUD，高效持久层 |
| 安全 | Spring Security + JWT | 请求认证与权限控制 |
| 数据库 | MySQL 8.0 | 主数据存储 |
| 缓存 | Redis | 可选，高速缓存层 |
| AI 能力 | 阿里云 DashScope（通义千问） | 任务拆解、计划生成等智能能力 |

### 系统架构图

```
┌─────────────────────────────────────────────────────────┐
│                      用户层（浏览器）                      │
│                   Vue 3 + Element Plus                    │
└─────────────────────────┬───────────────────────────────┘
                          │ HTTP / REST
┌─────────────────────────▼───────────────────────────────┐
│                    应用层（Spring Boot）                   │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐│
│  │ 用户模块  │  │ 任务模块  │  │ 分类模块  │  │ 统计模块  ││
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘│
│                    ┌──────────────────┐                   │
│                    │  AI 能力层（DashScope）│              │
│                    └──────────────────┘                   │
└─────────────────────────┬───────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────┐
│                     数据层（MySQL + Redis）                │
└─────────────────────────────────────────────────────────┘
```

---

## 项目结构

```
e:/aihelp/
│
├── prds/                          # 产品需求文档
│   ├── dp.md                      # 产品需求文档
│   ├── ui.md                      # UI 设计文档
│   └── tech.md                    # 技术架构文档
│
├── ai-help-backend/               # ⬢ 后端（Spring Boot）
│   └── src/main/java/com/aihelp/
│       ├── controller/            # 控制层（REST API）
│       ├── service/               # 业务逻辑层
│       ├── mapper/                # 数据访问层（MyBatis-Plus）
│       ├── entity/                # 实体类
│       ├── dto/                   # 数据传输对象
│       ├── vo/                    # 视图对象
│       ├── security/              # 安全认证（JWT Filter）
│       ├── ai/                    # AI 能力封装
│       └── common/                # 通用工具类
│
└── ai-help-frontend/              # ⬢ 前端（Vue 3）
    └── src/
        ├── api/                   # 接口请求封装
        ├── stores/                 # Pinia 状态管理
        ├── views/                  # 页面组件
        ├── router/                 # Vue Router 路由
        ├── types/                  # TypeScript 类型定义
        └── utils/                  # 工具函数
```

---

## 开发进度

| 阶段 | 内容 | 状态 |
|------|------|------|
| 阶段 1 | 项目初始化、脚手架搭建 | ✅ 已完成 |
| 阶段 2 | 数据库设计、后端基础 CRUD | ✅ 已完成 |
| 阶段 3 | 前端基础页面开发 | ✅ 已完成 |
| 阶段 4 | AI 能力集成（任务拆解、计划生成） | ✅ 已完成 |
| 阶段 5 | 前后端联调与性能优化 | 🔄 进行中 |
| 阶段 6 | 部署上线与文档完善 | ⏳ 待启动 |

---

## 参考文档

| 文档 | 路径 | 说明 |
|------|------|------|
| 产品需求文档 | `prds/dp.md` | 功能需求、用户故事 |
| UI 设计文档 | `prds/ui.md` | 界面设计稿、交互说明 |
| 技术架构文档 | `prds/tech.md` | 详细技术方案、接口设计 |

---

## 开发规范

- **命名规范**：后端遵循 Java 驼峰命名，前端遵循 Vue/TS 规范
- **提交规范**：采用 [Conventional Commits](https://www.conventionalcommits.org/) 格式
- **分支管理**：采用 Git Flow 工作流（main / develop / feature）
- **代码风格**：统一使用 ESLint + Prettier 格式化

---

## 许可证

本项目采用 [MIT License](LICENSE) 开源，欢迎 fork 和 star。

---

> 💡 开发过程中有任何问题，欢迎随时沟通交流。
