

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

// 确认删除：
    岗位管理、登录日志
    在线用户、服务监控、缓存监控、缓存列表
    系统接口

