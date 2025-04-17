package com.example.employeemanagementsystem.model.comparators;

import com.example.employeemanagementsystem.model.Employee;

import java.util.Comparator;

public class EmployeePerformanceComparator<T> implements Comparator<Employee<T>> {

    //sorting by performance Rating
    @Override
    public int compare(Employee<T> emp1, Employee<T> emp2) {
        return Double.compare(emp2.getPerformanceRating(),emp1.getPerformanceRating());
    }
}