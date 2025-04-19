package com.example.proj2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proj2.domain.Student;
import com.example.proj2.room.CMSDB;
import com.example.proj2.students.StudentListAdapter;
import com.example.proj2.students.StudentsViewModel;
import com.example.proj2.utils.LiveDataUtils;
import com.example.proj2.databinding.ActivityCourseDetailsBinding;

// This activity displays course information and enrolled students. There is also
// a back button to return to the main activity
public class CourseDetailsActivity extends AppCompatActivity {
    private CMSDB db;
    private ActivityCourseDetailsBinding binding;
    private StudentsViewModel studentsViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCourseDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = CMSDB.getDatabase(this);

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
        LiveDataUtils.observeOnce(db.courseDao().getCourse(courseId), this, course -> {
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

        // Fetch the enrolled students from the database
        {
            final RecyclerView recyclerView = binding.recyclerView;
            final StudentListAdapter adapter = new StudentListAdapter(new StudentListAdapter.StudentDiff(), courseId);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Set up the provider
            studentsViewModel = new ViewModelProvider(this).get(StudentsViewModel.class);
            studentsViewModel.getStudentsByCourse(courseId).observe(this, students -> {
                if (students == null || students.isEmpty()) {
                    binding.noEnrollmentsTextView.setVisibility(View.VISIBLE);
                } else {
                    binding.noEnrollmentsTextView.setVisibility(View.GONE);
                }
                adapter.submitList(students);
            });
        }
    }

    private void failedToFindCourse() {
        Toast.makeText(this, "Failed to find course. Please try again later", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void showStudentOptionsDialog(Student student, int courseId) {
        String[] options = {"Edit", "Remove"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Student Options")
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0: // "Edit"
                            // TODO: Navigate to EditStudentActivity
                            break;
                        case 1: // "Remove"
                            CMSDB.databaseWriteExecutor.execute(() -> {
                                db.enrollmentDao().deleteEnrollmentByCourseIdAndStudentId(courseId, student.getStudentId());
                            });
                            break;
                    }
                });
        builder.create().show();
    }
}
