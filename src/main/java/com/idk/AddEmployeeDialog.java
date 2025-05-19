package com.idk;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.time.format.DateTimeParseException;

public class AddEmployeeDialog extends JDialog {
    private JTextField idNumberField, nameField, positionField,
            employmentDateField, daysPresentField, daysAbsentField, salaryField;
    private JButton saveButton, cancelButton;
    private FireStoreConnection fireStoreConnection;
    private EmployeeTableModel model;
    private boolean isEditMode = false;
    private Employee employeeToEdit;

    public AddEmployeeDialog(JFrame parent, FireStoreConnection fireStoreConnection,
                             EmployeeTableModel model, Employee employeeToEdit) {
        this(parent, fireStoreConnection, model);
        this.employeeToEdit = employeeToEdit;
        this.isEditMode = true;
        setTitle("Edit Employee");
        populateFields();
    }

    public AddEmployeeDialog(JFrame parent, FireStoreConnection fireStoreConnection,
                             EmployeeTableModel model) {
        super(parent, "Add New Employee", true);
        this.fireStoreConnection = fireStoreConnection;
        this.model = model;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new GridLayout(8, 2, 10, 10)); // Back to 8 rows
        getContentPane().setBackground(Color.WHITE);

        idNumberField = new JTextField();
        nameField = new JTextField();
        positionField = new JTextField();
        employmentDateField = new JTextField();
        daysPresentField = new JTextField();
        daysAbsentField = new JTextField();
        salaryField = new JTextField();

        saveButton = createStyledButton("Save");
        cancelButton = createStyledButton("Cancel");

        add(createLabel("ID Number:"));
        add(idNumberField);
        add(createLabel("Name:"));
        add(nameField);
        add(createLabel("Position:"));
        add(positionField);
        add(createLabel("Employment Date (YYYY-MM-DD):"));
        add(employmentDateField);
        add(createLabel("Days Present:"));
        add(daysPresentField);
        add(createLabel("Days Absent:"));
        add(daysAbsentField);
        add(createLabel("Salary:"));
        add(salaryField);
        add(saveButton);
        add(cancelButton);

        saveButton.addActionListener(e -> saveEmployee());
        cancelButton.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(getParent());
    }

    private void populateFields() {
        if (employeeToEdit != null) {
            idNumberField.setText(employeeToEdit.getIdNumber());
            nameField.setText(employeeToEdit.getName());
            positionField.setText(employeeToEdit.getPosition());
            employmentDateField.setText(employeeToEdit.getEmploymentDate().toString());
            daysPresentField.setText(String.valueOf(employeeToEdit.getDaysPresent()));
            daysAbsentField.setText(String.valueOf(employeeToEdit.getDaysAbsent()));
            salaryField.setText(String.format("%.2f", employeeToEdit.getSalary()));
            idNumberField.setEditable(false);
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        return button;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.BLACK);
        return label;
    }

    private void saveEmployee() {
        if (!validateInput()) return;

        try {
            Employee employee = new Employee(
                    idNumberField.getText(),
                    nameField.getText(),
                    positionField.getText(),
                    LocalDate.parse(employmentDateField.getText()),
                    Integer.parseInt(daysPresentField.getText()),
                    Integer.parseInt(daysAbsentField.getText()),
                    Double.parseDouble(salaryField.getText())
            );

            if (isEditMode) {
                fireStoreConnection.updateEmployee(employee);
                JOptionPane.showMessageDialog(this,
                        "Employee updated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                fireStoreConnection.addEmployee(employee);
                JOptionPane.showMessageDialog(this,
                        "Employee added successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            List<Employee> employees = fireStoreConnection.getAllEmployees();
            model.updateEmployeeList(employees);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error saving employee: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateInput() {
        if (idNumberField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "ID Number is required",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Name is required",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (positionField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Position is required",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            LocalDate.parse(employmentDateField.getText());
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter employment date in YYYY-MM-DD format",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            Integer.parseInt(daysPresentField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid number for days present",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            Integer.parseInt(daysAbsentField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid number for days absent",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            Double.parseDouble(salaryField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid salary amount",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }
}