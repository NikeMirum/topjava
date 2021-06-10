package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealListDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private final MealListDao dao = MealListDao.getInstance();
    private static final Logger log = getLogger(MealServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forward to meals");
        String forward = "";
        String action = request.getParameter("action");
        if (action == null) {
            forward = "meals.jsp";
            request.setAttribute("meals", MealsUtil.getWithExceeding(dao.getAll(), MealsUtil.CALORIES_PER_DAY_LIMIT));
        } else if (action.equalsIgnoreCase("delete")) {
            int id = Integer.parseInt(request.getParameter("id"));
            dao.delete(id);
            log.debug("delete item # {}", id);
            forward = "meals.jsp";
            log.info("forward to meals");
            request.setAttribute("meals", MealsUtil.getWithExceeding(dao.getAll(), MealsUtil.CALORIES_PER_DAY_LIMIT));
        } else if (action.equalsIgnoreCase("create")) {
            forward = "mealsEdit.jsp";
            Meal meal = new Meal(null, LocalDateTime.now(), "", 700);
            request.setAttribute("meal", meal);
        } else if (action.equalsIgnoreCase("edit")) {
            forward = "mealsEdit.jsp";
            int id = Integer.parseInt(request.getParameter("id"));
            Meal meal = dao.getById(id);
            request.setAttribute("meal", meal);
        } else {
            forward = "meals.jsp";
            request.setAttribute("meals", dao.getAll());
        }
        request.getRequestDispatcher(forward).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer id = null;
        String idString = request.getParameter("id");
        if (!(idString == null || idString.isEmpty())) {
            id = Integer.parseInt(idString);
        }
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        Meal meal = new Meal(id, dateTime, description, calories);
        dao.createOrUpdate(meal);
        request.setAttribute("meals", dao.getAll());
        response.sendRedirect("meals");
    }

}
