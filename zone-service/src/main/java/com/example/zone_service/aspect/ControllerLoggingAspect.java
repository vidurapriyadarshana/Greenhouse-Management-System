package com.example.zone_service.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class ControllerLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(ControllerLoggingAspect.class);

    @Pointcut("within(com.example..controller..*)")
    public void controllerMethods() {}

    @Before("controllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("==> Entering: {}.{}() with arguments: {}",
            joinPoint.getSignature().getDeclaringTypeName(),
            joinPoint.getSignature().getName(),
            Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("<== Exiting: {}.{}() with result: {}",
            joinPoint.getSignature().getDeclaringTypeName(),
            joinPoint.getSignature().getName(),
            result);
    }

    @AfterThrowing(pointcut = "controllerMethods()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        logger.error("!!! Exception in: {}.{}() with message: {}",
            joinPoint.getSignature().getDeclaringTypeName(),
            joinPoint.getSignature().getName(),
            exception.getMessage());
    }

    @Around("controllerMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - startTime;
        logger.info("--- {}.{}() executed in {} ms",
            joinPoint.getSignature().getDeclaringTypeName(),
            joinPoint.getSignature().getName(),
            duration);
        return result;
    }
}
