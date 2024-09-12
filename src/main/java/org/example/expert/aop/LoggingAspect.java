package org.example.expert.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.example.expert.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

@Slf4j
@Aspect
public class LoggingAspect {
    @Autowired
    private JwtUtil jwtUtil;

    @Pointcut("@annotation(org.example.expert.aop.annotation.RequestTrack)")
    private void requestLog(){}

    @Around("requestLog()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String requestUrl = request.getRequestURL().toString();
        String userId = (String)request.getAttribute("userId");

        try {
            return joinPoint.proceed();
        } finally {
            Date requestTimestamp = new Date();
            log.info("::: requestTimestamp: {}", requestTimestamp);
            log.info("::: requestUrl: {}", requestUrl);
            log.info("::: userId: {}", userId);
        }
    }
}
