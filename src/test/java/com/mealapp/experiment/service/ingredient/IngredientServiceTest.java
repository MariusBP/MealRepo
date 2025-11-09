package com.mealapp.experiment.service.ingredient;

import com.mealapp.experiment.model.Ingredient;
import com.mealapp.experiment.repository.IngredientRepository;
import com.mealapp.experiment.service.utils.ServiceMapper;
import com.mealapp.openapi.ingredient.model.ListIngredientResponse;
import com.mealapp.openapi.ingredient.model.ReadIngredientResponse;
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
class IngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private ServiceMapper mapper;

    @InjectMocks
    private IngredientServiceImp ingredientService;

    private Ingredient ingredient;
    private ReadIngredientResponse readResponse;
    private ListIngredientResponse listResponse;

    @BeforeEach
    void setUp() {
        ingredient = buildIngredient();
        readResponse = buildReadResponse();
        listResponse = buildListResponse();
    }

    @Test
    void createIngredient_Success() {
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(ingredient);
        when(mapper.ingredientToReadIngredientResponse(any(Ingredient.class))).thenReturn(readResponse);

        ReadIngredientResponse result = ingredientService.createIngredient(ingredient);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Milk");

        verify(ingredientRepository).save(ingredient);
        verify(mapper).ingredientToReadIngredientResponse(ingredient);
    }

    @Test
    void getIngredient_Success() {
        when(ingredientRepository.findIngredientById(1L)).thenReturn(Optional.of(ingredient));
        when(mapper.ingredientToReadIngredientResponse(any(Ingredient.class))).thenReturn(readResponse);

        ReadIngredientResponse result = ingredientService.getIngredient(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Milk");

        verify(ingredientRepository).findIngredientById(1L);
        verify(mapper).ingredientToReadIngredientResponse(ingredient);
    }

    @Test
    void listIngredients_Success() {
        List<Ingredient> ingredients = Arrays.asList(ingredient, buildIngredientTwo());
        List<ListIngredientResponse> responses = Arrays.asList(listResponse, buildListResponseTwo());

        when(ingredientRepository.findAll()).thenReturn(ingredients);
        when(mapper.ingredientToListIngredientResponse(ingredients)).thenReturn(responses);

        List<ListIngredientResponse> result = ingredientService.listIngredients();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Milk");
        assertThat(result.get(1).getName()).isEqualTo("Bacon");

        verify(ingredientRepository).findAll();
        verify(mapper).ingredientToListIngredientResponse(ingredients);
    }

    @Test
    void listIngredients_EmptyList() {
        when(ingredientRepository.findAll()).thenReturn(Collections.emptyList());
        when(mapper.ingredientToListIngredientResponse(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<ListIngredientResponse> result = ingredientService.listIngredients();

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(ingredientRepository).findAll();
    }

    @Test
    void createIngredient_RepositoryException_ThrowsException() {
        Ingredient newIngredient = buildIngredient();
        newIngredient.setKcal(100.0);

        when(ingredientRepository.save(any(Ingredient.class)))
                .thenThrow(new RuntimeException("Database connection failed"));

        assertThatThrownBy(() -> ingredientService.createIngredient(newIngredient))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Database connection failed");

        verify(ingredientRepository).save(newIngredient);
        verify(mapper, never()).ingredientToReadIngredientResponse(any());
    }

    @Test
    void getIngredient_NotFound_ThrowsException() {
        when(ingredientRepository.findIngredientById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ingredientService.getIngredient(999L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Not found ingredient with id: 999");

        verify(ingredientRepository).findIngredientById(999L);
        verify(mapper, never()).ingredientToReadIngredientResponse(any());
    }

    private Ingredient buildIngredient() {
        return Ingredient.builder()
                .id(1L)
                .name("Milk")
                .kcal(50.0)
                .carb(10.0)
                .protein(5.0)
                .fat(2.0)
                .fiber(1.0)
                .sodium(10.0)
                .build();
    }

    private Ingredient buildIngredientTwo() {
        return Ingredient.builder()
                .id(2L)
                .name("Bacon")
                .kcal(150.0)
                .carb(20.0)
                .protein(10.0)
                .fat(30.0)
                .fiber(0.0)
                .sodium(50.0)
                .build();
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

    private ListIngredientResponse buildListResponseTwo() {
        ListIngredientResponse response = new ListIngredientResponse();
        response.setId(2L);
        response.setName("Bacon");
        return response;
    }
}
