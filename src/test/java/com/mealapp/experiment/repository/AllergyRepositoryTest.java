package com.mealapp.experiment.repository;

import com.mealapp.experiment.model.Allergy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class AllergyRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AllergyRepository allergyRepository;

    private Allergy testAllergy1;
    private Allergy testAllergy2;

    @BeforeEach
    void setUp() {
        allergyRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();

        testAllergy1 = buildAllergy("Peanuts");
        testAllergy2 = buildAllergy("Shellfish");
    }

    @Test
    void save_Allergy() {
        Allergy saved = allergyRepository.save(testAllergy1);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Peanuts");
    }

    @Test
    void findById_Success() {
        Allergy saved = allergyRepository.save(testAllergy1);
        entityManager.flush();
        entityManager.clear();

        Optional<Allergy> found = allergyRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Peanuts");
    }

    @Test
    void findById_NotFound() {
        Optional<Allergy> found = allergyRepository.findById(999999L);

        assertThat(found).isEmpty();
    }

    @Test
    void findByName_Success() {
        allergyRepository.save(testAllergy1);
        entityManager.flush();

        Optional<Allergy> found = allergyRepository.findByName("Peanuts");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Peanuts");
    }

    @Test
    void findByName_NotFound() {
        Optional<Allergy> found = allergyRepository.findByName("NonExistent");

        assertThat(found).isEmpty();
    }

    @Test
    void findAll_Allergies() {
        allergyRepository.save(testAllergy1);
        allergyRepository.save(testAllergy2);
        entityManager.flush();

        List<Allergy> allergies = allergyRepository.findAll();

        assertThat(allergies).hasSize(2);
        assertThat(allergies).extracting(Allergy::getName)
                .contains("Peanuts", "Shellfish");
    }

    @Test
    void delete_Allergy() {
        Allergy saved = allergyRepository.save(testAllergy1);
        entityManager.flush();

        allergyRepository.delete(saved);
        entityManager.flush();

        Optional<Allergy> found = allergyRepository.findById(saved.getId());

        assertThat(found).isEmpty();
    }

    @Test
    void update_Allergy() {
        Allergy saved = allergyRepository.save(testAllergy1);
        entityManager.flush();
        entityManager.clear();

        saved.setName("Updated Peanuts");
        Allergy updated = allergyRepository.save(saved);
        entityManager.flush();

        Optional<Allergy> found = allergyRepository.findById(updated.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Updated Peanuts");
    }

    private Allergy buildAllergy(String name) {
        return Allergy.builder()
                .name(name)
                .build();
    }
}
