package com.mealapp.experiment.repository;

import com.mealapp.experiment.model.Category;
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
class CategoryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category testCategory1;
    private Category testCategory2;
    private Category testCategory3;

    @BeforeEach
    void setUp() {
        testCategory1 = buildCategory("Test Category 1", "First test category");
        testCategory2 = buildCategory("Test Category 2", "Second test category");
        testCategory3 = buildCategory("Test Category 3", "Third test category");
    }

    @Test
    void save_Category() {
        Category saved = categoryRepository.save(testCategory1);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Test Category 1");
        assertThat(saved.getDescription()).isEqualTo("First test category");
    }

    @Test
    void find_Category() {
        Category saved = categoryRepository.save(testCategory1);
        entityManager.flush();
        entityManager.clear();

        Optional<Category> found = categoryRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test Category 1");
        assertThat(found.get().getDescription()).isEqualTo("First test category");
    }

    @Test
    void findAll_Categories() {
        categoryRepository.save(testCategory1);
        categoryRepository.save(testCategory2);
        categoryRepository.save(testCategory3);
        entityManager.flush();

        List<Category> categories = categoryRepository.findAll();

        assertThat(categories).hasSizeGreaterThanOrEqualTo(3);
        assertThat(categories).extracting(Category::getName)
                .contains("Test Category 1", "Test Category 2", "Test Category 3");
    }

    @Test
    void findById_WhenNotExists_ShouldReturnEmpty() {
        Optional<Category> found = categoryRepository.findById(999999L);

        assertThat(found).isEmpty();
    }

    @Test
    void delete_Category() {
        Category saved = categoryRepository.save(testCategory1);
        entityManager.flush();

        categoryRepository.delete(saved);
        entityManager.flush();

        Optional<Category> found = categoryRepository.findById(saved.getId());

        assertThat(found).isEmpty();
    }

    @Test
    void update_Category() {
        Category saved = categoryRepository.save(testCategory1);
        entityManager.flush();
        entityManager.clear();

        saved.setName("Updated Test Category");
        saved.setDescription("Updated description");
        Category updated = categoryRepository.save(saved);
        entityManager.flush();

        Optional<Category> found = categoryRepository.findById(updated.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Updated Test Category");
        assertThat(found.get().getDescription()).isEqualTo("Updated description");
    }

    @Test
    void findAll_WhenEmpty_ShouldReturnListWithSampleData() {
        List<Category> categories = categoryRepository.findAll();
        assertThat(categories).isNotEmpty();
    }

    private Category buildCategory(String name, String description) {
        return Category.builder()
                .name(name)
                .description(description)
                .build();
    }
}
