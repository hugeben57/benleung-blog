package com.ben.config;

import com.ben.interceptor.LoginInterceptor1;
import com.ben.interceptor.LoginInterceptor2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor1 loginInterceptor1;

    @Autowired
    private LoginInterceptor2 loginInterceptor2;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor1)
                .addPathPatterns("/user/**", "/blog/**")
                .excludePathPatterns("/user/signIn","/user/signUp","/blog/getBlogById/*","/blog/getBlogList");

        registry.addInterceptor(loginInterceptor2)
                .addPathPatterns("/blog/deleteBlog/*","/blog/updateBlog/*","/blog/addBlog");
    }




}
