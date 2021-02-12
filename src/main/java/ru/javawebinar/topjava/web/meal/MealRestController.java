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
import java.util.List;

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

    public List<MealTo> getAllWithFilter(HttpServletRequest req) {
        log.info("get All Meals with Filter");

        String startDateString = req.getParameter("startDate");
        String endDateString = req.getParameter("endDate");
        String startTimeString = req.getParameter("startTime");
        String endTimeString = req.getParameter("endTime");

        LocalDate startDate = startDateString == null || startDateString.isEmpty() ? LocalDate.MIN : LocalDate.parse(startDateString);
        LocalDate endDate = endDateString == null || endDateString.isEmpty() ? LocalDate.MAX : LocalDate.parse(endDateString);
        LocalTime startTime = startTimeString == null || startTimeString.isEmpty() ? LocalTime.MIN : LocalTime.parse(startTimeString);
        LocalTime endTime = endTimeString == null || endTimeString.isEmpty() ? LocalTime.MAX : LocalTime.parse(endTimeString);

        log.debug(String.format("Filter: start date - %s, end date - %s, start time - %s, end time %s",
                startDate, endDate, startTime, endTime));

        return service.getAllWithFilter(SecurityUtil.authUserId(),
                SecurityUtil.authUserCaloriesPerDay(),
                startDate, endDate, startTime, endTime);
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