package model;

import exception.ShelterFullException;

// Class representing Shelters and demonstrating User-Defined Exception throwing
public class Shelter {

    private int shelterId;
    private String name;
    private String location;
    private int capacity;
    private int occupied;

    public Shelter() {
        this.shelterId = 0;
        this.name = "Default Shelter";
        this.location = "Unknown";
        this.capacity = 50;
        this.occupied = 0;
    }

    public Shelter(int shelterId, String name, String location, int capacity, int occupied) {
        this.shelterId = shelterId;
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.occupied = occupied;
    }

    public int getShelterId() {
        return shelterId;
    }

    public void setShelterId(int shelterId) {
        this.shelterId = shelterId;
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getOccupied() {
        return occupied;
    }

    public void setOccupied(int occupied) {
        this.occupied = occupied;
    }

    // Method calculating remaining capacity
    public int getAvailableCapacity() {
        return capacity - occupied;
    }

    public boolean isAvailable() {
        return occupied < capacity;
    }

    // Demonstrating throw keyword with User-Defined Exception
    public void allocateVictim() throws ShelterFullException {
        if (occupied >= capacity) {
            throw new ShelterFullException("Shelter '" + name + "' (ID: " + shelterId + ") is FULL! Capacity (" 
                                           + capacity + "/" + capacity + ") reached.");
        }
        occupied++;
    }

    public void releaseVictim() {
        if (occupied > 0) {
            occupied--;
        }
    }

    public void displayDetails() {
        System.out.println("Shelter #" + shelterId + ": " + name + " (" + location + ") - Occupancy: " 
                           + occupied + "/" + capacity + " [Available: " + getAvailableCapacity() + "]");
    }
}
