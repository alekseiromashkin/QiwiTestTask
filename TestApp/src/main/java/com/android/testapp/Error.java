package com.android.testapp;

@SuppressWarnings("unused")
public class Error {

    private int id;
    private String message;

    public Error(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public String toString() {
        return this.message;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return this.id;
    }

    public String getMessage() {
        return this.message;
    }
}
