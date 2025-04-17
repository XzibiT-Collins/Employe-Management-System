package com.example.employeemanagementsystem.model.comparators;

import com.example.employeemanagementsystem.model.Employee;

import java.util.Comparator;

public class EmployeeSalaryComparator<T> implements Comparator<Employee<T>> {
    @Override
    public int compare(Employee<T> emp1, Employee<T> emp2) {
        return Double.compare(emp2.getSalary(),emp1.getSalary());
    }
}
