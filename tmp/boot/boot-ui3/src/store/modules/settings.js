import defaultSettings from '@/settings'
import {useDark, useToggle} from '@vueuse/core'
import {handleThemeStyle} from '@/utils/theme'

// 持久化存储Key：localStorage key constant
const LAYOUT_SETTING_KEY = 'layout-setting'

// 初始化暗黑模式：跟随系统
const isDark = useDark()
// 切换暗黑模式：联动更新
const toggleDark = useToggle(isDark)

// 持久化存储数据：从 localStorage 读取已有配置（如果有）
const storageSetting = JSON.parse(localStorage.getItem(LAYOUT_SETTING_KEY)) || {}

/**
 * 系统设置状态管理 Store
 *
 * 功能说明：
 * - 管理系统的全局配置项，包括主题、布局、标签页等设置
 * - 支持从本地存储恢复用户偏好设置
 * - 提供暗黑模式切换和动态标题更新功能
 */
const useSettingsStore = defineStore(
    /**
     * 存储名称：用于在 localStorage 中存储和检索设置数据
     */
    'settings',
    {
        /**
         * 状态定义
         *
         * 包含所有可配置的系统设置项，优先从 localStorage 读取用户自定义配置，
         * 如果不存在则使用默认配置
         */
        state: () => ({
            title: '',
            theme: storageSetting.theme || '#409EFF',
            sideTheme: storageSetting.sideTheme || defaultSettings.sideTheme,
            showSettings: defaultSettings.showSettings,
            navType: storageSetting.navType === undefined ? defaultSettings.navType : storageSetting.navType,
            tagsView: storageSetting.tagsView === undefined ? defaultSettings.tagsView : storageSetting.tagsView,
            tagsViewPersist: storageSetting.tagsViewPersist === undefined ? defaultSettings.tagsViewPersist : storageSetting.tagsViewPersist,
            tagsIcon: storageSetting.tagsIcon === undefined ? defaultSettings.tagsIcon : storageSetting.tagsIcon,
            tagsViewStyle: storageSetting.tagsViewStyle === undefined ? defaultSettings.tagsViewStyle : storageSetting.tagsViewStyle,
            fixedHeader: storageSetting.fixedHeader === undefined ? defaultSettings.fixedHeader : storageSetting.fixedHeader,
            sidebarLogo: storageSetting.sidebarLogo === undefined ? defaultSettings.sidebarLogo : storageSetting.sidebarLogo,
            dynamicTitle: storageSetting.dynamicTitle === undefined ? defaultSettings.dynamicTitle : storageSetting.dynamicTitle,
            footerVisible: storageSetting.footerVisible === undefined ? defaultSettings.footerVisible : storageSetting.footerVisible,
            footerContent: defaultSettings.footerContent,
            isDark: isDark.value
        }),
        /**
         * 动作方法定义
         *
         * 提供修改系统设置的接口，包括布局配置、标题设置和主题切换
         */
        actions: {
            /**
             * 修改布局设置
             *
             * 根据传入的键值对更新对应的配置项，仅更新 state 中已存在的属性
             *
             * @param {Object} data - 包含要修改的配置项数据
             * @param {string} data.key - 配置项的键名
             * @param {*} data.value - 配置项的新值
             */
            changeSetting(data) {
                const {key, value} = data
                if (this.hasOwnProperty(key)) {
                    this[key] = value
                }
            },
            /**
             * 将当前设置持久化到 localStorage
             */
            saveSetting() {
                const layoutSetting = {
                    navType: this.navType,
                    tagsView: this.tagsView,
                    tagsIcon: this.tagsIcon,
                    tagsViewStyle: this.tagsViewStyle,
                    tagsViewPersist: this.tagsViewPersist,
                    fixedHeader: this.fixedHeader,
                    sidebarLogo: this.sidebarLogo,
                    dynamicTitle: this.dynamicTitle,
                    footerVisible: this.footerVisible,
                    sideTheme: this.sideTheme,
                    theme: this.theme
                }
                localStorage.setItem(LAYOUT_SETTING_KEY, JSON.stringify(layoutSetting))
            },
            /**
             * 重置为默认设置并清除 localStorage 中的缓存
             */
            resetSetting() {
                localStorage.removeItem(LAYOUT_SETTING_KEY)
                // 恢复到默认配置
                this.theme = defaultSettings.theme || '#409EFF'
                this.sideTheme = defaultSettings.sideTheme
                this.showSettings = defaultSettings.showSettings
                this.navType = defaultSettings.navType
                this.tagsView = defaultSettings.tagsView
                this.tagsViewPersist = defaultSettings.tagsViewPersist
                this.tagsIcon = defaultSettings.tagsIcon
                this.tagsViewStyle = defaultSettings.tagsViewStyle
                this.fixedHeader = defaultSettings.fixedHeader
                this.sidebarLogo = defaultSettings.sidebarLogo
                this.dynamicTitle = defaultSettings.dynamicTitle
                this.footerVisible = defaultSettings.footerVisible
                this.footerContent = defaultSettings.footerContent
            },
            /**
             * 设置网页标题
             *
             * 更新 store 中的标题并同步到浏览器标签页标题
             *
             * @param {string} title - 要设置的网页标题
             */
            setTitle(title) {
                this.title = title

                // 动态标题
                if (this.dynamicTitle) {
                    document.title = this.title + ' - ' + defaultSettings.title
                } else {
                    document.title = defaultSettings.title
                }
            },
            /**
             * 切换暗黑模式
             *
             * 切换暗黑/明亮模式状态，并重新应用主题样式以确保视觉效果正确更新
             */
            toggleTheme() {
                this.isDark = !this.isDark
                toggleDark()
                nextTick(() => {
                    handleThemeStyle(this.theme)
                })
            }
        }
    })

export default useSettingsStore
