/**
 * 名称：用户会话 Store
 * 描述：负责管理登录态、当前用户资料、角色权限以及退出登录等用户会话相关能力。
 *
 * 职责划分：
 * 1. state：保存 token、用户基础资料、角色与权限；
 * 2. actions：封装登录、拉取用户信息、退出登录流程；
 * 3. getters：当前模块未定义 getters，页面直接读取 state 中的响应式数据。
 */
import router from '@/router'
import cache from '@/plugins/cache'
import { ElMessageBox, } from 'element-plus'
import { login, logout, getInfo } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { isHttp, isEmpty } from "@/utils/validate"
import defAva from '@/assets/images/profile.jpg'

const useUserStore = defineStore(
  'user',
  {
    /**
     * 状态定义
     *
     * - token：当前登录令牌，初始化时直接从本地缓存恢复；
     * - id/name/nickName/avatar：当前登录用户的展示资料；
     * - roles/permissions：权限控制依赖的角色与权限标识集合。
     */
    state: () => ({
      token: getToken(),
      id: '',
      name: '',
      nickName: '',
      avatar: '',
      roles: [],
      permissions: []
    }),
    /**
     * 动作方法定义
     *
     * 用户模块的所有核心行为都集中在 actions 中，方便页面以统一入口驱动登录态变化。
     */
    actions: {
      /**
       * 登录
       *
       * 只负责提交登录请求和落地 token；用户详情、角色权限等信息由 getInfo 单独加载。
       */
      login(userInfo) {
        const username = userInfo.username.trim()
        const password = userInfo.password
        const code = userInfo.code
        const uuid = userInfo.uuid
        return new Promise((resolve, reject) => {
          login(username, password, code, uuid).then(res => {
            setToken(res.token)
            this.token = res.token
            resolve()
          }).catch(error => {
            reject(error)
          })
        })
      },
      /**
       * 获取用户信息
       *
       * 处理流程：
       * 1. 请求当前用户资料；
       * 2. 规范化头像地址，兼容默认头像、相对路径和完整 URL；
       * 3. 回填角色、权限和用户基础资料；
       * 4. 记录密码字符类型，并在必要时弹出密码安全提示。
       */
      getInfo() {
        return new Promise((resolve, reject) => {
          getInfo().then(res => {
            const user = res.user
            let avatar = user.avatar || ""
            // 头像地址兼容处理：空值走默认头像，相对路径补齐后端访问前缀。
            if (!isHttp(avatar)) {
              avatar = (isEmpty(avatar)) ? defAva : import.meta.env.VITE_APP_BASE_API + avatar
            }
            // 角色为空时兜底为默认角色，避免权限判断链路出现空数组异常。
            if (res.roles && res.roles.length > 0) { // 验证返回的roles是否是一个非空数组
              this.roles = res.roles
              this.permissions = res.permissions
            } else {
              this.roles = ['ROLE_DEFAULT']
            }
            this.id = user.userId
            this.name = user.userName
            this.nickName = user.nickName
            this.avatar = avatar
            cache.session.set('pwrChrtype', res.pwdChrtype)
            /* 初始密码提示：首次使用默认密码时，引导用户跳转修改密码页。 */
            if(res.isDefaultModifyPwd) {
              ElMessageBox.confirm('您的密码还是初始密码，请修改密码！',  '安全提示', {  confirmButtonText: '确定',  cancelButtonText: '取消',  type: 'warning' }).then(() => {
                router.push({ name: 'Profile', params: { activeTab: 'resetPwd' } })
              }).catch(() => {})
            }
            /* 过期密码提示：密码过期但不是初始密码时，同样引导尽快修改。 */
            if(!res.isDefaultModifyPwd && res.isPasswordExpired) {
              ElMessageBox.confirm('您的密码已过期，请尽快修改密码！',  '安全提示', {  confirmButtonText: '确定',  cancelButtonText: '取消',  type: 'warning' }).then(() => {
                router.push({ name: 'Profile', params: { activeTab: 'resetPwd' } })
              }).catch(() => {})
            }
            resolve(res)
          }).catch(error => {
            reject(error)
          })
        })
      },
      /**
       * 退出系统
       *
       * 成功退出后同步清空 token、角色、权限和本地令牌缓存，保证会话状态彻底重置。
       */
      logOut() {
        return new Promise((resolve, reject) => {
          logout(this.token).then(() => {
            this.token = ''
            this.roles = []
            this.permissions = []
            removeToken()
            resolve()
          }).catch(error => {
            reject(error)
          })
        })
      }
    }
  })

export default useUserStore
