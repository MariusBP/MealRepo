package com.mealapp.experiment.interceptor;

import com.mealapp.experiment.common.utils.ExceptionUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    @Value("${X_Api_Key}")
    private String xApiKey;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String providedApiKey = request.getHeader("X-API-Key");

        if (!xApiKey.equals(providedApiKey)) {
            throw ExceptionUtils.exception(HttpStatus.UNAUTHORIZED, "Invalid API key").get();
        }

        return true;
    }
}

