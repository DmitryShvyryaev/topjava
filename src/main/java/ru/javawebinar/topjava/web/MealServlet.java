package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealsDAO;
import ru.javawebinar.topjava.dao.MealsDaoLmpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final MealsDAO dao = new MealsDaoLmpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("get request");

        String action = req.getParameter("action");

        log.debug("request Parameter Action = " + action);

        if (action == null || action.isEmpty()) {
            List<MealTo> meals = MealsUtil.filteredByStreams(dao.getAllMeals(), LocalTime.MIN, LocalTime.MAX, 2000);
            req.setAttribute("meals", meals);
            req.getRequestDispatcher("meals.jsp").forward(req, resp);
        } else if (action.equalsIgnoreCase("getById")) {
            int id = Integer.parseInt(req.getParameter("id"));
            log.debug("id is " + id);
            Meal meal = dao.getMeal(id);
            req.setAttribute("meal", meal);
            req.getRequestDispatcher("showMeal.jsp").forward(req, resp);
        } else if (action.equalsIgnoreCase("delete")) {
            int id = Integer.parseInt(req.getParameter("id"));
            log.debug("id is " + id);
            dao.deleteMeal(id);
            resp.sendRedirect("/topjava/meals");
        } else if (action.equalsIgnoreCase("addMeal")) {
            Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 0);
            meal.setId(0);
            req.setAttribute("meal", meal);
            req.getRequestDispatcher("showMeal.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("get post");
        req.setCharacterEncoding("UTF-8");

        int id = Integer.parseInt(req.getParameter("id"));
        LocalDateTime localDateTime = TimeUtil.getDateTime(req.getParameter("date"));
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));

        Meal meal = new Meal(localDateTime, description, calories);
        meal.setId(id);
        log.debug(meal.toString());

        if (id == 0) {
            log.debug("added Meal");
            dao.addMeal(meal);
        } else {
            log.debug("updated meal");
            dao.updateMeal(meal);
        }

        resp.sendRedirect("/topjava/meals");
    }
}
