package com.w.lee.configure;

import com.w.lee.infrastructure.annotation.ClearCaseCache;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author caoyue
 */
@Component
@Aspect
@Slf4j
public class ClearCacheDelayedAop {

    @Pointcut("@annotation(com.w.lee.ab.test.infrastructure.annotation.ClearCaseCache)")
    private void clearCachePoint() {
    }

    @Around("clearCachePoint()")
    public Object clearCacheAop(ProceedingJoinPoint proceeds) throws Throwable {
        Method method = ((MethodSignature) proceeds.getSignature()).getMethod();
        ClearCaseCache annotation = method.getAnnotation(ClearCaseCache.class);
        Object proceed = null;
        String codeFieldName = annotation.caseCodeFieldName();
        String idFieldName = annotation.caseIdFieldName();
        Object[] args = proceeds.getArgs();
        Class<?> aClass = args[0].getClass();
        String testCode;

        return proceed;
    }
}
