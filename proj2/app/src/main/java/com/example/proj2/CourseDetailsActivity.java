package com.example.proj2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.proj2.courses.CoursesViewModel;
import com.example.proj2.databinding.ActivityCreateCourseBinding;
import com.example.proj2.domain.Course;

// This activity displays course information and enrolled students. There is also
// a back button to return to the main activity
public class CourseDetailsActivity extends AppCompatActivity {
    private CoursesViewModel coursesViewModel;
    private ActivityCreateCourseBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get the course view model (this holds the course repository we need)
        coursesViewModel = new ViewModelProvider(this).get(CoursesViewModel.class);
    }
}
