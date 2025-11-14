package com.mealapp.experiment.service;

import com.mealapp.experiment.model.Category;
import com.mealapp.experiment.repository.CategoryRepository;
import com.mealapp.experiment.service.category.CategoryServiceImpl;
import com.mealapp.experiment.service.utils.ServiceMapper;
import com.mealapp.openapi.category.model.CategoryResponse;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ServiceMapper mapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryResponse categoryResponse;

    @BeforeEach
    void setUp() {
        category = buildCategory();
        categoryResponse = buildCategoryResponse();
    }

    @Test
    void createCategory_Success() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(mapper.categoryToCategoryResponse(any(Category.class))).thenReturn(categoryResponse);

        CategoryResponse result = categoryService.createCategory(category);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Breakfast");
        assertThat(result.getDescription()).isEqualTo("Morning meals");

        verify(categoryRepository).save(category);
        verify(mapper).categoryToCategoryResponse(category);
    }

    @Test
    void listCategories_Success() {
        List<Category> categories = Arrays.asList(category, buildCategoryTwo());
        List<CategoryResponse> responses = Arrays.asList(categoryResponse, buildCategoryResponseTwo());

        when(categoryRepository.findAll()).thenReturn(categories);
        when(mapper.categoryToListCategoryResponse(categories)).thenReturn(responses);

        List<CategoryResponse> result = categoryService.listCategories();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Breakfast");
        assertThat(result.get(0).getDescription()).isEqualTo("Morning meals");
        assertThat(result.get(1).getName()).isEqualTo("Lunch");
        assertThat(result.get(1).getDescription()).isEqualTo("Midday meals");

        verify(categoryRepository).findAll();
        verify(mapper).categoryToListCategoryResponse(categories);
    }

    @Test
    void listCategories_EmptyList() {
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());
        when(mapper.categoryToListCategoryResponse(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<CategoryResponse> result = categoryService.listCategories();

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(categoryRepository).findAll();
    }


    private Category buildCategory() {
        return Category.builder()
                .id(1L)
                .name("Breakfast")
                .description("Morning meals")
                .build();
    }

    private Category buildCategoryTwo() {
        return Category.builder()
                .id(2L)
                .name("Lunch")
                .description("Midday meals")
                .build();
    }

    private CategoryResponse buildCategoryResponse() {
        CategoryResponse response = new CategoryResponse();
        response.setId(1L);
        response.setName("Breakfast");
        response.setDescription("Morning meals");
        return response;
    }

    private CategoryResponse buildCategoryResponseTwo() {
        CategoryResponse response = new CategoryResponse();
        response.setId(2L);
        response.setName("Lunch");
        response.setDescription("Midday meals");
        return response;
    }
}

