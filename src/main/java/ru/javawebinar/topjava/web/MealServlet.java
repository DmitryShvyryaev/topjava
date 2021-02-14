package ru.javawebinar.topjava.web;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class MealServlet extends HttpServlet {

    private MealRestController controller;
    private ClassPathXmlApplicationContext context;

    @Override
    public void init() {
        context = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        controller = context.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        context.close();
        super.destroy();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (meal.isNew()) {
            controller.create(meal);
        } else {
            controller.update(meal, meal.getId());
        }

        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                controller.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        controller.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "filter":
                Map<String, Temporal> filterParameter = getParsedFilter(request);
                request.setAttribute("meals", controller.getAllWithFilter(filterParameter));
                request.setAttribute("filter", filterParameter);
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
            case "all":
            default:
                request.setAttribute("meals", controller.getAll());
                request.setAttribute("filter", new HashMap<>());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    private Map<String, Temporal> getParsedFilter(HttpServletRequest req) {
        HashMap<String, Temporal> result = new HashMap<>();

        String startDateString = req.getParameter("startDate");
        String endDateString = req.getParameter("endDate");
        String startTimeString = req.getParameter("startTime");
        String endTimeString = req.getParameter("endTime");

        result.put("startDate", startDateString.isEmpty() ? null : LocalDate.parse(startDateString));
        result.put("endDate", endDateString.isEmpty() ? null : LocalDate.parse(endDateString));
        result.put("startTime", startTimeString.isEmpty() ? null : LocalTime.parse(startTimeString));
        result.put("endTime", endTimeString.isEmpty() ? null : LocalTime.parse(endTimeString));

        return result;
    }
}
