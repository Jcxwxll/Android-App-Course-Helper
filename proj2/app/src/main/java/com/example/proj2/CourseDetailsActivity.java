package com.example.proj2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proj2.databinding.ActivityCourseDetailsBinding;
import com.example.proj2.domain.Student;
import com.example.proj2.room.CMSDB;
import com.example.proj2.students.StudentListAdapter;
import com.example.proj2.students.StudentsViewModel;
import com.example.proj2.utils.LiveDataUtils;

public class CourseDetailsActivity extends AppCompatActivity {
    private ActivityCourseDetailsBinding binding;
    private CMSDB db;
    private StudentsViewModel studentsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCourseDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize the DB and VM
        db = CMSDB.getDatabase(this);
        studentsViewModel = new ViewModelProvider(this).get(StudentsViewModel.class);

        // Retrieve courseId
        int courseId = getIntent().getIntExtra("courseId", -1);
        if (courseId == -1) {
            Toast.makeText(this, "Failed to find course!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Back button
        binding.backToMainButton.setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class))
        );

        // Display course info once
        LiveDataUtils.observeOnce(
                db.courseDao().getCourse(courseId),
                this,
                course -> {
                    if (course != null) {
                        binding.titleTextView.setText(
                                getString(R.string.viewing_course_title, course.getCourseName()));
                        binding.courseCodeTextView.setText(course.getCourseCode());
                        binding.courseNameTextView.setText(course.getCourseName());
                        binding.lecturerNameTextView.setText(course.getLecturerName());
                    }
                }
        );

        // RecyclerView + Adapter for enrolled students
        RecyclerView recyclerView = binding.recyclerView;
        StudentListAdapter adapter =
                new StudentListAdapter(new StudentListAdapter.StudentDiff(), courseId);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Observe students in this course
        studentsViewModel.getStudentsByCourse(courseId).observe(this, students -> {
            if (students == null || students.isEmpty()) {
                binding.noEnrollmentsTextView.setVisibility(RecyclerView.VISIBLE);
            } else {
                binding.noEnrollmentsTextView.setVisibility(RecyclerView.GONE);
            }
            adapter.submitList(students);
        });

        binding.addStudentButton.setOnClickListener(v -> {
            Intent addIntent = new Intent(this, AddStudentActivity.class);
            addIntent.putExtra("courseId", courseId);
            startActivity(addIntent);
        });
    }

    /** Student options dialog (View Details / Edit / Remove) */
    public void showStudentOptionsDialog(Student student, int courseId) {
        new AlertDialog.Builder(this)
                .setTitle("Student Options")
                .setItems(new String[]{"View Details", "Edit", "Remove"}, (dialog, which) -> {
                    switch (which) {
                        case 0:  // View Details
                            Intent detailIntent = new Intent(this, StudentDetailsActivity.class);
                            detailIntent.putExtra("studentId", student.getStudentId());
                            detailIntent.putExtra("courseId", courseId);
                            startActivity(detailIntent);
                            break;

                        case 1:  // Edit
                            Intent editIntent = new Intent(this, EditStudentActivity.class);
                            editIntent.putExtra("studentId", student.getStudentId());
                            editIntent.putExtra("courseId", courseId);
                            startActivity(editIntent);
                            break;

                        case 2:  // Remove
                            CMSDB.databaseWriteExecutor.execute(() ->
                                    db.enrollmentDao()
                                            .deleteEnrollmentByCourseIdAndStudentId(courseId,
                                                    student.getStudentId()));
                            break;
                    }
                })
                .show();
    }
}
