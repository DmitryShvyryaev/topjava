package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
@RequestMapping("/meals")
public class JspMealController {
    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    @Autowired
    private MealService service;

    @GetMapping
    public String getAll(HttpServletRequest request, Model model) {
        int userId = SecurityUtil.authUserId();
        log.info("getAll for user {}", userId);
        if (request.getParameterMap().size() == 0) {
            model.addAttribute("meals", MealsUtil.getTos(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay()));
        } else {
            LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
            LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
            LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
            LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
            List<Meal> meals = service.getBetweenInclusive(startDate, endDate, userId);
            model.addAttribute("meals", MealsUtil.getFilteredTos(meals, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime));
        }
        return "meals";
    }

    @GetMapping("/{id}")
    public String get(@PathVariable("id") int id, Model model) {
        final Meal meal = id == 0 ?
                new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                service.get(id, SecurityUtil.authUserId());
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping
    public String create(HttpServletRequest request) throws UnsupportedEncodingException {
        Meal meal = getMealFromRequest(request);
        int userId = SecurityUtil.authUserId();
        checkNew(meal);
        log.info("create {} for user {}", meal, userId);
        service.create(meal, userId);
        return "redirect:meals";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable("id") int id, HttpServletRequest request) {
        Meal meal = getMealFromRequest(request);
        int userId = SecurityUtil.authUserId();
        log.info("update {} for user {}", meal, userId);
        service.update(meal, userId);
        return "redirect:meals";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id, HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        log.info("delete meal {} for user {}", id, userId);
        service.delete(id, userId);
        return "redirect:meals";
    }

    private Meal getMealFromRequest(HttpServletRequest request) {
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        String paramId = request.getParameter("id");
        if (StringUtils.hasLength(paramId))
            meal.setId(Integer.parseInt(paramId));
        return meal;
    }
}
