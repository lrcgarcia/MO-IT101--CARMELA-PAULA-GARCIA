package com.mycompany.motorphpayroll;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

class Employee {
    String employeeNumber;
    String lastName;
    String firstName;
    String birthday;
    String address;
    String phoneNumber;
    String sssNumber;
    String philhealthNumber;
    String tinNumber;
    String pagibigNumber;
    String status;
    String position;
    String supervisor;
    double basicSalary;
    double riceSubsidy;
    double phoneAllowance;
    double clothingAllowance;
    double hourlyRate;

    Employee(String employeeNumber, String lastName, String firstName, String birthday,
             String address, String phoneNumber, String sssNumber, String philhealthNumber,
             String tinNumber, String pagibigNumber, String status, String position,
             String supervisor, double basicSalary, double riceSubsidy,
             double phoneAllowance, double clothingAllowance, double hourlyRate) {

        this.employeeNumber = employeeNumber;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthday = birthday;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.sssNumber = sssNumber;
        this.philhealthNumber = philhealthNumber;
        this.tinNumber = tinNumber;
        this.pagibigNumber = pagibigNumber;
        this.status = status;
        this.position = position;
        this.supervisor = supervisor;
        this.basicSalary = basicSalary;
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
        this.hourlyRate = hourlyRate;
    }

    String getFullName() {
        return firstName + " " + lastName;
    }
}

class PayrollResult {
    Employee employee;
    double hoursWorked;
    double grossPay;
    double riceSubsidy;
    double phoneAllowance;
    double clothingAllowance;
    double totalBenefits;
    double sss;
    double philhealth;
    double pagibig;
    double taxableIncome;
    double withholdingTax;
    double totalDeductions;
    double netPay;

    PayrollResult(Employee employee) {
        this.employee = employee;
    }
}

public class MotorPHPayroll {

    public static void main(String[] args) {
        String employeeFile = "employees.txt";
        String timecardFile = "timecard.txt";

        Map<String, Employee> employees = readEmployees(employeeFile);
        Map<String, Double> timecards = readTimecards(timecardFile);

        if (employees.isEmpty()) {
            System.out.println("No employee data found. Check employees.txt");
            return;
        }

        if (timecards.isEmpty()) {
            System.out.println("No timecard data found. Check timecard.txt");
            return;
        }

        System.out.println("=================================================");
        System.out.println("              MOTORPH PAYROLL SYSTEM");
        System.out.println("=================================================");

        double totalGross = 0;
        double totalBenefits = 0;
        double totalDeductions = 0;
        double totalNet = 0;

        for (Employee employee : employees.values()) {
            double hoursWorked = timecards.getOrDefault(employee.employeeNumber, 0.0);

            if (hoursWorked <= 0) {
                continue;
            }

            PayrollResult result = computePayroll(employee, hoursWorked);
            printPayslip(result);

            totalGross += result.grossPay;
            totalBenefits += result.totalBenefits;
            totalDeductions += result.totalDeductions;
            totalNet += result.netPay;
        }

        System.out.println("\n================ PAYROLL SUMMARY REPORT ================");
        System.out.printf("%-8s %-22s %-22s %-12s %-12s%n",
                "Emp ID", "Employee Name", "Position", "Gross Pay", "Net Pay");

        for (Employee employee : employees.values()) {
            double hoursWorked = timecards.getOrDefault(employee.employeeNumber, 0.0);

            if (hoursWorked <= 0) {
                continue;
            }

            PayrollResult result = computePayroll(employee, hoursWorked);

            System.out.printf("%-8s %-22s %-22s %-12.2f %-12.2f%n",
                    employee.employeeNumber,
                    shorten(employee.getFullName(), 22),
                    shorten(employee.position, 22),
                    result.grossPay,
                    result.netPay);
        }

        System.out.println("--------------------------------------------------------");
        System.out.printf("TOTAL GROSS PAY   : Php %.2f%n", totalGross);
        System.out.printf("TOTAL BENEFITS    : Php %.2f%n", totalBenefits);
        System.out.printf("TOTAL DEDUCTIONS  : Php %.2f%n", totalDeductions);
        System.out.printf("TOTAL NET PAY     : Php %.2f%n", totalNet);
        System.out.println("========================================================");
    }

    private static PayrollResult computePayroll(Employee employee, double hoursWorked) {
        PayrollResult result = new PayrollResult(employee);

        result.hoursWorked = hoursWorked;
        result.grossPay = employee.hourlyRate * hoursWorked;

        result.riceSubsidy = employee.riceSubsidy;
        result.phoneAllowance = employee.phoneAllowance;
        result.clothingAllowance = employee.clothingAllowance;
        result.totalBenefits = result.riceSubsidy + result.phoneAllowance + result.clothingAllowance;

        result.sss = computeSSS(result.grossPay);
        result.philhealth = computePhilHealth(result.grossPay);
        result.pagibig = computePagIbig(result.grossPay);

        result.taxableIncome = result.grossPay - (result.sss + result.philhealth + result.pagibig);
        result.withholdingTax = computeWithholdingTax(result.taxableIncome);

        result.totalDeductions = result.sss + result.philhealth + result.pagibig + result.withholdingTax;
        result.netPay = result.grossPay + result.totalBenefits - result.totalDeductions;

        return result;
    }

    private static void printPayslip(PayrollResult result) {
        Employee e = result.employee;

        System.out.println("\n=================================================");
        System.out.println("                 EMPLOYEE PAYSLIP");
        System.out.println("=================================================");
        System.out.println("Employee Number : " + e.employeeNumber);
        System.out.println("Employee Name   : " + e.getFullName());
        System.out.println("Birthday        : " + e.birthday);
        System.out.println("Status          : " + e.status);
        System.out.println("Position        : " + e.position);
        System.out.println("Department      : " + getDepartmentGroup(e.position));
        System.out.println("Supervisor      : " + e.supervisor);

        System.out.println("\n---------------- EMPLOYEE DETAILS ----------------");
        System.out.println("Address         : " + e.address);
        System.out.println("Phone Number    : " + e.phoneNumber);
        System.out.println("SSS Number      : " + e.sssNumber);
        System.out.println("PhilHealth No.  : " + e.philhealthNumber);
        System.out.println("TIN Number      : " + e.tinNumber);
        System.out.println("Pag-IBIG Number : " + e.pagibigNumber);

        System.out.println("\n------------------- EARNINGS --------------------");
        System.out.printf("Basic Salary    : Php %.2f%n", e.basicSalary);
        System.out.printf("Hourly Rate     : Php %.2f%n", e.hourlyRate);
        System.out.printf("Hours Worked    : %.2f%n", result.hoursWorked);
        System.out.printf("Gross Pay       : Php %.2f%n", result.grossPay);

        System.out.println("\n------------------- BENEFITS --------------------");
        System.out.printf("Rice Subsidy    : Php %.2f%n", result.riceSubsidy);
        System.out.printf("Phone Allowance : Php %.2f%n", result.phoneAllowance);
        System.out.printf("Clothing Allow. : Php %.2f%n", result.clothingAllowance);
        System.out.printf("Total Benefits  : Php %.2f%n", result.totalBenefits);

        System.out.println("\n------------------ DEDUCTIONS -------------------");
        System.out.printf("SSS             : Php %.2f%n", result.sss);
        System.out.printf("PhilHealth      : Php %.2f%n", result.philhealth);
        System.out.printf("Pag-IBIG        : Php %.2f%n", result.pagibig);
        System.out.printf("Taxable Income  : Php %.2f%n", result.taxableIncome);
        System.out.printf("Withholding Tax : Php %.2f%n", result.withholdingTax);
        System.out.printf("Total Deduction : Php %.2f%n", result.totalDeductions);

        System.out.println("\n------------------- SUMMARY ---------------------");
        System.out.printf("Net Pay         : Php %.2f%n", result.netPay);
        System.out.println("=================================================");
    }

    private static Map<String, Employee> readEmployees(String filename) {
        Map<String, Employee> employees = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                if (firstLine) {
                    firstLine = false;
                    if (line.toLowerCase().contains("employeenumber")) {
                        continue;
                    }
                }

                String[] parts = line.split(",", -1);

                if (parts.length < 18) {
                    System.out.println("Invalid employee row: " + line);
                    continue;
                }

                Employee employee = new Employee(
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[3].trim(),
                        parts[4].trim(),
                        parts[5].trim(),
                        parts[6].trim(),
                        parts[7].trim(),
                        parts[8].trim(),
                        parts[9].trim(),
                        parts[10].trim(),
                        parts[11].trim(),
                        parts[12].trim(),
                        parseDouble(parts[13]),
                        parseDouble(parts[14]),
                        parseDouble(parts[15]),
                        parseDouble(parts[16]),
                        parseDouble(parts[17])
                );

                employees.put(employee.employeeNumber, employee);
            }

        } catch (IOException e) {
            System.out.println("Error reading employees.txt: " + e.getMessage());
        }

        return employees;
    }

    private static Map<String, Double> readTimecards(String filename) {
        Map<String, Double> timecards = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                if (firstLine) {
                    firstLine = false;
                    if (line.toLowerCase().contains("employeenumber")) {
                        continue;
                    }
                }

                String[] parts = line.split(",", -1);

                if (parts.length < 4) {
                    System.out.println("Invalid timecard row: " + line);
                    continue;
                }

                String employeeNumber = parts[0].trim();
                double hoursWorked = parseDouble(parts[2]);
                String remarks = parts[3].trim();

                if (remarks.equalsIgnoreCase("Weekend") || remarks.equalsIgnoreCase("Holiday")) {
                    hoursWorked = 0;
                }

                timecards.put(employeeNumber, timecards.getOrDefault(employeeNumber, 0.0) + hoursWorked);
            }

        } catch (IOException e) {
            System.out.println("Error reading timecard.txt: " + e.getMessage());
        }

        return timecards;
    }

    private static double parseDouble(String value) {
        try {
            return Double.parseDouble(value.replace(",", "").trim());
        } catch (Exception e) {
            return 0.0;
        }
    }

    private static String shorten(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }

    private static String getDepartmentGroup(String position) {
        String lower = position.toLowerCase();

        if (lower.contains("finance") || lower.contains("account")) {
            return "Accounting";
        } else if (lower.contains("it")) {
            return "IT";
        } else if (lower.contains("customer")) {
            return "Customer Service";
        } else if (lower.contains("marketing")) {
            return "Marketing";
        } else if (lower.contains("human resource") || lower.contains("hr")) {
            return "HR";
        } else if (lower.contains("executive") || lower.contains("chief") || lower.contains("officer")) {
            return "Executive";
        }

        return "General";
    }

    private static double computeSSS(double grossPay) {
        if (grossPay < 3250) return 135.00;
        else if (grossPay < 3750) return 157.50;
        else if (grossPay < 4250) return 180.00;
        else if (grossPay < 4750) return 202.50;
        else if (grossPay < 5250) return 225.00;
        else if (grossPay < 5750) return 247.50;
        else if (grossPay < 6250) return 270.00;
        else if (grossPay < 6750) return 292.50;
        else if (grossPay < 7250) return 315.00;
        else if (grossPay < 7750) return 337.50;
        else if (grossPay < 8250) return 360.00;
        else if (grossPay < 8750) return 382.50;
        else if (grossPay < 9250) return 405.00;
        else if (grossPay < 9750) return 427.50;
        else if (grossPay < 10250) return 450.00;
        else if (grossPay < 10750) return 472.50;
        else if (grossPay < 11250) return 495.00;
        else if (grossPay < 11750) return 517.50;
        else if (grossPay < 12250) return 540.00;
        else if (grossPay < 12750) return 562.50;
        else if (grossPay < 13250) return 585.00;
        else if (grossPay < 13750) return 607.50;
        else if (grossPay < 14250) return 630.00;
        else if (grossPay < 14750) return 652.50;
        else if (grossPay < 15250) return 675.00;
        else if (grossPay < 15750) return 697.50;
        else if (grossPay < 16250) return 720.00;
        else if (grossPay < 16750) return 742.50;
        else if (grossPay < 17250) return 765.00;
        else if (grossPay < 17750) return 787.50;
        else if (grossPay < 18250) return 810.00;
        else if (grossPay < 18750) return 832.50;
        else if (grossPay < 19250) return 855.00;
        else if (grossPay < 19750) return 877.50;
        else if (grossPay < 20250) return 900.00;
        else if (grossPay < 20750) return 922.50;
        else if (grossPay < 21250) return 945.00;
        else if (grossPay < 21750) return 967.50;
        else if (grossPay < 22250) return 990.00;
        else if (grossPay < 22750) return 1012.50;
        else if (grossPay < 23250) return 1035.00;
        else if (grossPay < 23750) return 1057.50;
        else if (grossPay < 24250) return 1080.00;
        else if (grossPay < 24750) return 1102.50;
        else return 1125.00;
    }

    private static double computePhilHealth(double grossPay) {
        double premium = grossPay * 0.03;

        if (premium < 300) {
            premium = 300;
        } else if (premium > 1800) {
            premium = 1800;
        }

        return premium / 2.0;
    }

    private static double computePagIbig(double grossPay) {
        double contribution;

        if (grossPay >= 1000 && grossPay <= 1500) {
            contribution = grossPay * 0.01;
        } else if (grossPay > 1500) {
            contribution = grossPay * 0.02;
        } else {
            contribution = 0;
        }

        return contribution;
    }

    private static double computeWithholdingTax(double taxableIncome) {
        if (taxableIncome <= 20833) {
            return 0;
        } else if (taxableIncome <= 33333) {
            return (taxableIncome - 20833) * 0.20;
        } else if (taxableIncome <= 66667) {
            return 2500 + (taxableIncome - 33333) * 0.25;
        } else if (taxableIncome <= 166667) {
            return 10833 + (taxableIncome - 66667) * 0.30;
        } else if (taxableIncome <= 666667) {
            return 40833.33 + (taxableIncome - 166667) * 0.32;
        } else {
            return 200833.33 + (taxableIncome - 666667) * 0.35;
        }
    }
}