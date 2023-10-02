package com.isariev.orderservice.util.aop;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;

import static com.isariev.orderservice.util.aop.LogColorConstants.*;

@Log4j2
@Aspect
@Component
public class LoggingServiceClassesAspect {

    private LocalDateTime startTime;
    private final Statistics hibernateStatistics;

    public LoggingServiceClassesAspect(SessionFactory sessionFactory) {
        this.hibernateStatistics = sessionFactory.getStatistics();
    }

    @Pointcut("execution(public * com.isariev.orderservice.service..*.*(..))")
    public void callAtMyServicesPublicMethods() {
    }

    @Pointcut("execution(public * com.isariev.orderservice.repository..*.*(..))")
    public void callAtRepo() {
    }

    @Before("callAtMyServicesPublicMethods()")
    public void logBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        startTime = LocalDateTime.now();
        if (args.length > 0) {
            log.info(ANSI_BLUE + "Service: " + methodName + " - start. Args count - {}" + ANSI_RESET, args.length);
        } else {
            log.info(ANSI_BLUE + "Service: " + methodName + " - start." + ANSI_RESET);
        }
    }

    @AfterReturning(value = "callAtMyServicesPublicMethods()", returning = "returningValue")
    public void logAfter(JoinPoint joinPoint, Object returningValue) {
        String methodName = joinPoint.getSignature().toShortString();
        Object outputValue;
        log.info("Duration(in millis): " + Duration.between(startTime, LocalDateTime.now()).toMillis());
        if (returningValue != null) {
            if (returningValue instanceof Collection) {
                outputValue = "Collection size - " + ((Collection<?>) returningValue).size();
            } else if (returningValue instanceof byte[]) {
                outputValue = "File as byte[]";
            } else {
                outputValue = returningValue;
            }
            log.info(ANSI_BLUE + "Service: " + methodName + " - end. Returns - {}" + ANSI_RESET, outputValue);
        } else {
            log.info(ANSI_BLUE + "Service: " + methodName + " - end." + ANSI_RESET);
        }
    }
    @AfterReturning("callAtRepo()")
    public void countSqlQueries() {
        long queryCounter = hibernateStatistics.getPrepareStatementCount();
        log.info(ANSI_YELLOW + "SQL queries:" + queryCounter + ANSI_RESET);
        hibernateStatistics.clear();
    }

}
