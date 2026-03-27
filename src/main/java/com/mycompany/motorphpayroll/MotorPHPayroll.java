package com.mycompany.motorphpayroll;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

// Class for storing employee details
class Employee {
    String empNumber;
    String name;
    double hourlyRate;
    double hoursWorked;

    Employee(String empNumber, String name, double hourlyRate, double hoursWorked) {
        this.empNumber = empNumber;
        this.name = name;
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
    }
}

public class MotorPHPayroll {

    // Deduction rates
    static final double SSS_RATE = 0.0363;
    static final double PHILHEALTH_RATE = 0.03;
    static final double PAGIBIG_RATE = 0.02;
    static final double TAX_RATE = 0.05;

    public static void main(String[] args) {
        // Read employee data from file
        List<Employee> employees = readEmployeesFromFile("employees.txt");

        // Check if file has data
        if (employees.isEmpty()) {
            System.out.println("No employee data found. Check employees.txt");
            return;
        }

        System.out.println("====================================");
        System.out.println("       MOTORPH PAYROLL SYSTEM       ");
        System.out.println("====================================");

        // Process each employee
        for (Employee emp : employees) {
            double grossPay = emp.hourlyRate * emp.hoursWorked;

            double sss = grossPay * SSS_RATE;
            double philhealth = grossPay * PHILHEALTH_RATE;
            double pagibig = grossPay * PAGIBIG_RATE;
            double tax = grossPay * TAX_RATE;

            double totalDeductions = sss + philhealth + pagibig + tax;
            double netPay = grossPay - totalDeductions;

            // Display payroll details
            System.out.println("\n========== PAYROLL DETAILS ==========");
            System.out.println("Employee Number : " + emp.empNumber);
            System.out.println("Employee Name   : " + emp.name);
            System.out.printf("Hours Worked    : %.2f%n", emp.hoursWorked);
            System.out.printf("Hourly Rate     : Php %.2f%n", emp.hourlyRate);
            System.out.printf("Gross Pay       : Php %.2f%n", grossPay);
            System.out.printf("SSS             : Php %.2f%n", sss);
            System.out.printf("PhilHealth      : Php %.2f%n", philhealth);
            System.out.printf("Pag-IBIG        : Php %.2f%n", pagibig);
            System.out.printf("Tax             : Php %.2f%n", tax);
            System.out.printf("Total Deduction : Php %.2f%n", totalDeductions);
            System.out.printf("Net Pay         : Php %.2f%n", netPay);
            System.out.println("=====================================");
        }
    }

    // Method for reading employees from text file
    private static List<Employee> readEmployeesFromFile(String filename) {
        List<Employee> employees = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                // Check if line has complete data
                if (parts.length < 4) {
                    System.out.println("Invalid data: " + line);
                    continue;
                }

                String empNumber = parts[0].trim();
                String name = parts[1].trim();
                double hourlyRate = Double.parseDouble(parts[2].trim());
                double hoursWorked = Double.parseDouble(parts[3].trim());

                employees.add(new Employee(empNumber, name, hourlyRate, hoursWorked));
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return employees;
    }
}