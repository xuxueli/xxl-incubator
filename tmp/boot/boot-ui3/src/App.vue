<template>
  <router-view/>
</template>

<script setup>
// Store模块：系统设置
import useSettingsStore from '@/store/modules/settings'
// 主题工具：设置样式
import {handleThemeStyle} from '@/utils/theme'

/**
 * 组件挂载后的生命周期钩子
 *
 * 执行流程：
 * 1. 等待 DOM 更新完成（nextTick）
 * 2. 从 settings store 中获取当前主题配置
 * 3. 应用主题样式到全局
 */
onMounted(() => {
  nextTick(() => {
    handleThemeStyle(useSettingsStore().theme)
  })
})
</script>
