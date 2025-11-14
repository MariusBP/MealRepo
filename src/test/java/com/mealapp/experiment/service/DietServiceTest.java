package com.mealapp.experiment.service;

import com.mealapp.experiment.model.Diet;
import com.mealapp.experiment.repository.DietRepository;
import com.mealapp.experiment.service.diet.DietServiceImpl;
import com.mealapp.experiment.service.utils.ServiceMapper;
import com.mealapp.openapi.diet.model.ListDietResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DietServiceTest {

    @Mock
    private DietRepository dietRepository;

    @Mock
    private ServiceMapper mapper;

    @InjectMocks
    private DietServiceImpl dietService;

    private Diet diet;
    private ListDietResponse listResponse;

    @BeforeEach
    void setUp() {
        diet = buildDiet();
        listResponse = buildListResponse();
    }

    @Test
    void listDiets_Success() {
        List<Diet> diets = Arrays.asList(diet, buildDietTwo());
        List<ListDietResponse> responses = Arrays.asList(listResponse, buildListResponseTwo());

        when(dietRepository.findAll()).thenReturn(diets);
        when(mapper.dietToListDietResponse(diets)).thenReturn(responses);

        List<ListDietResponse> result = dietService.listDiets();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Vegetarian");
        assertThat(result.get(1).getName()).isEqualTo("Vegan");

        verify(dietRepository).findAll();
        verify(mapper).dietToListDietResponse(diets);
    }

    @Test
    void listDiets_EmptyList() {
        when(dietRepository.findAll()).thenReturn(Collections.emptyList());
        when(mapper.dietToListDietResponse(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<ListDietResponse> result = dietService.listDiets();

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(dietRepository).findAll();
    }

    @Test
    void listDiets_RepositoryException_ThrowsException() {
        when(dietRepository.findAll()).thenThrow(new RuntimeException("Database connection failed"));

        try {
            dietService.listDiets();
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).contains("Database connection failed");
        }

        verify(dietRepository).findAll();
        verify(mapper, never()).dietToListDietResponse(any());
    }

    private Diet buildDiet() {
        Diet diet = new Diet();
        diet.setId(1L);
        diet.setName("Vegetarian");
        diet.setDescription("A diet that excludes meat");
        diet.setPicture("vegetarian.jpg");
        return diet;
    }

    private Diet buildDietTwo() {
        Diet diet = new Diet();
        diet.setId(2L);
        diet.setName("Vegan");
        diet.setDescription("A diet that excludes all animal products");
        diet.setPicture("vegan.jpg");
        return diet;
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

