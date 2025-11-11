package com.mealapp.experiment.service.ingredient;

import com.mealapp.experiment.model.Ingredient;
import com.mealapp.experiment.repository.IngredientRepository;
import com.mealapp.experiment.service.utils.ServiceMapper;
import com.mealapp.experiment.common.utils.ExceptionUtils;
import com.mealapp.openapi.ingredient.model.ListIngredientResponse;
import com.mealapp.openapi.ingredient.model.ReadIngredientResponse;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class IngredientServiceImp implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final ServiceMapper mapper;

    @Override
    @Transactional
    public ReadIngredientResponse createIngredient(Ingredient ingredient) {
        log.info("Creating new ingredient with name: {}", ingredient.getName());
        Ingredient savedIngredient = ingredientRepository.save(ingredient);

        return mapper.ingredientToReadIngredientResponse(savedIngredient);
    }

    @Override
    @Transactional(readOnly = true)
    public ReadIngredientResponse getIngredient(Long id) {
        Ingredient ingredient = ingredientRepository.findIngredientById(id)
                .orElseThrow(ExceptionUtils.exception(HttpStatus.NOT_FOUND, "Not found ingredient with id: " + id));
        return mapper.ingredientToReadIngredientResponse(ingredient);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ListIngredientResponse> listIngredients() {
        return mapper.ingredientToListIngredientResponse(ingredientRepository.findAll());
    }

    @Override
    @Transactional
    public ReadIngredientResponse updateIngredient(Long id, Ingredient ingredient) {
        log.info("Updating ingredient with id: {}", id);

        Ingredient existingIngredient = ingredientRepository.findIngredientById(id)
                .orElseThrow(ExceptionUtils.exception(HttpStatus.NOT_FOUND, "Not found ingredient with id: " + id));

        Ingredient updatedIngredient = mapper.merge(ingredient, existingIngredient);
        return mapper.ingredientToReadIngredientResponse(updatedIngredient);
    }
}
