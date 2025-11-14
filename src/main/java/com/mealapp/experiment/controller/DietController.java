package com.mealapp.experiment.controller;

import com.mealapp.experiment.service.diet.DietService;
import com.mealapp.openapi.diet.api.DietApi;
import com.mealapp.openapi.diet.model.ListDietResponse;
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
public class DietController implements DietApi {

    @Autowired
    private DietService dietService;

    @PostConstruct
    public void init() {
        log.info("DietController initialized");
    }

    @Override
    public ResponseEntity<List<ListDietResponse>> listDiets(
            String accept,
            String contentType,
            String xRequestID,
            String userAgent,
            UUID xCorrelationID) {

        log.info("listDiets called");
        return ResponseEntity.ok(dietService.listDiets());
    }
}
