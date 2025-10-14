package com.mealapp.experiment.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "meal")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "recipe")
    private String recipe;

    @Column(name = "calories")
    private Integer calories;

    @Column(name = "prep_time")
    private Integer prepTime;

    @Column(name = "picture")
    private String picture;

    @Column(name = "servings")
    private Integer servings;

    @Column(name = "date_created")
    private LocalDate createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diet_id", nullable = false)
    private Diet diet;

    @OneToMany(mappedBy = "meal", fetch = FetchType.LAZY)
    private Set<IngredientMeal> ingredientMeals = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "category_meal",
            joinColumns = @JoinColumn(name = "meal_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @PrePersist
    private void onCreate() {
        this.createdDate = LocalDate.now();
    }
}
