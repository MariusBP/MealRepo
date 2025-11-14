package com.mealapp.experiment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mealapp.experiment.model.Category;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional <Category> findByName(String name);
}