package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private static final Sort SORT_DATE_TIME_DESC = Sort.by(Sort.Direction.DESC, "dateTime");
    private final CrudMealRepository crudRepository;
    private final CrudUserRepository userRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository, CrudUserRepository userRepository) {
        this.crudRepository = crudRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        meal.setUser(userRepository.getOne(userId));
        if (meal.isNew() || get(meal.id(), userId) != null)
            return crudRepository.save(meal);
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = crudRepository.findById(id).orElse(null);
        return meal == null || meal.getUser().getId() != userId ? null : meal;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.findByUserId(userId, SORT_DATE_TIME_DESC);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudRepository.findAllBetweenHalfOpen(userId, startDateTime, endDateTime, SORT_DATE_TIME_DESC);
    }
}
