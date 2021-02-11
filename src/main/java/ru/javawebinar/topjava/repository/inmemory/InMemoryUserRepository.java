package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.AbstractNamedEntity;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private final Map<Integer, User> repository = new ConcurrentHashMap<>();

    {
        save(new User(1, "Taisia", "taya@mail.ru", "111", Role.USER));
        save(new User(2, "Dmitry", "dmitry@mail.ru", "111", Role.ADMIN, Role.USER));
        save(new User(3, "Alexy", "alexy@mail.ru", "111", Role.USER));
        save(new User(4, "Michail", "mich@mail.ru", "111", Role.USER));
        save(new User(5, "Johanson", "jaoh@mail.ru", "111", Role.USER));
    }

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        if (user.isNew()) {
            user.setId(SecurityUtil.authUserId());
            user.setCaloriesPerDay(SecurityUtil.authUserCaloriesPerDay());
            repository.put(user.getId(), user);
            return user;
        }
        return repository.put(user.getId(), user);
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        List<User> users = new ArrayList<>(repository.values());
        //sorting
        users.sort(Comparator.comparing(AbstractNamedEntity::getName));

        return users;
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return repository.values().stream().filter(user -> user.getEmail().equals(email)).findFirst().get();
    }
}
