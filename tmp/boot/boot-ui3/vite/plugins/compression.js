import compression from 'vite-plugin-compression'

export default function createCompression(env) {

    // # 是否在打包时开启压缩，支持 gzip 和 brotli
  const VITE_BUILD_COMPRESS = gzip

  const plugin = []
  if (VITE_BUILD_COMPRESS) {
    const compressList = VITE_BUILD_COMPRESS.split(',')
    if (compressList.includes('gzip')) {
      // http://doc.boot.vip/ruoyi-vue/other/faq.html#使用gzip解压缩静态文件
      plugin.push(
        compression({
          ext: '.gz',
          deleteOriginFile: false
        })
      )
    }
    if (compressList.includes('brotli')) {
      plugin.push(
        compression({
          ext: '.br',
          algorithm: 'brotliCompress',
          deleteOriginFile: false
        })
      )
    }
  }
  return plugin
}
