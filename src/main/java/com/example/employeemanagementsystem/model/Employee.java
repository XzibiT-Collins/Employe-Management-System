package com.example.employeemanagementsystem.model;
import java.lang.Comparable;
import java.util.List;
import java.util.OptionalDouble;

public class Employee<T> implements Comparable<Employee<T>> {
    //Package private variables
    T employeeId;
    String name;
    String department;
    double salary;
    double performanceRating;
    boolean isActive;
    int yearsOfExperience;

    //constructor
    public Employee(T employeeId, String name, String department
            , double salary, double performanceRating,
                    boolean isActive, int yearsOfExperience){
        this.employeeId = employeeId;
        this.name = name;
        this.department = department;
        this.salary = salary;
        this.performanceRating = performanceRating;
        this.isActive = isActive;
        this.yearsOfExperience = yearsOfExperience;
    }

    //Getters
    public T getEmployeeId(){
        return this.employeeId;
    }

    public String getName(){
        return this.name;
    }

    public String getDepartment(){
        return this.department;
    }

    public double getSalary(){
        return this.salary;
    }

    public double getPerformanceRating(){
        return this.performanceRating;
    }

    public int getYearsOfExperience(){
        return this.yearsOfExperience;
    }

    public boolean getIsActive() {
        return isActive;
    }

    //Setters
    public void setName(String name){
        this.name = name;
    }

    public void setDepartment(String dep){
        this.department = dep;
    }

    public void setSalary(double salary){
        this.salary = salary;
    }

    public void setPerformanceRating(double performanceRating){
        this.performanceRating = performanceRating;
    }

    public void setYearsOfExperience(int yearsOfExperience){
        this.yearsOfExperience = yearsOfExperience;
    }

    public void setIsActive(boolean isActive){
        this.isActive = isActive;
    }

    //Salary raise
    public void salaryRaise(double percentage){
        double raise = (this.salary)*(percentage/100); //calculate raise
        this.salary += raise;
    }

    //Calculate average salary of employees in a specific department
    public static OptionalDouble averageSalary(List<Employee<?>> employeeList, String dep){
        if(employeeList == null || employeeList.isEmpty() || dep == null){
            return OptionalDouble.empty();
        }

        return employeeList.stream()
                .filter(emp -> dep.equals(emp.getDepartment())) //filter based on department
                .mapToDouble(Employee::getSalary)
                .average();
    }

    //Comparable to sort Employees by years of experience
    @Override
    public int compareTo(Employee<T> o) {
        return o.yearsOfExperience - this.yearsOfExperience; //descending order
    }
}
