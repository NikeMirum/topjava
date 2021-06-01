package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // Создаю HashMap, в которую либо заношу значение калорий, что было в этот день, либо увеличиваю его если встречается ещё одна запись
        HashMap<LocalDate, Integer> caloriesDailyMap = new HashMap<>();
        // Первый проход N
        for (UserMeal meal : meals) {
            LocalDate currentMealDate = meal.getDateTime().toLocalDate();
            if (caloriesDailyMap.containsKey(currentMealDate)) {
                caloriesDailyMap.put(currentMealDate, caloriesDailyMap.get(currentMealDate) + meal.getCalories());
            } else caloriesDailyMap.put(currentMealDate, meal.getCalories());
        }
        // Создаю список, в который заношу запусь уже UserMealWithExcess вместо UserMeal с учётом подсчитанных в HashMap калорий
        List<UserMealWithExcess> filteredMeals = new ArrayList<>();
        // Второй проход N
        for (UserMeal meal : meals) {
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                filteredMeals.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(),
                        meal.getCalories(), caloriesDailyMap.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }
        return filteredMeals;
    }

    public static List<UserMealWithExcess> filteredByCyclesOld(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> filteredMeals = new ArrayList<>();
        // Первый проход N
        for (UserMeal mealToFilter : meals) {
            int caloriesSum = 0;
            // Проход N*N
            for (UserMeal mealForCalorieSumCounting : meals) {
                if (mealToFilter.getDateTime().toLocalDate().equals(mealForCalorieSumCounting.getDateTime().toLocalDate())) {
                    caloriesSum += mealForCalorieSumCounting.getCalories();
                }
            }
            if (TimeUtil.isBetweenHalfOpen(mealToFilter.getDateTime().toLocalTime(), startTime, endTime)) {
                filteredMeals.add(new UserMealWithExcess(mealToFilter.getDateTime(), mealToFilter.getDescription(),
                        mealToFilter.getCalories(), caloriesSum > caloriesPerDay));
            }
        }
        return filteredMeals;
    }


    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        return null;
    }

}
