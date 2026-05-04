<template>
  <!-- 登录页：居中卡片表单 -->
  <div class="login-container">
    <el-card class="login-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <h2>系统登录</h2>
        </div>
      </template>

      <!-- 登录表单 -->
      <el-form
        ref="formRef"
        :model="loginForm"
        :rules="loginRules"
        label-width="80px"
        @submit.prevent="handleLogin"
      >
        <!-- 用户名 -->
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            prefix-icon="User"
            clearable
          />
        </el-form-item>

        <!-- 密码 -->
        <el-form-item label="密码" prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="Lock"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>

        <!-- 登录按钮 -->
        <el-form-item>
          <el-button
            type="primary"
            class="login-button"
            :loading="loading"
            @click="handleLogin"
          >
            登 录
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 演示账号提示 -->
      <div class="demo-hint">
        <el-text type="info" size="small">
          演示账号：admin / admin123
        </el-text>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import authApi from '@/api/auth'
import { useAuthStore } from '@/store'

/**
 * 登录页组件
 * 处理用户登录表单提交，登录成功后更新 Pinia 状态并跳转
 */

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

// 表单引用
const formRef = ref(null)

// 加载状态
const loading = ref(false)

/**
 * 登录表单数据
 */
const loginForm = reactive({
  username: '',
  password: ''
})

/**
 * 表单校验规则
 * 用户名和密码均为必填项
 */
const loginRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

/**
 * 处理登录
 * 1. 校验表单
 * 2. 调用登录 API
 * 3. 更新 Pinia 状态
 * 4. 跳转到首页或来源页
 */
async function handleLogin() {
  // 校验表单
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    // 调用后端登录接口
    const data = await authApi.login(loginForm)
    // 更新 Pinia 登录状态
    authStore.setLoginState(data.username)
    ElMessage.success('登录成功')

    // 跳转到来源页或首页
    const redirect = route.query.redirect || '/'
    router.push(redirect)
  } catch (error) {
    // 登录失败时 ElMessage 已在响应拦截器中显示
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
/* 全屏居中容器 */
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

/* 登录卡片 */
.login-card {
  width: 420px;
}

.card-header h2 {
  text-align: center;
  margin: 0;
  color: #333;
}

/* 登录按钮全宽 */
.login-button {
  width: 100%;
}

/* 演示账号提示 */
.demo-hint {
  text-align: center;
  margin-top: -10px;
}
</style>
