package com.example.employeemanagementsystem.db;
import com.example.employeemanagementsystem.exceptionHandling.EmployeeNotFoundException;
import com.example.employeemanagementsystem.exceptionHandling.InvalidDepartmentException;
import com.example.employeemanagementsystem.exceptionHandling.InvalidSalaryException;
import com.example.employeemanagementsystem.model.Employee;

import java.util.*;

public class EmployeeDatabase<T> {
    //create a new HashMap
    private final Map<T, Employee<T>> employeeMap = new HashMap<>(); // Final to prevent reassignment

    //add Employee to Map
    public void addEmployee(Employee<T> emp){
        // Validate input
        if (emp == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }

        T id = emp.getEmployeeId();
        if (id == null) {
            throw new IllegalArgumentException("Employee ID cannot be null");
        }

        // Prevent duplicate IDs
        if (employeeMap.containsKey(id)) {
            throw new IllegalStateException("Employee with ID " + id + " already exists");
        }

        employeeMap.put(id, emp);
    }

    //Remove or delete an Employee from Map
    public boolean removeEmployee(T employeeId) throws EmployeeNotFoundException {
        //throw an unchecked error when ID not found
        if(!employeeMap.containsKey(employeeId)){
            throw new EmployeeNotFoundException("The specified ID does not match any records.");
        }
        //use Employee Key=id to remove employee from Map
        employeeMap.remove(employeeId);
        return true;
    }

    //Retrieve all employees
    public List<Employee<T>> getAllEmployees(){
        return new ArrayList<>(employeeMap.values()); //return a copy of original Map
    }

    //Update Employee details
    public void updateEmployeeDetails(T employeeId, String field, Object newValue) throws EmployeeNotFoundException {
        //check if hashMap contains the id to be updated
        if(employeeMap.containsKey(employeeId)){
            //get employee to update
            Employee<T> emp = employeeMap.get(employeeId);
            // update field
            String lowerField = field.toLowerCase(); //convert input to lowercase

            switch (lowerField) {
                case "name" -> {
                    String value = (String) newValue;
                    emp.setName(value);
                }
                case "department" -> {
                    System.out.println("CHANGING DEPARTMENT");
                    String value = (String) newValue;
                    emp.setDepartment(value);
                }
                case "salary" -> {
                    double value = (double) newValue;
                    //throw unchecked exception when salary is negative
                    if(value < 0.0){
                        throw new InvalidSalaryException("Salary cannot be negative.");
                    }
                    emp.setSalary(value);
                }
                case "performancerating" -> {
                    double value = (double) newValue;
                    emp.setPerformanceRating(value);
                }
                case "yearsofexperience" -> {
                    int value = (int) newValue;
                    emp.setYearsOfExperience(value);
                }
                case "isactive" -> {
                    boolean value = (boolean) newValue;
                    emp.setIsActive(value);
                }
                default -> {
                }
            }
        }else{
            throw new EmployeeNotFoundException("Employee with specified ID not found");
        }
    }

    //Searching and Filtering
    //Search for employees by Department
    public List<Employee<T>> searchEmployeeByDepartment(String department){
        if(department.isBlank()){//validate input
            throw new IllegalArgumentException("Department cannot be blank.");
        }
        if(employeeMap.isEmpty()){
            return Collections.emptyList();
        }
        //use streams to filter and sort database
        List<Employee<T>> results = employeeMap.values().stream()
                .filter(e -> department.equalsIgnoreCase(e.getDepartment()))
                .toList();

        //throw unchecked exception when department is unknown.
        if(results.isEmpty()){
            throw new InvalidDepartmentException("No employee belongs to specified department.");
        }
        return results;
    }

    //Search for employees whose name contains a search term
    public List<Employee<T>> searchEmployeeByName(String nameSearchTerm){
        if(nameSearchTerm.isBlank()){//validate input
            throw new IllegalArgumentException("Name cannot be blank.");
        }
        if(employeeMap.isEmpty()){
            return Collections.emptyList();
        }

        String normalizedSearchTerm = nameSearchTerm.trim().toLowerCase();
        //use streams to filter and sort database
        return employeeMap.values().stream()
                .filter(e-> e.getName() != null)//Prevents Null Error
                .filter(e -> e.getName().toLowerCase().contains(normalizedSearchTerm))
                .toList();
    }

    //Search employee whose performance rating is higher than a specified value
    public List<Employee<T>> searchEmployeeByPerformanceRating(double performanceRating){
        if(performanceRating < 0.0){//validate input
            throw new IllegalArgumentException("Rating cannot be a negative number");
        }

        //return results
        return employeeMap.values().stream()
                .filter(e->e.getPerformanceRating() >= performanceRating)
                .sorted(Comparator.comparingDouble(Employee::getPerformanceRating))
                .toList();
    }

    //Search for employees within a specific salary range
    public List<Employee<T>> searchEmployeeSalaryRange(double minAmount,double maxAmount){
        //throw unchecked exception when salary values are invalid
        //Validate amount
        if(minAmount<0.0 || maxAmount < 0.0){
            throw new InvalidSalaryException("Amount cannot be negative");
        }
        //Ensure min amount is always less than max amount
        if(minAmount>maxAmount){
            throw new InvalidSalaryException("Minimum amount cannot be bigger than maximum amount.");
        }

        //ensure employee database is not empty
        if(employeeMap.isEmpty()){
            return Collections.emptyList();
        }

        return employeeMap.values().stream()
                .filter(e-> (e.getSalary() >= minAmount && e.getSalary()<=maxAmount))
                .sorted(Comparator.comparingDouble(Employee::getSalary))
                .toList();
    }

    //Print out employee details to terminal
    public void printAllEmployees() {
        if (employeeMap.isEmpty()) {
            System.out.println("No employees to display");
            return;
        }

//        System.out.println(employeeMap);

        System.out.printf("%-15s %-20s %-15s %-10s %-15s %-15s%n", "ID", "NAME", "DEPARTMENT", "PR", "YoE","SALARY");

        employeeMap.forEach((key, value) ->
                System.out.printf("%-15s %-20s %-15s %-10.1f %-15d %-15.2f %n",
                        value.getEmployeeId().toString(),
                        value.getName(),
                        value.getDepartment(),
                        value.getPerformanceRating(),
                        value.getYearsOfExperience(),
                        value.getSalary()
                )
        );

    }

    //Salary Raise for employees with over 4.5 performance Rating
    public void salaryRaise(double minRating, double raisePercentage){
        if(raisePercentage < 0.0){
            throw new InvalidSalaryException("Operation Unsuccessful:\n Rating cannot be negative");
        }
        if(employeeMap.isEmpty()){
            throw new IllegalArgumentException("Cannot perform salary raise operation on null employee List");
        }

        //Streams to help access employee data
        employeeMap.values().stream()
                .filter(e -> e.getPerformanceRating() >= minRating)
                .forEach((e -> {
                    double currentSalary = e.getSalary();
                    double newSalary = currentSalary * (1 + raisePercentage / 100); // calculate new salary includes base
                    e.setSalary(newSalary); // update employee salary
                }));
    }
}
