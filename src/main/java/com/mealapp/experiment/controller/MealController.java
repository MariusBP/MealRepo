package com.mealapp.experiment.controller;



import com.mealapp.openapi.api.MealApi;
import com.mealapp.openapi.model.ListMealResponse;
import com.mealapp.openapi.model.ReadMealResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MealController implements MealApi {


    @Override
    public ResponseEntity<ReadMealResponse> getMeal(
            String accept,
            Integer id,
            String contentType,
            String xRequestID,
            String userAgent) {
        // Your logic here
        return ResponseEntity.ok(new ReadMealResponse());
    }

    @Override
    public ResponseEntity<List<ListMealResponse>> listMeals(
            String accept,
            Integer dietId,
            List<Integer> categoryIdList,
            String contentType,
            String xRequestID,
            String userAgent) {
        // Your logic here
        return ResponseEntity.ok(List.of());
    }
}
