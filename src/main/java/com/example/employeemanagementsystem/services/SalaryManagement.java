package com.example.employeemanagementsystem.services;

import com.example.employeemanagementsystem.model.Employee;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SalaryManagement<T> {

    //Return top 5 employees with the highest salary
    public List<Employee<T>> top5highestPaidEmployees(List<Employee<T>> allemployees){
        if(allemployees == null || allemployees.isEmpty()){
            return Collections.emptyList(); // return an empty list
        }

        //sort incoming employee list
        return allemployees.stream()
                .sorted(Comparator.comparingDouble((Employee<T> emp)->emp.getSalary()).reversed())
                .limit(5)
                .toList(); // return 5 employees with the highest salary
    }
}
