package com.mealapp.experiment.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealapp.experiment.interceptor.ApiKeyInterceptor;
import com.mealapp.experiment.model.Ingredient;
import com.mealapp.experiment.repository.IngredientRepository;
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
class IngredientIntegrationTest {

    @MockitoBean
    private ApiKeyInterceptor apiKeyInterceptor;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() throws Exception {
        when(apiKeyInterceptor.preHandle(any(), any(), any())).thenReturn(true);
        ingredientRepository.deleteAll();
    }

    @Test
    void createIngredient_Ok() throws Exception {
        mockMvc.perform(post("/api/ingredient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(ingredient())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Flour"));
    }

    @Test
    void listIngredients_Ok() throws Exception {
        ingredientRepository.save(ingredientTwo());
        ingredientRepository.save(ingredientThree());
        ingredientRepository.save(ingredientFour());

        mockMvc.perform(get("/api/ingredients")
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
    void getIngredient_Ok() throws Exception {
        Ingredient savedIngredient = ingredientRepository.save(ingredientThree());
        Long ingredientId = savedIngredient.getId();

        mockMvc.perform(get("/api/ingredient")
                        .param("id", ingredientId.toString())
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ingredientId))
                .andExpect(jsonPath("$.name").value("Milk"))
                .andExpect(jsonPath("$.kcal").value(50.0))
                .andExpect(jsonPath("$.carb").value(10.0))
                .andExpect(jsonPath("$.protein").value(5.0))
                .andExpect(jsonPath("$.fat").value(2.0))
                .andExpect(jsonPath("$.fiber").value(1.0))
                .andExpect(jsonPath("$.sodium").value(10.0));
    }

    @Test
    void getIngredient_NotFound_ThrowsException() throws Exception {
        mockMvc.perform(get("/api/ingredient")
                        .param("id", "9999")
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void createIngredient_MissingRequiredField_ThrowsException() throws Exception {
        mockMvc.perform(post("/api/ingredient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(ingredientInvalid())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listIngredients_InvalidApiKey_ThrowsException() throws Exception {
        reset(apiKeyInterceptor);
        when(apiKeyInterceptor.preHandle(any(), any(), any())).thenThrow(
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid API key")
        );
        mockMvc.perform(get("/api/ingredients")
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                        .header("X-API-Key", "invalid-api-key-123"))
                .andExpect(status().isUnauthorized());
    }

    private Ingredient ingredient() {
        return Ingredient.builder()
                .name("Flour")
                .kcal(100.0)
                .carb(20.0)
                .protein(5.0)
                .fat(2.0)
                .fiber(1.0)
                .sodium(50.0)
                .build();
    }

    private Ingredient ingredientTwo() {
        return Ingredient.builder()
                .name("Bacon")
                .kcal(150.0)
                .carb(20.0)
                .protein(10.0)
                .fat(30.0)
                .fiber(0.0)
                .sodium(50.0)
                .build();
    }

    private Ingredient ingredientThree() {
        return Ingredient.builder()
                .name("Milk")
                .kcal(50.0)
                .carb(10.0)
                .protein(5.0)
                .fat(2.0)
                .fiber(1.0)
                .sodium(10.0)
                .build();
    }

    private Ingredient ingredientFour() {
        return Ingredient.builder()
                .name("Spinach")
                .kcal(10.0)
                .carb(0.0)
                .protein(0.0)
                .fat(0.0)
                .fiber(5.0)
                .sodium(0.0)
                .build();
    }

    private Ingredient ingredientInvalid() {
        return Ingredient.builder()
                .kcal(70.0)
                .carb(1.0)
                .protein(6.0)
                .fat(5.0)
                .build();
    }
}
