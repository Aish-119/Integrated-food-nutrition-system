package com.nutrition.nutrition.dto;

public class BeneficiaryRequest {

    private String name;
    private Integer age;
    private Integer householdSize;
    private String nutritionNeeds;
    private String address;

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
