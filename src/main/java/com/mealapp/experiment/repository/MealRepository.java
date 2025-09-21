package com.mealapp.experiment.repository;

import com.mealapp.experiment.model.Meal;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<Meal, Integer> {

    @EntityGraph(attributePaths = {"diet", "ingredients", "categories"})
    Optional<Meal> findById(Integer id);

    List<Meal> findByDietId(Integer dietId);

}
