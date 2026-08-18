package com.musiclibrary.repositories;

import java.util.List;

public interface IRepository<T> {
    void add(T entity);
    void update(T entity);
    void delete(String id);
    T getById(String id);
    List<T> getAll();
    void saveToFile();
    void loadFromFile();
}
