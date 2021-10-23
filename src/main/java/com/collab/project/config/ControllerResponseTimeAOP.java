package com.collab.project.config;

import com.collab.project.helpers.Constants;
import com.collab.project.util.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.json.JSONObject;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Component
@EnableAspectJAutoProxy
@Aspect
@ComponentScan(
    basePackages = {"com.collab.project.controller"},
    excludeFilters = {@ComponentScan.Filter(Aspect.class)})
@Slf4j
public class ControllerResponseTimeAOP {

    @Autowired
    ObjectMapper mapper;
    private static final String REF_ID = "refId";

    @Pointcut(Constants.POINTCUT)
    private void allControllers() {
    }

    @Before(Constants.ALLCONTROLLERS)
    public void settingLoggingAspect(JoinPoint joinPoint) {
        populateAspect(joinPoint, new JSONObject(JsonUtils.deepSerialize(joinPoint.getArgs()[0])));
    }

    public void populateAspect(JoinPoint joinPoint, JSONObject jsonObject) {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String sb = UUID.randomUUID().toString();
        MDC.put(Constants.REF_ID, sb);

        String apiName = String.join(".", joinPoint.getSignature().getDeclaringTypeName(),
            joinPoint.getSignature().getName());

        log.debug("----->>>>>API: {} Input: {}", apiName,
            getValueAsString(joinPoint.getArgs()));

        MDC.put(Constants.API_NAME, apiName);
        MDC.put(Constants.CURRENT_TIME, String.valueOf(System.currentTimeMillis()));
    }

    private String getValueAsString(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (Exception ex) {
            log.error("Error while deserializing", ex);
        }
        return "";
    }


    @AfterReturning(pointcut = Constants.ALLCONTROLLERS, returning = "response")
    public void successAspect(JoinPoint joinPoint, Object response) {
        log.debug("<<<<<-----API: {}  Output {}", getApiName(),
            getValueAsString(response));
        clearMdc();
    }

    @After(value = Constants.EXCEPTION_POINT)
    public void exceptionAspect(JoinPoint joinPoint) {
        log.debug("<<<<<-----API: {}  Output {}",
            getApiName(),
            JsonUtils.toJson(joinPoint.getArgs()));
        clearMdc();

    }

    private void getExecutionTime() {
        log.debug("Total Time Taken {} ms ",
            System.currentTimeMillis() - Long.valueOf(MDC.get(Constants.CURRENT_TIME)));
    }


    private String getApiName() {
        return MDC.get(Constants.API_NAME);
    }

    private void clearMdc() {
        getExecutionTime();
        MDC.remove(Constants.CURRENT_TIME);
        MDC.remove(Constants.REF_ID);
        MDC.remove(Constants.API_NAME);
    }
}

