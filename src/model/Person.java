package model;

// Base class demonstrating: Classes, Encapsulation, Static/Instance blocks, and Polymorphism
public class Person {

    // Instance variables (Encapsulation using private fields)
    private String name;
    private int age;
    private String phone;
    private String location;

    // Static variable shared across all instances
    private static int totalPersonsCreated = 0;

    // Static Initializer Block - runs once when class is loaded
    static {
        // System.out.println("[OOP Concept: Static Block] Person class loaded into JVM.");
    }

    // Instance Initializer Block - runs every time any object is instantiated
    {
        totalPersonsCreated++;
    }

    // Default constructor
    public Person() {
        this.name = "Unknown";
        this.age = 0;
        this.phone = "N/A";
        this.location = "Unknown";
    }

    // Parameterized constructor demonstrating the 'this' keyword
    public Person(String name, int age, String phone, String location) {
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.location = location;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // Static method
    public static int getTotalPersonsCreated() {
        return totalPersonsCreated;
    }

    // Method to be overridden in subclasses (Polymorphism)
    public void displayDetails() {
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Phone: " + phone);
        System.out.println("Location: " + location);
    }
}
