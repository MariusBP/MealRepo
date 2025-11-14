package com.mealapp.experiment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealapp.experiment.controller.utils.ControllerMapper;
import com.mealapp.experiment.interceptor.ApiKeyInterceptor;
import com.mealapp.experiment.model.Allergy;
import com.mealapp.experiment.service.allergy.AllergyService;
import com.mealapp.openapi.allergy.model.CreateAllergyRequest;
import com.mealapp.openapi.allergy.model.ListAllergyResponse;
import com.mealapp.openapi.allergy.model.ReadAllergyResponse;
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

@WebMvcTest(AllergyController.class)
@ActiveProfiles("test")
class AllergyControllerTest {

    private static final String BASE_URL_PLURAL = "/api/allergies";
    private static final String BASE_URL_SINGULAR = "/api/allergy";
    private static final Long ALLERGY_ID = 1L;
    private static final Long NON_EXISTENT_ALLERGY_ID = 999L;
    private static final String ALLERGY_NAME = "Peanuts";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AllergyService allergyService;

    @MockitoBean
    private ApiKeyInterceptor apiKeyInterceptor;

    @MockitoBean
    private ControllerMapper controllerMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateAllergyRequest createRequest;
    private ReadAllergyResponse readResponse;
    private ListAllergyResponse listResponse;
    private Allergy allergy;

    @BeforeEach
    void setUp() throws Exception {
        createRequest = buildCreateAllergy();
        allergy = buildAllergy();
        readResponse = buildReadResponse();
        listResponse = buildListResponse();

        when(apiKeyInterceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    @Test
    void createAllergy_Ok() throws Exception {
        when(controllerMapper.createAllergyRequestToAllergy(any(CreateAllergyRequest.class)))
                .thenReturn(allergy);
        when(allergyService.createAllergy(any(Allergy.class))).thenReturn(readResponse);

        mockMvc.perform(post(BASE_URL_SINGULAR)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ALLERGY_ID))
                .andExpect(jsonPath("$.name").value(ALLERGY_NAME));

        verify(controllerMapper).createAllergyRequestToAllergy(any(CreateAllergyRequest.class));
        verify(allergyService).createAllergy(any(Allergy.class));
    }

    @Test
    void getAllergy_Ok() throws Exception {
        when(allergyService.getAllergy(ALLERGY_ID)).thenReturn(readResponse);

        mockMvc.perform(get(BASE_URL_SINGULAR)
                        .param("id", ALLERGY_ID.toString())
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ALLERGY_ID))
                .andExpect(jsonPath("$.name").value(ALLERGY_NAME));

        verify(allergyService).getAllergy(ALLERGY_ID);
    }

    @Test
    void listAllergies_Ok() throws Exception {
        List<ListAllergyResponse> allergies = Collections.singletonList(listResponse);
        when(allergyService.listAllergies()).thenReturn(allergies);

        mockMvc.perform(get(BASE_URL_PLURAL)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(ALLERGY_ID))
                .andExpect(jsonPath("$[0].name").value(ALLERGY_NAME));

        verify(allergyService).listAllergies();
    }

    @Test
    void listAllergies_ReturnsEmptyList() throws Exception {
        when(allergyService.listAllergies()).thenReturn(List.of());

        mockMvc.perform(get(BASE_URL_PLURAL)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void createAllergy_WithInvalidJson_ThrowsException() throws Exception {
        mockMvc.perform(post(BASE_URL_SINGULAR)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .content("invalid json"))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(allergyService, never()).createAllergy(any());
    }

    @Test
    void getAllergy_WhenAllergyNotFound_ThrowsException() throws Exception {
        when(allergyService.getAllergy(NON_EXISTENT_ALLERGY_ID))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Allergy not found"));

        mockMvc.perform(get(BASE_URL_SINGULAR)
                        .param("id", NON_EXISTENT_ALLERGY_ID.toString())
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(allergyService).getAllergy(NON_EXISTENT_ALLERGY_ID);
    }

    private Allergy buildAllergy() {
        return Allergy.builder()
                .id(ALLERGY_ID)
                .name(ALLERGY_NAME)
                .build();
    }

    private CreateAllergyRequest buildCreateAllergy() {
        CreateAllergyRequest request = new CreateAllergyRequest();
        request.setName(ALLERGY_NAME);
        return request;
    }

    private ReadAllergyResponse buildReadResponse() {
        ReadAllergyResponse response = new ReadAllergyResponse();
        response.setId(ALLERGY_ID);
        response.setName(ALLERGY_NAME);
        return response;
    }

    private ListAllergyResponse buildListResponse() {
        ListAllergyResponse response = new ListAllergyResponse();
        response.setId(ALLERGY_ID);
        response.setName(ALLERGY_NAME);
        return response;
    }
}

