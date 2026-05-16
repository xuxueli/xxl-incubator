<!--
  名称：Layout 主框架布局组件（src/layout/index.vue）
  功能项摘要：
    1. 统一后台整体骨架（侧边栏 / 顶部导航 / 标签页 / 主内容 / 设置面板）。
    2. 处理桌面端与移动端的布局切换与侧边栏收展联动。
    3. 按主题与布局状态输出动态样式类与 CSS 变量。
-->
<template>
  <!--
    总体：模板层负责布局骨架渲染与页面结构编排。
    分项：
      1. 根容器 app-wrapper 统一挂载动态类名与主题色变量。
      2. 移动端侧栏展开时显示遮罩层，并绑定点击关闭事件。
      3. 主内容区按配置渲染导航栏、标签页、主体内容与设置面板。
  -->
  <div :class="classObj" class="app-wrapper" :style="{ '--current-color': theme, '--current-color-light': theme + '1a', '--current-color-dark-bg': theme + '33' }">
    <div v-if="device === 'mobile' && sidebar.opened" class="drawer-bg" @click="handleClickOutside"/>
    <sidebar v-if="!sidebar.hide" class="sidebar-container" />
    <div :class="{ hasTagsView: needTagsView, sidebarHide: sidebar.hide }" class="main-container">
      <div :class="{ 'fixed-header': fixedHeader }">
        <navbar @setLayout="setLayout" />
        <tags-view v-if="needTagsView" />
      </div>
      <app-main />
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

// 总体：根据设备状态与侧边栏状态，计算布局容器类名。
// 分项：控制侧栏显示状态、动画开关和移动端样式标记。
const classObj = computed(() => ({
  hideSidebar: !sidebar.value.opened,
  openSidebar: sidebar.value.opened,
  withoutAnimation: sidebar.value.withoutAnimation,
  mobile: device.value === 'mobile'
}))

const { width, height } = useWindowSize()
const WIDTH = 992 // refer to Bootstrap's responsive design

// 总体：设备状态变化时确保移动端不会保留展开侧边栏。
// 分项：当设备切换为 mobile 且侧边栏打开时，主动关闭侧边栏。
watch(() => device.value, () => {
  if (device.value === 'mobile' && sidebar.value.opened) {
    useAppStore().closeSideBar({ withoutAnimation: false })
  }
})

// 总体：监听窗口尺寸，维护响应式设备状态与侧边栏行为。
// 分项：
//   1. 小于阈值时切换为 mobile，并无动画收起侧边栏。
//   2. 大于等于阈值时切换为 desktop。
watchEffect(() => {
  if (width.value - 1 < WIDTH) {
    useAppStore().toggleDevice('mobile')
    useAppStore().closeSideBar({ withoutAnimation: true })
  } else {
    useAppStore().toggleDevice('desktop')
  }
})

function handleClickOutside() {
  useAppStore().closeSideBar({ withoutAnimation: false })
}

const settingRef = ref(null)
// 总体：提供导航栏触发入口，打开布局设置抽屉。
// 分项：通过 settingRef 调用 Settings 组件实例方法。
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
