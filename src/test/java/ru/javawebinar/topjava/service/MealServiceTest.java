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
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;

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
        Meal meal = mealService.get(100003, 100000);
        System.out.println(meal);
    }

    @Test
    public void getIncorrectId() {
        Assert.assertThrows(NotFoundException.class, () -> mealService.get(5, 100000));
    }

    @Test
    public void getIncorrectUserId() {
        Assert.assertThrows(NotFoundException.class, () -> mealService.get(100003, 200000));
    }

    @Test
    public void delete() {
        mealService.delete(100003, 100000);
        Assert.assertThrows(NotFoundException.class, ()->mealService.get(100003, 100000));
    }

    @Test
    public void deleteNotFound() {
        Assert.assertThrows(NotFoundException.class, ()->mealService.get(16, 100000));
    }

    @Test
    public void deleteIncorrectUserId() {
        Assert.assertThrows(NotFoundException.class, ()->mealService.get(100003, 100001));
    }

    @Test
    public void getBetweenInclusive() {
        mealService.getBetweenInclusive(LocalDate.of(2020, Month.JANUARY, 31),
                LocalDate.of(2020, Month.FEBRUARY, 1), 100000).forEach(System.out::println);
    }

    @Test
    public void getAll() {
        mealService.getAll(100000).forEach(System.out::println);
    }

    @Test
    public void update() {

    }

    @Test
    public void create() {
        Meal created = mealService.create(MealTestData.getNew(), 100000);
        Meal copy = new Meal(created);
        assertThat(created).usingRecursiveComparison().isEqualTo(copy);
        assertThat(copy).usingRecursiveComparison().isEqualTo(mealService.get(created.getId(), 100000));
    }

    @Test
    public void createDublicate() {
        Assert.assertThrows(DataAccessException.class, () ->
                mealService.create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0),
                        "Завтрак", 500), 100000));
    }
}