# Simple 前后端分离项目 — 技术方案

## 1 项目概述

### 1.1 背景

创建一个前后端分离的全栈项目，实现登录认证、用户管理、商品管理三大功能模块。
项目定位为技术演示和脚手架，便于后续扩展多业务模块。

### 1.2 目标

- 前端使用 Vue3 + Element Plus 构建现代化管理后台界面
- 后端使用 Spring Boot 构建 RESTful API，以内存 HashMap 替代数据库
- 基于 Cookie + Session 实现跨域认证，Session 存储于服务端内存
- 代码结构清晰、注释丰富，支持后续按业务模块水平扩展

### 1.3 项目地址

```
simple/
├── simple-api/    ← 后端服务（Java + Spring Boot）
├── simple-ui/     ← 前端应用（Vue3 + Element Plus）
└── doc/           ← 技术文档（本文档所在目录）
```

---

## 2 技术选型

### 2.1 前端

| 技术 | 版本 | 用途 |
|------|------|------|
| Vue | 3.4 | 响应式 UI 框架 |
| Vue Router | 4.3 | 前端路由，History 模式 |
| Pinia | 2.1 | 状态管理 |
| Element Plus | 2.7 | UI 组件库（Table/Form/Dialog/Menu 等） |
| Axios | 1.7 | HTTP 客户端 |
| Vite | 5.4 | 构建工具 + 开发服务器 |

### 2.2 后端

| 技术 | 版本 | 用途 |
|------|------|------|
| Java | 17 | 开发语言 |
| Spring Boot | 3.2.5 | Web 框架 |
| Jackson | — | JSON 序列化（Spring Boot 内嵌） |
| Maven | — | 依赖管理 |

### 2.3 为什么不用数据库

本项目定位为技术演示和脚手架，使用 `ConcurrentHashMap` 模拟数据存储：
- 避免依赖 MySQL/Redis 等外部基础设施，零配置启动
- 演示 CRUD 业务逻辑和 API 设计，不涉及持久化
- 后续接入真实数据库时，只需替换 Service 层实现即可

---

## 3 整体架构

```
┌─────────────────────────────────────────────────────────┐
│                        浏览器                            │
│  ┌───────────────────────────────────────────────────┐  │
│  │              前端 (localhost:5173)                  │  │
│  │  ┌───────┐  ┌──────┐  ┌───────┐  ┌─────────────┐  │  │
│  │  │ Login │  │Layout│  │ User  │  │   Product   │  │  │
│  │  │  .vue │  │ .vue │  │ Index │  │    Index    │  │  │
│  │  └───┬───┘  └──┬───┘  └───┬───┘  └──────┬──────┘  │  │
│  │      └─────────┴──────────┴──────────────┘         │  │
│  │                        │                           │  │
│  │              ┌─────────▼─────────┐                 │  │
│  │              │   request.js      │                 │  │
│  │              │  (axios 实例)     │                 │  │
│  │              └─────────┬─────────┘                 │  │
│  └────────────────────────┼───────────────────────────┘  │
└───────────────────────────┼──────────────────────────────┘
                            │ HTTP + Cookie
                            ▼
┌─────────────────────────────────────────────────────────┐
│              后端 (localhost:8080)                        │
│                                                         │
│  ┌───────────────────────────────────────────────────┐  │
│  │              AuthInterceptor                       │  │
│  │  校验 SIMPLE_SESSION_ID Cookie，白名单跳过登录      │  │
│  └──────────────────────────┬────────────────────────┘  │
│                             │                           │
│              ┌──────────────┼──────────────┐            │
│              ▼              ▼              ▼            │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐    │
│  │ AuthController│ │UserController│ │ProductCtrl  │    │
│  └──────┬───────┘ └──────┬───────┘ └──────┬───────┘    │
│         │                │                │            │
│         ▼                ▼                ▼            │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐    │
│  │ AuthService  │ │ UserService  │ │ProductSvc   │    │
│  └──────┬───────┘ └──────┬───────┘ └──────┬───────┘    │
│         │                │                │            │
│         ▼                ▼                ▼            │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐    │
│  │ SessionManager│ │userStore    │ │productStore  │    │
│  │(ConcurrentMap)│ │(ConcurMap)  │ │(ConcurMap)   │    │
│  └──────────────┘ └──────────────┘ └──────────────┘    │
└─────────────────────────────────────────────────────────┘
```

---

## 4 目录结构

### 4.1 后端

```
simple-api/
├── pom.xml                          # Maven 配置（Spring Boot 3.2.5）
└── src/main/
    ├── java/com/simple/api/
    │   ├── SimpleApiApplication.java    # 启动入口
    │   ├── common/
    │   │   ├── Result.java              # 统一响应包装 {code, message, data}
    │   │   └── SessionStore.java        # ConcurrentHashMap Session 存储
    │   ├── config/
    │   │   ├── SessionManager.java      # Session 创建/校验/销毁管理器
    │   │   └── WebConfig.java           # CORS + 拦截器注册
    │   ├── controller/
    │   │   ├── AuthController.java      # 登录/登出
    │   │   ├── UserController.java      # 用户 CRUD
    │   │   └── ProductController.java   # 商品 CRUD
    │   ├── entity/
    │   │   ├── User.java                # 用户实体
    │   │   └── Product.java             # 商品实体
    │   ├── interceptor/
    │   │   └── AuthInterceptor.java     # 认证拦截器
    │   ├── service/
    │   │   ├── AuthService.java         # 认证业务
    │   │   ├── UserService.java         # 用户业务
    │   │   └── ProductService.java      # 商品业务
    │   └── exception/
    │       └── GlobalExceptionHandler.java  # 全局异常处理
    └── resources/
        └── application.yml              # 端口/Cookie/Jackson 配置
```

### 4.2 前端

```
simple-ui/
├── package.json                     # 依赖声明
├── vite.config.js                   # Vite 构建配置
├── index.html                       # HTML 入口
└── src/
    ├── main.js                      # Vue 应用入口
    ├── App.vue                      # 根组件
    ├── router/index.js              # 路由配置 + beforeEach 守卫
    ├── store/index.js               # Pinia authStore
    ├── utils/request.js             # axios 实例（withCredentials + 拦截器）
    ├── api/
    │   ├── auth.js                  # 登录/登出 API
    │   ├── user.js                  # 用户 CRUD API
    │   └── product.js               # 商品 CRUD API
    └── views/
        ├── Login.vue                # 登录页
        ├── Layout.vue               # 主布局（侧边栏+顶栏+内容区）
        ├── user/Index.vue           # 用户管理页
        └── product/Index.vue        # 商品管理页
```

---

## 5 模块设计

### 5.1 认证模块

#### 5.1.1 认证方式

采用 **Cookie + Session** 方案：
- 登录成功后，后端生成 UUID 作为 SessionId，通过 `Set-Cookie` 响应头下发
- Cookie 名称：`SIMPLE_SESSION_ID`
- Cookie 属性：`HttpOnly=true`（防 XSS）、`Path=/`、`Max-Age=86400`（24 小时）
- 后续请求浏览器自动携带 Cookie，后端拦截器校验

#### 5.1.2 Session 存储

```
ConcurrentHashMap<String, SessionInfo>
    Key:   SessionId (UUID 字符串)
    Value: SessionInfo { sessionId, username, createTime, lastAccessTime }
```

选择 `ConcurrentHashMap` 保证线程安全，避免多线程并发读写导致 `ConcurrentModificationException`。

#### 5.1.3 认证流程

```
登录：
  前端 POST /api/auth/login {username, password}
      → AuthInterceptor 白名单跳过
      → AuthService 校验密码 (admin/admin123)
      → SessionManager.createSession() 生成 UUID
      → AuthController Set-Cookie: SIMPLE_SESSION_ID=<uuid>
      → 返回 { username }

受保护请求：
  浏览器自动携带 Cookie: SIMPLE_SESSION_ID=<uuid>
      → AuthInterceptor 提取 Cookie
      → SessionManager.validateSession() 校验并刷新 lastAccessTime
      → 校验通过：将 username 存入 request 属性，放行
      → 校验失败：返回 401 JSON，前端拦截器跳转 /login

登出：
  前端 POST /api/auth/logout
      → AuthInterceptor 校验 Cookie（此时仍有效）
      → AuthService.logout() 从 Map 中删除 Session
      → Set-Cookie: SIMPLE_SESSION_ID=; Max-Age=0（使 Cookie 过期）
      → 前端清除 Pinia 状态，跳转 /login
```

#### 5.1.4 拦截器设计

```
AuthInterceptor.preHandle():
  1. OPTIONS 预检请求 → 直接放行（CORS 需要）
  2. POST /api/auth/login → 白名单放行
  3. 提取 SIMPLE_SESSION_ID Cookie
     - 不存在 → 返回 401 "未登录"
     - 存在 → SessionManager.validateSession()
       - 无效 → 返回 401 "登录已过期"
       - 有效 → request.setAttribute("currentUsername", username)，放行
```

### 5.2 用户管理模块

#### 5.2.1 实体设计

| 字段 | 类型 | 说明 |
|------|------|------|
| id | String (UUID) | 主键，自动生成 |
| username | String | 用户名，唯一 |
| password | String | 密码（演示项目明文） |
| email | String | 邮箱 |
| createTime | LocalDateTime | 创建时间 |
| updateTime | LocalDateTime | 修改时间 |

#### 5.2.2 存储设计

```java
ConcurrentHashMap<String, User> userStore
    @PostConstruct 预置: admin, zhangsan, lisi
```

#### 5.2.3 API 列表

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/user/list | 查询用户列表 |
| GET | /api/user/{id} | 查询用户详情 |
| POST | /api/user | 创建用户 |
| PUT | /api/user/{id} | 更新用户 |
| DELETE | /api/user/{id} | 删除用户 |

### 5.3 商品管理模块

#### 5.3.1 实体设计

| 字段 | 类型 | 说明 |
|------|------|------|
| id | String (UUID) | 主键，自动生成 |
| name | String | 商品名称 |
| description | String | 商品描述 |
| price | BigDecimal | 价格（使用 BigDecimal 避免浮点精度问题） |
| stock | Integer | 库存数量 |
| createTime | LocalDateTime | 创建时间 |
| updateTime | LocalDateTime | 修改时间 |

#### 5.3.2 存储设计

```java
ConcurrentHashMap<String, Product> productStore
    @PostConstruct 预置: iPhone 15 Pro, MacBook Air M3, AirPods Pro 2
```

#### 5.3.3 API 列表

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/product/list | 查询商品列表 |
| GET | /api/product/{id} | 查询商品详情 |
| POST | /api/product | 创建商品 |
| PUT | /api/product/{id} | 更新商品 |
| DELETE | /api/product/{id} | 删除商品 |

---

## 6 接口协议

### 6.1 统一响应格式

所有 API 均返回 `Result<T>` 格式：

```json
{
  "code": 0,
  "message": "操作成功",
  "data": { ... }
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| code | int | 0=成功，非0=失败 |
| message | string | 提示信息 |
| data | T | 响应数据，成功时携带 |

失败码约定：

| code | 含义 | 示例 |
|------|------|------|
| 400 | 参数错误 | "用户名或密码错误" |
| 401 | 未授权 | "未登录，请先登录" |
| 500 | 服务器错误 | "服务器内部错误" |

### 6.2 登录接口

```
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}

响应 200:
{
  "code": 0,
  "message": "操作成功",
  "data": {
    "username": "admin"
  }
}
Set-Cookie: SIMPLE_SESSION_ID=550e8400-e29b-41d4-a716-446655440000; Path=/; HttpOnly
```

### 6.3 登出接口

```
POST /api/auth/logout
Cookie: SIMPLE_SESSION_ID=xxx

响应 200:
{
  "code": 0,
  "message": "操作成功",
  "data": null
}
Set-Cookie: SIMPLE_SESSION_ID=; Max-Age=0; Path=/; HttpOnly
```

### 6.4 用户 CRUD

```
# 查询列表
GET /api/user/list
Cookie: SIMPLE_SESSION_ID=xxx
→ [{ id, username, email, createTime, updateTime }, ...]

# 新增
POST /api/user
{ "username": "wangwu", "password": "654321", "email": "wangwu@simple.com" }
→ { id, username, email, createTime, updateTime }

# 更新
PUT /api/user/{id}
{ "username": "newname", "email": "new@simple.com" }
→ { id, username, email, createTime, updateTime }

# 删除
DELETE /api/user/{id}
→ { code: 0, message: "操作成功" }
```

### 6.5 商品 CRUD

```
# 查询列表
GET /api/product/list
→ [{ id, name, description, price, stock, createTime, updateTime }, ...]

# 新增
POST /api/product
{ "name": "新商品", "description": "描述", "price": 99.99, "stock": 10 }
→ { id, name, description, price, stock, createTime, updateTime }

# 更新
PUT /api/product/{id}
{ "name": "新名称", "price": 199.99, "stock": 50 }
→ { id, name, description, price, stock, createTime, updateTime }

# 删除
DELETE /api/product/{id}
→ { code: 0, message: "操作成功" }
```

---

## 7 关键技术点

### 7.1 跨域 Cookie 认证

这是本项目最关键的配置点，前后端缺一不可：

**后端 CORS 配置：**
```java
registry.addMapping("/api/**")
    .allowedOrigins("http://localhost:5173")  // 不能是 "*"
    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
    .allowCredentials(true)                    // 允许携带 Cookie
```

**前端 Axios 配置：**
```javascript
axios.create({
  baseURL: 'http://localhost:8080',
  withCredentials: true  // 跨域携带 Cookie
})
```

**常见踩坑：**
- `allowedOrigins("*")` + `allowCredentials(true)` 会报错，必须指定具体域名
- 忘记 `withCredentials: true` 会导致 Cookie 不发送
- 开发环境 `cookie.setSecure(false)`，生产环境必须设为 `true`

### 7.2 LocalDateTime 序列化

默认情况下，Jackson 将 `LocalDateTime` 序列化为数组 `[2026, 5, 4, 12, 30, 0]`。
通过以下配置改为字符串 `"2026-05-04 12:30:00"`：

```yaml
spring.jackson:
  date-format: yyyy-MM-dd HH:mm:ss
  time-zone: Asia/Shanghai
  serialization.write-dates-as-timestamps: false
```

### 7.3 UUID 主键

所有实体使用 `String` 类型的 UUID 作为主键，而非自增 `Long`：
- 多个 `ConcurrentHashMap` 之间 ID 不会冲突
- 新增时前端无需请求服务端分配 ID
- 避免与用户表、商品表的 ID 冲突

### 7.4 前端路由守卫

```javascript
router.beforeEach((to, from, next) => {
  // 公开路由（登录页）直接放行
  // 已登录访问 /login → 跳转首页
  // 未登录访问受保护页面 → 跳转 /login + 携带 redirect 参数
})
```

### 7.5 前端 401 自动跳转

Axios 响应拦截器统一处理：
```javascript
if (res.code === 401) {
  ElMessage.error(res.message || '登录已过期')
  router.push('/login')
}
```
无论用户主动登出还是 Session 过期，任何 401 响应都会触发自动跳转，无需每个组件单独处理。

---

## 8 启动说明

### 8.1 后端

```bash
cd simple-api
mvn spring-boot:run
# 服务运行在 http://localhost:8080
```

### 8.2 前端

```bash
cd simple-ui
npm install
npm run dev
# 前端运行在 http://localhost:5173
```

### 8.3 验证流程

1. 先启动后端，再启动前端
2. 浏览器访问 http://localhost:5173，自动跳转登录页
3. 输入演示账号 `admin` / `admin123` 登录
4. 登录后进入用户管理页，可查看预置的 3 条用户数据
5. 点击「新增用户」填写表单提交，列表自动刷新
6. 切换商品管理页，同理验证商品 CRUD
7. 点击右上角「退出登录」，自动跳转回登录页

### 8.4 后端独立验证（curl）

```bash
# 登录（-c 保存 Cookie）
curl -c cookies.txt -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# 查询用户列表（-b 携带 Cookie）
curl -b cookies.txt http://localhost:8080/api/user/list

# 未携带 Cookie 访问受保护接口（应返回 401）
curl http://localhost:8080/api/user/list

# 登出
curl -b cookies.txt -X POST http://localhost:8080/api/auth/logout
```


### 备注

原始诉求：
写个前后端分离项目，严格遵循下面要求：
1、前端写在 simple-ui 子目录下；后端写在 simple-api 子目录下。
2、前端技术栈： element-plus + vue3。
3、后端技术栈： java + springboot；数据层不用连接数据库，使用 hashmap 模拟替代即可。
4、项目结构：前端 + 后端项目，均要求清晰、规范，方便后续扩展多业务模块；
5、项目注释：前端 + 后端项目，均要求注释详细、丰富；
4、实现功能：
- 登录：前端登录页，以及后端登录API。采用 cookie + session 方式，后端session存储在内存里。
- 用户管理：前端支持数据 CRUD，后端API实现对应功能；API能够联调通顺。
- 商品管理：前端支持数据 CRUD，后端API实现对应功能；API能够联调通顺。