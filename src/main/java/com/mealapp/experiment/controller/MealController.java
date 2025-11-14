package com.mealapp.experiment.controller;


import com.mealapp.experiment.controller.utils.ControllerMapper;
import com.mealapp.experiment.service.meal.MealService;
import com.mealapp.openapi.meal.api.MealApi;
import com.mealapp.openapi.meal.model.CreateMealRequest;
import com.mealapp.openapi.meal.model.ListMealResponse;
import com.mealapp.openapi.meal.model.ReadMealResponse;
import com.mealapp.openapi.meal.model.UpdateMealRequest;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class MealController implements MealApi {

    @Autowired
    private MealService mealService;

    @Autowired
    private ControllerMapper mapper;

    @PostConstruct
    public void init() {
        log.info("MealController initialized");
    }

    @Override
    public ResponseEntity<ReadMealResponse> getMeal(
            String accept,
            Long id,
            String contentType,
            String xRequestID,
            String userAgent) {

        log.info("getMeal called with id: " + id);
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

        log.info("listMeals called with dietId: {}, categoryIdList: {}", dietId, categoryIdList);
        return ResponseEntity.ok(mealService.listMeals(dietId, categoryIdList));
    }

    @Override
    public ResponseEntity<ReadMealResponse> createMeal(
            String accept,
            CreateMealRequest createMealRequest,
            String contentType,
            String xRequestID,
            String userAgent) {

        log.info("createMeal called with request: {}", createMealRequest);
        ReadMealResponse readMealResponse = mealService.createMeal(
                mapper.createMealRequestToEntity(createMealRequest));
        return ResponseEntity.ok(readMealResponse);
    }

    @Override
    public ResponseEntity<ReadMealResponse> updateMeal(
            String accept,
            UpdateMealRequest updateMealRequest,
            String contentType,
            String xRequestID,
            String userAgent) {

        log.info("updateMeal called with request: {}", updateMealRequest);
        ReadMealResponse readMealResponse = mealService.updateMeal(
                updateMealRequest.getId(), mapper.updateMealRequestToEntity(updateMealRequest));
        return ResponseEntity.ok(readMealResponse);
    }
}
