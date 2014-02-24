package com.android.testapp;

@SuppressWarnings("unused")
public class User extends Item {

    private String name;
    private String secondName;

    public User(int id, String name, String secondName) {
        this.id = id;
        this.name = name;
        this.secondName = secondName;
    }

    @Override
    public String toString() {
        return (this.name + " " + this.secondName).trim();
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof User && this.id == (((User) object).getId());
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getName() {
        return this.name;
    }

    public String getSecondName() {
        return this.secondName;
    }

}
