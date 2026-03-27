package com.mycompany.motorphpayroll;


import java.io.*;
import java.util.*;

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

    static final double SSS_RATE = 0.0363;
    static final double PHILHEALTH_RATE = 0.03;
    static final double PAGIBIG_RATE = 0.02;
    static final double TAX_RATE = 0.05;

    public static void main(String[] args) {
        List<Employee> employees = readEmployeesFromFile("employees.txt");

        if (employees.isEmpty()) {
            System.out.println("No employee data found. Check employees.txt");
            return;
        }

        for (Employee emp : employees) {
            double grossPay = emp.hourlyRate * emp.hoursWorked;

            double sss = grossPay * SSS_RATE;
            double philhealth = grossPay * PHILHEALTH_RATE;
            double pagibig = grossPay * PAGIBIG_RATE;
            double tax = grossPay * TAX_RATE;

            double totalDeductions = sss + philhealth + pagibig + tax;
            double netPay = grossPay - totalDeductions;

            System.out.println("Employee Number: " + emp.empNumber);
            System.out.println("Name: " + emp.name);
            System.out.println("Gross Pay: Php " + String.format("%.2f", grossPay));
            System.out.println("Net Pay: Php " + String.format("%.2f", netPay));
            System.out.println("----------------------");
        }
    }

    private static List<Employee> readEmployeesFromFile(String filename) {
        List<Employee> employees = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                String empNumber = parts[0];
                String name = parts[1];
                double hourlyRate = Double.parseDouble(parts[2]);
                double hoursWorked = Double.parseDouble(parts[3]);

                employees.add(new Employee(empNumber, name, hourlyRate, hoursWorked));
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return employees;
    }
}