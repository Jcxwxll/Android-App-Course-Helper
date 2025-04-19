package com.example.proj2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.proj2.dao.EnrollmentDao;
import com.example.proj2.databinding.ActivityAddStudentBinding;
import com.example.proj2.domain.Course;
import com.example.proj2.domain.Enrollment;
import com.example.proj2.domain.Student;
import com.example.proj2.room.CMSDB;
import com.example.proj2.utils.LiveDataUtils;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

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
            // The Matric input layout should be hidden for this because
            // it will be auto generated
            binding.studentMatricInputLayout.setVisibility(View.GONE);

            AtomicBoolean Working = new AtomicBoolean(false);

            // Finally the button to add student!
            binding.addStudentButton.setOnClickListener(v -> {
                // Get input values
                String studentName = Objects.requireNonNull(binding.studentNameInputText.getText()).toString().trim();
                String studentEmail = Objects.requireNonNull(binding.studentEmailInputText.getText()).toString().trim();

                if (Working.get()) {
                    Toast.makeText(this, "A request is already in process. Please try again later.", Toast.LENGTH_LONG).show();
                    return;
                }
                Working.set(true);

                addStudent(studentName, studentEmail, new StudentResultCallback() {
                    @Override
                    public void onResult(boolean success) {
                        if (success) {
                            // Success, now move back to main activity
                            Intent intent = new Intent(AddStudentActivity.this, MainActivity.class);
                            startActivity(intent);
                            return;
                        }
                        Working.set(false);
                    }
                });
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

                    AtomicBoolean Working = new AtomicBoolean(false);

                    // Finally the button to enrol a student!
                    binding.addStudentButton.setOnClickListener(v -> {
                        // Get input values
                        String studentMatricString = Objects.requireNonNull(binding.studentMatricInputText.getText()).toString();
                        int studentMatric;
                        try {
                            if (!studentMatricString.isEmpty()) {
                                studentMatric = Integer.parseInt(studentMatricString);
                            } else {
                                Toast.makeText(this, "Matric must not be empty.", Toast.LENGTH_LONG).show();
                                return;
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(this, "Input a valid Matric, must be an integer.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        String studentName = Objects.requireNonNull(binding.studentNameInputText.getText()).toString().trim();
                        String studentEmail = Objects.requireNonNull(binding.studentEmailInputText.getText()).toString().trim();

                        if (Working.get()) {
                            Toast.makeText(this, "A request is already in process. Please try again later.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Working.set(true);

                        enrolStudentToCourse(studentMatric, studentName, studentEmail, course, new EnrollmentResultCallback() {
                            @Override
                            public void onResult(boolean success) {
                                if (success) {
                                    // Success, now move back to course details activity
                                    Intent intent = new Intent(AddStudentActivity.this, CourseDetailsActivity.class);
                                    intent.putExtra("courseId", courseId);
                                    startActivity(intent);
                                    return;
                                }
                                Working.set(false);
                            }
                        });
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

    interface StudentResultCallback {
        void onResult(boolean success);
    }

    private void addStudent(String studentName, String studentEmail, StudentResultCallback callback) {
        if (studentName.isEmpty() || studentEmail.isEmpty()) {
            Toast.makeText(this, "Name or Email must not be empty.", Toast.LENGTH_LONG).show();
            callback.onResult(false);
            return;
        }
        Toast.makeText(this, "Added student.", Toast.LENGTH_SHORT).show();
        CMSDB.databaseWriteExecutor.execute(() -> {
            Student student = new Student(studentName, studentEmail);
            db.studentDao().insert(student);
            callback.onResult(true);
        });
    }

    interface EnrollmentResultCallback {
        void onResult(boolean success);
    }

    private void enrolStudentToCourse(int studentMatric, String studentName, String studentEmail, Course course, EnrollmentResultCallback callback) {
        if (studentMatric < 0) {
            Toast.makeText(this, "Matric must not be negative.", Toast.LENGTH_LONG).show();
            callback.onResult(false);
            return;
        }
        if (studentName.isEmpty() || studentEmail.isEmpty()) {
            Toast.makeText(this, "Name or Email must not be empty.", Toast.LENGTH_LONG).show();
            callback.onResult(false);
            return;
        }
        EnrollmentDao enrollmentDao = db.enrollmentDao();
        LiveData<Enrollment> enrollmentLiveData = enrollmentDao.getEnrollmentByCourseIdAndStudentId(course.getCourseId(), studentMatric);
        LiveDataUtils.observeOnce(enrollmentLiveData, this, enrollmentResult -> {
            if (enrollmentResult != null) {
                Toast.makeText(this, "Student is already enrolled.", Toast.LENGTH_LONG).show();
                callback.onResult(false);
            } else {
                CMSDB.databaseWriteExecutor.execute(() -> {
                    Student existingStudent = db.studentDao().getStudentSync(studentMatric);
                    if (existingStudent == null) {
                        existingStudent = new Student(studentMatric, studentName, studentEmail);
                        db.studentDao().insert(existingStudent);
                    }

                    Enrollment newEnrollment = new Enrollment(existingStudent.getStudentId(), course.getCourseId());
                    enrollmentDao.insert(newEnrollment);

                    runOnUiThread(() -> {
                        Toast.makeText(this, "Enrolled student.", Toast.LENGTH_SHORT).show();
                        callback.onResult(true);
                    });
                });
            }
        });
    }
}
