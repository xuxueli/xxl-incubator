import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/store'
import authApi from '@/api/auth'

/**
 * 路由配置
 *
 * 路由结构：
 * /login         -> 登录页（公开）
 * /              -> 布局页（重定向到 /user，需登录）
 *   /user        -> 用户管理（需登录）
 *   /product     -> 商品管理（需登录）
 *
 * 路由守卫逻辑：
 * - 公开路由直接放行
 * - 已登录访问 /login → 跳转首页
 * - 未登录访问受保护页面 → 调用 /api/auth/me 检查 Cookie 是否有效
 *   - Cookie 有效：恢复 Pinia 状态，放行
 *   - Cookie 无效：跳转 /login
 */
const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { public: true }  // 标记为公开路由，不需要登录
  },
  {
    path: '/',
    component: () => import('@/views/Layout.vue'),
    redirect: '/user',      // 默认跳转到用户管理页
    children: [
      {
        path: 'user',
        name: 'User',
        component: () => import('@/views/user/Index.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'product',
        name: 'Product',
        component: () => import('@/views/product/Index.vue'),
        meta: { title: '商品管理' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),  // HTML5 History 模式，URL 无 # 号
  routes
})

/**
 * 全局路由守卫
 * 在每次路由跳转前执行，检查登录状态
 * 页面刷新时 Pinia 状态丢失，通过 /api/auth/me 恢复
 */
router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore()

  // 公开路由直接放行
  if (to.meta.public) {
    // 已登录用户访问登录页，跳转到首页
    if (to.path === '/login' && authStore.isLoggedIn) {
      next('/')
    } else {
      next()
    }
    return
  }

  // 受保护路由：前端状态未登录时，尝试通过 Cookie 恢复
  if (!authStore.isLoggedIn) {
    try {
      // 调用后端 /api/auth/me，根据 Cookie 校验 Session 是否有效
      const data = await authApi.me()
      // Session 有效，恢复 Pinia 状态
      authStore.setLoginState(data.username)
      next()
    } catch {
      // Cookie 无效或已过期，跳转登录页
      next({ path: '/login', query: { redirect: to.fullPath } })
    }
    return
  }

  // 已登录，放行
  next()
})

export default router
