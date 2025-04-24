package com.example.employeemanagementsystem.db;

import com.example.employeemanagementsystem.exceptionHandling.EmployeeNotFoundException;
import com.example.employeemanagementsystem.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeDatabaseTest {
    private EmployeeDatabase<String> employeeDatabase;
    private Employee<String> employee;

    @BeforeEach
    void setup(){
        //create employee
        employee = new Employee<>("E001","Collins",
                "BackEnd",5000.00,8.5,
                true,1);

        employeeDatabase = new EmployeeDatabase<>();
    }

    @Test
    void addEmployee() {
        //try to add employee
        employeeDatabase.addEmployee(employee);


        //assertions
        assertEquals(1,employeeDatabase.getAllEmployees().size());
        assertNotNull(employeeDatabase.getAllEmployees().getFirst());
        assertThrows(IllegalStateException.class, () ->{
            employeeDatabase.addEmployee(employee);
        });
    }

    @Test
    void removeEmployee() throws EmployeeNotFoundException {
        //remove an employee using ID
        //create employee for delete test
        Employee<String> emp1 = new Employee<>("E002","Test User",
                "QA",1000.00,6.8,
                true,4);
        employeeDatabase.addEmployee(emp1);
        employeeDatabase.removeEmployee("E002");

        //assertions
        assertEquals(0,employeeDatabase.getAllEmployees().size());
//        assertNotNull(employeeDatabase.getAllEmployees().getFirst());
        assertThrows(EmployeeNotFoundException.class, ()->{
            employeeDatabase.removeEmployee(emp1.getEmployeeId());
        });
    }

    @Test
    void searchEmployeeByDepartment(){
        employeeDatabase.addEmployee(employee);
        //search employee
        List<Employee<String>> results = employeeDatabase.searchEmployeeByDepartment("BackEnd");

        //assertions
        assertEquals(1,results.size());
        assertEquals("Collins", results.getFirst().getName());
    }
}