package com.mealapp.experiment.repository;

import com.mealapp.experiment.model.Diet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class DietRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DietRepository dietRepository;

    private Diet testDiet1;
    private Diet testDiet2;
    private Diet testDiet3;

    @BeforeEach
    void setUp() {
        testDiet1 = buildDiet("Test Diet 1", "First test diet", "test1.jpg");
        testDiet2 = buildDiet("Test Diet 2", "Second test diet", "test2.jpg");
        testDiet3 = buildDiet("Test Diet 3", "Third test diet", "test3.jpg");
    }

    @Test
    void save_Diet() {
        Diet saved = dietRepository.save(testDiet1);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Test Diet 1");
        assertThat(saved.getDescription()).isEqualTo("First test diet");
        assertThat(saved.getPicture()).isEqualTo("test1.jpg");
    }

    @Test
    void find_Diet() {
        Diet saved = dietRepository.save(testDiet1);
        entityManager.flush();
        entityManager.clear();

        Optional<Diet> found = dietRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test Diet 1");
        assertThat(found.get().getDescription()).isEqualTo("First test diet");
    }

    @Test
    void findAll_Diets() {
        dietRepository.save(testDiet1);
        dietRepository.save(testDiet2);
        dietRepository.save(testDiet3);
        entityManager.flush();

        List<Diet> diets = dietRepository.findAll();

        assertThat(diets).hasSizeGreaterThanOrEqualTo(3);
        assertThat(diets).extracting(Diet::getName)
                .contains("Test Diet 1", "Test Diet 2", "Test Diet 3");
    }

    @Test
    void findById_WhenNotExists_ShouldReturnEmpty() {
        Optional<Diet> found = dietRepository.findById(999999L);

        assertThat(found).isEmpty();
    }

    @Test
    void delete_Diet() {
        Diet saved = dietRepository.save(testDiet1);
        entityManager.flush();

        dietRepository.delete(saved);
        entityManager.flush();

        Optional<Diet> found = dietRepository.findById(saved.getId());

        assertThat(found).isEmpty();
    }

    @Test
    void update_Diet() {
        Diet saved = dietRepository.save(testDiet1);
        entityManager.flush();
        entityManager.clear();

        saved.setName("Updated Test Diet");
        saved.setDescription("Updated description");
        saved.setPicture("updated.jpg");
        Diet updated = dietRepository.save(saved);
        entityManager.flush();

        Optional<Diet> found = dietRepository.findById(updated.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Updated Test Diet");
        assertThat(found.get().getDescription()).isEqualTo("Updated description");
        assertThat(found.get().getPicture()).isEqualTo("updated.jpg");
    }

    @Test
    void findAll_WhenEmpty_ShouldReturnListWithSampleData() {
        List<Diet> diets = dietRepository.findAll();

        // Should at least contain sample data from Flyway migration
        assertThat(diets).isNotEmpty();
    }

    private Diet buildDiet(String name, String description, String picture) {
        Diet diet = new Diet();
        diet.setName(name);
        diet.setDescription(description);
        diet.setPicture(picture);
        return diet;
    }
}

