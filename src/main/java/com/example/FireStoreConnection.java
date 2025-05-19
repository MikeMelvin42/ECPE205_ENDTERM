package com.example;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FireStoreConnection {
    private Firestore db;

    public FireStoreConnection() throws IOException {
        try {
            // Load the service account key from resources
            InputStream serviceAccount = getClass().getClassLoader()
                    .getResourceAsStream("src/main/resources/first-f22fd-firebase-adminsdk-fbsvc-66d022565a.json");

            if (serviceAccount == null) {
                throw new IOException("Firebase credentials file not found in resources");
            }

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("\n" +
                            "https://first-f22fd-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            this.db = FirestoreClient.getFirestore();
        } catch (IOException e) {
            throw new IOException("Failed to initialize Firestore: " + e.getMessage(), e);
        }
    }

    public void addEmployee(Employee employee) throws InterruptedException, ExecutionException {
        Map<String, Object> employeeData = new HashMap<>();
        employeeData.put("idNumber", employee.getIdNumber());
        employeeData.put("name", employee.getName());
        employeeData.put("position", employee.getPosition());
        employeeData.put("employmentDate", employee.getEmploymentDate().toString());
        employeeData.put("daysPresent", employee.getDaysPresent());
        employeeData.put("daysAbsent", employee.getDaysAbsent());
        employeeData.put("salary", employee.getSalary());
        employeeData.put("contactNumber", employee.getContactNumber());

        ApiFuture<WriteResult> future = db.collection("employees")
                .document(employee.getIdNumber())
                .set(employeeData);

        future.get(); // Wait for the operation to complete
    }

    public List<Employee> getAllEmployees() throws InterruptedException, ExecutionException {
        List<Employee> employees = new ArrayList<>();
        ApiFuture<QuerySnapshot> query = db.collection("employees").get();

        for (DocumentSnapshot document : query.get().getDocuments()) {
            Employee employee = new Employee(
                    document.getString("idNumber"),
                    document.getString("name"),
                    document.getString("position"),
                    LocalDate.parse(document.getString("employmentDate")),
                    document.getLong("daysPresent").intValue(),
                    document.getLong("daysAbsent").intValue(),
                    document.getDouble("salary"),
                    document.getString("contactNumber")
            );
            employees.add(employee);
        }
        return employees;
    }

    public void updateEmployee(Employee employee) throws InterruptedException, ExecutionException {
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", employee.getName());
        updates.put("position", employee.getPosition());
        updates.put("employmentDate", employee.getEmploymentDate().toString());
        updates.put("daysPresent", employee.getDaysPresent());
        updates.put("daysAbsent", employee.getDaysAbsent());
        updates.put("salary", employee.getSalary());
        updates.put("contactNumber", employee.getContactNumber());

        ApiFuture<WriteResult> future = db.collection("employees")
                .document(employee.getIdNumber())
                .update(updates);

        future.get(); // Wait for the operation to complete
    }

    public void deleteEmployee(String idNumber) throws InterruptedException, ExecutionException {
        ApiFuture<WriteResult> future = db.collection("employees")
                .document(idNumber)
                .delete();

        future.get(); // Wait for the operation to complete
    }

    public Employee getEmployeeById(String idNumber) throws InterruptedException, ExecutionException {
        DocumentReference docRef = db.collection("employees").document(idNumber);
        DocumentSnapshot document = docRef.get().get();

        if (document.exists()) {
            return new Employee(
                    document.getString("idNumber"),
                    document.getString("name"),
                    document.getString("position"),
                    LocalDate.parse(document.getString("employmentDate")),
                    document.getLong("daysPresent").intValue(),
                    document.getLong("daysAbsent").intValue(),
                    document.getDouble("salary"),
                    document.getString("contactNumber")
            );
        }
        return null;
    }
}