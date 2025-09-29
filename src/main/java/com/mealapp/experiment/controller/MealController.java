package com.mealapp.experiment.controller;


import com.mealapp.experiment.controller.utils.ApiUtils;
import com.mealapp.experiment.controller.utils.ControllerMapper;
import com.mealapp.experiment.service.meal.MealService;
import com.mealapp.openapi.meal.api.MealApi;
import com.mealapp.openapi.meal.model.CreateMealRequest;
import com.mealapp.openapi.meal.model.ListMealResponse;
import com.mealapp.openapi.meal.model.ReadMealResponse;
import com.mealapp.openapi.meal.model.UpdateMealRequest;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ApiUtils apiUtils;

    @Autowired
    private ControllerMapper mapper;

    @PostConstruct
    public void init() {
        System.out.println("MealController initialized");
    }

    @Override
    public ResponseEntity<ReadMealResponse> getMeal(
            String accept,
            Long id,
            String contentType,
            String xRequestID,
            String userAgent) {

        apiUtils.validateApiKeyFromRequest(request.getHeader("X-API-Key"));

        System.out.println("getMeal called with id: " + id);
        return ResponseEntity.ok(mealService.getMeal(id));
    }

    @Override
    public ResponseEntity<List<ListMealResponse>> listMeals(
            String accept,
            Long dietId,
            String contentType,
            String xRequestID,
            String userAgent,
            List<Long> categoryIdList) {

        apiUtils.validateApiKeyFromRequest(request.getHeader("X-API-Key"));

        System.out.println("listMeals called with dietId: " + dietId + ", categoryIdList: " + categoryIdList);

        return ResponseEntity.ok(mealService.listMeals(dietId, categoryIdList));
    }

    @Override
    public ResponseEntity<ReadMealResponse> createMeal(
            String accept,
            CreateMealRequest createMealRequest,
            String contentType,
            String xRequestID,
            String userAgent) {

        apiUtils.validateApiKeyFromRequest(request.getHeader("X-API-Key"));

        ReadMealResponse readMealResponse = mealService.createMeal(mapper.createMealRequestToMeal(createMealRequest));
        System.out.println("createMeal called with request: " + createMealRequest);
        return ResponseEntity.ok(readMealResponse);
    }

    @Override
    public ResponseEntity<ReadMealResponse> updateMeal(
            String accept,
            UpdateMealRequest updateMealRequest,
            String contentType,
            String xRequestID,
            String userAgent) {

        apiUtils.validateApiKeyFromRequest(request.getHeader("X-API-Key"));

        ReadMealResponse readMealResponse = mealService.updateMeal(
                updateMealRequest.getId(), mapper.updateMealRequestToMeal(updateMealRequest));

        System.out.println("updateMeal called with request: " + updateMealRequest);
        return ResponseEntity.ok(readMealResponse);
    }
}
