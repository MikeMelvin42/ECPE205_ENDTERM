package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GUIFrame extends JFrame {
    private JButton addEmployeeButton, removeEmployeeButton, editButton, detailsButton;
    private JTable table;
    private EmployeeTableModel model;
    private FireStoreConnection fireStoreConnection;

    public GUIFrame(String title) throws Exception {
        // Initialize Firestore connection and table model
        fireStoreConnection = new FireStoreConnection();
        model = new EmployeeTableModel();
        table = new JTable(model);

        // Set white background for the frame
        getContentPane().setBackground(Color.WHITE);

        // Configure the table appearance
        configureTableAppearance();

        // Initialize buttons
        addEmployeeButton = new JButton("Add Employee");
        removeEmployeeButton = new JButton("Remove Employee");
        editButton = new JButton("Edit");
        detailsButton = new JButton("Details");

        // Set button backgrounds
        Color buttonColor = new Color(240, 240, 240); // Light gray
        addEmployeeButton.setBackground(buttonColor);
        removeEmployeeButton.setBackground(buttonColor);
        editButton.setBackground(buttonColor);
        detailsButton.setBackground(buttonColor);

        // Set layout
        setLayout(new BorderLayout());

        // Add components to the frame
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(addEmployeeButton);
        buttonPanel.add(removeEmployeeButton);
        buttonPanel.add(editButton);
        buttonPanel.add(detailsButton);

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        addEmployeeButton.addActionListener(e -> {
            AddEmployeeDialog dialog = new AddEmployeeDialog(this, fireStoreConnection, model);
            dialog.setVisible(true);
        });

        removeEmployeeButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                try {
                    Employee employee = model.getEmployeeAt(selectedRow);
                    fireStoreConnection.deleteEmployee(employee.getIdNumber());
                    model.removeEmployee(selectedRow);
                } catch (ExecutionException | InterruptedException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error deleting employee: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an employee to remove",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Load initial data
        loadEmployeeData();

        // Frame settings
        setTitle(title);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void configureTableAppearance() {
        // Set table background and colors
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.setGridColor(new Color(220, 220, 220)); // Light gray grid lines
        table.setSelectionBackground(new Color(200, 230, 255)); // Light blue selection
        table.setSelectionForeground(Color.BLACK);
        table.setRowHeight(25);
        table.setShowGrid(true);
        table.setFillsViewportHeight(true);

        // Set header appearance
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(70, 130, 180)); // Steel blue
        header.setForeground(Color.WHITE);
        header.setFont(new Font("SansSerif", Font.BOLD, 12));

        // Center-align all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Make the table non-opaque to let the background show through
        table.setOpaque(false);
    }

    private void loadEmployeeData() {
        try {
            List<Employee> employees = fireStoreConnection.getAllEmployees();
            model.updateEmployeeList(employees);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading employee data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}