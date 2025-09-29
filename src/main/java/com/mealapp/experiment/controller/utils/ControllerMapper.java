package com.mealapp.experiment.controller.utils;

import com.mealapp.experiment.model.Meal;
import com.mealapp.openapi.meal.model.CreateMealRequest;
import com.mealapp.openapi.meal.model.UpdateMealRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Named;

import java.net.URI;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ControllerMapper {

    @Mapping(target = "picture", source = "picture", qualifiedByName = "uriToString")
    Meal createMealRequestToMeal(CreateMealRequest createMealRequest);

    @Mapping(target = "picture", source = "picture", qualifiedByName = "uriToString")
    Meal updateMealRequestToMeal(UpdateMealRequest updateMealRequest);

    @Named("uriToString")
    default String uriToString(URI uri) {
        return uri != null ? uri.toString() : null;
    }
}
