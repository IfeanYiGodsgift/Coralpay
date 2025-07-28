package com.example.intern1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SerializationDemo {

    private static final String FILE_NAME = "employees.ser"; // .ser is a common extension for serialized objects

    public static void main(String[] args) {
        Employee emp1 = new Employee("Alice Smith", 101, "Engineering");
        Employee emp2 = new Employee("Bob Johnson", 102, "Marketing");
        Employee emp3 = new Employee("Charlie Brown", 103, "Sales");

        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(emp1);
        employeeList.add(emp2);
        employeeList.add(emp3);

        System.out.println("Original Employee List:");
        for (Employee emp : employeeList) {
            System.out.println("  " + emp);
        }

        serializeEmployeeList(employeeList, FILE_NAME);

        System.out.println("\n------------------------------\n");

        //Deserialize the Employee objects
        List<Employee> deserializedList = deserializeEmployeeList(FILE_NAME);
        if (deserializedList != null) {
            System.out.println("Deserialized Employee List:");
            for (Employee emp : deserializedList) {
                System.out.println("  " + emp);
            }
        }
    }

    // Method to serialize Employee objects to a file
    public static void serializeEmployeeList(List<Employee> employees, String filename) {
        try (FileOutputStream fileOut = new FileOutputStream(filename);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(employees);
            System.out.println("Employee list serialized successfully to " + filename);
        } catch (IOException i) {
            System.err.println("Serialization error: " + i.getMessage());
            i.printStackTrace();
        }
    }

    // Method to deserialize Employee objects from a file
    @SuppressWarnings("unchecked")
    public static List<Employee> deserializeEmployeeList(String filename) {
        List<Employee> employees = null;
        try (FileInputStream fileIn = new FileInputStream(filename);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            employees = (List<Employee>) in.readObject(); // Read the entire list back
            System.out.println("Employee list deserialized successfully from " + filename);
        } catch (IOException i) {
            System.err.println("Deserialization I/O error: " + i.getMessage());
            i.printStackTrace();
            return null;
        } catch (ClassNotFoundException c) {
            System.err.println("Class not found during deserialization.");
            c.printStackTrace();
            return null;
        }
        return employees;
    }
}