package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, 1));
        repository.put(counter.incrementAndGet(), new Meal(counter.get(), LocalDateTime
                .of(2021, Month.JANUARY, 30, 10, 0), "Завтрак", 500, 2));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setUserId(userId);
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        } else
            return (meal.getUserId() == userId) ? repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal) : null;
    }

    @Override
    public boolean delete(int id, int userId) {
        Meal mealToBeDeleted = repository.getOrDefault(id, null);
        if (mealToBeDeleted != null && mealToBeDeleted.getUserId() == userId) {
            return repository.remove(id) != null;
        } else return false;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal result = repository.getOrDefault(id, null);
        if (result != null && result.getUserId() == userId) {
            return result;
        } else return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.values().stream().filter(m -> m.getUserId() == userId)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed()).collect(Collectors.toList());
    }

    @Override
    public List<Meal> getFilteredByDateAndTime(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return repository.values().stream().filter(m -> m.getUserId() == userId)
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startDate.toLocalTime(), endDate.toLocalTime()))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed()).collect(Collectors.toList());
    }
}

