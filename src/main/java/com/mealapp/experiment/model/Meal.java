package com.mealapp.experiment.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Data
@Builder
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

}
