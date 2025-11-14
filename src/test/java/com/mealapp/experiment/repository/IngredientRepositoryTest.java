package com.mealapp.experiment.repository;

import com.mealapp.experiment.model.Allergy;
import com.mealapp.experiment.model.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class IngredientRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private AllergyRepository allergyRepository;

    private Ingredient testIngredient1;
    private Ingredient testIngredient2;
    private Ingredient testIngredient3;
    private Allergy testAllergy1;
    private Allergy testAllergy2;

    @BeforeEach
    void setUp() {
        testAllergy1 = createAllergy("Test Allergy 1");
        testAllergy2 = createAllergy("Test Allergy 2");

        testIngredient1 = buildIngredient("Test Ingredient 1", 50.0, 10.0, 5.0, 2.0, 1.0, 10.0);
        testIngredient2 = buildIngredient("Test Ingredient 2", 150.0, 20.0, 10.0, 30.0, 0.0, 50.0);
        testIngredient3 = buildIngredient("Test Ingredient 3", 10.0, 0.0, 0.0, 0.0, 5.0, 0.0);
    }

    @Test
    void save_Ingredient() {
        Ingredient saved = ingredientRepository.save(testIngredient1);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Test Ingredient 1");
        assertThat(saved.getKcal()).isEqualTo(50.0);
    }

    @Test
    void find_Ingredient() {
        Ingredient saved = ingredientRepository.save(testIngredient1);
        entityManager.flush();
        entityManager.clear();

        Optional<Ingredient> found = ingredientRepository.findIngredientById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test Ingredient 1");
    }

    @Test
    void findAll_Ingredients() {
        ingredientRepository.save(testIngredient1);
        ingredientRepository.save(testIngredient2);
        ingredientRepository.save(testIngredient3);
        entityManager.flush();

        List<Ingredient> ingredients = ingredientRepository.findAll();

        assertThat(ingredients).hasSizeGreaterThanOrEqualTo(3);
        assertThat(ingredients).extracting(Ingredient::getName)
                .contains("Test Ingredient 1", "Test Ingredient 2", "Test Ingredient 3");
    }

    @Test
    void findByAllergiesId_ShouldReturnIngredientsWithSpecificAllergy() {
        testIngredient1.getAllergies().add(testAllergy1);
        testIngredient2.getAllergies().add(testAllergy2);

        ingredientRepository.save(testIngredient1);
        ingredientRepository.save(testIngredient2);
        ingredientRepository.save(testIngredient3);
        entityManager.flush();

        List<Ingredient> ingredientsWithAllergy = ingredientRepository
                .findByAllergiesIdIn(Collections.singletonList(testAllergy1.getId()));

        assertThat(ingredientsWithAllergy).hasSizeGreaterThanOrEqualTo(1);
        assertThat(ingredientsWithAllergy).extracting(Ingredient::getName)
                .contains("Test Ingredient 1");
    }

    @Test
    void findByAllergiesId_WhenNoMatch_ShouldReturnEmptyList() {
        ingredientRepository.save(testIngredient1);
        ingredientRepository.save(testIngredient2);
        entityManager.flush();

        List<Ingredient> ingredients = ingredientRepository
                .findByAllergiesIdIn(List.of(999999L));

        assertThat(ingredients).isEmpty();
    }

    @Test
    void findIngredientById_WhenNotExists_ShouldReturnEmpty() {
        Optional<Ingredient> found = ingredientRepository.findIngredientById(999999L);

        assertThat(found).isEmpty();
    }

    private Ingredient buildIngredient(String name, Double kcal, Double carb, Double protein,
                                       Double fat, Double fiber, Double sodium) {
        return Ingredient.builder()
                .name(name)
                .kcal(kcal)
                .carb(carb)
                .protein(protein)
                .fat(fat)
                .fiber(fiber)
                .sodium(sodium)
                .build();
    }

    private Allergy createAllergy(String name) {
        Allergy allergy = new Allergy();
        allergy.setName(name);
        return allergyRepository.save(allergy);
    }
}
