package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.Map;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    private final MealService service;
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        log.info("get All Meals");
        return service.getAll(SecurityUtil.authUserId(), SecurityUtil.authUserCaloriesPerDay());
    }

    public List<MealTo> getAllWithFilter(Map<String, Temporal> filter) {
        log.info("get All Meals with Filter");

        LocalDate startDate = (LocalDate) filter.get("startDate");
        LocalDate endDate = (LocalDate) filter.get("endDate");
        LocalTime startTime = (LocalTime) filter.get("startTime");
        LocalTime endTime = (LocalTime) filter.get("endTime");

        log.debug(String.format("Filter: start date - %s, end date - %s, start time - %s, end time %s",
                startDate, endDate, startTime, endTime));

        return service.getAllWithFilter(SecurityUtil.authUserId(),
                SecurityUtil.authUserCaloriesPerDay(),
                startDate == null ? LocalDate.MIN : startDate,
                endDate == null ? LocalDate.MAX : endDate,
                startTime == null ? LocalTime.MIN : startTime,
                endTime == null ? LocalTime.MAX : endTime);
    }

    public Meal get(int id) {
        log.info("get meal {}", id);
        return service.get(id, SecurityUtil.authUserId());
    }

    public Meal create(Meal meal) {
        checkNew(meal);
        meal.setUserId(SecurityUtil.authUserId());
        log.info("create meal {}", meal);
        return service.create(meal, SecurityUtil.authUserId());
    }

    public void delete(int id) {
        log.info("delete meal {}", id);
        service.delete(id, SecurityUtil.authUserId());
    }

    public void update(Meal meal, int id) {
        meal.setUserId(SecurityUtil.authUserId());
        log.info("update meal {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        service.update(meal, SecurityUtil.authUserId());
    }
}