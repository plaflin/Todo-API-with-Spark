package com.teamtreehouse.techdegrees.Testing;

import java.util.Objects;

public class ApiErrorModel {
    private String errorMessage;
    private int status;

    public ApiErrorModel(String message, int status){
        this.errorMessage = message;
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getStatus() {
        return status;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiErrorModel that = (ApiErrorModel) o;
        return status == that.status &&
                Objects.equals(errorMessage, that.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorMessage, status);
    }

    @Override
    public String toString() {
        return "ApiErrorModel{" +
                "errorMessage='" + errorMessage + '\'' +
                ", status=" + status +
                '}';
    }
}
