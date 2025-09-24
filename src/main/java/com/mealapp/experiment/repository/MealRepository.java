package com.mealapp.experiment.repository;

import com.mealapp.experiment.model.Meal;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

    List<Meal> findByDietIdAndCategories_IdIn(Long dietId, List<Long> categoryIds);

    @EntityGraph(attributePaths = {"ingredients", "categories", "diet"})
    Optional<Meal> findWithAllById(Long id);
}
