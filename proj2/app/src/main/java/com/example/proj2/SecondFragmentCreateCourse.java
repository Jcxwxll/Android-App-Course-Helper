package com.example.proj2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.navigation.NavController;

import com.example.proj2.courses.CoursesViewModel;
import com.example.proj2.databinding.FragmentCreateCourseBinding;
import com.example.proj2.domain.Course;

import java.util.Objects;

public class SecondFragmentCreateCourse extends Fragment {
    private CoursesViewModel coursesViewModel;
    private FragmentCreateCourseBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentCreateCourseBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the course view model (this holds the course repository we need)
        coursesViewModel = new ViewModelProvider(requireActivity()).get(CoursesViewModel.class);

        // Add toolbar
        Toolbar toolbar = binding.toolbar;
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(toolbar);
        NavController navController = NavHostFragment.findNavController(this);
        NavigationUI.setupWithNavController(toolbar, navController);

        // Set up the forum
        EditText courseCodeEditText = binding.courseCodeEditText;
        EditText courseNameEditText = binding.courseNameEditText;
        EditText lecturerNameEditText = binding.lecturerNameEditText;
        Button createCourseButton = binding.createCourseButton;

        // Finally the button to create course!
        createCourseButton.setOnClickListener(v -> {
            // Get input values
            String courseCode = Objects.requireNonNull(courseCodeEditText.getText()).toString().trim();
            String courseName = Objects.requireNonNull(courseNameEditText.getText()).toString().trim();
            String lecturerName = Objects.requireNonNull(lecturerNameEditText.getText()).toString().trim();

            // Validate
            if (courseCode.isEmpty()) {
                Toast.makeText(getContext(), "Course code cannot be empty.", Toast.LENGTH_LONG).show();
                return;
            }
            if (courseName.isEmpty()) {
                Toast.makeText(getContext(), "Course name cannot be empty.", Toast.LENGTH_LONG).show();
                return;
            }
            if (lecturerName.isEmpty()) {
                Toast.makeText(getContext(), "Lecturer name cannot be empty.", Toast.LENGTH_LONG).show();
                return;
            }

            Toast.makeText(getContext(), "Course created.", Toast.LENGTH_LONG).show();

            // Add to DB and redirect back to the first fragment
            Course newCourse = new Course(courseCode, courseName, lecturerName);
            coursesViewModel.insert(newCourse);

            NavHostFragment.findNavController(SecondFragmentCreateCourse.this)
                    .navigate(R.id.action_SecondFragmentCreateCourse_to_FirstFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}