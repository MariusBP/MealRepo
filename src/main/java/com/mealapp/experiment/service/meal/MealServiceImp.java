package com.mealapp.experiment.service.meal;

import com.mealapp.experiment.repository.MealRepository;
import com.mealapp.experiment.service.ServiceMapper;
import com.mealapp.openapi.meal.model.ListMealResponse;
import com.mealapp.openapi.meal.model.ReadMealResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MealServiceImp implements MealService{

    private final MealRepository mealRepository;
    private final ServiceMapper mapper;

    @Override
    public ReadMealResponse getMeal(Integer id) {
        return mapper.mealToReadMealResponse(mealRepository.findById(id).orElseThrow());
    }

    @Override
    public List<ListMealResponse> listMeals(Integer dietId, List<Integer> categoryIdList) {
        return List.of();
    }
}

