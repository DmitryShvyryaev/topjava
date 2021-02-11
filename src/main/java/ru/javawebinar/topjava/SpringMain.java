package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
//            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
//            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));

            //test
            UserRepository repository = appCtx.getBean(UserRepository.class);

            System.out.println("");
            repository.getAll().forEach(System.out::println);
            System.out.println("");
            User user = repository.getByEmail("dmitry@mail.ru");
            System.out.println(user);
            user.setName("Grisha");
            System.out.println(repository.save(user));
            System.out.println(repository.get(2));
            System.out.println(repository.delete(2));
            repository.getAll().forEach(System.out::println);
        }
    }
}
