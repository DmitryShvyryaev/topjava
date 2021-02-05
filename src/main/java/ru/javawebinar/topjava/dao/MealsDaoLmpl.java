package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MealsDaoLmpl implements MealsDAO {
    private final Map<Integer, Meal> data = new ConcurrentHashMap<>();
    private static Integer id = 0;

    public MealsDaoLmpl() {
        data.put(1, new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        data.put(2, new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        data.put(3, new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        data.put(4, new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        data.put(5, new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        data.put(6, new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        data.put(7, new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    public List<Meal> getAllMeals() {
        return Collections.unmodifiableList(new ArrayList<>(data.values()));
    }

    @Override
    public void addMeal(Meal meal) {
        data.put(++id, meal);
    }

    @Override
    public void deleteMeal() {

    }

    @Override
    public void updateMeal() {

    }
}