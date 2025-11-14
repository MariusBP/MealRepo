package com.mealapp.experiment.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealapp.experiment.interceptor.ApiKeyInterceptor;
import com.mealapp.experiment.model.Diet;
import com.mealapp.experiment.model.Meal;
import com.mealapp.experiment.repository.DietRepository;
import com.mealapp.experiment.repository.MealRepository;
import com.mealapp.openapi.meal.model.CreateMealRequest;
import com.mealapp.openapi.meal.model.DietObject;
import com.mealapp.openapi.meal.model.UpdateMealRequest;
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
class MealIntegrationTest {

    private static final String MEAL_ENDPOINT = "/api/meal";
    private static final String MEALS_LIST_ENDPOINT = "/api/meals";
    private static final Long NON_EXISTENT_ID = 999999L;

    @MockitoBean
    private ApiKeyInterceptor apiKeyInterceptor;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private DietRepository dietRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Diet testDiet;

    @BeforeEach
    void setup() throws Exception {
        when(apiKeyInterceptor.preHandle(any(), any(), any())).thenReturn(true);
        mealRepository.deleteAll();
        dietRepository.deleteAll();
        testDiet = createTestDiet();
    }

    @Test
    void createMeal_Ok() throws Exception {
        mockMvc.perform(post(MEAL_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(buildCreateMealRequest())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Test Integration Meal"))
                .andExpect(jsonPath("$.description").value("Integration test meal"));
    }

    @Test
    void getMeal_Ok() throws Exception {
        Meal savedMeal = mealRepository.save(buildTestMeal("Test Meal One", "First test meal"));
        Long mealId = savedMeal.getId();

        mockMvc.perform(get(MEAL_ENDPOINT)
                        .param("id", mealId.toString())
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(mealId))
                .andExpect(jsonPath("$.name").value("Test Meal One"));
    }

    @Test
    void updateMeal_Ok() throws Exception {
        Meal savedMeal = mealRepository.save(buildTestMeal("Test Meal", "Original description"));
        Long mealId = savedMeal.getId();

        mockMvc.perform(patch(MEAL_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(buildUpdateMealRequest(mealId))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(mealId));
    }

    @Test
    void listMeals_Ok() throws Exception {
        mealRepository.save(buildTestMeal("Test Meal One", "First test meal"));
        mealRepository.save(buildTestMeal("Test Meal Two", "Second test meal"));

        mockMvc.perform(get(MEALS_LIST_ENDPOINT)
                        .param("diet_id", testDiet.getId().toString())
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getMeal_NonExistentId_ThrowsException() throws Exception {
        mockMvc.perform(get(MEAL_ENDPOINT)
                        .param("id", NON_EXISTENT_ID.toString())
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void createMeal_MissingRequiredField_ThrowsException() throws Exception {
        mockMvc.perform(post(MEAL_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(buildInvalidMealRequest())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listMeals_InvalidApiKey_ThrowsException() throws Exception {
        reset(apiKeyInterceptor);
        when(apiKeyInterceptor.preHandle(any(), any(), any())).thenThrow(
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid API key")
        );

        mockMvc.perform(get(MEALS_LIST_ENDPOINT)
                        .param("diet_id", testDiet.getId().toString())
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                        .header("X-API-Key", "invalid-api-key-123"))
                .andExpect(status().isUnauthorized());
    }

    private Diet createTestDiet() {
        return dietRepository.save(Diet.builder()
                .name("Test Integration Diet")
                .description("Diet for integration testing")
                .picture("test.jpg")
                .build());
    }

    private DietObject buildDietObject() {
        DietObject dietObject = new DietObject();
        dietObject.setId(testDiet.getId());
        return dietObject;
    }

    private Meal buildTestMeal(String name, String description) {
        return Meal.builder()
                .name(name)
                .description(description)
                .recipe("Test recipe")
                .prepTime(30)
                .calories(400)
                .servings(4)
                .diet(testDiet)
                .build();
    }

    private CreateMealRequest buildCreateMealRequest() {
        CreateMealRequest request = new CreateMealRequest();
        request.setName("Test Integration Meal");
        request.setDescription("Integration test meal");
        request.setRecipe("Test recipe");
        request.setPrepTime(30);
        request.setCalories(400);
        request.setServings(4);
        request.setDiet(buildDietObject());
        return request;
    }

    private UpdateMealRequest buildUpdateMealRequest(Long mealId) {
        UpdateMealRequest request = new UpdateMealRequest();
        request.setId(mealId);
        request.setName("Updated Meal Name");
        request.setDescription("Updated description");
        request.setRecipe("Updated recipe");
        request.setPrepTime(45);
        request.setCalories(500);
        request.setServings(6);
        request.setDiets(buildDietObject());
        return request;
    }

    private CreateMealRequest buildInvalidMealRequest() {
        CreateMealRequest request = new CreateMealRequest();
        request.setDescription("Missing name field");
        request.setRecipe("Test recipe");
        return request;
    }
}
