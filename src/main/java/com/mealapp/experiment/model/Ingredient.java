package com.mealapp.experiment.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;


@Getter
@Setter
@Builder
@Entity
@Table(name = "ingredient")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "kcal")
    private Double kcal;

    @Column(name = "carb")
    private Double carb;

    @Column(name = "fat")
    private Double fat;

    @Column(name = "protein")
    private Double protein;

    @Column(name = "fiber")
    private Double fiber;

    @Column(name = "sodium")
    private Double sodium;

    @ManyToMany(mappedBy = "ingredients")
    private Set<Allergy> allergies;
}
