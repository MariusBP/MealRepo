package com.mealapp.experiment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealapp.experiment.controller.utils.ControllerMapper;
import com.mealapp.experiment.interceptor.ApiKeyInterceptor;
import com.mealapp.experiment.model.Ingredient;
import com.mealapp.experiment.service.ingredient.IngredientService;
import com.mealapp.openapi.ingredient.model.CreateIngredientRequest;
import com.mealapp.openapi.ingredient.model.ListIngredientResponse;
import com.mealapp.openapi.ingredient.model.ReadIngredientResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(IngredientController.class)
@ActiveProfiles("test")
class IngredientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IngredientService ingredientService;

    @MockitoBean
    private ApiKeyInterceptor apiKeyInterceptor;

    @MockitoBean
    private ControllerMapper controllerMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateIngredientRequest createRequest;
    private ReadIngredientResponse readResponse;
    private ListIngredientResponse listResponse;
    private Ingredient ingredient;

    private static final String BASE_URL_PLURAL = "/api/ingredients";
    private static final String BASE_URL_SINGULAR = "/api/ingredient";

    @BeforeEach
    void setUp() throws Exception {
        createRequest = buildCreateIngredient();
        ingredient = buildIngredient();
        readResponse = buildReadResponse();
        listResponse = buildListResponse();

        when(apiKeyInterceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    @Test
    void createIngredient_Success() throws Exception {
        when(controllerMapper.createIngredientRequestToIngredient(any(CreateIngredientRequest.class)))
                .thenReturn(ingredient);
        when(ingredientService.createIngredient(any(Ingredient.class))).thenReturn(readResponse);

        mockMvc.perform(post(BASE_URL_SINGULAR)
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Milk"));

        verify(controllerMapper).createIngredientRequestToIngredient(any(CreateIngredientRequest.class));
        verify(ingredientService).createIngredient(any(Ingredient.class));
    }

    @Test
    void getIngredient_Success() throws Exception {
        when(ingredientService.getIngredient(1L)).thenReturn(readResponse);

        mockMvc.perform(get(BASE_URL_SINGULAR)
                        .param("id", "1")
                        .header("Accept", "application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Milk"));

        verify(ingredientService).getIngredient(1L);
    }

    @Test
    void listIngredients_Success() throws Exception {
        List<ListIngredientResponse> ingredients = Collections.singletonList(listResponse);
        when(ingredientService.listIngredients()).thenReturn(ingredients);

        mockMvc.perform(get(BASE_URL_PLURAL)
                        .header("Accept", "application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Milk"));

        verify(ingredientService).listIngredients();
    }

    @Test
    void listIngredients_EmptyList() throws Exception {
        when(ingredientService.listIngredients()).thenReturn(List.of());

        mockMvc.perform(get(BASE_URL_PLURAL)
                        .header("Accept", "application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void createIngredient_InvalidRequestBody_ThrowsException() throws Exception {
        mockMvc.perform(post(BASE_URL_SINGULAR)
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        .content("invalid json"))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(ingredientService, never()).createIngredient(any());
    }

    @Test
    void getIngredient_NotFound_ThrowsException() throws Exception {
        when(ingredientService.getIngredient(999L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Ingredient not found"));

        mockMvc.perform(get(BASE_URL_SINGULAR)
                        .param("id", "999")
                        .header("Accept", "application/json"))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(ingredientService).getIngredient(999L);
    }

    private Ingredient buildIngredient() {
        return Ingredient.builder()
                .id(1L)
                .name("Milk")
                .kcal(100.0)
                .carb(20.0)
                .protein(5.0)
                .fat(2.0)
                .fiber(1.0)
                .sodium(50.0)
                .build();
    }

    private CreateIngredientRequest buildCreateIngredient() {
        CreateIngredientRequest response = new CreateIngredientRequest();
        response.setName("Milk");
        return response;
    }

    private ReadIngredientResponse buildReadResponse() {
        ReadIngredientResponse response = new ReadIngredientResponse();
        response.setId(1L);
        response.setName("Milk");
        return response;
    }

    private ListIngredientResponse buildListResponse() {
        ListIngredientResponse response = new ListIngredientResponse();
        response.setId(1L);
        response.setName("Milk");
        return response;
    }
}
