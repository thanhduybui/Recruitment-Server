package com.edu.hcmute.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;


@Aspect
@Component
@Slf4j
public class LoggerAspect {
    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
            " || within(@org.springframework.stereotype.Service *)" +
            " || within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
    }

    @Pointcut("within(com.edu.hcmute.controller..*)" +
            " || within(com.edu.hcmute.service..*)" +
            " || within(com.edu.hcmute.repository..*)" +
            " || within(com.edu.hcmute.exception.GlobalExceptionHandler*)")
    public void applicationPackagePointcut() {
    }

    @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause() : "NULL");
    }

    @Around("applicationPackagePointcut() && springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return joinPoint.proceed();
        }
        HttpServletRequest request = attributes.getRequest();
        if (isRestController(joinPoint.getTarget())) {
            String url = request.getRequestURL().toString();

            if (log.isDebugEnabled()) {
                log.debug("Enter: {} {} with argument[s] = {}", request.getMethod(), url, Arrays.toString(joinPoint.getArgs()));
            }

            Object result = joinPoint.proceed();

            if (log.isDebugEnabled()) {
                log.debug("Exit: {} {} with result = {}", request.getMethod(), url, result);
            }

            return result;
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
            }
            try {
                Object result = joinPoint.proceed();
                if (log.isDebugEnabled()) {
                    log.debug("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
                            joinPoint.getSignature().getName(), result);
                }
                return result;
            } catch (IllegalArgumentException e) {
                log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                        joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
                throw e;
            }
        }
    }

    private boolean isRestController(Object bean) {
        Class<?> beanClass = bean.getClass();
        return beanClass.isAnnotationPresent(RestController.class);
    }
}
