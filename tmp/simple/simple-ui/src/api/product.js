import request from '@/utils/request'

/**
 * 商品管理 API
 * 封装商品增删改查的请求，统一使用 request 实例
 */
export default {
  /**
   * 分页查询商品列表
   * @param {Object} params - { name, pageNum, pageSize }
   * @returns {Promise<Object>} { total, records }
   */
  list(params = {}) {
    return request.get('/api/product/list', { params })
  },

  /**
   * 根据 ID 查询商品详情
   * @param {String} id - 商品ID
   * @returns {Promise<Object>} 商品对象
   */
  getById(id) {
    return request.get(`/api/product/${id}`)
  },

  /**
   * 创建新商品
   * @param {Object} data - 商品信息 { name, description, price, stock }
   * @returns {Promise<Object>} 创建后的商品对象
   */
  create(data) {
    return request.post('/api/product', data)
  },

  /**
   * 更新商品信息
   * @param {String} id - 商品ID
   * @param {Object} data - 商品信息
   * @returns {Promise<Object>} 更新后的商品对象
   */
  update(id, data) {
    return request.put(`/api/product/${id}`, data)
  },

  /**
   * 删除商品
   * @param {String} id - 商品ID
   * @returns {Promise}
   */
  delete(id) {
    return request.delete(`/api/product/${id}`)
  }
}
