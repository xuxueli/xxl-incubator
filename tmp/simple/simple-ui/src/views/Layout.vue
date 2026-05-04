<template>
  <!-- 主布局：侧边栏 + 顶栏 + 内容区 -->
  <el-container class="layout-container">
    <!-- 侧边栏：导航菜单 -->
    <el-aside width="220px">
      <div class="logo">Simple Admin</div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409eff"
      >
        <el-menu-item index="/user">
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="/product">
          <el-icon><Goods /></el-icon>
          <span>商品管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 右侧区域 -->
    <el-container>
      <!-- 顶栏：显示当前用户名和登出按钮 -->
      <el-header class="header">
        <div class="header-left">
          <span class="page-title">{{ currentTitle }}</span>
        </div>
        <div class="header-right">
          <span class="username">欢迎，{{ authStore.currentUser }}</span>
          <el-button type="danger" size="small" @click="handleLogout">
            退出登录
          </el-button>
        </div>
      </el-header>

      <!-- 主内容区：子路由渲染区域 -->
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import authApi from '@/api/auth'
import { useAuthStore } from '@/store'

/**
 * 布局页组件
 * 侧边栏导航 + 顶栏用户信息 + 内容区
 * 所有受保护页面均使用此布局
 */

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

/**
 * 当前激活的菜单项（与路由路径对应）
 */
const activeMenu = computed(() => route.path)

/**
 * 当前页面标题（来自路由 meta.title）
 */
const currentTitle = computed(() => route.meta.title || '')

/**
 * 处理登出
 * 1. 确认弹窗
 * 2. 调用后端登出接口
 * 3. 清除前端状态
 * 4. 跳转登录页
 */
async function handleLogout() {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await authApi.logout()
    authStore.logout()
    ElMessage.success('已退出登录')
    router.push('/login')
  } catch {
    // 用户取消操作，不做处理
  }
}
</script>

<style scoped>
/* 全屏布局 */
.layout-container {
  height: 100vh;
}

/* 侧边栏 */
.el-aside {
  background-color: #304156;
  color: #fff;
}

/* Logo 区域 */
.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  font-size: 18px;
  font-weight: bold;
  color: #fff;
  background-color: #263445;
  border-bottom: 1px solid #3a4a5e;
}

/* 顶栏 */
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
}

.page-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.username {
  color: #666;
  font-size: 14px;
}

/* 主内容区 */
.main-content {
  background-color: #f0f2f5;
}
</style>
