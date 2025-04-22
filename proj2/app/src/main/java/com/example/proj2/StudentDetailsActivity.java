package com.example.proj2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proj2.students.StudentsViewModel;
import com.example.proj2.students.StudentCoursesAdapter;

public class StudentDetailsActivity extends AppCompatActivity {

    private StudentsViewModel studentsViewModel;
    private TextView studentName, studentEmail;
    private RecyclerView coursesRecyclerView;
    private StudentCoursesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        // Pull the studentId back out as an int (matches how CourseDetailsActivity put it)
        int rawId = getIntent().getIntExtra("studentId", -1);
        if (rawId < 0) {
            // No valid studentId → bail out
            finish();
            return;
        }
        // Pull the courseId for the back button
        int courseId = getIntent().getIntExtra("courseId", -1);
        if (courseId < 0) {
            // No valid courseId → bail out
            finish();
            return;
        }

        findViewById(R.id.backButton).setOnClickListener(v -> {
            Intent addIntent = new Intent(this, CourseDetailsActivity.class);
            addIntent.putExtra("courseId", courseId);
            startActivity(addIntent);
        });

        long studentId = rawId;

        studentName        = findViewById(R.id.studentNameText);
        studentEmail       = findViewById(R.id.studentEmailText);
        coursesRecyclerView = findViewById(R.id.coursesRecyclerView);

        // RecyclerView + Adapter for courses
        adapter = new StudentCoursesAdapter();
        coursesRecyclerView.setAdapter(adapter);
        coursesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ViewModel
        studentsViewModel = new ViewModelProvider(this).get(StudentsViewModel.class);

        // Observe and display the student’s details
        studentsViewModel.getStudentById(studentId).observe(this, student -> {
            if (student != null) {
                studentName.setText(student.getName());
                studentEmail.setText(student.getEmail());
            }
        });

        // Observe and display the list of their courses
        studentsViewModel.getCoursesForStudent(studentId).observe(this, courses -> {
            adapter.setCourses(courses);
        });
    }
}
