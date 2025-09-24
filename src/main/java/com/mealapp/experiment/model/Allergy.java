package com.mealapp.experiment.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "allergy")
public class Allergy {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @ManyToMany(mappedBy = "allergies")
    private Set<Ingredient> ingredients = new HashSet<>();
}
