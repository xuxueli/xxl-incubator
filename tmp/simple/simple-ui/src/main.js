import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router'

/**
 * Vue 应用入口
 * 依次注册 Pinia、Router、ElementPlus 及图标库
 */
const app = createApp(App)

// 注册 Pinia 状态管理
app.use(createPinia())

// 注册 Vue Router
app.use(router)

// 注册 ElementPlus UI 组件库
app.use(ElementPlus)

// 全局注册所有 ElementPlus 图标，方便在模板中直接使用
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.mount('#app')
