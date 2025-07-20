package com.mealapp.experiment.controller;

import com.mealApp.openapi.api.MealApi;
import com.mealApp.openapi.model.ReadMealResponse;
import org.springframework.http.ResponseEntity;

public class MealController implements MealApi {


    @Override
    public ResponseEntity<ReadMealResponse> getMeal(
            String accept, Integer id, String contentType, String xRequestID, String userAgent) {

        // Create a meal response
        ReadMealResponse meal = new ReadMealResponse();
        meal.setId(id);
        meal.setName("Spaghetti Bolognese");
        meal.setDescription("Classic Italian pasta dish");
        meal.setRecipe("Cook pasta, make sauce with ground beef and tomatoes, combine and serve.");
        meal.setPrepTime(30);
        meal.setPicture("https://example.com/images/spaghetti.jpg");

        // Add ingredients, diets, allergies and categories as needed

        return ResponseEntity.ok(meal);
    }
}
