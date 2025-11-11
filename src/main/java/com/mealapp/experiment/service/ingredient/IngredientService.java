package com.mealapp.experiment.service.ingredient;

import com.mealapp.openapi.ingredient.model.ListIngredientResponse;
import com.mealapp.openapi.ingredient.model.ReadIngredientResponse;
import com.mealapp.experiment.model.Ingredient;

import java.util.List;

public interface IngredientService {

    ReadIngredientResponse createIngredient(Ingredient ingredient);

    ReadIngredientResponse getIngredient(Long id);

    List<ListIngredientResponse> listIngredients();

    ReadIngredientResponse updateIngredient(Long id, Ingredient ingredient);
}
