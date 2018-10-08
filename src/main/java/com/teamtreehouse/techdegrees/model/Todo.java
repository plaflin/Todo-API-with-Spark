package com.teamtreehouse.techdegrees.model;

import java.util.Objects;

public class Todo {

    private int id;
    private String task;
    private boolean completed;

    public Todo(String task, boolean completed) {

        this.task = task;
        this.completed = completed;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return id == todo.id &&
                completed == todo.completed &&
                Objects.equals(task, todo.task);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, task, completed);
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", task='" + task + '\'' +
                ", completed=" + completed +
                '}';
    }
}
