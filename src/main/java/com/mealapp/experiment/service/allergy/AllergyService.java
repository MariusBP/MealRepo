package com.mealapp.experiment.service.allergy;

import com.mealapp.experiment.model.Allergy;
import com.mealapp.openapi.allergy.model.ListAllergyResponse;
import com.mealapp.openapi.allergy.model.ReadAllergyResponse;


import java.util.List;

public interface AllergyService {

    ReadAllergyResponse createAllergy(Allergy allergy);

    ReadAllergyResponse getAllergy(Long id);

    List<ListAllergyResponse> listAllergies();

}
