import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

/**
 * Axios 实例配置
 *
 * 关键配置说明：
 * - baseURL: 后端 API 地址，前后端分离时必需
 * - withCredentials: true 允许跨域携带 Cookie（跨域认证的核心配置）
 *   必须与后端 CORS allowCredentials(true) 配合使用
 * - timeout: 请求超时时间，防止长时间等待无响应
 */
const request = axios.create({
  baseURL: 'http://localhost:8080',  // 后端服务地址
  withCredentials: true,             // 跨域请求携带 Cookie
  timeout: 10000                     // 10 秒超时
})

/**
 * 请求拦截器
 * 每次发送请求前可添加通用逻辑（如 Loading 动画）
 * 当前保持简单，直接放行
 */
request.interceptors.request.use(
  config => config,
  error => Promise.reject(error)
)

/**
 * 响应拦截器
 * 统一处理后端返回的错误码：
 * - code === 0: 正常响应，直接返回 data
 * - code === 401: 未登录或 Session 过期，跳转登录页
 * - 其他 code: 显示错误消息
 */
request.interceptors.response.use(
  response => {
    const res = response.data
    // 后端统一返回 Result 格式 { code, message, data }
    if (res.code !== 0) {
      // 401 未授权：清除状态并跳转登录页
      if (res.code === 401) {
        ElMessage.error(res.message || '登录已过期')
        router.push('/login')
      } else {
        ElMessage.error(res.message || '请求失败')
      }
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    // 成功时只返回 data 字段，简化调用方代码
    return res.data
  },
  error => {
    // 网络错误或请求失败
    ElMessage.error(error.message || '网络异常')
    return Promise.reject(error)
  }
)

export default request
