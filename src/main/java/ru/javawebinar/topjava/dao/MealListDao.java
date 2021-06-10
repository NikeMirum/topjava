package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealListDao implements IMealListDao{
    private static MealListDao dao;
    public synchronized static MealListDao getInstance() {
        if (dao == null) {
            dao = new MealListDao();
        }
        return dao;
    }

    private Map<Integer,Meal> mealMap = new ConcurrentHashMap<>();
    private AtomicInteger mealIdCounter = new AtomicInteger();
    {
        for (Meal meal : MealsUtil.meals) {
            mealMap.put(mealIdCounter.addAndGet(1),meal);
        }
    }

    @Override
    public void delete(int id) {
        mealMap.remove(id);
    }

    @Override
    public Meal createOrUpdate(Meal meal) {
        if (meal.getId() == null){
            meal.setId(mealIdCounter.incrementAndGet());
        }
        return dao.mealMap.put(meal.getId(),meal);
    }

    @Override
    public Collection<Meal> getAll() {
        for (Meal value : mealMap.values()) {
            System.out.println(value.toString());
        }
        return mealMap.values();
    }

    @Override
    public Meal getById(int id) {
        return mealMap.get(id);
    }
}
