package com.example.proj2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proj2.databinding.ActivityAddStudentBinding;

public class AddStudentActivity extends AppCompatActivity {
    private ActivityAddStudentBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int courseId = getIntent().getIntExtra("courseId", -1);
        if (courseId == -1) {
            // Adding a new student
            binding.titleTextView.setText(getString(R.string.add_new_student));
            // Set up button to return back to main activity
            Button backButton = binding.backButton;
            backButton.setOnClickListener(v -> {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            });
        } else {
            // Adding a new student OR enrolling student in course
            binding.titleTextView.setText(getString(R.string.enrol_student));
            // Set up button to return back to course details activity
            Button backButton = binding.backButton;
            backButton.setOnClickListener(v -> {
                Intent intent = new Intent(this, CourseDetailsActivity.class);
                intent.putExtra("courseId", courseId);
                startActivity(intent);
            });
        }
    }
}
