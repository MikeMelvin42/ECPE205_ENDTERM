package com.example;

import java.time.LocalDate;

public class Employee {
    private String idNumber; // Changed from int to String
    private String name;
    private String position;
    private LocalDate employmentDate;
    private int daysPresent;
    private int daysAbsent;
    private double salary; // Changed from int to double
    private String contactNumber;

    // Default constructor
    public Employee(String text, String nameFieldText, String positionFieldText, LocalDate parse, int i, int parseInt, double v) {}

    // Full constructor
    public Employee(String idNumber, String name, String position, LocalDate employmentDate,
                    int daysPresent, int daysAbsent, double salary, String contactNumber) {
        this.idNumber = idNumber;
        this.name = name;
        this.position = position;
        this.employmentDate = employmentDate;
        this.daysPresent = daysPresent;
        this.daysAbsent = daysAbsent;
        this.salary = salary;
        this.contactNumber = contactNumber;
    }

    // Getters and setters
    public String getIdNumber() { return idNumber; }
    public void setIdNumber(String idNumber) { this.idNumber = idNumber; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public LocalDate getEmploymentDate() { return employmentDate; }
    public void setEmploymentDate(LocalDate employmentDate) { this.employmentDate = employmentDate; }
    public int getDaysPresent() { return daysPresent; }
    public void setDaysPresent(int daysPresent) { this.daysPresent = daysPresent; }
    public int getDaysAbsent() { return daysAbsent; }
    public void setDaysAbsent(int daysAbsent) { this.daysAbsent = daysAbsent; }
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
}