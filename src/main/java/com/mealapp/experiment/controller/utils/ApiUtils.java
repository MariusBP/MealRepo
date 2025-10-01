package com.mealapp.experiment.controller.utils;


import com.mealapp.experiment.common.utils.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ApiUtils {

    @Value("${X_Api_Key}")
    private String xApiKey;

    public void validateApiKeyFromRequest(String providedApiKey) {
        if (!xApiKey.equals(providedApiKey)) {
            throw ExceptionUtils.exception(HttpStatus.UNAUTHORIZED, "Invalid API key").get();
        }
    }
}
