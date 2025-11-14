package com.mealapp.experiment.service.validation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceValidation {

    void validateEntityId(Long id, JpaRepository<?, Long> repository, String entityName);

    void validateStringNotEmpty(String value, String fieldName);

    void validateListNotEmpty(java.util.List<?> list, String fieldName);

}
