package com.isariev.orderservice.util.aop;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import static com.isariev.orderservice.util.aop.LogColorConstants.ANSI_GREEN;
import static com.isariev.orderservice.util.aop.LogColorConstants.ANSI_RESET;

@Log4j2
@Aspect
@Component
public class LoggingControllerClassesAspect {

    @Pointcut("execution(public * com.isariev.orderservice.controller.OrderController.*(..))")
    public void callAtMyControllersPublicMethods() {
    }

    @Before("callAtMyControllersPublicMethods()")
    public void logBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        //log.info("Controller: " + methodName + " - start.");
        log.info(ANSI_GREEN + "Controller: " + methodName + " - start." + ANSI_RESET);
    }

    @AfterReturning(value = "callAtMyControllersPublicMethods()")
    public void logAfter(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        log.info(ANSI_GREEN + "Controller: " + methodName + " - end." + ANSI_RESET);
    }
}
