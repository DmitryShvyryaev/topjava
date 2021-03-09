package ru.javawebinar.topjava.service.datajpa;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaMealServiceTest extends MealServiceTest {

    @Test
    public void getWithOwner() {
        Meal actual = service.getMealWithOwner(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER.assertMatch(actual, adminMeal1);
        USER_MATCHER.assertMatch(actual.getUser(), admin);
    }

    @Test
    public void getNotFoundWithOwner() {
        Assert.assertThrows(NotFoundException.class, () -> service.getMealWithOwner(MealTestData.NOT_FOUND, USER_ID));
    }

    @Test
    public void getNotOwnWithOwner() {
        Assert.assertThrows(NotFoundException.class, () -> service.getMealWithOwner(ADMIN_MEAL_ID, USER_ID));
    }
}
