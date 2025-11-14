package com.mealapp.experiment.repository;

import com.mealapp.experiment.model.Diet;
import com.mealapp.experiment.model.Meal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class MealRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private DietRepository dietRepository;

    private Meal testMeal1;
    private Meal testMeal2;
    private Diet testDiet;

    @BeforeEach
    void setUp() {
        testDiet = createDiet("Test Diet", "Test diet description");

        testMeal1 = buildMeal("Test Meal 1", "First test meal", testDiet);
        testMeal2 = buildMeal("Test Meal 2", "Second test meal", testDiet);
    }

    @Test
    void save_Meal() {
        Meal saved = mealRepository.save(testMeal1);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Test Meal 1");
        assertThat(saved.getDescription()).isEqualTo("First test meal");
        assertThat(saved.getDiet()).isNotNull();
        assertThat(saved.getDiet().getId()).isEqualTo(testDiet.getId());
    }

    @Test
    void findMealById_Success() {
        Meal saved = mealRepository.save(testMeal1);
        entityManager.flush();
        entityManager.clear();

        Optional<Meal> found = mealRepository.findMealById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test Meal 1");
        assertThat(found.get().getDiet().getName()).isEqualTo("Test Diet");
    }

    @Test
    void findMealById_NotFound() {
        Optional<Meal> found = mealRepository.findMealById(999999L);

        assertThat(found).isEmpty();
    }

    @Test
    void findAll_Meals() {
        mealRepository.save(testMeal1);
        mealRepository.save(testMeal2);
        entityManager.flush();

        List<Meal> meals = mealRepository.findAll();

        assertThat(meals).hasSizeGreaterThanOrEqualTo(2);
        assertThat(meals).extracting(Meal::getName)
                .contains("Test Meal 1", "Test Meal 2");
    }

    @Test
    void findByDietId() {
        mealRepository.save(testMeal1);
        mealRepository.save(testMeal2);
        entityManager.flush();

        List<Meal> meals = mealRepository.findByDietIdAndCategoriesIds(testDiet.getId(), List.of(), 0);

        assertThat(meals).hasSizeGreaterThanOrEqualTo(2);
        assertThat(meals).allMatch(meal -> meal.getDiet().getId().equals(testDiet.getId()));
    }

    @Test
    void delete_Meal() {
        Meal saved = mealRepository.save(testMeal1);
        entityManager.flush();

        mealRepository.delete(saved);
        entityManager.flush();

        Optional<Meal> found = mealRepository.findMealById(saved.getId());

        assertThat(found).isEmpty();
    }

    @Test
    void update_Meal() {
        Meal saved = mealRepository.save(testMeal1);
        entityManager.flush();
        entityManager.clear();

        saved.setName("Updated Test Meal");
        saved.setDescription("Updated description");
        saved.setCalories(500);
        Meal updated = mealRepository.save(saved);
        entityManager.flush();

        Optional<Meal> found = mealRepository.findMealById(updated.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Updated Test Meal");
        assertThat(found.get().getDescription()).isEqualTo("Updated description");
        assertThat(found.get().getCalories()).isEqualTo(500);
    }

    private Meal buildMeal(String name, String description, Diet diet) {
        return Meal.builder()
                .name(name)
                .description(description)
                .recipe("Test recipe")
                .prepTime(30)
                .calories(400)
                .servings(4)
                .diet(diet)
                .createdDate(LocalDate.now())
                .build();
    }

    private Diet createDiet(String name, String description) {
        Diet diet = new Diet();
        diet.setName(name);
        diet.setDescription(description);
        diet.setPicture("test.jpg");
        return dietRepository.save(diet);
    }
}
