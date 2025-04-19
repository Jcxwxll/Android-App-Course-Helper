package com.example.proj2;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proj2.courses.CourseListAdapter;
import com.example.proj2.courses.CoursesViewModel;
import com.example.proj2.databinding.ActivityMainBinding;

import android.view.Menu;

// This activity displays all courses and a FAB to create a new course
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private CoursesViewModel courseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up our recycler view and the course list
        final RecyclerView recyclerView = binding.recyclerView;
        final CourseListAdapter adapter = new CourseListAdapter(new CourseListAdapter.CourseDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up the provider
        courseViewModel = new ViewModelProvider(this).get(CoursesViewModel.class);
        courseViewModel.getAllCourses().observe(this, adapter::submitList);

        // Set up FAB
        binding.fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateCourseActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}