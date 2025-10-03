package com.exist.HelpdeskApp.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
public class LoggingAspect {

    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";
    private static final Logger logger = Logger.getLogger("ServiceLogger");

    @AfterReturning("execution(* com.exist.service.*.*(..))")
    public void logServiceMethods(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();

        if (methodName.toLowerCase().contains("attempted")) {
            logger.info(RED + "Method called: " + methodName + RESET);
        } else {
            logger.info("Method called: " + methodName);
        }

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg != null && arg.toString().toLowerCase().contains("attempted")) {
                logger.info(RED + "Argument contains 'attempted': " + arg + RESET);
            }
        }
    }
}
