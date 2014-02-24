package com.android.testapp;

public abstract class Item {

    int id;

    @Override
    public int hashCode() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

}
