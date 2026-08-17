package com.ben.utils;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ControllerTimeAspect {

    private static final Logger logger = LoggerFactory.getLogger(ControllerTimeAspect.class);

    // 切点：扫描你所有Controller下的方法，改成你自己controller所在包
    @Pointcut("execution(* com.ben.controller..*.*(..))")
    public void controllerPointCut() {
    }

    /**
     * 环绕通知：可以计时
     */
    @Around("controllerPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        // 获取方法名
        String methodName = joinPoint.getSignature().toShortString();
        // 获取请求参数
        Object[] args = joinPoint.getArgs();

        logger.info("【接口开始】方法:{} , 参数:{}", methodName, args);

        // 执行目标controller方法
        Object result = joinPoint.proceed();

        long cost = System.currentTimeMillis() - start;

        logger.info("【接口结束】方法:{} , 总耗时: {} ms", methodName, cost);

        return result;
    }
}
