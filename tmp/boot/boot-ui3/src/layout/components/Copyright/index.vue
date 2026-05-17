<template>
  <!--
    页脚容器：
    1）通过 visible 控制是否渲染整个 footer（v-if 为 false 时不创建 DOM）。
    2）content 为页脚展示文案，来自系统设置（如版权信息、公司信息等）。
  -->
  <footer v-if="visible" class="copyright">
    <!-- 页脚文本内容：由配置中心（settingsStore.footerContent）统一提供 -->
    <span>{{ content }}</span>
  </footer>
</template>

<script setup>
/**
 * 组件名称：Copyright（布局页脚版权组件）
 * 功能摘要：
 * - 在系统布局底部展示可配置的版权信息；
 * - 根据全局设置动态决定“是否显示”和“显示内容”；
 * - 作为纯展示组件，不包含业务操作、副作用或异步流程。
 *
 * 组件开发说明（详细）：
 * 1）职责边界
 *    - 仅负责“读取状态 + 渲染页脚”，不在组件内维护额外状态。
 *    - 可见性与文案统一从 settings store 获取，避免多处硬编码。
 * 2）数据来源
 *    - footerVisible：控制页脚整体显隐；
 *    - footerContent：控制页脚展示文案；
 *    - 二者都通过 computed 包装，保持与 Pinia 状态响应式联动。
 * 3）扩展建议
 *    - 若需增加图标、链接、备案号等内容，优先扩展 store 配置字段；
 *    - 组件内部尽量保持“无副作用、无请求”的轻量模式，便于复用与测试；
 *    - 新增视觉元素时，请保持 fixed 底部布局与右对齐风格一致。
 *
 * 组件使用说明（详细）：
 * 1）接入方式
 *    - 在布局页面中引入并渲染该组件（通常放在主布局容器末尾）。
 * 2）控制显示
 *    - 在 settings store 中设置 footerVisible 为 true/false 即可开关页脚。
 * 3）设置内容
 *    - 在 settings store 中设置 footerContent 字符串，组件会自动实时更新文案。
 * 4）注意事项
 *    - 本组件采用 fixed 定位，页面底部若有其他固定区域需注意层级与高度冲突；
 *    - 若需要国际化，建议将 footerContent 接入 i18n 或后端配置后再写入 store。
 */
import useSettingsStore from '@/store/modules/settings'

// 获取全局设置状态仓库（Pinia）
const settingsStore = useSettingsStore()

// 是否显示页脚：响应式读取 store 中的 footerVisible 配置
const visible = computed(() => settingsStore.footerVisible)
// 页脚展示文本：响应式读取 store 中的 footerContent 配置
const content = computed(() => settingsStore.footerContent)
</script>

<style scoped>
/* 页脚固定在视口底部，横向铺满并保持浅灰背景风格 */
.copyright {
  /* 固定定位：滚动页面时始终贴底显示 */
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  /* 高度与内边距：保证单行文案可读且与布局留白一致 */
  height: 36px;
  padding: 10px 20px;
  /* 文案右对齐，符合管理后台常见页脚布局 */
  text-align: right;
  /* 视觉样式：浅色背景 + 灰色文字 + 顶部细边框分隔 */
  background-color: #f8f8f8;
  color: #666;
  font-size: 14px;
  border-top: 1px solid #e7e7e7;
  /* 层级提升：避免被普通内容遮挡 */
  z-index: 999;
}
</style>
