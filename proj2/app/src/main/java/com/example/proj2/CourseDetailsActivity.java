package com.example.proj2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.proj2.utils.LiveDataUtils;
import com.example.proj2.courses.CoursesViewModel;
import com.example.proj2.databinding.ActivityCourseDetailsBinding;

// This activity displays course information and enrolled students. There is also
// a back button to return to the main activity
public class CourseDetailsActivity extends AppCompatActivity {
    private CoursesViewModel coursesViewModel;
    private ActivityCourseDetailsBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCourseDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get the course view model (this holds the course repository we need)
        coursesViewModel = new ViewModelProvider(this).get(CoursesViewModel.class);

        int courseId = getIntent().getIntExtra("courseId", -1);
        if (courseId == -1) {
            failedToFindCourse();
            return;
        }

        // Set up button to return back to main activity
        Button backToMainButton = binding.backToMainButton;
        backToMainButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        // Fetch the course from the database
        LiveDataUtils.observeOnce(coursesViewModel.courseRepository.getCourse(courseId), this, course -> {
            if (course != null) {
                binding.titleTextView.setText(getString(R.string.viewing_course_title, course.getCourseName()));
                binding.courseCodeTextView.setText(course.getCourseCode());
                binding.courseNameTextView.setText(course.getCourseName());
                binding.lecturerNameTextView.setText(course.getLecturerName());
            } else {
                failedToFindCourse();
            }
        });

        // Set up button to add student
        binding.addEnrollmentButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddStudentActivity.class);
            intent.putExtra("courseId", courseId);
            startActivity(intent);
        });
    }

    private void failedToFindCourse() {
        Toast.makeText(this, "Failed to find course. Please try again later.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
