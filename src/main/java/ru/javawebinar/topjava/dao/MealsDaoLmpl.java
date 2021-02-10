package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealsDaoLmpl implements MealsDAO {
    private static final AtomicInteger id = new AtomicInteger(0);
    private final Map<Integer, Meal> data = new ConcurrentHashMap<>();

    public MealsDaoLmpl() {
        addMeal(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        addMeal(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        addMeal(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        addMeal(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        addMeal(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        addMeal(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        addMeal(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    public List<Meal> getAllMeals() {
        return Collections.unmodifiableList(new ArrayList<>(data.values()));
    }

    @Override
    public Meal getMeal(int id) {
        return data.get(id);
    }

    @Override
    public void addMeal(Meal meal) {
        meal.setId(id.incrementAndGet());
        data.put(meal.getId(), meal);
    }

    @Override
    public void deleteMeal(int id) {
        data.remove(id);
    }

    @Override
    public void updateMeal(Meal meal) {
        data.put(meal.getId(), meal);
    }

}