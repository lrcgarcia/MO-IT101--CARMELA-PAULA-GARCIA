# MotorPH Payroll System

## Description
This project is a simple payroll system made for MotorPH. It reads employee information and timecard records from text files, then computes each employee’s gross pay, benefits, deductions, and net pay.

The program also prints a detailed payslip for each employee and a payroll summary report for all employees.

This project was created as part of a programming requirement.

## Features
- Reads employee data from `employees.txt`
- Reads attendance records from `timecard.txt`
- Computes total worked hours
- Calculates gross pay using hourly rate
- Adds benefits:
  - Rice subsidy
  - Phone allowance
  - Clothing allowance
- Applies deductions:
  - SSS
  - PhilHealth
  - Pag-IBIG
  - Withholding Tax
- Displays employee payslips
- Displays payroll summary report

## Files Used
- `MotorPHPayroll.java` – main Java program
- `employees.txt` – employee details
- `timecard.txt` – attendance records

## How to Run
1. Open the project in NetBeans
2. Make sure `employees.txt` and `timecard.txt` are in the project root folder
3. Run `MotorPHPayroll.java`
4. The output will display the payslips and payroll summary

## Notes
- The data used in this project is simplified for academic purposes
- Government contribution values are based on the provided project tables
- This is a console-based payroll system

## Author
Carmela Paula Garcia
