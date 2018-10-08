package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.exc.DaoException;
import com.teamtreehouse.techdegrees.model.Todo;

import java.util.List;

public interface TodoDao {

    List<Todo> findAll();

    int add(Todo todo) throws DaoException;

    Todo findById(int id);

    public void update(Todo todo) throws DaoException;

    public void delete(int id) throws DaoException;
}
