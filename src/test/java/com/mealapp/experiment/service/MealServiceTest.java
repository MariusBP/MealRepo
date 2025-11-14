package com.mealapp.experiment.service;

import com.mealapp.experiment.model.Diet;
import com.mealapp.experiment.model.Meal;
import com.mealapp.experiment.repository.MealRepository;
import com.mealapp.experiment.service.meal.MealServiceImpl;
import com.mealapp.experiment.service.utils.ServiceMapper;
import com.mealapp.openapi.meal.model.ListMealResponse;
import com.mealapp.openapi.meal.model.ReadMealResponse;
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
class MealServiceTest {

    @Mock
    private MealRepository mealRepository;

    @Mock
    private ServiceMapper mapper;

    @InjectMocks
    private MealServiceImpl mealService;

    private Meal meal;
    private ReadMealResponse readResponse;
    private ListMealResponse listResponse;

    @BeforeEach
    void setUp() {
        meal = buildMeal();
        readResponse = buildReadResponse();
        listResponse = buildListResponse();
    }

    @Test
    void getMeal_Success() {
        when(mealRepository.findMealById(1L)).thenReturn(Optional.of(meal));
        when(mapper.mealToReadMealResponse(any(Meal.class))).thenReturn(readResponse);

        ReadMealResponse result = mealService.getMeal(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Grilled Chicken Salad");

        verify(mealRepository).findMealById(1L);
        verify(mapper).mealToReadMealResponse(meal);
    }

    @Test
    void listMeals_Success() {
        List<Meal> meals = Arrays.asList(meal, buildMealTwo());
        List<ListMealResponse> responses = Arrays.asList(listResponse, buildListResponseTwo());

        when(mealRepository.findByDietIdAndCategoriesIds(1L, Arrays.asList(1L, 2L), 2)).thenReturn(meals);
        when(mapper.mealToListMealResponse(meals)).thenReturn(responses);

        List<ListMealResponse> result = mealService.listMeals(1L, Arrays.asList(1L, 2L));

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Grilled Chicken Salad");
        assertThat(result.get(1).getName()).isEqualTo("Pasta Carbonara");

        verify(mealRepository).findByDietIdAndCategoriesIds(1L, Arrays.asList(1L, 2L), 2);
        verify(mapper).mealToListMealResponse(meals);
    }

    @Test
    void listMeals_EmptyList() {
        when(mealRepository.findByDietIdAndCategoriesIds(1L, Collections.emptyList(), 0)).thenReturn(Collections.emptyList());
        when(mapper.mealToListMealResponse(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<ListMealResponse> result = mealService.listMeals(1L, Collections.emptyList());

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(mealRepository).findByDietIdAndCategoriesIds(1L, Collections.emptyList(), 0);
    }

    @Test
    void createMeal_Success() {
        when(mealRepository.save(any(Meal.class))).thenReturn(meal);
        when(mealRepository.findMealById(1L)).thenReturn(Optional.of(meal));
        when(mapper.mealToReadMealResponse(any(Meal.class))).thenReturn(readResponse);

        ReadMealResponse result = mealService.createMeal(meal);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Grilled Chicken Salad");

        verify(mealRepository).save(meal);
        verify(mealRepository).findMealById(1L);
        verify(mapper).mealToReadMealResponse(meal);
    }

    @Test
    void updateMeal_Success() {
        Meal updatedMeal = buildMeal();
        updatedMeal.setName("Updated Meal Name");

        when(mealRepository.findMealById(1L)).thenReturn(Optional.of(meal));
        when(mapper.mealToReadMealResponse(any(Meal.class))).thenReturn(readResponse);

        ReadMealResponse result = mealService.updateMeal(1L, updatedMeal);

        assertThat(result).isNotNull();

        verify(mealRepository, times(2)).findMealById(1L);
        verify(mapper).merge(updatedMeal, meal);
        verify(mapper).mealToReadMealResponse(meal);
    }

    @Test
    void getMeal_NotFound_ThrowsException() {
        when(mealRepository.findMealById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mealService.getMeal(999L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Did not find meal with id: 999");

        verify(mealRepository).findMealById(999L);
        verify(mapper, never()).mealToReadMealResponse(any());
    }

    @Test
    void updateMeal_NotFound_ThrowsException() {
        when(mealRepository.findMealById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mealService.updateMeal(999L, meal))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Did not find meal with id: 999");

        verify(mealRepository).findMealById(999L);
        verify(mapper, never()).merge(any(Meal.class), any(Meal.class));
    }

    private Meal buildMeal() {
        Diet diet = new Diet();
        diet.setId(1L);
        diet.setName("Vegetarian");

        return Meal.builder()
                .id(1L)
                .name("Grilled Chicken Salad")
                .description("Healthy grilled chicken with fresh vegetables")
                .recipe("Grill chicken, mix with vegetables")
                .prepTime(30)
                .calories(450)
                .servings(4)
                .diet(diet)
                .build();
    }

    private Meal buildMealTwo() {
        Diet diet = new Diet();
        diet.setId(1L);
        diet.setName("Vegetarian");

        return Meal.builder()
                .id(2L)
                .name("Pasta Carbonara")
                .description("Classic Italian pasta dish")
                .recipe("Cook pasta, add sauce")
                .prepTime(20)
                .calories(600)
                .servings(2)
                .diet(diet)
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

    private ListMealResponse buildListResponseTwo() {
        ListMealResponse response = new ListMealResponse();
        response.setId(2L);
        response.setName("Pasta Carbonara");
        return response;
    }
}
