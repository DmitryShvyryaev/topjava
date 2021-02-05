package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealsDAO {
    List<Meal> getAllMeals();

    void addMeal(Meal meal);

    void deleteMeal();

    void updateMeal();
}
