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

    private Ingredient milk;
    private Ingredient bacon;
    private Ingredient spinach;
    private Allergy lactoseAllergy;
    private Allergy glutenAllergy;

    @BeforeEach
    void setUp() {
        ingredientRepository.deleteAll();
        allergyRepository.deleteAll();

        lactoseAllergy = createAllergy("Lactose");
        glutenAllergy = createAllergy("Gluten");

        milk = buildIngredient("Milk", 50.0, 10.0, 5.0, 2.0, 1.0, 10.0);
        bacon = buildIngredient("Bacon", 150.0, 20.0, 10.0, 30.0, 0.0, 50.0);
        spinach = buildIngredient("Spinach", 10.0, 0.0, 0.0, 0.0, 5.0, 0.0);
    }

    @Test
    void save_Ingredient() {
        Ingredient saved = ingredientRepository.save(milk);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Milk");
        assertThat(saved.getKcal()).isEqualTo(50.0);
    }

    @Test
    void find_Ingredient() {
        Ingredient saved = ingredientRepository.save(milk);
        entityManager.flush();
        entityManager.clear();

        Optional<Ingredient> found = ingredientRepository.findIngredientById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Milk");
    }

    @Test
    void findAll_Ingredients() {
        ingredientRepository.save(milk);
        ingredientRepository.save(bacon);
        ingredientRepository.save(spinach);
        entityManager.flush();

        List<Ingredient> ingredients = ingredientRepository.findAll();

        assertThat(ingredients).hasSize(3);
        assertThat(ingredients).extracting(Ingredient::getName)
                .containsExactlyInAnyOrder("Milk", "Bacon", "Spinach");
    }

    @Test
    void findByAllergiesId_ShouldReturnIngredientsWithSpecificAllergy() {
        milk.getAllergies().add(lactoseAllergy);
        bacon.getAllergies().add(glutenAllergy);

        ingredientRepository.save(milk);
        ingredientRepository.save(bacon);
        ingredientRepository.save(spinach);
        entityManager.flush();

        List<Ingredient> ingredientsWithLactose = ingredientRepository
                .findByAllergiesIdIn(Collections.singletonList(lactoseAllergy.getId()));

        assertThat(ingredientsWithLactose).hasSize(1);
        assertThat(ingredientsWithLactose.getFirst().getName()).isEqualTo("Milk");
    }

    @Test
    void findByAllergiesId_WhenNoMatch_ShouldReturnEmptyList() {
        ingredientRepository.save(milk);
        ingredientRepository.save(bacon);
        entityManager.flush();

        List<Ingredient> ingredients = ingredientRepository
                .findByAllergiesIdIn(List.of(999L));

        assertThat(ingredients).isEmpty();
    }

    @Test
    void findIngredientById_WhenNotExists_ShouldReturnEmpty() {
        Optional<Ingredient> found = ingredientRepository.findIngredientById(999L);

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
