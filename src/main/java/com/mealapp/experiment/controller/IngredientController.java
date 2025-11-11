package com.mealapp.experiment.controller;

import com.mealapp.experiment.controller.utils.ApiUtils;
import com.mealapp.experiment.controller.utils.ControllerMapper;
import com.mealapp.experiment.service.ingredient.IngredientService;
import com.mealapp.openapi.ingredient.api.IngredientApi;
import com.mealapp.openapi.ingredient.model.CreateIngredientRequest;
import com.mealapp.openapi.ingredient.model.ListIngredientResponse;
import com.mealapp.openapi.ingredient.model.ReadIngredientResponse;
import com.mealapp.openapi.ingredient.model.UpdateIngredientRequest;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class IngredientController implements IngredientApi {

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ApiUtils apiUtils;

    @Autowired
    private ControllerMapper mapper;

    @PostConstruct
    public void init() {
        System.out.println("IngredientController initialized");
    }

    @Override
    public ResponseEntity<ReadIngredientResponse> createIngredient(
            String accept,
            CreateIngredientRequest createIngredientRequest,
            String contentType,
            String xRequestID,
            String userAgent) {

        apiUtils.validateApiKeyFromRequest(request.getHeader("X-API-Key"));

        ReadIngredientResponse readIngredientResponse =
                ingredientService.createIngredient(mapper.createIngredientRequestToIngredient(createIngredientRequest));

        System.out.println("createIngredient called.");
        return ResponseEntity.ok(readIngredientResponse);
    }

    @Override
    public ResponseEntity<ReadIngredientResponse> getIngredient(
            String accept,
            Long id,
            String contentType,
            String xRequestID,
            String userAgent) {

        apiUtils.validateApiKeyFromRequest(request.getHeader("X-API-Key"));

        System.out.println("getIngredient called.");
        return ResponseEntity.ok(ingredientService.getIngredient(id));
    }

    @Override
    public ResponseEntity<List<ListIngredientResponse>> listIngredients(
            String accept,
            String contentType,
            String xRequestID,
            String userAgent) {

        apiUtils.validateApiKeyFromRequest(request.getHeader("X-API-Key"));

        System.out.println("listIngredients called.");
        return ResponseEntity.ok(ingredientService.listIngredients());
    }

    //TODO: implement deleteIngredient
    //TODO: complete implementation of updateIngredient
    @Override
    public ResponseEntity<ReadIngredientResponse> updateIngredient(
            String accept,
            Long id,
            UpdateIngredientRequest updateIngredientRequest,
            String contentType,
            String xRequestID,
            String userAgent) {

        apiUtils.validateApiKeyFromRequest(request.getHeader("X-API-Key"));

        ReadIngredientResponse readIngredientResponse =
                ingredientService.updateIngredient(id, mapper.updateIngredientRequestToIngredient(updateIngredientRequest));

        System.out.println("updateIngredient called.");
        return ResponseEntity.ok(readIngredientResponse);
    }
}
