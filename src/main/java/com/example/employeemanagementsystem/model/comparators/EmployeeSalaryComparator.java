package com.example.employeemanagementsystem.model.comparators;

import com.example.employeemanagementsystem.model.Employee;

import java.util.Comparator;

public class EmployeeSalaryComparator<T> implements Comparator<Employee<T>> {
    @Override
    public int compare(Employee<T> emp1, Employee<T> emp2) {
        //handle cases of null employees
        if(emp1 == emp2){
            return 0;
        }
        if(emp1 == null){
            return 1;
        };
        if(emp2 == null){
            return -1;
        }

        //handle cases of null attributes
        Double salary1 = emp1.getSalary();
        Double salary2 = emp2.getSalary();

        if(salary1 == salary2){
            return 0;
        }
        if(salary1 == null){
            return 1;
        }
        if(salary2 == null){
            return -1;
        }

        return Double.compare(salary2,salary1); //descending order
    }
}
