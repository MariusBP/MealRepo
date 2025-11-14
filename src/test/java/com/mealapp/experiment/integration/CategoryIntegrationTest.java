package com.mealapp.experiment.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealapp.experiment.interceptor.ApiKeyInterceptor;
import com.mealapp.experiment.model.Category;
import com.mealapp.experiment.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CategoryIntegrationTest {

    @MockitoBean
    private ApiKeyInterceptor apiKeyInterceptor;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() throws Exception {
        when(apiKeyInterceptor.preHandle(any(), any(), any())).thenReturn(true);
        categoryRepository.deleteAll();
    }

    @Test
    void createCategory_Ok() throws Exception {
        mockMvc.perform(post("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(category())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Breakfast"))
                .andExpect(jsonPath("$.description").value("Morning meals"));
    }

    @Test
    void listCategories_Ok() throws Exception {
        categoryRepository.save(categoryTwo());
        categoryRepository.save(categoryThree());
        categoryRepository.save(categoryFour());

        mockMvc.perform(get("/api/category")
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[1].name").exists())
                .andExpect(jsonPath("$[2].name").exists());
    }

    @Test
    void listCategories_EmptyList_ReturnsEmptyArray() throws Exception {
        mockMvc.perform(get("/api/category")
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void createCategory_MissingRequiredField_ThrowsException() throws Exception {
        mockMvc.perform(post("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(categoryInvalid())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listCategories_InvalidApiKey_ThrowsException() throws Exception {
        reset(apiKeyInterceptor);
        when(apiKeyInterceptor.preHandle(any(), any(), any())).thenThrow(
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid API key")
        );
        mockMvc.perform(get("/api/category")
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                        .header("X-API-Key", "invalid-api-key-123"))
                .andExpect(status().isUnauthorized());
    }

    private Category category() {
        return Category.builder()
                .name("Breakfast")
                .description("Morning meals")
                .build();
    }

    private Category categoryTwo() {
        return Category.builder()
                .name("Lunch")
                .description("Midday meals")
                .build();
    }

    private Category categoryThree() {
        return Category.builder()
                .name("Dinner")
                .description("Evening meals")
                .build();
    }

    private Category categoryFour() {
        return Category.builder()
                .name("Snacks")
                .description("Light meals between main meals")
                .build();
    }

    private Category categoryInvalid() {
        return Category.builder()
                .description("Missing name field")
                .build();
    }
}
