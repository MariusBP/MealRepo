package com.mealapp.experiment.repository;

import com.mealapp.experiment.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    List<Ingredient> findByAllergiesIdIn(List<Long> allergyIdList);

    Optional<Ingredient> findIngredientById(Long id);

    Optional<Ingredient> findByName(String name);

}
