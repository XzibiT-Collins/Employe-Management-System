package com.example.employeemanagementsystem.ui;

import com.example.employeemanagementsystem.db.EmployeeDatabase;
import com.example.employeemanagementsystem.model.Employee;
import com.example.employeemanagementsystem.model.comparators.EmployeePerformanceComparator;
import com.example.employeemanagementsystem.model.comparators.EmployeeSalaryComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Test {
    public static void main(String[] args){
        //create a database for employees
        EmployeeDatabase<String> db = new EmployeeDatabase<>();

        //create employees
        Employee<String> emp1 = new Employee<>("E001","John Doe","IT",10000.00,5.5,true,2);
        Employee<String> emp2 = new Employee<>("E002","Collins Yaw","Finance",1000.00,1.5,true,4);
        Employee<String> emp3 = new Employee<>("E003","Maxwell Craig","Accounting",50000.00,6.5,true,8);

        //add employees to list
        db.addEmployee(emp1);
        db.addEmployee(emp2);
        db.addEmployee(emp3);

        //Print all employees
        System.out.println("CALLING PRINT ALL EMPLOYEES ON DATABASE:");
        db.printAllEmployees();
        System.out.println();

        //Sort employee using comparable
        List<Employee<String>> employeesList =new ArrayList<> (db.getAllEmployees());
        //apply sort on List using Collections.sort()
        Collections.sort(employeesList);
        //Print sorted List
        System.out.println("RESULTS OF COMPARABLE SORTING BY YoE IN DESCENDING ORDER:");

        System.out.printf("%-15s %-20s %-15s %-10s %-15s %-15s %n", "ID", "NAME", "DEPARTMENT", "PR", "YoE","SALARY");

        employeesList.forEach(e ->
                System.out.printf("%-15s %-20s %-15s %-10.1f %-15d %-15.2f %n",
                        e.getEmployeeId(),
                        e.getName(),
                        e.getDepartment(),
                        e.getPerformanceRating(),
                        e.getYearsOfExperience(),
                        e.getSalary()
                )
        );

        System.out.println();
        System.out.println("APPLYING SALARY RAISE TO EMPLOYEES ABOVE 2.0 PR:");

        //Perform salary raise on all employees above 2.0 PR
        db.salaryRaise(0.0,10);
        //print to verify salary raise
        db.printAllEmployees();


        //Custom Sorting using Comparators
        System.out.println();
        System.out.println("APPLYING CUSTOM SORTING USING COMPARATORS:");
        System.out.println("EMPLOYEE PERFORMANCE COMPARATOR");
        Collections.sort(employeesList, new EmployeePerformanceComparator<>());
        employeesList.forEach(e ->
                System.out.printf("%-15s %-20s %-15s %-10.1f %-15d %-15.2f %n",
                        e.getEmployeeId(),
                        e.getName(),
                        e.getDepartment(),
                        e.getPerformanceRating(),
                        e.getYearsOfExperience(),
                        e.getSalary()
                )
        );

        System.out.println();
        System.out.println("EMPLOYEE SALARY COMPARATOR");
        Collections.sort(employeesList, new EmployeeSalaryComparator<>());
        employeesList.forEach(e ->
                System.out.printf("%-15s %-20s %-15s %-10.1f %-15d %-15.2f %n",
                        e.getEmployeeId(),
                        e.getName(),
                        e.getDepartment(),
                        e.getPerformanceRating(),
                        e.getYearsOfExperience(),
                        e.getSalary()
                )
        );
    }
}
