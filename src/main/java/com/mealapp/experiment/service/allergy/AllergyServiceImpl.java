package com.mealapp.experiment.service.allergy;


import com.mealapp.experiment.common.utils.ExceptionUtils;
import com.mealapp.experiment.model.Allergy;
import com.mealapp.experiment.repository.AllergyRepository;
import com.mealapp.experiment.service.utils.ServiceMapper;
import com.mealapp.openapi.allergy.model.ListAllergyResponse;
import com.mealapp.openapi.allergy.model.ReadAllergyResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class AllergyServiceImpl implements AllergyService {

    private final AllergyRepository allergyRepository;
    private final ServiceMapper mapper;

    @Override
    @Transactional
    public ReadAllergyResponse createAllergy(Allergy allergy) {
        log.info("Creating new allergy with name: {}", allergy.getName());

        validateAllergyNameExists(allergy.getName());

        Allergy savedAllergy = allergyRepository.save(allergy);
        log.info("Successfully created allergy with id: {}", savedAllergy.getId());
        return mapper.allergyToReadAllergyResponse(savedAllergy);
    }

    @Override
    @Transactional(readOnly = true)
    public ReadAllergyResponse getAllergy(Long id) {
        log.info("Fetching allergy with id: {}", id);

        Allergy allergy = allergyRepository.findById(id).orElseThrow(
                ExceptionUtils.exception(HttpStatus.NOT_FOUND, "Not found allergy with id: " + id)
        );
        return mapper.allergyToReadAllergyResponse(allergy);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ListAllergyResponse> listAllergies() {
        log.info("Listing all allergies");

        List<Allergy> allergies = allergyRepository.findAll();
        return mapper.allergyToListAllergyResponse(allergies);
    }

    private void validateAllergyNameExists(String name) {
        allergyRepository.findByName(name).ifPresent(allergy -> {
            throw ExceptionUtils.exception(HttpStatus.CONFLICT,
                    "Allergy with name '" + name + "' already exists.").get();
        });
    }
}
