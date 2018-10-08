package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.model.Todo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

import static org.junit.Assert.*;

public class Sql2oTodoDaoTest {

    private Sql2oTodoDao sql2oTodoDao;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        String jdbcUrl = "jdbc:h2:mem:test;" + "INIT=RUNSCRIPT from 'classpath:db/init.sql'";
        Sql2o sql2o = new Sql2o(jdbcUrl, "", "");
        sql2oTodoDao = new Sql2oTodoDao(sql2o);
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    private Todo newTodo() {
        return new Todo("Test", false);
    }

    @Test
    public void savingTodoSetsId() throws Exception {
        Todo todo = newTodo();
        int newTodoId = sql2oTodoDao.add(todo);
        assertEquals(1, newTodoId);
    }

    @Test
    public void existingTodosCanBeFoundById() throws Exception {
        Todo todo = newTodo();
        sql2oTodoDao.add(todo);
        Todo foundTodo = sql2oTodoDao.findById(todo.getId());
        assertEquals(todo, foundTodo);
    }

    @Test
    public void existingTodosCanBeFoundByUsingFindAll() throws Exception {
        Todo todo = newTodo();
        sql2oTodoDao.add(todo);

        List<Todo> todoList = sql2oTodoDao.findAll();

        assertEquals(1,todoList.size());
    }

    @Test
    public void updateTodChangesTodo() throws Exception {
        Todo todo = newTodo();
        sql2oTodoDao.add(todo);
        todo.setTask("New Task");
        sql2oTodoDao.update(todo);
        Todo foundTodo = sql2oTodoDao.findById(todo.getId());
        assertEquals(todo, foundTodo);
    }

    @Test
    public void deletingTodoRemovesTodo() throws Exception {
        Todo todo = newTodo();
        sql2oTodoDao.add(todo);
        sql2oTodoDao.delete(todo.getId());
        assertEquals(null, sql2oTodoDao.findById(1));
    }
}