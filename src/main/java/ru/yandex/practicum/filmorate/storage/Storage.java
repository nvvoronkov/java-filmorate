package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;

public interface Storage<T> {
    T add(T obj);

    T update(T obj);

    T getById(Integer id);

    Collection<T> getAll();
}
