package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
//            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
//            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
//            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));

            //test
            MealRepository repository = appCtx.getBean(MealRepository.class);
//            MealRestController controller = appCtx.getBean(MealRestController.class);

            repository.getAll(1).stream().
                    filter(meal -> DateTimeUtil.isBetweenOpen(meal.getDate(), LocalDate.of(2021, Month.JANUARY, 30),
                            LocalDate.of(2021, Month.JANUARY, 30))).
                    forEach(System.out::println);
//            controller.getAllWithFilter().forEach(System.out::println);

            System.out.println(DateTimeUtil.isBetweenOpen(LocalDate.now(), LocalDate.of(2021, Month.FEBRUARY, 12),
                    LocalDate.of(2021, Month.FEBRUARY, 12)));

            LocalDate ld = LocalDate.of(2021, Month.JANUARY, 30);
            LocalDate start = LocalDate.of(2021, Month.JANUARY, 30);
            LocalDate end = LocalDate.of(2021, Month.JANUARY, 30);

            System.out.println(DateTimeUtil.isBetweenOpen(ld, start, end));
        }
    }
}