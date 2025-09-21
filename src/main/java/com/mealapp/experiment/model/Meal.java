package com.mealapp.experiment.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "diet_id")
    private Diet diet;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "recipe")
    private String recipe;

    @Column(name = "prep_Time")
    private Integer prepTime;

    @Column(name = "picture")
    private String picture;

    @Column(name = "date_created")
    private LocalDate createdDate;


    @ManyToMany(mappedBy = "meals")
    private Set<Category> categories;

    @ManyToMany
    @JoinTable(
            name = "ingredient_to_meal",
            joinColumns = @JoinColumn(name = "meal_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private Set<Ingredient> ingredients;

}
