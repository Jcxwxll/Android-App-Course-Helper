package com.example.proj2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proj2.databinding.ActivityAddStudentBinding;
import com.example.proj2.domain.Course;
import com.example.proj2.domain.Student;
import com.example.proj2.room.CMSDB;
import com.example.proj2.utils.LiveDataUtils;

import java.util.Objects;

public class AddStudentActivity extends AppCompatActivity {
    private CMSDB db;
    private ActivityAddStudentBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = CMSDB.getDatabase(this);

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
            // The Metric input layout should be hidden for this because
            // it will be auto generated
            binding.studentMetricInputLayout.setVisibility(View.GONE);
            // Finally the button to add student!
            binding.addStudentButton.setOnClickListener(v -> {
                // Get input values
                String studentName = Objects.requireNonNull(binding.courseNameEditText.getText()).toString().trim();
                String studentEmail = Objects.requireNonNull(binding.lecturerNameEditText.getText()).toString().trim();

                if (addStudent(studentName, studentEmail)) {
                    // Success, now move back to main activity
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            binding.titleTextView.setText(getString(R.string.loading));

            // Fetch the course from the database
            LiveDataUtils.observeOnce(db.courseDao().getCourse(courseId), this, course -> {
                if (course != null) {
                    // Adding a new student OR enrolling student in course
                    binding.titleTextView.setText(getString(R.string.enrol_student, course.getCourseName()));

                    // Set up button to return back to course details activity
                    Button backButton = binding.backButton;
                    backButton.setOnClickListener(v -> {
                        Intent intent = new Intent(this, CourseDetailsActivity.class);
                        intent.putExtra("courseId", courseId);
                        startActivity(intent);
                    });

                    // Finally the button to enrol a student!
                    binding.addStudentButton.setOnClickListener(v -> {
                        // Get input values
                        String studentMetric = Objects.requireNonNull(binding.courseCodeEditText.getText()).toString().trim();
                        String studentName = Objects.requireNonNull(binding.courseNameEditText.getText()).toString().trim();
                        String studentEmail = Objects.requireNonNull(binding.lecturerNameEditText.getText()).toString().trim();

                        if (enrolStudentToCourse(studentMetric, studentName, studentEmail, course)) {
                            // Success, now move back to course details activity
                            Intent intent = new Intent(this, CourseDetailsActivity.class);
                            intent.putExtra("courseId", courseId);
                            startActivity(intent);
                        }
                    });
                } else {
                    // Could not find course, send back to main activity
                    Toast.makeText(this, "Failed to find course. Please try again later.", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private boolean addStudent(String studentName, String studentEmail) {
        if (studentName.isEmpty() || studentEmail.isEmpty()) {
            Toast.makeText(this, "Name or Email must not be empty.", Toast.LENGTH_LONG).show();
            return false;
        }
        Student student = new Student(studentName, studentEmail);
        db.studentDao().insert(student);
        return true;
    }

    private boolean enrolStudentToCourse(String studentMetric, String studentName, String studentEmail, Course course) {
        return false;
    }
}
