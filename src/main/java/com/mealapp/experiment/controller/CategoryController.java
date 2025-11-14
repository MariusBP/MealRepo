package com.mealapp.experiment.controller;

import com.mealapp.experiment.controller.utils.ControllerMapper;
import com.mealapp.experiment.service.category.CategoryService;
import com.mealapp.openapi.category.api.CategoryApi;
import com.mealapp.openapi.category.model.CategoryResponse;
import com.mealapp.openapi.category.model.CreateCategoryRequest;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class CategoryController implements CategoryApi {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ControllerMapper mapper;

    @PostConstruct
    public void init() {
        System.out.println("CategoryController initialized");
    }

    @Override
    public ResponseEntity<CategoryResponse> createCategory(
            String accept, CreateCategoryRequest createCategoryRequest,
            String contentType, String xRequestID,
            String userAgent, UUID xCorrelationID) {

        CategoryResponse response = categoryService.createCategory(
                mapper.createCategoryRequestToCategory(createCategoryRequest));
        System.out.println("createCategory called.");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<CategoryResponse>> listCategories(
            String accept, String contentType,
            String xRequestID, String userAgent,
            UUID xCorrelationID) {

        System.out.println("listCategories called.");
        return ResponseEntity.ok(categoryService.listCategories());
    }
}
