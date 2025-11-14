package com.mealapp.experiment.controller;

import com.mealapp.experiment.controller.utils.ControllerMapper;
import com.mealapp.experiment.interceptor.ApiKeyInterceptor;
import com.mealapp.experiment.model.Diet;
import com.mealapp.experiment.model.Meal;
import com.mealapp.experiment.service.meal.MealService;
import com.mealapp.openapi.meal.model.CreateMealRequest;
import com.mealapp.openapi.meal.model.DietObject;
import com.mealapp.openapi.meal.model.ListMealResponse;
import com.mealapp.openapi.meal.model.ReadMealResponse;
import com.mealapp.openapi.meal.model.UpdateMealRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MealController.class)
@ActiveProfiles("test")
class MealControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MealService mealService;

    @MockitoBean
    private ControllerMapper controllerMapper;

    @MockitoBean
    private ApiKeyInterceptor apiKeyInterceptor;

    @Autowired
    private ObjectMapper objectMapper;

    private Meal meal;
    private ReadMealResponse readResponse;
    private ListMealResponse listResponse;
    private CreateMealRequest createRequest;
    private UpdateMealRequest updateRequest;

    @BeforeEach
    void setUp() throws Exception {
        meal = buildMeal();
        readResponse = buildReadResponse();
        listResponse = buildListResponse();
        createRequest = buildCreateRequest();
        updateRequest = buildUpdateRequest();

        when(apiKeyInterceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    @Test
    void getMeal_Success() throws Exception {
        when(mealService.getMeal(1L)).thenReturn(readResponse);

        mockMvc.perform(get("/api/meal")
                        .param("id", "1")
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Grilled Chicken Salad"));

        verify(mealService).getMeal(1L);
    }

    @Test
    void listMeals_Success() throws Exception {
        List<ListMealResponse> responses = List.of(listResponse);
        when(mealService.listMeals(any(Long.class), any())).thenReturn(responses);

        mockMvc.perform(get("/api/meals")
                        .param("diet_id", "1")
                        .param("category_id_list", "1", "2")
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Grilled Chicken Salad"));

        verify(mealService).listMeals(any(Long.class), any());
    }

    @Test
    void listMeals_EmptyList() throws Exception {
        when(mealService.listMeals(any(Long.class), any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/meals")
                        .param("diet_id", "1")
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(mealService).listMeals(any(Long.class), any());
    }

    @Test
    void createMeal_Success() throws Exception {
        when(controllerMapper.createMealRequestToEntity(any(CreateMealRequest.class))).thenReturn(meal);
        when(mealService.createMeal(any(Meal.class))).thenReturn(readResponse);

        mockMvc.perform(post("/api/meal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Grilled Chicken Salad"));

        verify(controllerMapper).createMealRequestToEntity(any(CreateMealRequest.class));
        verify(mealService).createMeal(any(Meal.class));
    }

    @Test
    void updateMeal_Success() throws Exception {
        when(controllerMapper.updateMealRequestToEntity(any(UpdateMealRequest.class))).thenReturn(meal);
        when(mealService.updateMeal(eq(1L), any(Meal.class))).thenReturn(readResponse);

        mockMvc.perform(patch("/api/meal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Grilled Chicken Salad"));

        verify(controllerMapper).updateMealRequestToEntity(any(UpdateMealRequest.class));
        verify(mealService).updateMeal(eq(1L), any(Meal.class));
    }

    @Test
    void getMeal_NotFound_ThrowsException() throws Exception {
        when(mealService.getMeal(999L))
                .thenThrow(new ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND,
                        "Meal not found"));

        mockMvc.perform(get("/api/meal")
                        .param("id", "999")
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());

        verify(mealService).getMeal(999L);
    }

    @Test
    void createMeal_InvalidJson_ThrowsException() throws Exception {
        mockMvc.perform(post("/api/meal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                        .content("invalid json"))
                .andExpect(status().isBadRequest());

        verify(mealService, never()).createMeal(any());
    }

    private Meal buildMeal() {
        Diet diet = new Diet();
        diet.setId(1L);
        diet.setName("Omnivore");

        return Meal.builder()
                .id(1L)
                .name("Grilled Chicken Salad")
                .description("Healthy grilled chicken with fresh vegetables")
                .recipe("Grill chicken, mix with vegetables")
                .prepTime(30)
                .calories(450)
                .servings(4)
                .diet(diet)
                .createdDate(LocalDate.now())
                .build();
    }

    private ReadMealResponse buildReadResponse() {
        ReadMealResponse response = new ReadMealResponse();
        response.setId(1L);
        response.setName("Grilled Chicken Salad");
        return response;
    }

    private ListMealResponse buildListResponse() {
        ListMealResponse response = new ListMealResponse();
        response.setId(1L);
        response.setName("Grilled Chicken Salad");
        return response;
    }

    private CreateMealRequest buildCreateRequest() {
        DietObject dietObject = new DietObject();
        dietObject.setId(1L);

        CreateMealRequest request = new CreateMealRequest();
        request.setName("Grilled Chicken Salad");
        request.setDescription("Healthy grilled chicken with fresh vegetables");
        request.setPrepTime(30);
        request.setCalories(450);
        request.setServings(4);
        request.setDiet(dietObject);
        return request;
    }

    private UpdateMealRequest buildUpdateRequest() {
        DietObject dietObject = new DietObject();
        dietObject.setId(1L);

        UpdateMealRequest request = new UpdateMealRequest();
        request.setId(1L);
        request.setName("Grilled Chicken Salad");
        request.setDescription("Healthy grilled chicken with fresh vegetables");
        request.setPrepTime(30);
        request.setCalories(450);
        request.setServings(4);
        request.setDiets(dietObject);
        return request;
    }
}
