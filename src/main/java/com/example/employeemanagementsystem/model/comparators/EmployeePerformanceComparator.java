package com.example.employeemanagementsystem.model.comparators;

import com.example.employeemanagementsystem.model.Employee;

import java.util.Comparator;

public class EmployeePerformanceComparator<T> implements Comparator<Employee<T>> {

    //sorting by performance Rating
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
        Double pr1 = emp1.getPerformanceRating();
        Double pr2 = emp2.getPerformanceRating();

        if(pr1 == pr2){
            return 0;
        }
        if(pr1 == null){
            return 1;
        }
        if(pr2 == null){
            return -1;
        }
        return Double.compare(pr2,pr1); //descending order
    }
}