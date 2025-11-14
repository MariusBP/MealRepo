package com.mealapp.experiment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealapp.experiment.controller.utils.ControllerMapper;
import com.mealapp.experiment.interceptor.ApiKeyInterceptor;
import com.mealapp.experiment.model.Category;
import com.mealapp.experiment.service.category.CategoryService;
import com.mealapp.openapi.category.model.CategoryResponse;
import com.mealapp.openapi.category.model.CreateCategoryRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(CategoryController.class)
@ActiveProfiles("test")
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private ApiKeyInterceptor apiKeyInterceptor;

    @MockitoBean
    private ControllerMapper controllerMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateCategoryRequest createRequest;
    private CategoryResponse categoryResponse;
    private Category category;

    private static final String BASE_URL = "/api/category";

    @BeforeEach
    void setUp() throws Exception {
        createRequest = buildCreateCategory();
        category = buildCategory();
        categoryResponse = buildCategoryResponse();

        when(apiKeyInterceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    @Test
    void createCategory_Success() throws Exception {
        when(controllerMapper.createCategoryRequestToCategory(any(CreateCategoryRequest.class)))
                .thenReturn(category);
        when(categoryService.createCategory(any(Category.class))).thenReturn(categoryResponse);

        mockMvc.perform(post(BASE_URL)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .content(objectMapper.writeValueAsString(createRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Breakfast"))
                .andExpect(jsonPath("$.description").value("Morning meals"));

        verify(controllerMapper).createCategoryRequestToCategory(any(CreateCategoryRequest.class));
        verify(categoryService).createCategory(any(Category.class));
    }

    @Test
    void listCategories_Success() throws Exception {
        List<CategoryResponse> categories = Collections.singletonList(categoryResponse);
        when(categoryService.listCategories()).thenReturn(categories);

        mockMvc.perform(get(BASE_URL)
                .header("Accept", "application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Breakfast"))
                .andExpect(jsonPath("$[0].description").value("Morning meals"));

        verify(categoryService).listCategories();
    }

    @Test
    void listCategories_EmptyList() throws Exception {
        when(categoryService.listCategories()).thenReturn(List.of());

        mockMvc.perform(get(BASE_URL)
                .header("Accept", "application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void createCategory_InvalidRequestBody() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .content("invalid json"))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(categoryService, never()).createCategory(any());
    }

    private Category buildCategory() {
        return Category.builder()
                .id(1L)
                .name("Breakfast")
                .description("Morning meals")
                .build();
    }

    private CreateCategoryRequest buildCreateCategory() {
        CreateCategoryRequest request = new CreateCategoryRequest();
        request.setName("Breakfast");
        request.setDescription("Morning meals");
        return request;
    }

    private CategoryResponse buildCategoryResponse() {
        CategoryResponse response = new CategoryResponse();
        response.setId(1L);
        response.setName("Breakfast");
        response.setDescription("Morning meals");
        return response;
    }
}
