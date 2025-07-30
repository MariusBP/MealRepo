package com.mealapp.experiment.controller;

import com.mealapp.openapi.api.MealApi;
import com.mealapp.openapi.model.ListMealResponse;
import com.mealapp.openapi.model.ReadMealResponse;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
public class MealController implements MealApi {

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

        System.out.println("listMeals called with dietId: " + dietId + ", categoryIdList: " + categoryIdList);
        return ResponseEntity.ok(List.of());
    }
}
