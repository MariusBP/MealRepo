package com.mealapp.experiment.service;

import com.mealapp.experiment.model.Meal;
import com.mealapp.openapi.meal.model.ListMealResponse;
import com.mealapp.openapi.meal.model.ReadMealResponse;
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
public interface ServiceMapper {

    @Mapping(target = "picture", source = "picture", qualifiedByName = "stringToUri")
    ReadMealResponse mealToReadMealResponse(Meal meal);

    @Mapping(target = "picture", source = "picture", qualifiedByName = "stringToUri")
    ListMealResponse mealToListMealResponse(Meal meal);


    @Named("stringToUri")
    default URI stringToUri(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return URI.create(value);
        } catch (Exception e) {
            return null;
        }
    }
}
