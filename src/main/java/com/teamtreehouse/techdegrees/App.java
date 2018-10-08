package com.teamtreehouse.techdegrees;


import com.google.gson.Gson;
import com.teamtreehouse.techdegrees.dao.Sql2oTodoDao;
import com.teamtreehouse.techdegrees.dao.TodoDao;
import com.teamtreehouse.techdegrees.exc.ApiError;
import com.teamtreehouse.techdegrees.model.Todo;
import org.sql2o.Sql2o;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class App {

    static final String apiVersion = "/api/v1/";

    public static void main(String[] args) {

        staticFileLocation("/publc");
        String dataSource = "jdbc:h2:~/todos.db";

        if(args.length > 0){
            if(args.length != 2){
                System.out.println("java Api <port> <jdbcUrl>");
            }
            port(Integer.parseInt(args[0]));
            dataSource = args[1];
        }


        Sql2o sql2o = new Sql2o(
                String.format("%s;INIT=RUNSCRIPT from 'classpath:/db/init.sql'", dataSource)
                ,"","");

        TodoDao todoDao = new Sql2oTodoDao(sql2o);
        Gson gson = new Gson();

        // Method to return all Todos
        get(apiVersion + "todos", "application/json",
                (req, res) -> todoDao.findAll(), gson::toJson);

        // Method to add a new Todo to the list
        post(apiVersion + "todos", "application/json", (req, res) -> {
           Todo todo = gson.fromJson(req.body(), Todo.class);
           todoDao.add(todo);
           res.status(201);
           return todo;
        }, gson::toJson);

        // Method to change the details of an already saved Todo
        put(apiVersion + "todos/:id", "application/json", (req, res) -> {
            Todo todo = todoDao.findById(Integer.parseInt(req.params("id")));
            todo.setTask(gson.fromJson(req.body(), Todo.class).getTask());
            todo.setCompleted(gson.fromJson(req.body(), Todo.class).isCompleted());
            todoDao.update(todo);
            return todo;
        },gson::toJson);

        // Method to delete a Todo that is not needed anymore
        delete(apiVersion + "todos/:id", "application/json", (req, res) -> {
            todoDao.delete(Integer.parseInt(req.params("id")));
            res.status(204);
            return null;
        }, gson::toJson);

        after((req, res) -> {
            res.type("application/json");
        });

        exception(ApiError.class, (exception, request, response) -> {
            ApiError apiError = (ApiError) exception;
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("status", apiError.getStatus());
            jsonMap.put("errorMessage", apiError.getMessage());
            response.type("application/json");
            response.status(apiError.getStatus());
            response.body(gson.toJson(jsonMap));
        });

        exception(NumberFormatException.class, (exception, request, response) -> {
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("status", 500);
            jsonMap.put("errorMessage", "Error Parsing Id of todo");
            response.type("application/json");
            response.status(500);
            response.body(gson.toJson(jsonMap));
        });
    }

}
