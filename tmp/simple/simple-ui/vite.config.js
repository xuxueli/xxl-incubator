import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { fileURLToPath, URL } from 'node:url'

/**
 * Vite 构建配置
 */
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))  // @ 指向 src 目录
    }
  },
  server: {
    port: 5173,
    open: true
  }
})
