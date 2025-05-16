package org.example;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class EmployeeTableModel extends AbstractTableModel {
    private List<Employee> employees;
    private String[] columns = {"#", "ID Number", "Last Name", "First Name", "Address", "Phone Number"};

    public EmployeeTableModel() {
        employees = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return employees.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Employee employee = employees.get(rowIndex);
        switch (columnIndex) {
            case 0: return rowIndex + 1;
            case 1: return employee.getIdNumber();
            case 2: return employee.getLastName();
            case 3: return employee.getFirstName();
            case 4: return employee.getAddress();
            case 5: return employee.getContactNumber();
            default: return null;
        }
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
        fireTableRowsInserted(employees.size() - 1, employees.size() - 1);
    }

    public void removeEmployee(int rowIndex) {
        employees.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public Employee getEmployeeAt(int rowIndex) {
        return employees.get(rowIndex);
    }

    public void updateEmployeeList(List<Employee> newEmployees) {
        employees = new ArrayList<>(newEmployees);
        fireTableDataChanged();
    }
}