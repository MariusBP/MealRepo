package com.mealapp.experiment.controller;

import com.mealapp.experiment.controller.utils.ControllerMapper;
import com.mealapp.experiment.service.allergy.AllergyService;
import com.mealapp.openapi.allergy.api.AllergyApi;
import com.mealapp.openapi.allergy.model.CreateAllergyRequest;
import com.mealapp.openapi.allergy.model.ListAllergyResponse;
import com.mealapp.openapi.allergy.model.ReadAllergyResponse;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api")
public class AllergyController implements AllergyApi {

    @Autowired
    private AllergyService allergyService;

    @Autowired
    private ControllerMapper mapper;

    @PostConstruct
    public void init() {
        log.info("AllergyController initialized");
    }

    @Override
    public ResponseEntity<ReadAllergyResponse> createAllergy(
            String accept, CreateAllergyRequest createAllergyRequest,
            String contentType, String xRequestID,
            String userAgent, UUID xCorrelationID) {
        log.info("Create Allergy called with name: {}", createAllergyRequest.getName());

        ReadAllergyResponse response = allergyService.createAllergy(
                mapper.createAllergyRequestToAllergy(createAllergyRequest));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ReadAllergyResponse> getAllergy(
            String accept, Long id, String contentType,
            String xRequestID, String userAgent,
            UUID xCorrelationID) {
        log.info("Fetch Allergy called with id: {}", id);

        ReadAllergyResponse response = allergyService.getAllergy(id);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<ListAllergyResponse>> listAllergies(
            String accept, String contentType,
            String xRequestID, String userAgent,
            UUID xCorrelationID) {
        log.info("List Allergies called.");

        List<ListAllergyResponse> responses = allergyService.listAllergies();
        return ResponseEntity.ok(responses);
    }
}
