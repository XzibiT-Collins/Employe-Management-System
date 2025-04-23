package com.example.employeemanagementsystem.exceptionHandling;

public class EmployeeNotFoundException extends Exception {
    public EmployeeNotFoundException(String message){
        super(message);
    }
}
