package com.mealapp.experiment.service.validation;

import com.mealapp.experiment.common.utils.ExceptionUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceValidationImpl implements ServiceValidation {

    @Override
    public void validateEntityId(Long id, JpaRepository<?, Long> repository, String entityName) {
        if (!repository.existsById(id)) {
            throw ExceptionUtils.exception(HttpStatus.NOT_FOUND, entityName + " with ID " + id + " not found").get();
        }
    }

    @Override
    public void validateStringNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw ExceptionUtils.exception(HttpStatus.BAD_REQUEST, fieldName + " cannot be null or empty").get();
        }
    }

    @Override
    public void validateListNotEmpty(List<?> list, String fieldName) {
        if (list == null || list.isEmpty()) {
            throw ExceptionUtils.exception(HttpStatus.BAD_REQUEST, fieldName + " cannot be null or empty").get();
        }
    }
}
