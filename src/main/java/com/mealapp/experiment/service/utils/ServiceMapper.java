package com.mealapp.experiment.service.utils;

import com.mealapp.experiment.model.Allergy;
import com.mealapp.experiment.model.Diet;
import com.mealapp.experiment.model.IngredientMeal;
import com.mealapp.experiment.model.Meal;
import com.mealapp.openapi.diet.model.ListDietResponse;
import com.mealapp.openapi.meal.model.AllergyObject;
import com.mealapp.openapi.meal.model.IngredientObject;
import com.mealapp.openapi.meal.model.ListMealResponse;
import com.mealapp.openapi.meal.model.ReadMealResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Named;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ServiceMapper {

    @Mapping(target = "allergies", source = "ingredientMeals", qualifiedByName = "extractAllergies")
    @Mapping(target = "ingredients", source = "ingredientMeals", qualifiedByName = "extractIngredients")
    ReadMealResponse mealToReadMealResponse(Meal meal);

    ListMealResponse mealToListMealResponse(Meal meal);

    List<ListMealResponse> mealToListMealResponse(List<Meal> mealList);

    AllergyObject allergyToAllergyObject(Allergy allergy);

    Meal merge(Meal newMeal, @MappingTarget Meal existingMeal);

    List<ListDietResponse> dietToListDietResponse(List<Diet> dietList);

    @Named("extractIngredients")
    default List<IngredientObject> extractIngredients(Set<IngredientMeal> ingredientMeals) {
        if (ingredientMeals == null || ingredientMeals.isEmpty()) {
            return List.of();
        }
        return ingredientMeals.stream()
                .filter(Objects::nonNull)
                .map(im -> {
                    if (im.getIngredient() == null) {
                        return null;
                    }
                    IngredientObject ingredientObject = new IngredientObject();
                    ingredientObject.setId(im.getIngredient().getId());
                    ingredientObject.setName(im.getIngredient().getName());
                    ingredientObject.setAmount(im.getAmount());
                    ingredientObject.setUnit(im.getUnit());
                    return ingredientObject;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Named("extractAllergies")
    default List<AllergyObject> extractAllergies(Set<IngredientMeal> ingredientMeals) {
        if (ingredientMeals == null || ingredientMeals.isEmpty()) {
            return List.of();
        }
        return ingredientMeals.stream()
                .filter(Objects::nonNull)
                .flatMap(im -> {
                    if (im.getIngredient() == null) {
                        return Stream.<Allergy>empty();
                    }
                    var allergies = im.getIngredient().getAllergies();
                    if (allergies == null || allergies.isEmpty()) {
                        return Stream.<Allergy>empty();
                    }else {
                        return allergies.stream();
                    }
                })
                .distinct()
                .map(this::allergyToAllergyObject)
                .collect(Collectors.toList());
    }
}
