package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserServiceTest;

import java.util.Collections;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {

    @Test
    public void getWithMeals() {
        User actual = service.getWithMeals(USER_ID);
        USER_MATCHER.assertMatch(actual, user);
        MEAL_MATCHER.assertMatch(actual.getMeals(), meals);
    }

    @Test
    public void getWithMealsOnUserWithoutMeals() {
        User created = service.create(UserTestData.getNew());
        User newUser = UserTestData.getNew();
        newUser.setId(created.getId());
        User actual = service.getWithMeals(newUser.getId());
        USER_MATCHER.assertMatch(actual, newUser);
        MEAL_MATCHER.assertMatch(actual.getMeals(), Collections.EMPTY_LIST);
    }
}
