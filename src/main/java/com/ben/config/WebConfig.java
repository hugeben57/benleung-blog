package com.ben.config;

import com.ben.interceptor.LoginInterceptor1;
import com.ben.interceptor.LoginInterceptor2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor1 loginInterceptor1;

    @Autowired
    private LoginInterceptor2 loginInterceptor2;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 根路径默认打开 dashboard 首页
        registry.addViewController("/").setViewName("forward:/blogFront.html");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor1)
                .addPathPatterns("/user/**", "/blog/**", "/Picture/**")
                .excludePathPatterns("/user/signIn","/user/signUp","/blog/getBlogById/*","/blog/getBlogList","/blog/getBlogTypes","/blog/getBlogPages","/blog/getBlogByTypes"
                ,"/Picture/getPictures", "/Picture/getPictureById/*");

        registry.addInterceptor(loginInterceptor2)
                .addPathPatterns("/blog/deleteBlog/*","/blog/updateBlog/*","/blog/addBlog","/blog/getAdminBlogList","/blog/published/*",
                        "/Picture/uploadPicture", "/Picture/deletePictureById/*");
    }




}
