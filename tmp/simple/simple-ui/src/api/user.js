import request from '@/utils/request'

/**
 * 用户管理 API
 * 封装用户增删改查的请求，统一使用 request 实例
 */
export default {
  /**
   * 查询所有用户列表
   * @returns {Promise<Array>} 用户数组
   */
  list() {
    return request.get('/api/user/list')
  },

  /**
   * 根据 ID 查询用户详情
   * @param {String} id - 用户ID
   * @returns {Promise<Object>} 用户对象
   */
  getById(id) {
    return request.get(`/api/user/${id}`)
  },

  /**
   * 创建新用户
   * @param {Object} data - 用户信息 { username, password, email }
   * @returns {Promise<Object>} 创建后的用户对象
   */
  create(data) {
    return request.post('/api/user', data)
  },

  /**
   * 更新用户信息
   * @param {String} id - 用户ID
   * @param {Object} data - 用户信息
   * @returns {Promise<Object>} 更新后的用户对象
   */
  update(id, data) {
    return request.put(`/api/user/${id}`, data)
  },

  /**
   * 删除用户
   * @param {String} id - 用户ID
   * @returns {Promise}
   */
  delete(id) {
    return request.delete(`/api/user/${id}`)
  }
}
