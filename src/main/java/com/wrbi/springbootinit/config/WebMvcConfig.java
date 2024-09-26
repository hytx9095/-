package com.wrbi.springbootinit.config;

import com.wrbi.springbootinit.interceptor.AuthInterceptorHandler;
import com.wrbi.springbootinit.common.JwtProperties;
import com.wrbi.springbootinit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private UserService userService;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptorHandler())
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/emailLogin")
                .excludePathPatterns("/user/sendEmail")
                .excludePathPatterns("/**/register")
                .excludePathPatterns("/swagger-ui.html","/swagger-resources/**"
                        ,"/webjars/**","/swagger-ui/**","/v2/api-docs/**","/doc.html");
//        registry.addInterceptor(authInterceptorHandler())
//                .excludePathPatterns("/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 覆盖所有请求
        registry.addMapping("/**")
                // 允许发送 Cookie
                .allowCredentials(true)
                // 放行哪些域名（必须用 patterns，否则 * 会和 allowCredentials 冲突）
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("*");
    }
    @Bean
    public AuthInterceptorHandler authInterceptorHandler(){
        return new AuthInterceptorHandler(jwtProperties, userService);
    }
}
