package model;

// Class representing Disaster events in RescueNet
public class Disaster {

    private int disasterId;
    private String type;     // e.g. "Flood", "Earthquake", "Fire", "Cyclone", "Landslide"
    private String location;
    private String severity; // e.g. "Low", "Medium", "High", "Critical"

    // Static member tracking total registered disasters
    private static int totalDisasters = 0;

    public Disaster() {
        this.disasterId = 0;
        this.type = "General";
        this.location = "Unknown";
        this.severity = "Medium";
        totalDisasters++;
    }

    public Disaster(int disasterId, String type, String location, String severity) {
        this.disasterId = disasterId;
        this.type = type;
        this.location = location;
        this.severity = severity;
        totalDisasters++;
    }

    public int getDisasterId() {
        return disasterId;
    }

    public void setDisasterId(int disasterId) {
        this.disasterId = disasterId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public static int getTotalDisasters() {
        return totalDisasters;
    }

    public void displayDetails() {
        System.out.println("Disaster #" + disasterId + " [" + type + "] - Location: " + location + " (Severity: " + severity + ")");
    }
}
