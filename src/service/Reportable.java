package service;

// Interface demonstrating Abstraction and Contract Definition
public interface Reportable {

    // Abstract method to generate a formatted text summary
    String generateSummary();

    // Abstract method to save report to file
    boolean saveToFile(String filePath);
}
