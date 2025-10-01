package com.mealapp.experiment.service.diet;

import com.mealapp.openapi.diet.model.ListDietResponse;

import java.util.List;

public interface DietService {
    List<ListDietResponse> listDiets();
}
