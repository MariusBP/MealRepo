package com.mealapp.experiment.repository;

import com.mealapp.experiment.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    List<Ingredient> findByAllergies_Id(List<Long> allergyIdList);

}
