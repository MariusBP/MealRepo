package com.mealapp.experiment.service.meal;

import com.mealapp.experiment.model.Meal;
import com.mealapp.experiment.repository.MealRepository;
import com.mealapp.experiment.service.utils.ServiceMapper;
import com.mealapp.experiment.common.utils.ExceptionUtils;
import com.mealapp.openapi.meal.model.ListMealResponse;
import com.mealapp.openapi.meal.model.ReadMealResponse;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;
    private final ServiceMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public ReadMealResponse getMeal(Long id) {
        log.info("Fetching meal with id: {}", id);
        return mapper.mealToReadMealResponse(fetchMeal(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ListMealResponse> listMeals(Long dietId, List<Long> categoryIdList) {
        log.info("Fetching list of  meal with diet id: {}", dietId);

        return mapper.mealToListMealResponse(mealRepository.findByDietIdAndCategoriesIds(dietId, categoryIdList, categoryIdList == null ? 0 : categoryIdList.size()));
    }

    @Override
    @Transactional
    public ReadMealResponse createMeal(Meal createMealRequest) {
        log.info("Creating new meal with name: {}", createMealRequest.getName());
        Meal savedMeal = mealRepository.save(createMealRequest);
        return mapper.mealToReadMealResponse(fetchMeal(savedMeal.getId()));
    }

    @Override
    @Transactional
    public ReadMealResponse updateMeal(Long id, Meal updateMealRequest) {
        log.info("Updating meal with id: {}", id);
        Meal existingMeal = fetchMeal(id);
        mapper.merge(updateMealRequest, existingMeal);
        return mapper.mealToReadMealResponse(fetchMeal(id));
    }

    private Meal fetchMeal(Long id) {
        return mealRepository.findMealById(id)
                .orElseThrow(ExceptionUtils.exception(HttpStatus.NOT_FOUND, "Did not find meal with id: " + id));
    }
}
