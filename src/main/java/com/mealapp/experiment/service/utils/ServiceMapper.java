package com.mealapp.experiment.service.utils;

import com.mealapp.experiment.model.Allergy;
import com.mealapp.experiment.model.Ingredient;
import com.mealapp.experiment.model.Meal;
import com.mealapp.openapi.meal.model.AllergyObject;
import com.mealapp.openapi.meal.model.CreateMealRequest;
import com.mealapp.openapi.meal.model.ListMealResponse;
import com.mealapp.openapi.meal.model.ReadMealResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Named;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ServiceMapper {

    @Mapping(target = "allergies", source = "ingredients", qualifiedByName = "extractAllergies")
    ReadMealResponse mealToReadMealResponse(Meal meal);

    ListMealResponse mealToListMealResponse(Meal meal);

    List<ListMealResponse> mealToListMealResponse(List<Meal> mealList);

    AllergyObject allergyToAllergyObject(Allergy allergy);

    Meal merge(Meal newMeal, @MappingTarget Meal existingMeal);


    @Named("extractAllergies")
    default List<AllergyObject> extractAllergies(Set<Ingredient> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            return List.of();
        }
        return ingredients.stream()
                .flatMap(ingredient -> ingredient.getAllergies().stream())
                .distinct()
                .map(this::allergyToAllergyObject)
                .collect(Collectors.toList());
    }
}
