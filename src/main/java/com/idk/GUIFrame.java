package com.idk;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GUIFrame extends JFrame {
    private JButton addEmployeeButton, editEmployeeButton, deleteEmployeeButton,
            payslipButton, yearEndButton;
    private JTable table;
    private EmployeeTableModel model;
    private final String[] columns = {"#", "ID Number", "Name", "Position", "Employment Date",
            "No. of Days Present", "No. of Days Absent", "Salary"};
    private FireStoreConnection fireStoreConnection;

    public GUIFrame(String title) {
        super(title);
        try {
            this.fireStoreConnection = new FireStoreConnection();
            initializeComponents();
            setupLayout();
            loadEmployees();
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            pack();
            setLocationRelativeTo(null);
            setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to initialize: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void initializeComponents() {
        addEmployeeButton = new JButton("Add Employee");
        editEmployeeButton = new JButton("Edit Employee");
        deleteEmployeeButton = new JButton("Delete Employee");
        payslipButton = new JButton("Generate Payslip");
        yearEndButton = new JButton("Year End Report");

        model = new EmployeeTableModel(columns);
        table = new JTable(model);

        // Add button actions
        addEmployeeButton.addActionListener(e -> {
            AddEmployeeDialog dialog = new AddEmployeeDialog(this, fireStoreConnection, model);
            dialog.setVisible(true);
        });

        editEmployeeButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                        "Please select an employee to edit",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Employee selected = model.getEmployeeAt(selectedRow);
            AddEmployeeDialog dialog = new AddEmployeeDialog(
                    this, fireStoreConnection, model, selected);
            dialog.setVisible(true);
        });

        deleteEmployeeButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                        "Please select an employee to delete",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Employee selected = model.getEmployeeAt(selectedRow);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Delete employee " + selected.getName() + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    fireStoreConnection.deleteEmployee(selected.getIdNumber());
                    loadEmployees();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error deleting employee: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void setupLayout() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addEmployeeButton);
        buttonPanel.add(editEmployeeButton);
        buttonPanel.add(deleteEmployeeButton);
        buttonPanel.add(payslipButton);
        buttonPanel.add(yearEndButton);

        add(buttonPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        setPreferredSize(new Dimension(1000, 600));
    }

    private void loadEmployees() {
        try {
            List<Employee> employees = fireStoreConnection.getAllEmployees();
            model.updateEmployeeList(employees);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading employees: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}