package com.mealapp.experiment.service.category;

import com.mealapp.experiment.model.Category;
import com.mealapp.openapi.category.model.CategoryResponse;

import java.util.List;

public interface CategoryService {

    List<CategoryResponse> listCategories();

    CategoryResponse createCategory(Category category);
}
