package com.example.employeemanagementsystem;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import java.util.logging.Logger;

import com.example.employeemanagementsystem.db.EmployeeDatabase;
import com.example.employeemanagementsystem.exceptionHandling.EmployeeNotFoundException;
import com.example.employeemanagementsystem.exceptionHandling.InvalidSalaryException;
import com.example.employeemanagementsystem.model.Employee;
import com.example.employeemanagementsystem.model.comparators.EmployeePerformanceComparator;
import com.example.employeemanagementsystem.model.comparators.EmployeeSalaryComparator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField deleteID;

    @FXML
    private TextField department;

    @FXML
    private TextField id;

    @FXML
    private CheckBox isActive;

    @FXML
    private TextField maxValue;

    @FXML
    private TextField minValue;

    @FXML
    private TextField name;

    @FXML
    private TextField newValue;

    @FXML
    private TextField performanceRating;

    @FXML
    private TextField salary;

    @FXML
    private ComboBox<String> searchFilter;

    @FXML
    private TableView<Employee<String>> searchTable;

    @FXML
    private TextField searchTerm;

    @FXML
    private ComboBox<String> sortFilter;

    @FXML
    private TableView<Employee<String>> sortTable;

    @FXML
    private TableView<Employee<String>> sortTable1;

    @FXML
    private TableColumn<Employee<String>, String> search_DepCol;

    @FXML
    private TableColumn<Employee<String>, String> search_IdCol;

    @FXML
    private TableColumn<Employee<String>, String> search_NameCol;

    @FXML
    private TableColumn<Employee<String>, Double> search_PrCol;

    @FXML
    private TableColumn<Employee<String>, Double> search_SalaryCol;

    @FXML
    private TableColumn<Employee<String>, Boolean> search_StatusCol;

    @FXML
    private TableColumn<Employee<String>, Integer> search_YoeCol;

    @FXML
    private TableColumn<Employee<String>, String> sort_DepCol;

    @FXML
    private TableColumn<Employee<String>, String> sort_IdCol;

    @FXML
    private TableColumn<Employee<String>, String> sort_NameCol;

    @FXML
    private TableColumn<Employee<String>, Double> sort_PrCol;

    @FXML
    private TableColumn<Employee<String>, Double> sort_SalaryCol;

    @FXML
    private TableColumn<Employee<String>, Boolean> sort_StatusCol;

    @FXML
    private TableColumn<Employee<String>, Integer> sort_YoeCol;

    @FXML
    private TableColumn<Employee<String>, String> emp_IdCol;

    @FXML
    private TableColumn<Employee<String>, String> emp_NameCol;

    @FXML
    private TableColumn<Employee<String>, String> emp_DepCol;

    @FXML
    private TableColumn<Employee<String>, Integer> emp_YoeCol;

    @FXML
    private TableColumn<Employee<String>, Double> emp_PrCol;

    @FXML
    private TableColumn<Employee<String>, Double> emp_SalaryCol;

    @FXML
    private TableColumn<Employee<String>, Boolean> emp_StatusCol;

    @FXML
    private ComboBox<String> updateField;

    @FXML
    private TextField updateID;

    @FXML
    private TextField yearsOfExperience;

    private static final Logger logger = Logger.getLogger(MainController.class.getName());

    // Database instance
    private final EmployeeDatabase<String> db = new EmployeeDatabase<>();

    @FXML
    void handleDeleteEmployee(ActionEvent event) throws EmployeeNotFoundException {
        String strDelId = deleteID.getText().trim();

        if(strDelId.isEmpty()) {
            showAlert("Error", "ID cannot be empty", Alert.AlertType.ERROR);
            return;
        }

        try{
            db.removeEmployee(strDelId);
            showAlert("Success", "Employee deleted successfully", Alert.AlertType.INFORMATION);
            deleteID.clear();
            //update employee list
            handlePopulateEmployeeTable();
        }catch(Exception e){
            showAlert("Error", "Employee not found", Alert.AlertType.ERROR);
            logger.info("An error occurred "+ e.getMessage());
        }
    }

    @FXML
    void handleEmployeeCreation(ActionEvent event) {
        // Get input values
        String e_id = id.getText().trim();
        String e_name = name.getText().trim();
        String e_department = department.getText().trim();
        String str_salary = salary.getText().trim();
        String str_Pr = performanceRating.getText().trim();
        String str_YoE = yearsOfExperience.getText().trim();
        boolean e_isActive = isActive.isSelected();

        // Validate required fields
        if(e_id.isEmpty() || e_name.isEmpty() || e_department.isEmpty()) {
            showAlert("Error", "ID, Name, and Department are required", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Convert and validate numerical values
            double e_salary = str_salary.isEmpty() ? 0 : Double.parseDouble(str_salary);
            double e_Pr = str_Pr.isEmpty() ? 0 : Double.parseDouble(str_Pr);
            int e_YoE = str_YoE.isEmpty() ? 0 : Integer.parseInt(str_YoE);

            // Create and add employee
            Employee<String> emp = new Employee<>(e_id, e_name, e_department, e_salary, e_Pr, e_isActive, e_YoE);
            db.addEmployee(emp);

            //update employee table
            handlePopulateEmployeeTable();

            // Clear fields and show success
            clearCreationFields();
            showAlert("Success", "Employee created successfully", Alert.AlertType.INFORMATION);
            db.printAllEmployees();
        } catch (NumberFormatException e) {
            logger.info(e.getMessage());
            showAlert("Error", "Invalid number format for Salary, Performance Rating, or Years of Experience", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void handleSearchEmployee(ActionEvent event) {
        String filter = searchFilter.getValue();
        if(filter == null) {
            showAlert("Error", "Please select a search filter", Alert.AlertType.ERROR);
            return;
        }

        List<Employee<String>> employeeList = null;

        switch(filter) {
            case "Department":
                String deptValue = searchTerm.getText().trim();
                if(!deptValue.isEmpty()) {
                    try{
                        employeeList = db.searchEmployeeByDepartment(deptValue);
                    }catch (Exception e){
                        logger.info(e.getMessage());
                        showAlert("Error", e.getMessage(), Alert.AlertType.WARNING);
                    }
                }
                break;

            case "Name":
                String nameValue = searchTerm.getText().trim();
                if(!nameValue.isEmpty()) {
                    try{
                        employeeList = db.searchEmployeeByName(nameValue);
                    }catch (Exception e){
                        logger.info(e.getMessage());
                        showAlert("Error", e.getMessage(), Alert.AlertType.WARNING);
                    }
                }
                break;

            case "Salary":
                try {
                    double min = minValue.getText().isEmpty() ? 0 : Double.parseDouble(minValue.getText());
                    double max = maxValue.getText().isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxValue.getText());
                    try{
                        employeeList = db.searchEmployeeSalaryRange(min, max);
                    }catch(InvalidSalaryException e){
                        logger.info(e.getMessage());
                        showAlert("WARNING",e.getMessage(),Alert.AlertType.WARNING);
                    }
                } catch (NumberFormatException e) {
                    logger.info(e.getMessage());
                    showAlert("Error", "Invalid salary range values", Alert.AlertType.ERROR);
                }
                break;

            case "Performance":
                try {
                    double min = minValue.getText().isEmpty() ? 0 : Double.parseDouble(minValue.getText());
                    employeeList = db.searchEmployeeByPerformanceRating(min);
                } catch (NumberFormatException e) {
                    logger.info(e.getMessage());
                    showAlert("Error", "Invalid performance rating value", Alert.AlertType.ERROR);
                } catch (IllegalArgumentException e){
                    logger.info(e.getMessage());
                    showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
                }
                break;
        }

        if(employeeList != null && !employeeList.isEmpty()) {
            ObservableList<Employee<String>> employeeData = FXCollections.observableArrayList(employeeList);
            searchTable.setItems(employeeData);
        } else {
            searchTable.getItems().clear();
            showAlert("Info", "No employees found matching criteria", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    void handleSortEmployees(ActionEvent event) {
        String sortTerm = sortFilter.getValue();
        sortTable.refresh();

        if(sortTerm == null) {
            showAlert("Error", "Please select a sort filter", Alert.AlertType.ERROR);
            return;
        }

        List<Employee<String>> employeeList = db.getAllEmployees();
        if(employeeList == null || employeeList.isEmpty()) {
            showAlert("Info", "No employees to sort", Alert.AlertType.INFORMATION);
            return;
        }

        switch (sortTerm) {
            case "Salary":
                employeeList.sort(new EmployeeSalaryComparator());
                break;
            case "Performance Rating":
                employeeList.sort(new EmployeePerformanceComparator());
                break;
            case "Years of Experience":
                Collections.sort(employeeList);
                break;
        }

        ObservableList<Employee<String>> employeeData = FXCollections.observableArrayList(employeeList);
        sortTable.setItems(employeeData);
    }

    @FXML
    void handleUpdateEmployee(ActionEvent event) {
        String id = updateID.getText().trim();
        String str_field = updateField.getValue();
        String str_newVal = newValue.getText().trim();

        if(id.isEmpty() || str_field == null || str_newVal.isEmpty()) {
            showAlert("Error", "All update fields are required", Alert.AlertType.ERROR);
            return;
        }

        try {
            switch (str_field) {
                case "Performance Rating":
                case "Salary":
                    double e_value = Double.parseDouble(str_newVal);
                    db.updateEmployeeDetails(id, str_field, e_value);
                    break;
                case "Years of Experience":
                    int e_YoE = Integer.parseInt(str_newVal);
                    db.updateEmployeeDetails(id, str_field, e_YoE);
                    break;
                default:
                    db.updateEmployeeDetails(id, str_field, str_newVal);
            }
            //update employee table
            handlePopulateEmployeeTable();

            showAlert("Success", "Employee updated successfully", Alert.AlertType.INFORMATION);
            updateID.clear();
            newValue.clear();
        } catch (NumberFormatException | EmployeeNotFoundException e) {
            showAlert("Error", "Invalid number format for " + str_field, Alert.AlertType.ERROR);
            logger.info(e.getMessage());
        }
    }


    @FXML
    void handlePopulateEmployeeTable() {
        List<Employee<String>> employeeList = db.getAllEmployees();
        if(employeeList == null || employeeList.isEmpty()) {
            //showAlert("Info", "No employees to display", Alert.AlertType.INFORMATION);
            return;
        }

        ObservableList<Employee<String>> employeeData = FXCollections.observableArrayList(employeeList);
        sortTable1.setItems(employeeData);
    }

    @FXML
    void initialize() {
        // Validate FXML injections
        assert deleteID != null : "fx:id=\"deleteID\" was not injected: check your FXML file 'home.fxml'.";
        assert department != null : "fx:id=\"department\" was not injected: check your FXML file 'home.fxml'.";
        assert id != null : "fx:id=\"id\" was not injected: check your FXML file 'home.fxml'.";
        assert isActive != null : "fx:id=\"isActive\" was not injected: check your FXML file 'home.fxml'.";
        assert maxValue != null : "fx:id=\"maxValue\" was not injected: check your FXML file 'home.fxml'.";
        assert minValue != null : "fx:id=\"minValue\" was not injected: check your FXML file 'home.fxml'.";
        assert name != null : "fx:id=\"name\" was not injected: check your FXML file 'home.fxml'.";
        assert newValue != null : "fx:id=\"newValue\" was not injected: check your FXML file 'home.fxml'.";
        assert performanceRating != null : "fx:id=\"performanceRating\" was not injected: check your FXML file 'home.fxml'.";
        assert salary != null : "fx:id=\"salary\" was not injected: check your FXML file 'home.fxml'.";
        assert searchFilter != null : "fx:id=\"searchFilter\" was not injected: check your FXML file 'home.fxml'.";
        assert searchTable != null : "fx:id=\"searchTable\" was not injected: check your FXML file 'home.fxml'.";
        assert sortFilter != null : "fx:id=\"sortFilter\" was not injected: check your FXML file 'home.fxml'.";
        assert sortTable != null : "fx:id=\"sortTable\" was not injected: check your FXML file 'home.fxml'.";
        assert updateField != null : "fx:id=\"updateField\" was not injected: check your FXML file 'home.fxml'.";
        assert updateID != null : "fx:id=\"updateID\" was not injected: check your FXML file 'home.fxml'.";
        assert yearsOfExperience != null : "fx:id=\"yearsOfExperience\" was not injected: check your FXML file 'home.fxml'.";

        // Initialize combo boxes
        searchFilter.getItems().addAll("Department", "Name", "Salary", "Performance");
        sortFilter.getItems().addAll("Salary", "Performance Rating", "Years of Experience");
        updateField.getItems().addAll("Name", "Department", "Salary", "Performance Rating", "Years of Experience", "Status");

        // Configure table columns
        initializeTableColumns();

        //Populate Employee data table
        handlePopulateEmployeeTable();

        // Hide input fields initially
        minValue.setVisible(false);
        maxValue.setVisible(false);
        searchTerm.setVisible(false);

        // Add listeners to show/hide appropriate search fields
        searchFilter.valueProperty().addListener((obs, oldVal, newVal) -> {
            searchTerm.setVisible(newVal.equals("Department") || newVal.equals("Name"));
            minValue.setVisible(newVal.equals("Salary") || newVal.equals("Performance"));
            maxValue.setVisible(newVal.equals("Salary"));
        });
    }

    private void initializeTableColumns() {
        // Initialize sort table columns
        sort_IdCol.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        sort_NameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        sort_DepCol.setCellValueFactory(new PropertyValueFactory<>("department"));
        sort_YoeCol.setCellValueFactory(new PropertyValueFactory<>("yearsOfExperience"));
        sort_PrCol.setCellValueFactory(new PropertyValueFactory<>("performanceRating"));
        sort_SalaryCol.setCellValueFactory(new PropertyValueFactory<>("salary"));
        sort_StatusCol.setCellValueFactory(new PropertyValueFactory<>("isActive"));

        // Initialize search table columns
        search_IdCol.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        search_NameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        search_DepCol.setCellValueFactory(new PropertyValueFactory<>("department"));
        search_YoeCol.setCellValueFactory(new PropertyValueFactory<>("yearsOfExperience"));
        search_PrCol.setCellValueFactory(new PropertyValueFactory<>("performanceRating"));
        search_SalaryCol.setCellValueFactory(new PropertyValueFactory<>("salary"));
        search_StatusCol.setCellValueFactory(new PropertyValueFactory<>("isActive")); // Initialize search table columns

        emp_IdCol.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        emp_NameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emp_DepCol.setCellValueFactory(new PropertyValueFactory<>("department"));
        emp_YoeCol.setCellValueFactory(new PropertyValueFactory<>("yearsOfExperience"));
        emp_PrCol.setCellValueFactory(new PropertyValueFactory<>("performanceRating"));
        emp_SalaryCol.setCellValueFactory(new PropertyValueFactory<>("salary"));
        emp_StatusCol.setCellValueFactory(new PropertyValueFactory<>("isActive"));
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearCreationFields() {
        id.clear();
        name.clear();
        department.clear();
        salary.clear();
        performanceRating.clear();
        yearsOfExperience.clear();
        isActive.setSelected(false);
    }
}