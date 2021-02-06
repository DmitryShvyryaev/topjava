package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealsDAO {
    List<Meal> getAllMeals();

    Meal getMeal(int id);

    void addMeal(Meal meal);

    void deleteMeal(int id);

    void updateMeal(int id);
}
