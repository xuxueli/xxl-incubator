/**
 * 名称：应用状态Store
 * 描述：用于管理全局状态，包括 侧边栏状态、字体大小 ... 等。
 */
import Cookies from 'js-cookie'

const useAppStore = defineStore(
    'app',
    {
        state: () => ({
            // 侧边栏状态
            sidebar: {
                opened: Cookies.get('sidebarStatus') ? !!+Cookies.get('sidebarStatus') : true,    // 0: 关闭，1: 开启
                withoutAnimation: false,                                                          // 侧边栏切换动画
                hide: false                                                                       // 侧边栏隐藏
            },
            // 设备状态
            device: 'desktop',
            // 字体大小
            size: Cookies.get('size') || 'default'
        }),
        actions: {
            /**
             * 切换侧边栏状态
             * @param withoutAnimation   切换动画
             */
            toggleSideBar(withoutAnimation) {
                if (this.sidebar.hide) {
                    return false
                }
                // 切换侧边栏状态
                this.sidebar.opened = !this.sidebar.opened
                // 设置是否无动画
                this.sidebar.withoutAnimation = withoutAnimation
                // 设置侧边栏状态
                if (this.sidebar.opened) {
                    Cookies.set('sidebarStatus', 1)
                } else {
                    Cookies.set('sidebarStatus', 0)
                }
            },
            /**
             * 关闭侧边栏
             * @param param0.withoutAnimation  设置是否无动画
             */
            closeSideBar({withoutAnimation}) {
                Cookies.set('sidebarStatus', 0)
                this.sidebar.opened = false
                this.sidebar.withoutAnimation = withoutAnimation
            },
            /**
             * 设置设备状态
             * @param device  设备状态
             */
            toggleDevice(device) {
                this.device = device
            },
            /**
             * 设置字体大小
             * @param size  字体大小
             */
            setSize(size) {
                this.size = size
                Cookies.set('size', size)
            },
            /**
             * 侧边栏隐藏
             * @param status  侧边栏隐藏状态
             */
            toggleSideBarHide(status) {
                this.sidebar.hide = status
            }
        }
    })

export default useAppStore
