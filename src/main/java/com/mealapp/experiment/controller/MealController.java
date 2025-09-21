package com.mealapp.experiment.controller;


import com.mealapp.openapi.meal.api.MealApi;
import com.mealapp.openapi.meal.model.ListMealResponse;
import com.mealapp.openapi.meal.model.ReadMealResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class MealController implements MealApi {

    @Value("${authentication.jwt.secret}")
    private String XApiKey;

    @PostConstruct
    public void init() {
        System.out.println("MealController initialized");
    }

    @Override
    public ResponseEntity<ReadMealResponse> getMeal(
            String accept,
            Integer id,
            String contentType,
            String xRequestID,
            String userAgent) {

        // Check API key from request header
        getApiKeyFromRequest();

        System.out.println("getMeal called with id: " + id);
        ReadMealResponse response = new ReadMealResponse();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<ListMealResponse>> listMeals(
            String accept,
            Integer dietId,
            List<Integer> categoryIdList,
            String contentType,
            String xRequestID,
            String userAgent) {

        // Check API key from request header
        getApiKeyFromRequest();

        System.out.println("listMeals called with dietId: " + dietId + ", categoryIdList: " + categoryIdList);
        return ResponseEntity.ok(List.of());
    }

    private void getApiKeyFromRequest() {
        getRequest()
            .map(request -> request.getHeader("X-API-Key"))
            .filter(apiKey -> XApiKey.equals(apiKey))
            .orElseThrow(() -> new RuntimeException("Invalid API key"));
    }
}
