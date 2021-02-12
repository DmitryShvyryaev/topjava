package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.stream().peek(meal -> meal.setUserId(1)).forEach(meal -> save(meal, SecurityUtil.authUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return repository.get(meal.getId()).getUserId().equals(userId) ?
                repository.put(meal.getId(), meal) : null;
    }

    @Override
    public boolean delete(int id, int userId) {
        if (repository.containsKey(id) && repository.get(id).getUserId().equals(userId)) {
            return repository.remove(id) != null;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal =  repository.get(id);
        return meal.getUserId().equals(userId) ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return filterWithPredicate(meal -> meal.getUserId().equals(userId));
    }

    @Override
    public List<Meal> getAllWithFilter(int userId, LocalDate startDate, LocalDate endDate) {
        return filterWithPredicate(meal -> meal.getUserId().equals(userId) &&
                DateTimeUtil.isBetweenOpen(meal.getDate(), startDate, endDate));
    }

    private List<Meal> filterWithPredicate(Predicate<Meal> filter) {
        return repository.values().stream().
                filter(filter).
                sorted((meal1, meal2) -> meal2.getDateTime().compareTo(meal1.getDateTime())).
                collect(Collectors.toList());
    }
}

