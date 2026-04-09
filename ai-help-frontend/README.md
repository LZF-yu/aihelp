# AI智能待办助手 - 前端

基于 Vue3 + TypeScript 的前端应用

## 技术栈

- Vue 3.4 + Composition API
- Vite 5
- TypeScript
- Element Plus
- Pinia（状态管理）
- Vue Router 4
- Axios
- ECharts 5
- dayjs

## 项目结构

```
ai-help-frontend/
├── src/
│   ├── api/                # API请求封装
│   ├── stores/             # Pinia状态管理
│   ├── views/               # 页面组件
│   │   ├── Login.vue       # 登录页
│   │   ├── Register.vue    # 注册页
│   │   ├── Layout.vue      # 布局组件
│   │   ├── Home.vue        # 首页（待办列表）
│   │   └── Profile.vue     # 个人中心
│   ├── router/              # 路由配置
│   ├── types/               # TypeScript类型
│   ├── utils/               # 工具函数
│   ├── App.vue              # 根组件
│   └── main.ts              # 入口文件
├── public/
├── index.html
├── vite.config.ts
├── tsconfig.json
└── package.json
```

## 快速启动

### 1. 安装依赖

```bash
npm install
```

### 2. 启动开发服务器

```bash
npm run dev
```

访问地址：http://localhost:5173

### 3. 构建生产版本

```bash
npm run build
```

## 页面说明

### 登录/注册页
- 用户名密码登录
- 表单验证
- 登录状态持久化

### 首页（待办列表）
- 左侧分类筛选
- 中间任务列表（表格展示）
- 右侧数据仪表盘
- AI功能入口

### 个人中心
- 个人信息修改
- 密码修改

## 代理配置

开发环境下，API请求会代理到 `http://localhost:8080`。

如需修改，编辑 `vite.config.ts`：

```typescript
server: {
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true
    }
  }
}
```
