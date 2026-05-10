

项目目录：
```
    /doc
    /xxl-boot-admin             : 单体项目
    /xxl-boot-api               : 后端API服务
    /xxl-boot-ui-vue            : 前端UI项目（vue3 + element）
        /src
        .App.vue                        ：根组件：整个应用的顶层容器组件
        .main.js                        ：应用入口：创建 Vue 实例，注册全局组件、插件、指令，初始化应用
        .permission.js                  ：路由权限控制：路由拦截器，验证用户登录状态和访问权限
        .settings.js                    ：全局配置：定义系统默认配置项（主题色、布局模式、是否显示标签页等）
            /api/                               ：接口请求层：统一管理所有后端 API 接口调用，按业务模块分类（system、monitor、tool），封装 axios 请求方法
            /assets/                            ：静态资源：存放图片、样式文件、SVG 图标、Logo 等静态资源文件
            /components/                        ：公共组件：可复用的业务组件（如分页、上传、富文本编辑器等），在 main.js 中全局注册
            /directive/                         ：自定义指令：Vue 自定义指令，如权限判断指令 v-hasPermi、复制文本指令 v-copyText
            /layout/                            ：布局组件：系统整体布局框架，包含侧边栏、顶栏、主内容区、标签页等布局结构
            /plugins/                           ：插件封装：对常用功能进行封装并挂载到 Vue 实例，如消息提示 $modal、标签页操作 $tab、权限判断等
            /router/                            ：路由配置：Vue Router 路由配置文件，管理页面跳转、动态路由加载、路由守卫等
            /store/                             ：状态管理：使用 Pinia 进行全局状态管理，包含用户信息、权限路由、系统设置、标签页等模块
            /utils/                             ：工具函数库：通用工具方法集合，如时间格式化、请求封装、权限判断、字典处理、表单验证等
            /views/                             ：页面视图：路由对应的页面组件，按业务模块组织（system、monitor、tool 等），每个页面对应一个路由
```

FontEnd（单体）：
```
    首页：核心模块 + 系统介绍 + 报表；
    组织管理：org
        用户管理：略
        角色管理：略
        资源管理：略
        部门管理：维护部门组织信息，树形结构；与用户关联；// TODO，改为 dept；
    系统管理：system
        字典管理：// TODO-dict，维护字段信息（字典key表/id-type&name + 数据列表/code-value&lable）
        参数管理：// TODO-config，维护系统参数信息（参数表/key-value）（默认皮肤样式key、验证码开关）
        通知公告：略
        审计日志：略
    系统工具：tool
        代码生成：略
        表单设计：// TODO-formbuild，Form表单在线设计生成代码；
    帮助中心：help
```

FontEnd（前后端分离）：
```
    首页：同单体
    组织管理：
        用户管理：同单体
        角色管理：同单体
        资源管理：同单体
        部门管理：同单体
    系统管理：
        字典管理：// TODO-dict，维护字段信息（字典key表/id-type&name + 数据列表/code-value&lable）
        参数管理：// TODO-config，维护系统参数信息（参数表/key-value）（默认皮肤样式key、验证码开关）
        通知公告：略
        审计日志：略
    系统工具：
        代码生成：略
        表单设计：// TODO-formbuild，Form表单在线设计生成代码；
    帮助中心：
```

前端Module：
```
    /src    
        /api                    // ing
        /assets                 // --     
        /components             // TODO 3
        /directive              // --
        /layout                 // TODO 3
        /plugins                // -- 
        /router                 // TODO 3
        /store                  // ing
        /utils                  // TODO 1
        /views                  // TODO 3
        App.vue                 :--
        main.js                 :--
        settings.js             :--
    env.development             :--
    index.html                  :--
    package.json                :--
    vite.config.js              :--
```

---

## 前端项目结构依赖图（boot-ui3）

### 一、分层依赖总览

```
┌─────────────────────────────────────────────────────────────────────┐
│                        【配置层 / Config】                           │
│                                                                     │
│   .env.development                                                  │
│   .env.staging            ──→  vite.config.js  ──→  package.json   │
│   .env.production                                                   │
└──────────────────────────────────┬──────────────────────────────────┘
                                   │ 构建 / 启动
┌──────────────────────────────────▼──────────────────────────────────┐
│                       【入口层 / Entry】                             │
│                                                                     │
│              index.html  ──→  src/main.js                           │
│                               src/App.vue                           │
│                               src/settings.js                       │
└──────────────────────────────────┬──────────────────────────────────┘
                                   │ 注册 / 挂载
┌──────────────────────────────────▼──────────────────────────────────┐
│                      【核心框架层 / Framework】                       │
│                                                                     │
│   src/router/          src/store/          src/plugins/             │
│   ├── index.js         ├── index.js        ├── index.js             │
│   └── guards.js        └── modules/        ├── modal.js             │
│                            ├── app.js      ├── tab.js               │
│                            ├── dict.js     ├── auth.js              │
│                            ├── user.js     ├── cache.js             │
│                            ├── permission  └── download.js          │
│                            ├── settings                             │
│                            └── tagsView                             │
└──────────────────────────────────┬──────────────────────────────────┘
                                   │ 依赖
┌──────────────────────────────────▼──────────────────────────────────┐
│                      【基础能力层 / Utilities】                       │
│                                                                     │
│   src/utils/                        src/directive/                  │
│   ├── request.js  (axios封装)        ├── index.js                   │
│   ├── auth.js     (token/cookie)     └── permission/                │
│   ├── boot.js     (通用工具方法)          └── index.js              │
│   ├── dict.js     (字典缓存)                                        │
│   ├── theme.js    (主题切换)                                        │
│   └── validate.js (表单校验)                                        │
└──────────────────────────────────┬──────────────────────────────────┘
                                   │ 调用
┌──────────────────────────────────▼──────────────────────────────────┐
│                        【接口层 / API】                               │
│                                                                     │
│   src/api/                                                          │
│   ├── login.js                                                      │
│   ├── menu.js                                                       │
│   └── sys/  system/  tool/  (按业务模块分类的接口方法)               │
└──────────────────────────────────┬──────────────────────────────────┘
                                   │ 数据驱动
┌──────────────────────────────────▼──────────────────────────────────┐
│                      【UI 展示层 / View】                             │
│                                                                     │
│   src/layout/          src/views/              src/components/      │
│   └── index.vue        ├── login.vue           ├── Pagination       │
│                        ├── redirect/           ├── DictTag          │
│                        ├── error/              ├── RightToolbar     │
│                        ├── sys/                ├── Editor           │
│                        ├── system/             ├── FileUpload       │
│                        └── tool/               ├── ImageUpload      │
│                                                ├── ImagePreview     │
│                                                └── SvgIcon          │
└─────────────────────────────────────────────────────────────────────┘
                                   │
┌──────────────────────────────────▼──────────────────────────────────┐
│                      【静态资源层 / Assets】                          │
│                                                                     │
│   src/assets/                                                       │
│   ├── icons/svg/       (SVG 图标)                                   │
│   └── styles/          (全局样式 SCSS)                               │
└─────────────────────────────────────────────────────────────────────┘
```

---

### 二、各层依赖关系说明

#### 1. 配置层
| 文件 | 作用 | 被谁读取 |
|------|------|----------|
| `.env.development` | 开发环境变量（端口、API地址、路由前缀） | `vite.config.js`（`loadEnv`） |
| `.env.staging` | 预发布环境变量 | `vite.config.js`（`loadEnv`） |
| `.env.production` | 生产环境变量 | `vite.config.js`（`loadEnv`） |
| `vite.config.js` | 构建工具配置（插件、代理、别名、输出） | Vite 启动时自动加载 |
| `package.json` | 依赖声明（vue/pinia/axios/element-plus 等）及 scripts | Vite 构建时解析依赖 |

#### 2. 入口层
| 文件 | 作用 | 依赖 |
|------|------|------|
| `index.html` | HTML 模板，挂载点 `#app` | 引用 `src/main.js` |
| `src/main.js` | Vue 应用入口，注册全局插件、指令、组件、方法 | router、store、plugins、directive、components、utils、api |
| `src/App.vue` | 根组件，顶层容器 | router-view |
| `src/settings.js` | 全局 UI 配置（主题、布局、标签页等） | store/modules/settings.js |

#### 3. 核心框架层
| 模块 | 文件 | 依赖 |
|------|------|------|
| 路由 | `src/router/index.js` | vue-router |
| 路由守卫 | `src/router/guards.js` | store/user、store/permission、utils/auth、nprogress |
| 状态管理 | `src/store/modules/user.js` | utils/auth（token）、api/login |
| 状态管理 | `src/store/modules/permission.js` | api/menu、utils/permission |
| 状态管理 | `src/store/modules/settings.js` | src/settings.js |
| 状态管理 | `src/store/modules/dict.js` | api/sys/dict |
| 状态管理 | `src/store/modules/tagsView.js` | — |
| 状态管理 | `src/store/modules/app.js` | js-cookie |
| 插件 | `src/plugins/modal.js` | element-plus（ElMessage/ElMessageBox） |
| 插件 | `src/plugins/tab.js` | vue-router、store/tagsView |
| 插件 | `src/plugins/auth.js` | store/user |
| 插件 | `src/plugins/cache.js` | js-cookie |
| 插件 | `src/plugins/download.js` | utils/request、file-saver |

#### 4. 基础能力层
| 文件 | 作用 | 依赖 |
|------|------|------|
| `src/utils/request.js` | axios 封装，统一请求/响应拦截，token 注入 | axios、utils/auth、plugins/modal |
| `src/utils/auth.js` | token 存取（Cookie） | js-cookie |
| `src/utils/boot.js` | 通用工具（时间格式化、树形处理、表单重置等） | — |
| `src/utils/dict.js` | 字典数据懒加载与缓存 | store/dict |
| `src/utils/theme.js` | 主题色动态切换 | — |
| `src/utils/validate.js` | 表单校验规则集合 | — |
| `src/directive/` | 自定义指令（v-hasPermi、v-hasRole） | store/user |

#### 5. 接口层
| 目录/文件 | 作用 | 依赖 |
|-----------|------|------|
| `src/api/login.js` | 登录、登出、用户信息接口 | utils/request |
| `src/api/menu.js` | 动态菜单接口 | utils/request |
| `src/api/sys/` | 系统模块接口（字典、参数、日志等） | utils/request |
| `src/api/system/` | 组织模块接口（用户、角色、部门等） | utils/request |
| `src/api/tool/` | 工具模块接口（代码生成等） | utils/request |

#### 6. UI 展示层
| 模块 | 依赖 |
|------|------|
| `src/layout/` | store/app、store/settings、store/tagsView、store/permission、router |
| `src/views/` | src/api/、store/、utils/、components/ |
| `src/components/` | element-plus、utils/ |

---

### 三、应用启动链路（Boot Flow）

```
Vite 读取 .env.*
    │
    ▼
vite.config.js 加载环境变量，配置插件（vue、autoImport、svgIcons、compression）
及开发代理（/api → 后端）
    │
    ▼
index.html 加载，挂载 <div id="app">
    │
    ▼
src/main.js 执行：
    ├── 1. createApp(App)          创建 Vue 实例
    ├── 2. app.use(router)         注册路由（+副作用导入 guards.js，激活路由守卫）
    ├── 3. app.use(store)          注册 Pinia 状态管理
    ├── 4. app.use(plugins)        注册全局插件（modal / tab / auth / cache / download）
    ├── 5. app.use(ElementPlus)    注册 UI 组件库（中文语言包 + 尺寸配置）
    ├── 6. directive(app)          注册自定义指令（v-hasPermi / v-hasRole）
    ├── 7. app.component(...)      注册全局业务组件（Pagination / DictTag / Editor 等）
    ├── 8. app.config.globalProperties  挂载全局方法（useDict / download / parseTime 等）
    └── 9. app.mount('#app')       挂载到 DOM
    │
    ▼
路由守卫（guards.js）拦截每次跳转：
    ├── 未登录 → 跳转 /login
    ├── 已登录但未加载权限路由 → 调用 store/permission 动态加载菜单路由
    └── 权限通过 → 放行，渲染对应 views/ 页面
    │
    ▼
src/App.vue 渲染 <router-view />，由 src/layout/index.vue 承载整体布局
（侧边栏 + 顶栏 + 标签页 + 主内容区）
    │
    ▼
views/ 页面组件渲染，通过 src/api/ 调用后端接口，
数据流转经 utils/request.js（axios）→ 后端 API 服务
```

