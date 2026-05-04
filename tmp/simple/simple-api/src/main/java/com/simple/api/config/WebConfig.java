package com.simple.api.config;

import com.simple.api.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置类
 * 注册拦截器和 CORS 跨域配置
 *
 * 关键 CORS 设置：
 * - allowedOrigins 必须指定具体域名，不能使用 "*"（否则 withCredentials 不生效）
 * - allowCredentials(true) 允许携带 Cookie 的跨域请求
 * - 前端 Vite 开发服务器默认运行在 http://localhost:5173
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    public WebConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    /**
     * 注册拦截器
     * 对所有 /api/** 路径启用认证拦截，Spring Boot 内部端点除外
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**")           // 拦截所有 API 请求
                .excludePathPatterns(
                        "/error",                      // 排除错误页面
                        "/actuator/**",                // 排除监控端点
                        "/favicon.ico",                // 排除浏览器图标请求
                        "/webjars/**",                 // 排除 webjars 静态资源
                        "/*.html"                      // 排除根目录 HTML 文件
                );
    }

    /**
     * CORS 跨域配置
     * 前端运行在 localhost:5173，后端运行在 localhost:8080，属于跨域场景
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                // 允许前端开发服务器地址（不能使用 "*" 通配符）
                .allowedOrigins("http://localhost:5173")
                // 允许所有 HTTP 方法
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // 允许所有请求头
                .allowedHeaders("*")
                // 允许携带 Cookie（跨域认证的关键配置）
                .allowCredentials(true)
                // 预检请求缓存时间（秒），减少 OPTIONS 请求频率
                .maxAge(3600);
    }
}
