import request from '@/utils/request'

/**
 * 认证相关 API
 */
export default {
  /**
   * 用户登录
   * @param {Object} data - { username, password }
   * @returns {Promise} 返回 { username }
   */
  login(data) {
    return request.post('/api/auth/login', data)
  },

  /**
   * 用户登出
   * @returns {Promise}
   */
  logout() {
    return request.post('/api/auth/logout')
  },

  /**
   * 获取当前登录用户信息
   * 前端刷新页面时调用，根据 Cookie 恢复登录状态
   * 返回 200 表示 Session 有效，401 表示未登录
   * @returns {Promise} 返回 { username }
   */
  me() {
    return request.get('/api/auth/me')
  }
}
