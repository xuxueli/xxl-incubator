import { defineStore } from 'pinia'
import { ref } from 'vue'

/**
 * 认证状态管理
 * 维护前端 UI 层的登录状态，不持久化到 localStorage
 * Session 的有效性完全由后端 Cookie 决定，前端状态仅用于 UI 展示和路由守卫
 */
export const useAuthStore = defineStore('auth', () => {
  /** 是否已登录 */
  const isLoggedIn = ref(false)

  /** 当前登录的用户名 */
  const currentUser = ref('')

  /**
   * 设置登录状态
   * 登录成功后调用，更新 isLoggedIn 和 currentUser
   * @param {String} username - 用户名
   */
  function setLoginState(username) {
    isLoggedIn.value = true
    currentUser.value = username
  }

  /**
   * 退出登录
   * 清除前端状态，路由跳转由调用方处理
   */
  function logout() {
    isLoggedIn.value = false
    currentUser.value = ''
  }

  return {
    isLoggedIn,
    currentUser,
    setLoginState,
    logout
  }
})
