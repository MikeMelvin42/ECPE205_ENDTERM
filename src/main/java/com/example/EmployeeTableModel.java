package com.example;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class EmployeeTableModel extends AbstractTableModel {
    private final String[] columnNames;
    private List<Employee> employees;

    public EmployeeTableModel(String[] columnNames) {
        this.columnNames = columnNames;
        this.employees = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return employees.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Employee employee = employees.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> rowIndex + 1;
            case 1 -> employee.getIdNumber();
            case 2 -> employee.getName();
            case 3 -> employee.getPosition();
            case 4 -> employee.getEmploymentDate();
            case 5 -> employee.getDaysPresent();
            case 6 -> employee.getDaysAbsent();
            case 7 -> employee.getSalary();
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void updateEmployeeList(List<Employee> employees) {
        this.employees = employees;
        fireTableDataChanged();
    }

    public Employee getEmployeeAt(int row) {
        return employees.get(row);
    }
}