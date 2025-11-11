package com.mealapp.experiment.controller.utils;

import com.mealapp.experiment.model.Ingredient;
import com.mealapp.experiment.model.Meal;
import com.mealapp.openapi.ingredient.model.CreateIngredientRequest;
import com.mealapp.openapi.ingredient.model.UpdateIngredientRequest;
import com.mealapp.openapi.meal.model.CreateMealRequest;
import com.mealapp.openapi.meal.model.UpdateMealRequest;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ControllerMapper {

    Meal createMealRequestToEntity(CreateMealRequest createMealRequest);

    Meal updateMealRequestToEntity(UpdateMealRequest updateMealRequest);

    Ingredient createIngredientRequestToIngredient(CreateIngredientRequest createIngredientRequest);

    Ingredient updateIngredientRequestToIngredient(UpdateIngredientRequest updateIngredientRequest);

}
