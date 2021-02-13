package ru.javawebinar.topjava;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
//        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
//            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
//            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
//            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));

        Map<Integer, Integer> map = new ConcurrentHashMap<>();
        map.put(1, 1);

        System.out.println(map.remove(2));
    }
}
//}