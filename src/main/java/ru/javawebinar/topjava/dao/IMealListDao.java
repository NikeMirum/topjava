package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;
import java.util.List;

public interface IMealListDao {
    void delete(int id);

    Meal createOrUpdate(Meal meal);

    Collection<Meal> getAll();

    Meal getById(int id);
}
