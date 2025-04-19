package com.example.proj2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.proj2.courses.CoursesViewModel;
import com.example.proj2.databinding.ActivityCreateCourseBinding;
import com.example.proj2.domain.Course;

import java.util.Objects;

// This activity displays a form for the user to fill out to create a
// new course, a create button, and a back button to return to the main activity
public class CreateCourseActivity extends AppCompatActivity {
    private CoursesViewModel coursesViewModel;
    private ActivityCreateCourseBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get the course view model (this holds the course repository we need)
        coursesViewModel = new ViewModelProvider(this).get(CoursesViewModel.class);

        // Set up the forum
        EditText courseCodeEditText = binding.courseCodeEditText;
        EditText courseNameEditText = binding.courseNameEditText;
        EditText lecturerNameEditText = binding.lecturerNameEditText;
        Button createCourseButton = binding.createCourseButton;

        // Set up button to return back to main activity
        Button backToMainButton = binding.backToMainButton;
        backToMainButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        // Finally the button to create course!
        createCourseButton.setOnClickListener(v -> {
            // Get input values
            String courseCode = Objects.requireNonNull(courseCodeEditText.getText()).toString().trim();
            String courseName = Objects.requireNonNull(courseNameEditText.getText()).toString().trim();
            String lecturerName = Objects.requireNonNull(lecturerNameEditText.getText()).toString().trim();

            // Validate
            if (courseCode.isEmpty()) {
                Toast.makeText(this, "Course code cannot be empty.", Toast.LENGTH_LONG).show();
                return;
            }
            if (courseName.isEmpty()) {
                Toast.makeText(this, "Course name cannot be empty.", Toast.LENGTH_LONG).show();
                return;
            }
            if (lecturerName.isEmpty()) {
                Toast.makeText(this, "Lecturer name cannot be empty.", Toast.LENGTH_LONG).show();
                return;
            }

            Toast.makeText(this, "Course created.", Toast.LENGTH_LONG).show();

            // Add to DB and redirect back to to MainActivity
            Course newCourse = new Course(courseCode, courseName, lecturerName);
            coursesViewModel.insert(newCourse);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }
}