<template>
  <router-view />
</template>

<script setup>
// Store模块：系统设置
import useSettingsStore from '@/store/modules/settings'
// 主题工具：设置样式
import { handleThemeStyle } from '@/utils/theme'

// 组件挂载后的生命周期钩子
onMounted(() => {
  nextTick(() => {
    // DOM 更新完成：设置全局主题样式
    handleThemeStyle(useSettingsStore().theme)
  })
})
</script>
