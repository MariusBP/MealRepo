package com.mealapp.experiment.controller;


import com.mealapp.experiment.service.meal.MealService;
import com.mealapp.openapi.meal.api.MealApi;
import com.mealapp.openapi.meal.model.ListMealResponse;
import com.mealapp.openapi.meal.model.ReadMealResponse;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class MealController implements MealApi {

    @Autowired
    private MealService mealService;

    @Autowired
    private HttpServletRequest request;

    @Value("${authentication.jwt.secret}")
    private String XApiKey;

    @PostConstruct
    public void init() {
        System.out.println("MealController initialized");
        System.out.println("XApiKey configured: " + (XApiKey != null ? "Yes" : "No"));
    }

    @Override
    public ResponseEntity<ReadMealResponse> getMeal(
            String accept,
            Integer id,
            String contentType,
            String xRequestID,
            String userAgent) {

        getApiKeyFromRequest();

        System.out.println("getMeal called with id: " + id);
        return ResponseEntity.ok(mealService.getMeal(id));
    }

    @Override
    public ResponseEntity<List<ListMealResponse>> listMeals(
            String accept,
            Integer dietId,
            List<Integer> categoryIdList,
            String contentType,
            String xRequestID,
            String userAgent) {

        getApiKeyFromRequest();

        System.out.println("listMeals called with dietId: " + dietId + ", categoryIdList: " + categoryIdList);
        return ResponseEntity.ok(mealService.listMeals(dietId, categoryIdList));
    }

    private void getApiKeyFromRequest() {
        String apiKeyFromHeader = request.getHeader("X-API-Key");

        if (!XApiKey.equals(apiKeyFromHeader)) {
            throw new RuntimeException("Invalid API key");
        }
    }
}
