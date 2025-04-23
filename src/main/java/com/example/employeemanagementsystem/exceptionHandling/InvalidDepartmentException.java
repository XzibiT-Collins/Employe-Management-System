package com.example.employeemanagementsystem.exceptionHandling;

public class InvalidDepartmentException extends RuntimeException{
    public InvalidDepartmentException(String message){
        super(message);
    }
}
