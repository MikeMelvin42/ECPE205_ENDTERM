package org.example;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FireStoreConnection {
    private Firestore db;

    public FireStoreConnection() throws Exception {
        FileInputStream serviceAccount = new FileInputStream("src/main/java/org/example/first-f22fd-firebase-adminsdk-fbsvc-72e38c05e6.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }

        this.db = FirestoreClient.getFirestore();
    }

    public void addEmployee(Employee employee) throws ExecutionException, InterruptedException {
        Map<String, Object> data = new HashMap<>();
        data.put("idNumber", employee.getIdNumber());
        data.put("firstName", employee.getFirstName());
        data.put("lastName", employee.getLastName());
        data.put("address", employee.getAddress());
        data.put("contactNumber", employee.getContactNumber());

        db.collection("employees").document(employee.getIdNumber()).set(data).get();
    }

    public List<Employee> getAllEmployees() throws ExecutionException, InterruptedException {
        List<Employee> employees = new ArrayList<>();

        ApiFuture<QuerySnapshot> query = db.collection("employees").get();
        QuerySnapshot querySnapshot = query.get();

        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
            Employee employee = new Employee(
                    document.getString("idNumber"),
                    document.getString("firstName"),
                    document.getString("lastName"),
                    document.getString("address"),
                    document.getString("contactNumber")
            );
            employees.add(employee);
        }

        return employees;
    }

    public Employee getEmployeeById(String idNumber) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = db.collection("employees").document(idNumber).get().get();
        if (document.exists()) {
            return new Employee(
                    document.getString("idNumber"),
                    document.getString("firstName"),
                    document.getString("lastName"),
                    document.getString("address"),
                    document.getString("contactNumber")
            );
        }
        return null;
    }

    public void updateEmployee(Employee employee) throws ExecutionException, InterruptedException {
        Map<String, Object> data = new HashMap<>();
        data.put("idNumber", employee.getIdNumber());
        data.put("firstName", employee.getFirstName());
        data.put("lastName", employee.getLastName());
        data.put("address", employee.getAddress());
        data.put("contactNumber", employee.getContactNumber());

        db.collection("employees").document(employee.getIdNumber()).set(data).get();
    }

    public void deleteEmployee(String idNumber) throws ExecutionException, InterruptedException {
        db.collection("employees").document(idNumber).delete().get();
    }
}