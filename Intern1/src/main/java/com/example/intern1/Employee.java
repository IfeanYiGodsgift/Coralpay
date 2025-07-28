package com.example.intern1;

import java.io.Serializable;

public class Employee implements Serializable {

    // A serialVersionUID is recommended for Serializable classes
    // It's used to ensure that the sender and receiver of a serialized object have loaded
    // classes that are compatible with respect to serialization.
    private static final long serialVersionUID = 1L;

    private String name;
    private int id;
    private String department;

    public Employee(String name, int id, String department) {
        this.name = name;
        this.id = id;
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", department='" + department + '\'' +
                '}';
    }
}