package com.mealapp.experiment.service;

import com.mealapp.experiment.model.Allergy;
import com.mealapp.experiment.repository.AllergyRepository;
import com.mealapp.experiment.service.allergy.AllergyServiceImpl;
import com.mealapp.experiment.service.utils.ServiceMapper;
import com.mealapp.openapi.allergy.model.ListAllergyResponse;
import com.mealapp.openapi.allergy.model.ReadAllergyResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AllergyServiceTest {

    private static final Long ALLERGY_ID_1 = 1L;
    private static final Long ALLERGY_ID_2 = 2L;
    private static final Long NON_EXISTENT_ALLERGY_ID = 999L;
    private static final String ALLERGY_NAME_1 = "Peanuts";
    private static final String ALLERGY_NAME_2 = "Shellfish";

    @Mock
    private AllergyRepository allergyRepository;

    @Mock
    private ServiceMapper mapper;

    @InjectMocks
    private AllergyServiceImpl allergyService;

    private Allergy allergy;
    private ReadAllergyResponse readResponse;
    private ListAllergyResponse listResponse;

    @BeforeEach
    void setUp() {
        allergy = buildAllergy();
        readResponse = buildReadResponse();
        listResponse = buildListResponse();
    }

    @Test
    void createAllergy_Success() {
        when(allergyRepository.findByName(ALLERGY_NAME_1)).thenReturn(Optional.empty());
        when(allergyRepository.save(any(Allergy.class))).thenReturn(allergy);
        when(mapper.allergyToReadAllergyResponse(any(Allergy.class))).thenReturn(readResponse);

        ReadAllergyResponse result = allergyService.createAllergy(allergy);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(ALLERGY_ID_1);
        assertThat(result.getName()).isEqualTo(ALLERGY_NAME_1);

        verify(allergyRepository).findByName(ALLERGY_NAME_1);
        verify(allergyRepository).save(allergy);
        verify(mapper).allergyToReadAllergyResponse(allergy);
    }

    @Test
    void getAllergy_Success() {
        when(allergyRepository.findById(ALLERGY_ID_1)).thenReturn(Optional.of(allergy));
        when(mapper.allergyToReadAllergyResponse(any(Allergy.class))).thenReturn(readResponse);

        ReadAllergyResponse result = allergyService.getAllergy(ALLERGY_ID_1);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(ALLERGY_ID_1);
        assertThat(result.getName()).isEqualTo(ALLERGY_NAME_1);

        verify(allergyRepository).findById(ALLERGY_ID_1);
        verify(mapper).allergyToReadAllergyResponse(allergy);
    }

    @Test
    void listAllergies_Success() {
        List<Allergy> allergies = Arrays.asList(allergy, buildAllergyTwo());
        List<ListAllergyResponse> responses = Arrays.asList(listResponse, buildListResponseTwo());

        when(allergyRepository.findAll()).thenReturn(allergies);
        when(mapper.allergyToListAllergyResponse(allergies)).thenReturn(responses);

        List<ListAllergyResponse> result = allergyService.listAllergies();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo(ALLERGY_NAME_1);
        assertThat(result.get(1).getName()).isEqualTo(ALLERGY_NAME_2);

        verify(allergyRepository).findAll();
        verify(mapper).allergyToListAllergyResponse(allergies);
    }

    @Test
    void listAllergies_ReturnsEmptyList() {
        when(allergyRepository.findAll()).thenReturn(Collections.emptyList());
        when(mapper.allergyToListAllergyResponse(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<ListAllergyResponse> result = allergyService.listAllergies();

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(allergyRepository).findAll();
    }

    @Test
    void getAllergy_WhenAllergyNotFound_ThrowsException() {
        when(allergyRepository.findById(NON_EXISTENT_ALLERGY_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> allergyService.getAllergy(NON_EXISTENT_ALLERGY_ID))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Did not find allergy with id: " + NON_EXISTENT_ALLERGY_ID);

        verify(allergyRepository).findById(NON_EXISTENT_ALLERGY_ID);
        verify(mapper, never()).allergyToReadAllergyResponse(any());
    }

    @Test
    void createAllergy_WithDuplicateName_ThrowsException() {
        when(allergyRepository.findByName(ALLERGY_NAME_1)).thenReturn(Optional.of(allergy));

        assertThatThrownBy(() -> allergyService.createAllergy(allergy))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Allergy with name '" + ALLERGY_NAME_1 + "' already exists");

        verify(allergyRepository).findByName(ALLERGY_NAME_1);
        verify(allergyRepository, never()).save(any());
        verify(mapper, never()).allergyToReadAllergyResponse(any());
    }

    private Allergy buildAllergy() {
        return Allergy.builder()
                .id(ALLERGY_ID_1)
                .name(ALLERGY_NAME_1)
                .build();
    }

    private Allergy buildAllergyTwo() {
        return Allergy.builder()
                .id(ALLERGY_ID_2)
                .name(ALLERGY_NAME_2)
                .build();
    }

    private ReadAllergyResponse buildReadResponse() {
        ReadAllergyResponse response = new ReadAllergyResponse();
        response.setId(ALLERGY_ID_1);
        response.setName(ALLERGY_NAME_1);
        return response;
    }

    private ListAllergyResponse buildListResponse() {
        ListAllergyResponse response = new ListAllergyResponse();
        response.setId(ALLERGY_ID_1);
        response.setName(ALLERGY_NAME_1);
        return response;
    }

    private ListAllergyResponse buildListResponseTwo() {
        ListAllergyResponse response = new ListAllergyResponse();
        response.setId(ALLERGY_ID_2);
        response.setName(ALLERGY_NAME_2);
        return response;
    }
}

