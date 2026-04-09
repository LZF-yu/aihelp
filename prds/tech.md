# AI智能待办助手 - 技术架构文档

## 文档信息

| 项目名称 | AI智能待办助手 |
|---------|---------------|
| 版本号   | V1.0          |
| 创建日期 | 2026-04-08    |
| 文档用途 | 技术架构设计与技术选型指南 |

---

## 1. 架构设计原则

### 1.1 核心原则

| 原则 | 说明 |
|-----|------|
| **拥抱开源** | 优先使用成熟的开源框架和组件，避免重复造轮子 |
| **生态完善** | 选择社区活跃、文档丰富的中间件，降低学习成本 |
| **易于维护** | 采用主流技术栈，应届生可快速上手和后续维护 |
| **AI优先** | AI能力作为核心亮点，合理设计AI交互架构 |

### 1.2 技术选型策略

```
┌─────────────────────────────────────────────────────────────┐
│                      项目技术选型策略                         │
├─────────────────────────────────────────────────────────────┤
│  前端：Vue3 + Vite + Element Plus + ECharts                │
│  后端：Spring Boot 3.x + MyBatis-Plus                      │
│  数据库：MySQL 8.0                                          │
│  缓存：Redis（可选，用于AI对话历史缓存）                       │
│  AI接口：通义千问 / 讯飞星火（HTTP调用）                       │
│  安全：Spring Security + JWT                                │
│  构建：Maven + npm/vite                                     │
└─────────────────────────────────────────────────────────────┘
```

---

## 2. 前端技术架构

### 2.1 技术栈总览

```
┌────────────────────────────────────────────────────────────────┐
│                        前端技术栈                              │
├──────────────┬──────────────┬──────────────┬──────────────────┤
│   框架       │   构建工具    │   UI组件库    │   图表库         │
├──────────────┼──────────────┼──────────────┼──────────────────┤
│  Vue 3       │   Vite 5     │ Element Plus │  ECharts 5       │
│  (Composition│   极速构建    │ 成熟组件库    │  仪表盘可视化    │
│   API)       │   HMR热更新   │ 电脑端首选    │  轻量集成       │
├──────────────┼──────────────┼──────────────┼──────────────────┤
│   路由       │   状态管理    │   HTTP库     │   工具库         │
├──────────────┼──────────────┼──────────────┼──────────────────┤
│  Vue Router  │   Pinia      │   Axios      │  Day.js / Luxon  │
│  4.x         │  轻量状态管理  │  请求拦截器   │  日期处理       │
└──────────────┴──────────────┴──────────────┴──────────────────┘
```

### 2.2 开源组件详细选型

#### 2.2.1 UI组件库对比与选型

| 组件库 | 成熟度 | Vue3适配 | 文档完善度 | 推荐指数 | 适用场景 |
|-------|-------|---------|-----------|---------|---------|
| **Element Plus** | ⭐⭐⭐⭐⭐ | 完美 | 中文文档完善 | ⭐⭐⭐⭐⭐ | **首选**，企业级PC端应用 |
| Ant Design Vue | ⭐⭐⭐⭐⭐ | 完美 | 中文文档完善 | ⭐⭐⭐⭐ | 复杂后台系统 |
| Vuetify | ⭐⭐⭐⭐ | 完美 | 英文为主 | ⭐⭐⭐ | Material Design风格 |
| Naive UI | ⭐⭐⭐⭐ | 完美 | 中文文档 | ⭐⭐⭐⭐ | 现代化风格 |

**推荐方案：Element Plus**
- 理由：与Vue3深度集成，组件丰富（70+组件），中文文档完善，符合PRD要求的简约风格
- GitHub: https://github.com/element-plus/element-plus

#### 2.2.2 状态管理对比

| 方案 | 包大小 | 学习曲线 | 推荐场景 | 推荐指数 |
|-----|-------|---------|---------|---------|
| **Pinia** | ~2KB | 低 | **首选**，Vue3官方推荐 | ⭐⭐⭐⭐⭐ |
| Vuex | ~30KB | 中 | Vue2迁移项目 | ⭐⭐⭐⭐ |
| MobX-Vue | 较大 | 高 | 复杂响应式场景 | ⭐⭐⭐ |

**推荐方案：Pinia**
- 理由：Vue3官方推荐状态管理库，API简洁，TypeScript支持好，完全兼容Composition API

#### 2.2.3 HTTP请求库对比

| 方案 | 功能完整性 | 拦截器支持 | 推荐场景 | 推荐指数 |
|-----|-----------|-----------|---------|---------|
| **Axios** | ⭐⭐⭐⭐⭐ | 完善 | **首选**，通用场景 | ⭐⭐⭐⭐⭐ |
| Fetch API | 基础 | 需封装 | 简单请求 | ⭐⭐⭐ |
| tauri/http | 完善 | 完善 | Tauri桌面应用 | ⭐⭐⭐ |

**推荐方案：Axios**
- 理由：功能完善，支持请求/响应拦截，自动JSON转换，社区生态丰富

#### 2.2.4 图表库选型

| 库名 | 包大小 | 交互性 | 文档质量 | 推荐场景 | 推荐指数 |
|-----|-------|-------|---------|---------|---------|
| **ECharts** | ~1MB | 丰富 | 中文完善 | **仪表盘首选** | ⭐⭐⭐⭐⭐ |
| AntV/G2Plot | ~200KB | 丰富 | 中文完善 | 统计图表 | ⭐⭐⭐⭐ |
| Chart.js | ~200KB | 中等 | 英文为主 | 简单图表 | ⭐⭐⭐⭐ |
| ApexCharts | ~300KB | 丰富 | 英文为主 | 现代风格 | ⭐⭐⭐ |

**推荐方案：ECharts + vue-echarts**
- 理由：百度开源，图表类型丰富（50+），支持自定义主题，与Vue集成方案成熟（vue-echarts）

### 2.3 前端项目结构

```
ai-help-frontend/
├── public/                      # 静态资源
│   └── favicon.ico
├── src/
│   ├── api/                     # API接口层（使用Axios封装）
│   │   ├── index.ts             # Axios实例配置
│   │   ├── request.ts           # 请求/响应拦截器
│   │   ├── user.ts              # 用户相关接口
│   │   ├── task.ts              # 任务相关接口
│   │   └── ai.ts                # AI功能接口
│   │
│   ├── assets/                  # 静态资源
│   │   ├── images/
│   │   └── styles/
│   │       ├── variables.scss    # 全局样式变量
│   │       └── global.scss      # 全局样式
│   │
│   ├── components/              # 公共组件（复用Element Plus）
│   │   ├── common/
│   │   │   ├── PageHeader.vue    # 页面头部
│   │   │   └── EmptyState.vue    # 空状态展示
│   │   ├── task/
│   │   │   ├── TaskCard.vue      # 任务卡片
│   │   │   ├── TaskForm.vue      # 任务表单
│   │   │   └── TaskFilter.vue    # 任务筛选
│   │   └── ai/
│   │       ├── AIDialog.vue      # AI功能通用弹窗
│   │       └──AILoading.vue      # AI加载动画
│   │
│   ├── composables/            # 组合式函数（复用逻辑）
│   │   ├── useTask.ts           # 任务相关逻辑
│   │   ├── useCategory.ts       # 分类相关逻辑
│   │   ├── useAI.ts             # AI功能封装
│   │   └── useAuth.ts           # 认证相关逻辑
│   │
│   ├── layouts/                # 布局组件
│   │   ├── DefaultLayout.vue    # 默认布局
│   │   ├── AuthLayout.vue        # 认证页布局
│   │   └── components/
│   │       ├── AppHeader.vue     # 顶部导航
│   │       ├── AppSidebar.vue   # 侧边栏
│   │       └── AppFooter.vue     # 底部
│   │
│   ├── pages/                 # 页面组件
│   │   ├── auth/
│   │   │   ├── Login.vue         # 登录页
│   │   │   └── Register.vue       # 注册页
│   │   ├── task/
│   │   │   ├── TaskList.vue       # 任务列表页（首页）
│   │   │   └── TaskDetail.vue      # 任务详情页
│   │   ├── category/
│   │   │   └── CategoryManage.vue  # 分类管理页
│   │   ├── dashboard/
│   │   │   └── Dashboard.vue       # 仪表盘页
│   │   └── profile/
│   │       └── Profile.vue        # 个人中心页
│   │
│   ├── router/                # 路由配置
│   │   ├── index.ts            # 路由实例
│   │   ├── routes.ts           # 路由定义
│   │   └── guards.ts           # 路由守卫
│   │
│   ├── stores/                # Pinia状态管理
│   │   ├── user.ts             # 用户状态
│   │   ├── task.ts             # 任务状态
│   │   └── app.ts              # 应用状态
│   │
│   ├── types/                # TypeScript类型定义
│   │   ├── api.d.ts           # API响应类型
│   │   ├── user.d.ts          # 用户类型
│   │   └── task.d.ts          # 任务类型
│   │
│   ├── utils/                # 工具函数
│   │   ├── storage.ts         # 本地存储封装
│   │   ├── format.ts          # 格式化函数
│   │   └── validate.ts        # 校验函数
│   │
│   ├── App.vue               # 根组件
│   └── main.ts               # 入口文件
│
├── .env.production           # 生产环境变量
├── .env.development          # 开发环境变量
├── index.html
├── package.json
├── vite.config.ts            # Vite配置
├── tsconfig.json             # TypeScript配置
└── README.md
```

### 2.4 前端依赖清单

```json
{
  "dependencies": {
    // 核心框架
    "vue": "^3.4.0",
    "vue-router": "^4.2.0",
    "pinia": "^2.1.0",
    
    // UI组件库（Element Plus - 70+开箱即用组件）
    "element-plus": "^2.5.0",
    "@element-plus/icons-vue": "^2.3.0",
    
    // HTTP请求
    "axios": "^1.6.0",
    
    // 图表库（ECharts）
    "echarts": "^5.5.0",
    "vue-echarts": "^6.6.0",
    
    // 工具库
    "dayjs": "^1.11.0",
    
    // 生产环境依赖
    "compression": "^1.7.4"
  },
  "devDependencies": {
    // 构建工具
    "vite": "^5.0.0",
    "@vitejs/plugin-vue": "^5.0.0",
    
    // TypeScript
    "typescript": "^5.3.0",
    "vue-tsc": "^1.8.0",
    
    // CSS预处理
    "sass": "^1.69.0",
    
    // 代码规范
    "eslint": "^8.55.0",
    "@typescript-eslint/eslint-plugin": "^6.15.0",
    "prettier": "^3.1.0"
  }
}
```

---

## 3. 后端技术架构

### 3.1 技术栈总览

```
┌────────────────────────────────────────────────────────────────┐
│                        后端技术栈                               │
├──────────────┬──────────────┬──────────────┬──────────────────┤
│   框架       │   ORM框架     │   数据库      │   安全框架        │
├──────────────┼──────────────┼──────────────┼──────────────────┤
│  Spring Boot │   MyBatis-   │   MySQL 8.0  │  Spring Security │
│  3.2.x       │   Plus 3.x   │  关系型数据库  │  + JWT           │
│  主流成熟框架 │  简化CRUD操作 │  免费开源      │  无状态认证       │
├──────────────┼──────────────┼──────────────┼──────────────────┤
│   缓存       │   AI接入      │   JSON处理    │   工具库          │
├──────────────┼──────────────┼──────────────┼──────────────────┤
│   Redis      │   Hutool HTTP │   Jackson    │   Lombok         │
│  （可选）     │  + OKHttp    │  JSON序列化   │  简化代码         │
└──────────────┴──────────────┴──────────────┴──────────────────┘
```

### 3.2 开源组件详细选型

#### 3.2.1 ORM框架对比

| 框架 | SQL控制 | CRUD便利性 | 学习曲线 | 推荐场景 | 推荐指数 |
|-----|-------|-----------|---------|---------|---------|
| **MyBatis-Plus** | 完全掌控 | 自动CRUD | 低 | **首选**，快速开发 | ⭐⭐⭐⭐⭐ |
| MyBatis | 完全掌控 | 需手写 | 中 | 复杂SQL场景 | ⭐⭐⭐⭐ |
| JPA/Hibernate | 半自动 | 较好 | 中 | 面向对象开发 | ⭐⭐⭐ |
| Spring Data JPA | 弱 | 很好 | 低 | 简单CRUD | ⭐⭐⭐ |

**推荐方案：MyBatis-Plus**
- 理由：国产优质框架，内置通用CRUD，省去大量样板代码，支持分页插件，与Spring Boot集成简单
- GitHub: https://github.com/baomidou/mybatis-plus

#### 3.2.2 AI接入方案

| 方案 | 易用性 | 免费额度 | 文档质量 | 推荐指数 |
|-----|-------|---------|---------|---------|
| **通义千问（阿里云）** | 简单 | 有一定免费额度 | 中文完善 | ⭐⭐⭐⭐⭐ |
| 讯飞星火 | 简单 | 有免费额度 | 中文完善 | ⭐⭐⭐⭐ |
| 智谱GLM | 简单 | 有免费额度 | 中文完善 | ⭐⭐⭐⭐ |
| 百度文心一言 | 简单 | 有免费额度 | 中文完善 | ⭐⭐⭐⭐ |

**推荐方案：通义千问 + Hutool HTTP**
- 理由：阿里云服务稳定，API文档完善，Hutool提供简洁的HTTP调用封装
- 集成方式：使用 Hutool 的 `HttpRequest` 封装API调用，无需额外SDK

```java
// AI调用封装示例（使用Hutool，无需引入LangChain4j等重框架）
@Service
public class AIService {
    
    @Value("${ai.api.url}")
    private String apiUrl;
    
    @Value("${ai.api.key}")
    private String apiKey;
    
    public String chat(String prompt) {
        // 使用Hutool发送HTTP请求
        String result = HttpRequest.post(apiUrl)
            .header("Authorization", "Bearer " + apiKey)
            .header("Content-Type", "application/json")
            .body(buildRequestBody(prompt))
            .timeout(30000)
            .execute()
            .body();
        
        return parseAIResponse(result);
    }
}
```

#### 3.2.3 工具库选型

| 工具库 | 功能范围 | 文档质量 | 推荐指数 | 适用场景 |
|-------|---------|---------|---------|---------|
| **Hutool** | 全面 | 中文完善 | ⭐⭐⭐⭐⭐ | **首选**，Java工具库瑞士军刀 |
| Lombok | 语法糖 | 完善 | ⭐⭐⭐⭐⭐ | 简化POJO代码 |
| Guava | 全面 | 英文完善 | ⭐⭐⭐⭐ | 集合、缓存等 |
| Apache Commons | 基础 | 完善 | ⭐⭐⭐⭐ | 基础工具 |

**推荐方案：Hutool + Lombok**
- Hutool： Hutool是一个小而全的Java工具类库，涵盖日期、字符串、HTTP、JSON、加密等，几乎涵盖Java开发所有常用工具
- Lombok： 通过注解简化POJO代码（getter/setter/构造器），减少样板代码

#### 3.2.4 JSON处理

| 方案 | 性能 | 功能完整性 | Spring Boot集成 | 推荐指数 |
|-----|-----|-----------|----------------|---------|
| **Jackson** | 高 | 完善 | 自动集成 | ⭐⭐⭐⭐⭐ |
| Gson | 中 | 完善 | 需手动配置 | ⭐⭐⭐⭐ |
| Fastjson | 高（但有安全漏洞） | 完善 | 需手动配置 | ⭐⭐⭐ |

**推荐方案：Jackson（Spring Boot默认）**
- 理由：Spring Boot默认集成，性能优秀，功能完善，无需额外引入

### 3.3 后端项目结构

```
ai-help-backend/
├── src/
│   └── main/
│       ├── java/com/aihelp/
│       │   │
│       │   ├── AiHelpApplication.java     # 启动类
│       │   │
│       │   ├── config/                    # 配置类
│       │   │   ├── CorsConfig.java        # 跨域配置
│       │   │   ├── SecurityConfig.java    # 安全配置
│       │   │   └── MybatisPlusConfig.java # MyBatis-Plus配置
│       │   │
│       │   ├── controller/                # 控制层
│       │   │   ├── AuthController.java    # 认证接口
│       │   │   ├── TaskController.java    # 任务接口
│       │   │   ├── CategoryController.java# 分类接口
│       │   │   ├── DashboardController.java# 仪表盘接口
│       │   │   └── AIController.java      # AI功能接口
│       │   │
│       │   ├── service/                   # 业务层
│       │   │   ├── impl/                  # 业务实现
│       │   │   │   ├── UserServiceImpl.java
│       │   │   │   ├── TaskServiceImpl.java
│       │   │   │   ├── CategoryServiceImpl.java
│       │   │   │   └── AIServiceImpl.java
│       │   │   └── interface/
│       │   │       ├── IUserService.java
│       │   │       ├── ITaskService.java
│       │   │       ├── ICategoryService.java
│       │   │       └── IAIService.java
│       │   │
│       │   ├── mapper/                    # 数据层（MyBatis-Plus）
│       │   │   ├── UserMapper.java
│       │   │   ├── TaskMapper.java
│       │   │   └── CategoryMapper.java
│       │   │
│       │   ├── entity/                     # 实体类
│       │   │   ├── User.java
│       │   │   ├── Task.java
│       │   │   └── Category.java
│       │   │
│       │   ├── dto/                        # 数据传输对象
│       │   │   ├── request/
│       │   │   │   ├── LoginRequest.java
│       │   │   │   ├── RegisterRequest.java
│       │   │   │   ├── TaskRequest.java
│       │   │   │   └── AIRequest.java
│       │   │   └── response/
│       │   │       ├── Response.java
│       │   │       ├── TaskResponse.java
│       │   │       └── AIResponse.java
│       │   │
│       │   ├── ai/                         # AI能力封装
│       │   │   ├── AIProvider.java         # AI服务提供商接口
│       │   │   ├── TongyiProvider.java      # 通义千问实现
│       │   │   └── prompt/                 # 提示词模板
│       │   │       ├── TaskDecomposePrompt.java
│       │   │       ├── PrioritySortPrompt.java
│       │   │       └── PlanGeneratePrompt.java
│       │   │
│       │   ├── security/                   # 安全认证
│       │   │   ├── JwtUtil.java            # JWT工具类
│       │   │   ├── JwtAuthFilter.java      # JWT过滤器
│       │   │   └── UserDetailsServiceImpl.java
│       │   │
│       │   ├── common/                     # 通用类
│       │   │   ├── Constants.java          # 常量定义
│       │   │   ├── ResultCode.java         # 响应码枚举
│       │   │   └── GlobalException.java    # 全局异常
│       │   │
│       │   └── util/                       # 工具类
│       │       ├── PasswordEncoder.java    # 密码加密
│       │       └── DateUtil.java           # 日期工具
│       │
│       └── resources/
│           ├── mapper/                     # MyBatis XML映射文件
│           │   ├── UserMapper.xml
│           │   ├── TaskMapper.xml
│           │   └── CategoryMapper.xml
│           ├── application.yml             # 主配置文件
│           ├── application-dev.yml         # 开发环境配置
│           └── application-prod.yml        # 生产环境配置
│
├── pom.xml                                # Maven依赖配置
└── README.md
```

### 3.4 后端依赖清单

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project>
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
    </parent>
    
    <dependencies>
        <!-- Web开发（内置Tomcat） -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <!-- Spring Security（安全认证） -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        
        <!-- MyBatis-Plus（ORM框架） -->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
            <version>3.5.5</version>
        </dependency>
        
        <!-- MySQL驱动 -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
        
        <!-- Redis（可选，缓存AI对话历史） -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        
        <!-- JWT（无状态认证） -->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>0.12.3</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>0.12.3</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>0.12.3</version>
            <scope>runtime</scope>
        </dependency>
        
        <!-- Hutool（Java工具库瑞士军刀） -->
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>5.8.23</version>
        </dependency>
        
        <!-- Lombok（简化POJO） -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        
        <!-- Validation（参数校验） -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        
        <!-- Test（单元测试） -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

---

## 4. 数据库设计

### 4.1 数据库选型

| 数据库 | 类型 | 推荐指数 | 说明 |
|-------|-----|---------|------|
| **MySQL 8.0** | 关系型 | ⭐⭐⭐⭐⭐ | 首选，免费开源，资料丰富 |
| PostgreSQL | 关系型 | ⭐⭐⭐⭐ | 功能强大，适合复杂查询 |
| SQLite | 嵌入式 | ⭐⭐⭐ | 轻量，单机版可选 |

**推荐方案：MySQL 8.0**
- 理由：互联网行业主流数据库，Spring Boot生态集成完善，适合本项目数据量

### 4.2 数据库表结构

#### 4.2.1 用户表（user）

```sql
CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名（唯一）',
    `password` VARCHAR(100) NOT NULL COMMENT '密码（BCrypt加密）',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
```

#### 4.2.2 任务分类表（task_category）

```sql
CREATE TABLE `task_category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID（外键）',
    `category_name` VARCHAR(50) NOT NULL COMMENT '分类名称',
    `category_color` VARCHAR(20) DEFAULT '#409EFF' COMMENT '分类颜色',
    `sort_order` INT DEFAULT 0 COMMENT '排序顺序',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务分类表';
```

#### 4.2.3 待办任务表（task）

```sql
CREATE TABLE `task` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '任务ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID（外键）',
    `category_id` BIGINT DEFAULT NULL COMMENT '分类ID（外键，可为NULL）',
    `parent_id` BIGINT DEFAULT NULL COMMENT '父任务ID（用于子任务/拆解任务）',
    `title` VARCHAR(200) NOT NULL COMMENT '任务标题',
    `description` TEXT COMMENT '任务描述',
    `priority` VARCHAR(10) DEFAULT 'middle' COMMENT '优先级：high/中/middle/low',
    `status` TINYINT DEFAULT 0 COMMENT '状态：0-未完成，1-已完成',
    `deadline` DATETIME DEFAULT NULL COMMENT '截止时间',
    `reminder_time` DATETIME DEFAULT NULL COMMENT '提醒时间',
    `completed_time` DATETIME DEFAULT NULL COMMENT '完成时间',
    `tags` VARCHAR(500) DEFAULT NULL COMMENT '标签，JSON数组',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_status` (`status`),
    KEY `idx_deadline` (`deadline`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='待办任务表';
```

### 4.3 MyBatis-Plus配置

```yaml
# application.yml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/ai_help?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: root
    password: your_password

mybatis-plus:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.aihelp.entity
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl  # 开发环境打印SQL
  global-config:
    db-config:
      id-type: auto
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0
```

---

## 5. AI能力架构

### 5.1 AI接入策略

```
┌─────────────────────────────────────────────────────────────┐
│                     AI能力调用架构                            │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│   ┌─────────┐    ┌─────────────┐    ┌─────────────────┐   │
│   │ 用户请求  │───▶│ AI Controller │───▶│  AI Service     │   │
│   └─────────┘    └─────────────┘    └────────┬────────┘   │
│                                               │              │
│                              ┌────────────────▼────────────────┐
│                              │       AI Provider 抽象层        │
│                              │  ┌─────────────────────────┐   │
│                              │  │      Prompt 模板管理      │   │
│                              │  └─────────────────────────┘   │
│                              └────────────────┬────────────────┘
│                                               │              │
│              ┌────────────────────────────────┼───────────┐ │
│              │                                │           │ │
│              ▼                                ▼           ▼ │
│       ┌──────────┐                    ┌──────────┐  ┌───────┐│
│       │ 通义千问   │                    │ 讯飞星火  │  │智谱GLM ││
│       │ Tongyi   │                    │  Spark  │  │  GLM  ││
│       └──────────┘                    └──────────┘  └───────┘│
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### 5.2 AI Provider 抽象

```java
/**
 * AI服务提供商接口
 * 使用策略模式，便于切换不同的AI服务
 */
public interface AIProvider {
    
    /**
     * 获取提供商名称
     */
    String getProviderName();
    
    /**
     * 发送对话请求
     * @param prompt 提示词
     * @return AI响应内容
     */
    String chat(String prompt);
    
    /**
     * 解析JSON格式响应
     * @param prompt 提示词
     * @param clazz 目标类型
     * @return 解析后的对象
     */
    <T> T chatForObject(String prompt, Class<T> clazz);
}
```

### 5.3 提示词模板管理

```java
/**
 * AI任务拆解提示词
 */
@Component
public class TaskDecomposePrompt {
    
    private static final String TEMPLATE = """
        请将以下任务拆解为具体可执行的子任务。
        
        原任务：%s
        %s
        
        要求：
        1. 拆解为5-10个子任务
        2. 每个子任务包含：标题、建议截止时间（距离今天的天数）、优先级（高/中/低）
        3. 子任务之间有合理的依赖关系
        
        请以JSON数组格式返回，格式如下：
        [
            {
                "title": "子任务标题",
                "days": 3,
                "priority": "high"
            }
        ]
        """;
    
    public String build(String taskTitle, String taskDescription) {
        String desc = StringUtils.hasText(taskDescription) 
            ? "任务描述：" + taskDescription : "";
        return String.format(TEMPLATE, taskTitle, desc);
    }
}
```

### 5.4 通义千问实现

```java
/**
 * 通义千问AI服务实现
 */
@Component
@RequiredArgsConstructor
public class TongyiProvider implements AIProvider {
    
    @Value("${ai.tongyi.api-url}")
    private String apiUrl;
    
    @Value("${ai.tongyi.api-key}")
    private String apiKey;
    
    private final ObjectMapper objectMapper;
    
    @Override
    public String getProviderName() {
        return "通义千问";
    }
    
    @Override
    public String chat(String prompt) {
        // 构建请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "qwen-turbo");
        
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "user", "content", prompt));
        requestBody.put("messages", messages);
        
        try {
            // 使用Hutool发送HTTP请求
            String response = HttpRequest.post(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(objectMapper.writeValueAsString(requestBody))
                .timeout(30000)
                .execute()
                .body();
            
            // 解析响应
            return parseResponse(response);
        } catch (Exception e) {
            throw new RuntimeException("AI服务调用失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public <T> T chatForObject(String prompt, Class<T> clazz) {
        String json = chat(prompt);
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("AI响应解析失败", e);
        }
    }
    
    private String parseResponse(String response) throws JsonProcessingException {
        // 解析通义千问响应格式
        JsonNode root = objectMapper.readTree(response);
        return root.path("choices")
                   .path(0)
                   .path("message")
                   .path("content")
                   .asText();
    }
}
```

---

## 6. 安全认证架构

### 6.1 JWT认证流程

```
┌─────────────────────────────────────────────────────────────┐
│                     JWT认证流程                              │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  1. 登录请求                                                 │
│     ┌────────┐                                              │
│     │ 用户   │───▶ POST /api/auth/login ──▶ JwtAuthFilter    │
│     └────────┘              │                    │        │
│                              ▼                    ▼        │
│                         验证用户      ┌─────────────────┐   │
│                         密码正确      │  生成JWT Token  │   │
│                              │       └─────────────────┘   │
│                              ▼                │            │
│                         返回Token ◀────────────┘            │
│                              │                              │
│  2. 请求认证                 │                              │
│     ┌────────┐              │                              │
│     │ 用户   │───▶ GET /api/task ◀── Header: Authorization  │
│     └────────┘              │                    │         │
│                              ▼                    ▼         │
│                        JwtAuthFilter ◀──── 验证Token       │
│                              │                    │         │
│                              ▼                    ▼         │
│                      ┌────────────┐      ┌──────────────┐   │
│                      │ Token无效   │      │  放行，请求   │   │
│                      │ 返回401     │      │  到达Controller│   │
│                      └────────────┘      └──────────────┘   │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### 6.2 核心安全配置

```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationEntryPointImpl authenticationEntryPoint;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 禁用CSRF（前后端分离项目）
            .csrf(csrf -> csrf.disable())
            
            // 禁用Session（使用JWT无状态认证）
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 配置请求权限
            .authorizeHttpRequests(auth -> auth
                // 公开接口
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/ai/**").permitAll()
                .requestMatchers("/doc.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                // 其他接口需要认证
                .anyRequest().authenticated()
            )
            
            // 添加JWT过滤器
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            
            // 配置异常处理
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(authenticationEntryPoint)
            );
        
        return http.build();
    }
}
```

### 6.3 JWT工具类

```java
@Component
public class JwtUtil {
    
    private static final String SECRET_KEY = "your-secret-key-change-in-production";
    private static final long EXPIRATION = 7 * 24 * 60 * 60 * 1000; // 7天
    
    /**
     * 生成Token
     */
    public String generateToken(Long userId, String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION);
        
        return Jwts.builder()
            .subject(userId.toString())
            .claim("username", username)
            .issuedAt(now)
            .expiration(expiration)
            .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
            .compact();
    }
    
    /**
     * 验证Token并获取用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
            .build()
            .parseSignedClaims(token)
            .getPayload();
        
        return Long.parseLong(claims.getSubject());
    }
    
    /**
     * 验证Token是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
```

---

## 7. 部署架构

### 7.1 开发环境部署

```
┌─────────────────────────────────────────────────────────────┐
│                    开发环境架构                               │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│   ┌─────────────┐      ┌─────────────┐      ┌───────────┐  │
│   │   前端开发   │      │   后端开发   │      │  数据库   │  │
│   │   localhost │      │   localhost │      │ localhost │  │
│   │   :5173     │      │   :8080     │      │   :3306   │  │
│   └──────┬──────┘      └──────┬──────┘      └─────┬─────┘  │
│          │                    │                  │         │
│          │      HTTP API      │                  │         │
│          └────────────────────┴──────────────────┘         │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### 7.2 生产环境部署

```
┌─────────────────────────────────────────────────────────────┐
│                    生产环境架构                               │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│                      ┌─────────────┐                        │
│                      │   Nginx     │                        │
│                      │  反向代理    │                        │
│                      │  静态资源    │                        │
│                      └──────┬──────┘                        │
│                             │                               │
│         ┌───────────────────┴───────────────────┐          │
│         │                                       │          │
│         ▼                                       ▼          │
│  ┌─────────────┐                       ┌─────────────┐     │
│  │  前端静态文件 │                       │  后端服务    │     │
│  │   (dist)   │        HTTP API        │  Spring Boot│     │
│  └─────────────┘ ◀─────────────────────▶└──────┬──────┘     │
│                                                │            │
│                                                ▼            │
│                                        ┌─────────────┐     │
│                                        │   MySQL     │     │
│                                        │  数据库服务   │     │
│                                        └─────────────┘     │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### 7.3 Nginx配置

```nginx
server {
    listen 80;
    server_name your-domain.com;
    
    # 前端静态资源
    location / {
        root /var/www/ai-help/dist;
        try_files $uri $uri/ /index.html;
    }
    
    # 后端API代理
    location /api/ {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        
        # 超时配置
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
    }
    
    # 开启Gzip压缩
    gzip on;
    gzip_types text/plain text/css application/json application/javascript;
}
```

---

## 8. 开源组件汇总表

### 8.1 前端开源组件

| 组件名称 | 版本 | 用途 | GitHub/官网 |
|---------|-----|------|------------|
| Vue.js | 3.4.x | 前端框架 | https://vuejs.org |
| Vue Router | 4.2.x | 路由管理 | https://router.vuejs.org |
| Pinia | 2.1.x | 状态管理 | https://pinia.vuejs.org |
| Element Plus | 2.5.x | UI组件库 | https://element-plus.org |
| Axios | 1.6.x | HTTP请求 | https://axios-http.com |
| ECharts | 5.5.x | 图表库 | https://echarts.apache.org |
| vue-echarts | 6.6.x | Vue+ECharts集成 | https://github.com/ecomfe/vue-echarts |
| dayjs | 1.11.x | 日期处理 | https://day.js.org |
| Vite | 5.0.x | 构建工具 | https://vitejs.dev |

### 8.2 后端开源组件

| 组件名称 | 版本 | 用途 | GitHub/官网 |
|---------|-----|------|------------|
| Spring Boot | 3.2.x | 后端框架 | https://spring.io |
| Spring Security | 6.x | 安全认证 | https://spring.io/security |
| MyBatis-Plus | 3.5.x | ORM框架 | https://baomidou.com |
| MySQL | 8.0 | 数据库 | https://mysql.com |
| Redis | 7.x | 缓存（可选） | https://redis.io |
| Hutool | 5.8.x | Java工具库 | https://hutool.cn |
| Lombok | 1.18.x | 代码简化 | https://projectlombok.org |
| JJWT | 0.12.x | JWT处理 | https://github.com/jwtk/jjwt |
| Apache Commons | 3.x | 基础工具 | https://commons.apache.org |

### 8.3 AI服务提供商

| 服务商 | 免费额度 | API稳定性 | 文档质量 | 推荐指数 |
|-------|---------|----------|---------|---------|
| 通义千问（阿里云） | 有一定额度 | 高 | 中文完善 | ⭐⭐⭐⭐⭐ |
| 讯飞星火 | 有免费额度 | 高 | 中文完善 | ⭐⭐⭐⭐ |
| 智谱GLM | 有免费额度 | 高 | 中文完善 | ⭐⭐⭐⭐ |
| 百度文心一言 | 有免费额度 | 高 | 中文完善 | ⭐⭐⭐⭐ |

---

## 9. 技术文档清单

| 文档名称 | 文档用途 | 优先级 |
|---------|--------|--------|
| **技术架构文档** | 本文档，总体技术方案 | 必读 |
| 数据库设计文档 | 表结构详细说明、ER图 | 必读 |
| API接口文档 | 接口定义、请求/响应示例 | 必读 |
| 前端开发指南 | 前端代码规范、组件使用 | 参考 |
| 后端开发指南 | 后端代码规范、分层说明 | 参考 |
| AI功能设计文档 | AI提示词模板、AI能力说明 | 参考 |
| 部署文档 | 环境配置、部署步骤 | 参考 |
| 运维文档 | 监控、日志、备份 | 可选 |

---

## 10. 总结

本技术文档基于PRD需求，采用**"拥抱开源、避免重复造轮子"**的原则，为AI智能待办助手项目制定了完整的技术架构方案：

1. **前端技术选型**：Vue3 + Vite + Element Plus + ECharts，组件丰富、开发高效
2. **后端技术选型**：Spring Boot 3.x + MyBatis-Plus + MySQL，主流稳定
3. **AI能力方案**：通义千问 + Hutool HTTP，轻量集成、稳定可靠
4. **安全认证方案**：Spring Security + JWT，无状态认证
5. **开发工具链**：Maven + npm，生态完善

所有技术选型均遵循以下原则：
- ✅ 选择成熟稳定的主流框架
- ✅ 优先选择中文文档完善的组件
- ✅ 适配应届生技术能力
- ✅ 避免手搓代码，充分利用开源组件

---

*文档结束*
