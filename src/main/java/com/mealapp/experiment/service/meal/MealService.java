package com.mealapp.experiment.service.meal;

import com.mealapp.openapi.meal.model.ListMealResponse;
import com.mealapp.openapi.meal.model.ReadMealResponse;

import java.util.List;

public interface MealService {

    ReadMealResponse getMeal(Long id);

    List<ListMealResponse> listMeals(Long dietId, List<Long> categoryIdList);
}
