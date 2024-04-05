package com.example.firebaseexample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private EditText editTextId, editTextName, editTextAge;
    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("students");

        // Initialize UI elements
        editTextId = findViewById(R.id.editTextId);
        editTextName = findViewById(R.id.editTextName);
        editTextAge = findViewById(R.id.editTextAge);
        textViewResult = findViewById(R.id.textViewResult);
    }

    // Create student record
    public void createStudent(View view) {
        String id = editTextId.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        int age = Integer.parseInt(editTextAge.getText().toString().trim());
        Student student = new Student(id, name, age);
        databaseReference.child(id).setValue(student);
        clearFields();
        textViewResult.setText("Student created: " + student.getName());
    }

    // Read all student records
    public void readStudents(View view) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Student> students = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Student student = snapshot.getValue(Student.class);
                    students.add(student);
                }
                displayStudents(students);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                textViewResult.setText("Failed to read students: " + databaseError.getMessage());
            }
        });
    }
    // Update student record
    // Update student record
    public void updateStudent(View view) {
        String id = editTextId.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String ageText = editTextAge.getText().toString().trim();

        if (id.isEmpty()) {
            textViewResult.setText("Please enter student ID to update.");
            return;
        }

        if (name.isEmpty() || ageText.isEmpty()) {
            textViewResult.setText("Please enter name and age to update student.");
            return;
        }

        int age = Integer.parseInt(ageText);
        Student student = new Student(id, name, age);
        databaseReference.child(id).setValue(student);
        clearFields();
        textViewResult.setText("Student updated: " + student.getName());
    }


    // Display student records
    private void displayStudents(List<Student> students) {
        StringBuilder result = new StringBuilder();
        for (Student student : students) {
            result.append("ID: ").append(student.getId()).append(", Name: ").append(student.getName())
                    .append(", Age: ").append(student.getAge()).append("\n");
        }
        textViewResult.setText(result.toString());
    }

    // Clear input fields
    private void clearFields() {
        editTextId.setText("");
        editTextName.setText("");
        editTextAge.setText("");
    }
}