package com.mealapp.experiment.repository;

import com.mealapp.experiment.model.Meal;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

    @Query("SELECT DISTINCT m FROM Meal m JOIN m.categories c WHERE m.diet.id = :dietId AND (:categoryIds IS NULL OR c.id IN :categoryIds)")
    List<Meal> findByDietIdAndCategoriesIds(@Param("dietId") Long dietId, @Param("categoryIds") List<Long> categoryIds);

    @EntityGraph(attributePaths = {
            "ingredientMeals",
            "ingredientMeals.ingredient",
            "ingredientMeals.ingredient.allergies",
            "categories",
            "diet"
    })
    Optional<Meal> findMealById(Long id);
}
