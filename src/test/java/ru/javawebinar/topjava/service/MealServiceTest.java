package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.Util;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService mealService;

    @Test
    public void get() {
        Meal expected = TEST_MEALS.get(4);
        Meal actual = mealService.get(expected.getId(), USER_ID);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void getNotExist() {
        Assert.assertThrows(NotFoundException.class, () -> mealService.get(NOT_FOUND_ID, USER_ID));
    }

    @Test
    public void getWithIncorrectUserId() {
        Meal expected = TEST_MEALS.get(4);
        Assert.assertThrows(NotFoundException.class, () -> mealService.get(expected.getId(), 9));
    }

    @Test
    public void delete() {
        int id = TEST_MEALS.get(5).getId();
        mealService.delete(id, USER_ID);
        Assert.assertThrows(NotFoundException.class, () -> mealService.get(id, USER_ID));
    }

    @Test
    public void deleteNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> mealService.get(NOT_FOUND_ID, USER_ID));
    }

    @Test
    public void deleteIncorrectUserId() {
        int id = TEST_MEALS.get(3).getId();
        Assert.assertThrows(NotFoundException.class, () -> mealService.get(id, 999));
    }

    @Test
    public void getBetweenInclusiveForInterSec() {
        List<Meal> meals = mealService.getBetweenInclusive(START_DATE_TEST, END_DATE_TEST, USER_ID);
        List<Meal> expected = TEST_MEALS.stream().filter(meal -> Util.isBetweenHalfOpen(meal.getDateTime(),
                DateTimeUtil.atStartOfDayOrMin(START_DATE_TEST), DateTimeUtil.atStartOfNextDayOrMax(END_DATE_TEST))).collect(Collectors.toList());
        assertThat(meals).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }

    @Test
    public void getBetweenInclusiveForStartDate() {
        List<Meal> meals = mealService.getBetweenInclusive(null, END_DATE_TEST, USER_ID);
        List<Meal> expected = TEST_MEALS.stream().filter(meal -> Util.isBetweenHalfOpen(meal.getDateTime(),
                null, DateTimeUtil.atStartOfNextDayOrMax(END_DATE_TEST))).collect(Collectors.toList());
        assertThat(meals).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }

    @Test
    public void getBetweenInclusiveForEndDate() {
        List<Meal> meals = mealService.getBetweenInclusive(START_DATE_TEST, null, USER_ID);
        List<Meal> expected = TEST_MEALS.stream().filter(meal -> Util.isBetweenHalfOpen(meal.getDateTime(),
                DateTimeUtil.atStartOfDayOrMin(START_DATE_TEST), null)).collect(Collectors.toList());
        assertThat(meals).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }

    @Test
    public void getBetweenInclusiveForNulls() {
        List<Meal> meals = mealService.getBetweenInclusive(null, null, USER_ID);
        assertThat(meals).usingRecursiveFieldByFieldElementComparator().isEqualTo(TEST_MEALS);
    }

    @Test
    public void getAll() {
        List<Meal> meals = mealService.getAll(USER_ID);
        assertThat(meals).usingRecursiveFieldByFieldElementComparator().isEqualTo(TEST_MEALS);
    }

    @Test
    public void getAllWithoutMeals() {
        List<Meal> meals = mealService.getAll(ALIEN_ID);
        assertThat(meals).usingRecursiveFieldByFieldElementComparator().isEqualTo(Collections.EMPTY_LIST);
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        mealService.update(updated, USER_ID);
        assertThat(mealService.get(updated.getId(), USER_ID)).usingRecursiveComparison().isEqualTo(getUpdated());
    }

    @Test
    public void updateNotMineMeal() {
        Assert.assertThrows(NotFoundException.class, () -> mealService.update(getUpdated(), NOT_FOUND_ID));
    }

    @Test
    public void updateNotExist() {
        Meal updated = getUpdated();
        updated.setId(150);
        Assert.assertThrows(NotFoundException.class, () -> mealService.update(updated, USER_ID));
    }

    @Test
    public void create() {
        Meal created = mealService.create(getNew(), USER_ID);
        Meal newMeal = getNew();
        newMeal.setId(created.getId());
        assertThat(created).usingRecursiveComparison().ignoringFields("dateTime").isEqualTo(newMeal);
        assertThat(mealService.get(newMeal.getId(), USER_ID)).usingRecursiveComparison().ignoringFields("dateTime").isEqualTo(newMeal);
    }

    @Test
    public void createDublicate() {
        Assert.assertThrows(DataAccessException.class, () ->
                mealService.create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0),
                        "Завтрак", 500), USER_ID));
    }
}