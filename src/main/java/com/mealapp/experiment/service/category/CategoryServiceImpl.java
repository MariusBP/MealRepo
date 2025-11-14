package com.mealapp.experiment.service.category;


import com.mealapp.experiment.common.utils.ExceptionUtils;
import com.mealapp.experiment.model.Category;
import com.mealapp.experiment.repository.CategoryRepository;
import com.mealapp.experiment.service.utils.ServiceMapper;
import com.mealapp.openapi.category.model.CategoryResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final ServiceMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> listCategories() {
        log.info("Fetching list of Category successful");
        return  mapper.categoryToListCategoryResponse(categoryRepository.findAll());
    }

    @Override
    @Transactional()
    public CategoryResponse createCategory(Category category) {
        log.info("Creating new category: {}", category.getName());

        validateCategoryNameExists(category.getName());
        Category savedCategory = categoryRepository.save(category);
        return mapper.categoryToCategoryResponse(savedCategory);
    }

    private void validateCategoryNameExists(String name) {
        categoryRepository.findByName(name).ifPresent(category -> {
            throw ExceptionUtils.exception(HttpStatus.CONFLICT, "Category with name '" + name + "' already exists.").get();
        });
    }
}
