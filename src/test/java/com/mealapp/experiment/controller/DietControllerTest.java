package com.mealapp.experiment.controller;

import com.mealapp.experiment.interceptor.ApiKeyInterceptor;
import com.mealapp.experiment.service.diet.DietService;
import com.mealapp.openapi.diet.model.ListDietResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DietController.class)
@ActiveProfiles("test")
class DietControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DietService dietService;

    @MockitoBean
    private ApiKeyInterceptor apiKeyInterceptor;

    private ListDietResponse listResponse;

    @BeforeEach
    void setUp() throws Exception {
        listResponse = buildListResponse();
        when(apiKeyInterceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    @Test
    void listDiets_Success() throws Exception {
        List<ListDietResponse> responses = Arrays.asList(listResponse, buildListResponseTwo());
        when(dietService.listDiets()).thenReturn(responses);

        mockMvc.perform(get("/api/diets")
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Vegetarian"))
                .andExpect(jsonPath("$[1].name").value("Vegan"));

        verify(dietService).listDiets();
    }

    @Test
    void listDiets_EmptyList() throws Exception {
        when(dietService.listDiets()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/diets")
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(dietService).listDiets();
    }

    private ListDietResponse buildListResponse() {
        ListDietResponse response = new ListDietResponse();
        response.setId(1L);
        response.setName("Vegetarian");
        response.setDescription("A diet that excludes meat");
        return response;
    }

    private ListDietResponse buildListResponseTwo() {
        ListDietResponse response = new ListDietResponse();
        response.setId(2L);
        response.setName("Vegan");
        response.setDescription("A diet that excludes all animal products");
        return response;
    }
}
