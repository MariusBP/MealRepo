package com.mealapp.experiment.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealapp.experiment.interceptor.ApiKeyInterceptor;
import com.mealapp.experiment.model.Allergy;
import com.mealapp.experiment.repository.AllergyRepository;
import com.mealapp.openapi.allergy.model.CreateAllergyRequest;
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
class AllergyIntegrationTest {

    private static final String ALLERGY_ENDPOINT = "/api/allergy";
    private static final String ALLERGIES_LIST_ENDPOINT = "/api/allergies";
    private static final Long NON_EXISTENT_ID = 999999L;

    @MockitoBean
    private ApiKeyInterceptor apiKeyInterceptor;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AllergyRepository allergyRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() throws Exception {
        when(apiKeyInterceptor.preHandle(any(), any(), any())).thenReturn(true);
        allergyRepository.deleteAll();
    }

    @Test
    void createAllergy_Ok() throws Exception {
        mockMvc.perform(post(ALLERGY_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(buildCreateAllergyRequest())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Peanuts"));
    }

    @Test
    void getAllergy_Ok() throws Exception {
        Allergy savedAllergy = allergyRepository.save(buildTestAllergy("Peanuts"));
        Long allergyId = savedAllergy.getId();

        mockMvc.perform(get(ALLERGY_ENDPOINT)
                        .param("id", allergyId.toString())
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(allergyId))
                .andExpect(jsonPath("$.name").value("Peanuts"));
    }

    @Test
    void listAllergies_Ok() throws Exception {
        allergyRepository.save(buildTestAllergy("Peanuts"));
        allergyRepository.save(buildTestAllergy("Shellfish"));

        mockMvc.perform(get(ALLERGIES_LIST_ENDPOINT)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void listAllergies_ReturnsEmptyList() throws Exception {
        mockMvc.perform(get(ALLERGIES_LIST_ENDPOINT)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void createAllergy_DuplicateName_ThrowsException() throws Exception {
        allergyRepository.save(buildTestAllergy("Peanuts"));

        mockMvc.perform(post(ALLERGY_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(buildCreateAllergyRequest())))
                .andExpect(status().isConflict());
    }

    @Test
    void createAllergy_MissingRequiredField_ThrowsException() throws Exception {
        CreateAllergyRequest invalidRequest = new CreateAllergyRequest();

        mockMvc.perform(post(ALLERGY_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllergy_NonExistentId_ThrowsException() throws Exception {
        mockMvc.perform(get(ALLERGY_ENDPOINT)
                        .param("id", NON_EXISTENT_ID.toString())
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void listAllergies_InvalidApiKey_ReturnsUnauthorized() throws Exception {
        reset(apiKeyInterceptor);
        when(apiKeyInterceptor.preHandle(any(), any(), any())).thenThrow(
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid API key")
        );

        mockMvc.perform(get(ALLERGIES_LIST_ENDPOINT)
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                        .header("X-API-Key", "invalid-api-key-123"))
                .andExpect(status().isUnauthorized());
    }

    private Allergy buildTestAllergy(String name) {
        return Allergy.builder()
                .name(name)
                .build();
    }

    private CreateAllergyRequest buildCreateAllergyRequest() {
        CreateAllergyRequest request = new CreateAllergyRequest();
        request.setName("Peanuts");
        return request;
    }
}

