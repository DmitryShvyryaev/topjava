package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final int USER_ID = START_SEQ;
    public static final int ALIEN_ID = START_SEQ + 1;
    private static int START_MEAL_ID = START_SEQ + 1;

    public static final int NOT_FOUND_ID = START_SEQ + 150;

    public static final LocalDate START_DATE_TEST = LocalDate.of(2020, Month.JANUARY, 31);
    public static final LocalDate END_DATE_TEST = LocalDate.of(2020, Month.JANUARY, 30);

    public static final List<Meal> TEST_MEALS = MealsUtil.meals.stream().peek(meal -> meal.setId(++START_MEAL_ID)).collect(Collectors.toList());

    static {
        TEST_MEALS.sort(Comparator.comparing(Meal::getDateTime).reversed());
    }

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.now(), "testDesc", 1250);
    }

    public static Meal getUpdated() {
        Meal meal = new Meal(TEST_MEALS.get(6));
        meal.setDateTime(LocalDateTime.of(2020, Month.MARCH, 15, 12, 34, 0));
        meal.setCalories(666);
        meal.setDescription("UniqueDescription");
        return meal;
    }
}
