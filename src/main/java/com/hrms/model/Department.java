package com.hrms.model;

public class Department {
    private int id;          // manually assigned, continuous
    private String name;     // UNIQUE
    private String location;

    // ✅ Constructor for new departments (no id yet, handled in DAO)
    public Department(String name, String location) {
        this.name = name;
        this.location = location;
    }

    // ✅ Constructor for existing departments
    public Department(int id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return String.format("Department{id=%d, name='%s', location='%s'}",
                id, name, location);
    }
}
