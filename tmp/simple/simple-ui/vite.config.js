// 引入 Vite 的配置辅助函数，用于在编辑器/类型检查时获得更好的提示
import { defineConfig } from 'vite'
// Vue 插件：将 .vue 文件解析为 Vite 可识别的模块
import vue from '@vitejs/plugin-vue'
// Node 的 URL 工具：用于把相对路径转换为文件系统路径（兼容 ESM）
import { fileURLToPath, URL } from 'node:url'

/**
 * Vite 构建配置
 *
 * 说明：
 * - 这个文件导出一个基于 `defineConfig` 的配置对象，方便 IDE 提供类型提示。
 * - 这里只包含常用的开发时配置（插件、别名、开发服务器），可根据项目需要继续扩展
 */
export default defineConfig({
  // 插件数组：在这里注册 Vite 插件（例如 Vue、React、图像优化等）
  // vue() 插件负责处理 .vue 单文件组件的编译和热重载
  plugins: [vue()],

  // 模块解析相关配置
  resolve: {
    // alias：路径别名可以让导入更简洁，常见用法是把 '@' 指向项目的 src 目录
    // 示例： import Foo from '@/components/Foo.vue' 替代相对路径 '../../../components/Foo.vue'
    alias: {
      // 将字符串 '@' 映射到项目的 ./src 目录。使用 fileURLToPath + URL 保证在 ESM 环境下路径解析正确。
      '@': fileURLToPath(new URL('./src', import.meta.url))  // @ 指向 src 目录
    }
  },

  // 本地开发服务器配置（只影响 dev 模式）
  server: {
    // 指定开发服务器端口，默认是 5173（可以通过环境变量覆盖或修改为其它端口）
    port: 5173,
    // 启动开发服务器后自动在浏览器中打开页面
    open: true
    // 常见可选项还有：
    // host: '0.0.0.0'     // 允许局域网访问
    // proxy: { '/api': 'http://localhost:8080' } // 本地代理转发，用于开发时避开跨域
  }

  // 更多可配置项（可按需解注并配置）：
  // base: '/your-sub-path/', // 设置部署时的公共路径
  // build: { outDir: 'dist', sourcemap: true }, // 打包配置
})
