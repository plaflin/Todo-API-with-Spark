package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.exc.DaoException;
import com.teamtreehouse.techdegrees.model.Todo;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oTodoDao implements TodoDao {

    private final Sql2o sql2o;

    public Sql2oTodoDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public List<Todo> findAll() {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM todos")
                    .executeAndFetch(Todo.class);
        }
    }

    @Override
    public int add(Todo todo) throws DaoException {
        String sql = "INSERT INTO todos(task, completed) VALUES (:task, :completed)";

        try(Connection con = sql2o.open()){
            int id = (int) con.createQuery(sql)
                    .bind(todo)
                    .executeUpdate()
                    .getKey();
            todo.setId(id);
            return id;
        } catch (Sql2oException ex) {
            throw new DaoException(ex, "Problem adding Todo");
        }
    }

    @Override
    public Todo findById(int id) {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM todos WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(Todo.class);
        }
    }

    public void update(Todo todo) throws DaoException {
        String sql = "UPDATE todos SET task = :task, completed = :completed WHERE id = :id";
        try(Connection con = sql2o.open()){
            con.createQuery(sql)
                    .bind(todo)
                    .executeUpdate();
        } catch (Sql2oException ex){
            throw new DaoException(ex, "Problem updating Todo");
        }
    }

    @Override
    public void delete(int id) throws DaoException {
        String sql = "DELETE FROM todos WHERE id = :id";
        try(Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            throw new DaoException(ex, "Problem deleting Todo");
        }
    }
}