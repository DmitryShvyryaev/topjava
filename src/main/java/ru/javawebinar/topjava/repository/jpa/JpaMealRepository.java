package ru.javawebinar.topjava.repository.jpa;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            User ref = entityManager.getReference(User.class, userId);
            meal.setUser(ref);
            entityManager.persist(meal);
            return meal;
        } else {
            Meal m = get(meal.id(), userId);
            if (m == null)
                return null;
            User ref = entityManager.getReference(User.class, userId);
            meal.setUser(ref);
            return entityManager.merge(meal);
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        Meal removed = get(id, userId);
        if (removed == null)
            return false;
        entityManager.remove(removed);
        return true;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> meals = entityManager.createNamedQuery(Meal.FIND, Meal.class).
                setParameter("id", id).
                setParameter("userId", userId).
                getResultList();
        return DataAccessUtils.singleResult(meals);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return entityManager.createNamedQuery(Meal.ALL, Meal.class).setParameter("userId", userId).getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return entityManager.createNamedQuery(Meal.ALL_FILTER, Meal.class).
                setParameter("userId", userId).
                setParameter("startDateTime", startDateTime).
                setParameter("endDateTime", endDateTime).getResultList();
    }
}