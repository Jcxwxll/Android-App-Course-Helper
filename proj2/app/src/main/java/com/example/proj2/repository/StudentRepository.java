package com.example.proj2.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.proj2.dao.CourseDao;
import com.example.proj2.dao.EnrollmentDao;
import com.example.proj2.dao.StudentDao;
import com.example.proj2.domain.Enrollment;
import com.example.proj2.domain.Student;
import com.example.proj2.room.CMSDB;

import java.util.ArrayList;
import java.util.List;

public class StudentRepository {
    private StudentDao studentDao;
    private EnrollmentDao enrollmentDao;
    private CourseDao courseDao;
    private LiveData<List<Student>> allStudents;

    public StudentRepository(Application application) {
        CMSDB db = CMSDB.getDatabase(application);
        studentDao = db.studentDao();
        enrollmentDao = db.enrollmentDao();
        courseDao = db.courseDao();
        allStudents = studentDao.getAllStudents();
    }

    public LiveData<List<Student>> getAllStudents() {
        return allStudents;
    }

    public LiveData<List<Student>> getStudentsByCourse(int courseId) {
        return studentDao.getStudentsForCourse(courseId);
    }

    public void insert(Student student) {
        CMSDB.databaseWriteExecutor.execute(() -> {
            studentDao.insert(student);
        });
    }

    public void deleteAll() {
        CMSDB.databaseWriteExecutor.execute(() -> {
            studentDao.deleteAll();
        });
    }

    public LiveData<Student> getStudent(int studentId) {
        return studentDao.getStudent(studentId);
    }

    public void update(Student student) {
        CMSDB.databaseWriteExecutor.execute(() -> {
            studentDao.update(student);
        });
    }
}
