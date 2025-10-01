package com.mealapp.experiment.service.diet;

import com.mealapp.experiment.model.Diet;
import com.mealapp.experiment.repository.DietRepository;
import com.mealapp.experiment.service.utils.ServiceMapper;
import com.mealapp.openapi.diet.model.ListDietResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class DietServiceImp implements DietService {

    private final DietRepository dietRepository;
    private final ServiceMapper mapper;

    @Override
    public List<ListDietResponse> listDiets() {
        List<Diet> diets = dietRepository.findAll();
        return mapper.dietToListDietResponse(diets);
    }
}
