package com.mealapp.experiment.controller;

import com.mealapp.experiment.controller.utils.ApiUtils;
import com.mealapp.experiment.service.diet.DietService;
import com.mealapp.openapi.diet.api.DietApi;
import com.mealapp.openapi.diet.model.ListDietResponse;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class DietController implements DietApi {

    @Autowired
    private DietService dietService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ApiUtils apiUtils;

    @PostConstruct
    public void init() {
        System.out.println("DietController initialized");
    }

    @Override
    public ResponseEntity<List<ListDietResponse>> listDiets(
            String accept,
            String contentType,
            String xRequestID,
            String userAgent,
            UUID xCorrelationID) {

        apiUtils.validateApiKeyFromRequest(request.getHeader("X-API-Key"));

        System.out.println("listDiets called");
        return ResponseEntity.ok(dietService.listDiets());
    }
}
