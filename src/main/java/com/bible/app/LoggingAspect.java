package com.bible.app;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* com.bible.app.controller..*(..))")

    public Object profileAllMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        final StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();

        String runtime = stopWatch.getTotalTimeMillis() + "ms";
        if (stopWatch.getTotalTimeMillis() == 0) {
            runtime = (stopWatch.getTotalTimeNanos() / 1000) + "μs";
        }

        LOGGER.info("Runtime of " + className + "." + methodName + ": " + runtime);
        return result;
    }
}
