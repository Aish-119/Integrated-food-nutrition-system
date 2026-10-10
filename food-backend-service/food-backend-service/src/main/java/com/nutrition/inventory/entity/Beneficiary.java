
package com.nutrition.inventory.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "beneficiaries")
public class Beneficiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Integer age;

    @Column(name = "household_size", nullable = false)
    private Integer householdSize = 1;

    @Column(name = "nutrition_needs")
    private String nutritionNeeds;

    private String address;

    public Beneficiary() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getHouseholdSize() {
        return householdSize;
    }

    public void setHouseholdSize(Integer householdSize) {
        this.householdSize = householdSize;
    }

    public String getNutritionNeeds() {
        return nutritionNeeds;
    }

    public void setNutritionNeeds(String nutritionNeeds) {
        this.nutritionNeeds = nutritionNeeds;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}