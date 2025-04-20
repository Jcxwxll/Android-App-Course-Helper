package com.example.proj2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.proj2.databinding.ActivityEditStudentBinding;
import com.example.proj2.domain.Student;
import com.example.proj2.students.StudentsViewModel;

public class EditStudentActivity extends AppCompatActivity {
    private ActivityEditStudentBinding binding;
    private StudentsViewModel viewModel;
    private int studentId;
    private String originalUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        studentId = getIntent().getIntExtra("studentId", -1);

        viewModel = new ViewModelProvider(this)
                .get(StudentsViewModel.class);

        viewModel.getStudent(studentId)
                .observe(this, student -> {
                    if (student != null) {
                        binding.studentNameInputText.setText(student.getName());
                        binding.studentEmailInputText.setText(student.getEmail());
                        originalUserName = student.getUserName();
                    }
                });

        binding.backButton.setOnClickListener(v -> finish());

        binding.addStudentButton.setOnClickListener(v -> {
            String newName  = binding.studentNameInputText.getText().toString().trim();
            String newEmail = binding.studentEmailInputText.getText().toString().trim();

            Student updated = new Student(newName, newEmail, originalUserName);
            updated.setStudentId(studentId);

            viewModel.update(updated);
            finish();
        });
    }
}
