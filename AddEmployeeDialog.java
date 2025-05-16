package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AddEmployeeDialog extends JDialog {
    private JTextField idNumberField, firstNameField, lastNameField, addressField, contactNumberField;
    private JButton saveButton, cancelButton;
    private FireStoreConnection fireStoreConnection;
    private EmployeeTableModel model;
    private boolean isEditMode = false;
    private Employee employeeToEdit;

    public AddEmployeeDialog(JFrame parent, FireStoreConnection fireStoreConnection, EmployeeTableModel model) {
        super(parent, "Add New Employee", true);
        this.fireStoreConnection = fireStoreConnection;
        this.model = model;
        initializeUI();
    }

    public AddEmployeeDialog(JFrame parent, FireStoreConnection fireStoreConnection,
                             EmployeeTableModel model, Employee employeeToEdit) {
        this(parent, fireStoreConnection, model);
        this.employeeToEdit = employeeToEdit;
        this.isEditMode = true;
        setTitle("Edit Employee");
        populateFields();
    }

    private void initializeUI() {
        setLayout(new GridLayout(6, 2, 5, 5));

        // Create fields
        idNumberField = new JTextField();
        firstNameField = new JTextField();
        lastNameField = new JTextField();
        addressField = new JTextField();
        contactNumberField = new JTextField();

        // Create buttons
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");

        // Add components
        add(new JLabel("ID Number:"));
        add(idNumberField);
        add(new JLabel("First Name:"));
        add(firstNameField);
        add(new JLabel("Last Name:"));
        add(lastNameField);
        add(new JLabel("Address:"));
        add(addressField);
        add(new JLabel("Contact Number:"));
        add(contactNumberField);
        add(saveButton);
        add(cancelButton);

        // Add action listeners
        saveButton.addActionListener(e -> saveEmployee());
        cancelButton.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(getParent());
    }

    private void populateFields() {
        if (employeeToEdit != null) {
            idNumberField.setText(employeeToEdit.getIdNumber());
            firstNameField.setText(employeeToEdit.getFirstName());
            lastNameField.setText(employeeToEdit.getLastName());
            addressField.setText(employeeToEdit.getAddress());
            contactNumberField.setText(employeeToEdit.getContactNumber());
        }
    }

    private void saveEmployee() {
        try {
            Employee employee = new Employee(
                    idNumberField.getText(),
                    firstNameField.getText(),
                    lastNameField.getText(),
                    addressField.getText(),
                    contactNumberField.getText()
            );

            if (isEditMode) {
                fireStoreConnection.updateEmployee(employee);
            } else {
                fireStoreConnection.addEmployee(employee);
            }

            // Refresh the table
            List<Employee> employees = fireStoreConnection.getAllEmployees();
            model.updateEmployeeList(employees);

            dispose();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving employee: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}