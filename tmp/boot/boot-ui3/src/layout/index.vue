<!--
  名称：Layout 主框架布局组件（src/layout/index.vue）
  功能项摘要：
    1. 统一后台整体骨架（侧边栏 / 顶部导航 / 标签页 / 主内容 / 设置面板）。
    2. 处理桌面端与移动端的布局切换与侧边栏收展联动。
    3. 按主题与布局状态输出动态样式类与 CSS 变量。
-->
<template>
  <!-- 分项1：根容器绑定布局状态类与主题变量。 -->
  <div :class="classObj" class="app-wrapper" :style="{ '--current-color': theme, '--current-color-light': theme + '1a', '--current-color-dark-bg': theme + '33' }">
    <!-- 分项2：仅在移动端且侧栏展开时显示遮罩，点击触发关闭。 -->
    <div v-if="device === 'mobile' && sidebar.opened" class="drawer-bg" @click="handleClickOutside"/>
    <!-- 分项3：侧边栏未隐藏时渲染导航菜单。 -->
    <sidebar v-if="!sidebar.hide" class="sidebar-container" />
    <!-- 分项4：主区域根据标签页与侧栏隐藏状态切换容器样式。 -->
    <div :class="{ hasTagsView: needTagsView, sidebarHide: sidebar.hide }" class="main-container">
      <!-- 分项5：头部区域按 fixedHeader 控制固定定位。 -->
      <div :class="{ 'fixed-header': fixedHeader }">
        <!-- 分项5.1：导航栏提供 setLayout 事件入口。 -->
        <navbar @setLayout="setLayout" />
        <!-- 分项5.2：按配置决定是否显示标签页。 -->
        <tags-view v-if="needTagsView" />
      </div>
      <!-- 分项6：路由内容主体渲染区。 -->
      <app-main />
      <!-- 分项7：设置面板实例，供 setLayout 调用打开。 -->
      <settings ref="settingRef" />
    </div>
  </div>
</template>

<script setup>
import { useWindowSize } from '@vueuse/core'
import Sidebar from './components/Sidebar/index.vue'
import { AppMain, Navbar, Settings, TagsView } from './components'
import useAppStore from '@/store/modules/app'
import useSettingsStore from '@/store/modules/settings'

const settingsStore = useSettingsStore()
const theme = computed(() => settingsStore.theme)
const sidebar = computed(() => useAppStore().sidebar)
const device = computed(() => useAppStore().device)
const needTagsView = computed(() => settingsStore.tagsView)
const fixedHeader = computed(() => settingsStore.fixedHeader)

// 分项8：根据状态计算布局类名（侧栏显隐、动画开关、移动端标记）。
const classObj = computed(() => ({
  hideSidebar: !sidebar.value.opened,
  openSidebar: sidebar.value.opened,
  withoutAnimation: sidebar.value.withoutAnimation,
  mobile: device.value === 'mobile'
}))

const { width, height } = useWindowSize()
const WIDTH = 992 // refer to Bootstrap's responsive design

// 分项9：设备变更为 mobile 且侧栏处于展开时，主动收起侧栏。
watch(() => device.value, () => {
  if (device.value === 'mobile' && sidebar.value.opened) {
    useAppStore().closeSideBar({ withoutAnimation: false })
  }
})

// 分项10：窗口宽度小于阈值时切换 mobile 并无动画收起侧栏；否则切换 desktop。
watchEffect(() => {
  if (width.value - 1 < WIDTH) {
    useAppStore().toggleDevice('mobile')
    useAppStore().closeSideBar({ withoutAnimation: true })
  } else {
    useAppStore().toggleDevice('desktop')
  }
})

function handleClickOutside() {
  // 分项11：遮罩点击行为，关闭移动端侧栏。
  useAppStore().closeSideBar({ withoutAnimation: false })
}

const settingRef = ref(null)
// 分项12：导航栏触发设置入口，通过组件实例打开 Settings 面板。
function setLayout() {
  settingRef.value.openSetting()
}
</script>

<style lang="scss" scoped>
@use "@/assets/styles/mixin.scss" as mix;
@use "@/assets/styles/variables.module.scss" as vars;

.app-wrapper {
  @include mix.clearfix;
  position: relative;
  height: 100%;
  width: 100%;

  &.mobile.openSidebar {
    position: fixed;
    top: 0;
  }
}

.main-container:has(.fixed-header) {
  height: 100vh;
  overflow: hidden;
}

.drawer-bg {
  background: #000;
  opacity: 0.3;
  width: 100%;
  top: 0;
  height: 100%;
  position: absolute;
  z-index: 999;
}

.fixed-header {
  position: fixed;
  top: 0;
  right: 0;
  z-index: 9;
  width: calc(100% - #{vars.$base-sidebar-width});
  transition: width 0.28s;
}

.hideSidebar .fixed-header {
  width: calc(100% - 54px);
}

.sidebarHide .fixed-header {
  width: 100%;
}

.mobile .fixed-header {
  width: 100%;
}
</style>
