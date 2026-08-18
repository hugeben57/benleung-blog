package com.ben.interceptor;

import com.ben.utils.JwtUtils;
import com.ben.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Map;

@Component
public class LoginInterceptor1 implements HandlerInterceptor {

    public void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setCharacterEncoding("UTF-8"); // 设置字符编码为UTF-8
        response.setContentType("application/json;charset=UTF-8"); // 设置响应的Content-Type
        response.getWriter().write(message);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token=request.getHeader("Authorization");
        if(token==null){
            sendErrorResponse(response, 401, "noSignIn");
            return false;
        }
        try {
            Map<String, Object> claim = JwtUtils.parseToken(token);
            long minTime = 15*60*1000;
            if (JwtUtils.getExpireTime(token)<minTime){
                response.setHeader("Authorization", JwtUtils.getToken(claim));
            }
            ThreadLocalUtil.set(claim);
            return true;
        } catch (Exception e) {
            // token 已过期或非法，统一按未登录处理
            sendErrorResponse(response, 401, "noSignIn");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.remove();
    }
}
