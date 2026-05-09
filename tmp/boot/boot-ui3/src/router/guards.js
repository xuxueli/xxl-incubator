/**
 * Description: 路由守卫，判断用户权限
 *
 *  1、获取用户权限：通过 getToken() 获取用户的 token，判断用户是否已登录；如果已登录，则通过 useUserStore().getInfo() 获取用户信息，包括角色和权限；如果未登录，则根据白名单判断是否允许访问目标路由。
 *  2、动态添加路由：如果用户已登录但没有角色信息，则调用 usePermissionStore().generateRoutes() 生成可访问的动态路由，并通过 router.addRoute() 添加到路由系统中。
 *  3、路由重定向：在用户登录后，如果访问了 /login 路由，则重定向到主页；如果访问了锁屏页 /lock，但未锁屏，则重定向到主页；如果访问了其他需要权限的路由，但没有权限，则重定向到登录页。
 *  4、进度条显示：使用 NProgress 显示页面加载进度，在路由跳转开始时调用 NProgress.start()，在路由跳转结束时调用 NProgress.done()。
 *  5、错误处理：如果用户登录失败，则调用 useUserStore().logOut() 清空用户信息，并返回登录页；如果用户权限不足，则返回登录页。
 *  6、异常处理：如果用户权限不足，则返回登录页。
 *
 *  注意：
 *    isPathMatch() 函数用于判断路径是否匹配指定模式，如果匹配，则返回 true，否则返回 false。
 *    isHttp() 函数用于判断路径是否为 HTTP 协议，如果路径为 HTTP 协议，则返回 true，否则返回 false。
 *
 */
import router from './index'
import { ElMessage } from 'element-plus'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken } from '@/utils/auth'
import { isHttp, isPathMatch } from '@/utils/validate'
import { isRelogin } from '@/utils/request'
import useUserStore from '@/store/modules/user'
import useLockStore from '@/store/modules/lock'
import useSettingsStore from '@/store/modules/settings'
import usePermissionStore from '@/store/modules/permission'

NProgress.configure({ showSpinner: false })

const whiteList = ['/login', '/register']

const isWhiteList = (path) => {
  return whiteList.some(pattern => isPathMatch(pattern, path))
}

router.beforeEach(async (to, from) => {
  NProgress.start()
  if (getToken()) {
    to.meta.title && useSettingsStore().setMenuTitle(to.meta.title)
    const isLock = useLockStore().isLock
    if (to.path === '/login') {
      NProgress.done()
      return { path: '/' }
    }
    if (isWhiteList(to.path)) {
      return true
    }
    if (isLock && to.path !== '/lock') {
      NProgress.done()
      return { path: '/lock' }
    }
    if (!isLock && to.path === '/lock') {
      NProgress.done()
      return { path: '/' }
    }
    if (useUserStore().roles.length === 0) {
      isRelogin.show = true
      try {
        // 拉取user_info信息
        await useUserStore().getInfo()
        isRelogin.show = false
        // 根据roles权限生成可访问的路由
        const accessRoutes = await usePermissionStore().generateRoutes()
        accessRoutes.forEach(route => {
          if (!isHttp(route.path)) {
            router.addRoute(route)
          }
        })
        // 重新导航到目标路由，确保动态路由已注册
        return { ...to, replace: true }
      } catch (err) {
        await useUserStore().logOut()
        ElMessage.error(err)
        return { path: '/' }
      }
    }
    return true
  } else {
    // 没有token
    if (isWhiteList(to.path)) {
      // 在免登录白名单，直接进入
      return true
    }
    NProgress.done()
    return `/login?redirect=${to.fullPath}` // 否则全部重定向到登录页
  }
})

router.afterEach(() => {
  NProgress.done()
})

