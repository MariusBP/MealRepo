package com.mealapp.experiment.service.meal;

import com.mealapp.experiment.model.Meal;
import com.mealapp.experiment.repository.MealRepository;
import com.mealapp.experiment.service.utils.ServiceMapper;
import com.mealapp.experiment.common.utils.ExceptionUtils;
import com.mealapp.openapi.meal.model.ListMealResponse;
import com.mealapp.openapi.meal.model.ReadMealResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MealServiceImp implements MealService {

    private final MealRepository mealRepository;
    private final ServiceMapper mapper;

    @Override
    public ReadMealResponse getMeal(Long id) {

        Meal meal = mealRepository.findAllById(id).orElseThrow(ExceptionUtils.exception(HttpStatus.NOT_FOUND, "Not found meal with id: " + id));
        return mapper.mealToReadMealResponse(meal);
    }

    @Override
    public List<ListMealResponse> listMeals(Long dietId, List<Long> categoryIdList) {
        return mapper.mealToListMealResponse(mealRepository.findByDietIdAndCategoriesIds(dietId, categoryIdList));
    }

    @Override
    public ReadMealResponse createMeal(Meal createMealRequest) {
        log.info("Creating new meal with name: {}", createMealRequest.getName());
        Meal savedMeal = mealRepository.save(createMealRequest);

        Meal mealWithAllergies = mealRepository.findAllById(savedMeal.getId())
                .orElseThrow(() -> new RuntimeException("Failed to retrieve saved meal"));

        return mapper.mealToReadMealResponse(mealWithAllergies);
    }

    @Override
    public ReadMealResponse updateMeal(Long id, Meal updateMealRequest) {
        log.info("Updating meal with id: {}", id);

        Meal existingMeal = mealRepository.findAllById(id)
                .orElseThrow(ExceptionUtils.exception(HttpStatus.NOT_FOUND, "Not found meal with id: " + id));

        mapper.merge(updateMealRequest, existingMeal);
        Meal mealWithAllergies = mealRepository.findAllById(existingMeal.getId())
                .orElseThrow(() -> new RuntimeException("Failed to retrieve updated meal"));

        return mapper.mealToReadMealResponse(mealWithAllergies);
    }
}
