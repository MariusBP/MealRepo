package com.mealapp.experiment.service.ingredient;

import com.mealapp.experiment.common.utils.ExceptionUtils;
import com.mealapp.experiment.model.Ingredient;
import com.mealapp.experiment.repository.AllergyRepository;
import com.mealapp.experiment.repository.IngredientRepository;
import com.mealapp.experiment.service.utils.ServiceMapper;
import com.mealapp.experiment.service.validation.ServiceValidation;
import com.mealapp.openapi.ingredient.model.ListIngredientResponse;
import com.mealapp.openapi.ingredient.model.ReadIngredientResponse;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final AllergyRepository allergyRepository;
    private final ServiceMapper mapper;
    private final ServiceValidation validation;

    @Override
    @Transactional
    public ReadIngredientResponse createIngredient(Ingredient ingredient) {
        log.info("Creating new ingredient with name: {}", ingredient.getName());

        validateIngredientNameExists(ingredient.getName());
        if (!ingredient.getAllergies().isEmpty()) {
            ingredient.getAllergies().forEach(allergy -> {
                validation.validateEntityId(allergy.getId(), allergyRepository, "Allergy");
            });
        }

        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        return mapper.ingredientToReadIngredientResponse(savedIngredient);
    }

    @Override
    @Transactional(readOnly = true)
    public ReadIngredientResponse getIngredient(Long id) {
        log.info("Fetching ingredient with id: {}", id);

        Ingredient ingredient = ingredientRepository.findIngredientById(id).orElseThrow(
                ExceptionUtils.exception(HttpStatus.NOT_FOUND, "Not found ingredient with id: " + id)
        );
        return mapper.ingredientToReadIngredientResponse(ingredient);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ListIngredientResponse> listIngredients() {
        log.info("Listing all ingredients");
        return mapper.ingredientToListIngredientResponse(ingredientRepository.findAll());
    }

    @Override
    @Transactional
    public ReadIngredientResponse updateIngredient(Long id, Ingredient ingredient) {
        log.info("Updating ingredient with id: {}", id);
        //TODO: implement update logic
        return null;
    }

    private void validateIngredientNameExists(String name) {
        ingredientRepository.findByName(name).ifPresent(ingredient -> {
            throw ExceptionUtils.exception(HttpStatus.CONFLICT,"Ingredient with name '" + name + "' already exists.").get();
        });
    }
}
