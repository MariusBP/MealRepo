package com.mealapp.experiment.controller;


import com.mealapp.experiment.service.meal.MealService;
import com.mealapp.openapi.meal.api.MealApi;
import com.mealapp.openapi.meal.model.CreateMealRequest;
import com.mealapp.openapi.meal.model.ListMealResponse;
import com.mealapp.openapi.meal.model.ReadMealResponse;
import com.mealapp.openapi.meal.model.UpdateMealRequest;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
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
        return ResponseEntity.ok(mealService.getMeal(id.longValue()));
    }

    @Override
    public ResponseEntity<List<ListMealResponse>> listMeals(
            String accept,
            Integer dietId,
            String contentType,
            String xRequestID,
            String userAgent,
            List<Integer> categoryIdList) {

        getApiKeyFromRequest();

        System.out.println("listMeals called with dietId: " + dietId + ", categoryIdList: " + categoryIdList);

        return ResponseEntity.ok(mealService.listMeals(dietId.longValue(), null));
    }

    @Override
    public ResponseEntity<ReadMealResponse> createMeal(
            String accept,
            CreateMealRequest createMealRequest,
            String contentType,
            String xRequestID,
            String userAgent) {

        getApiKeyFromRequest();

        System.out.println("createMeal called with request: " + createMealRequest);
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<ReadMealResponse> updateMeal(
            String accept,
            UpdateMealRequest updateMealRequest,
            String contentType,
            String xRequestID,
            String userAgent) {

        getApiKeyFromRequest();

        System.out.println("updateMeal called with request: " + updateMealRequest);
        return ResponseEntity.ok(null);
    }

    private void getApiKeyFromRequest() {
        String apiKeyFromHeader = request.getHeader("X-API-Key");

        if (!XApiKey.equals(apiKeyFromHeader)) {
            throw new RuntimeException("Invalid API key");
        }
    }
}
