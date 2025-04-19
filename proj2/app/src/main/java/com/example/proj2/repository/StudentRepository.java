package com.example.proj2.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.proj2.dao.StudentDao;
import com.example.proj2.domain.Student;
import com.example.proj2.room.CMSDB;

import java.util.List;

public class StudentRepository {
    private StudentDao studentDao;
    private LiveData<List<Student>> allStudents;

    public StudentRepository(Application application) {
        CMSDB db = CMSDB.getDatabase(application);
        studentDao = db.studentDao();
        allStudents = studentDao.getAllStudents();
    }

    public LiveData<List<Student>> getAllStudents() {
        return allStudents;
    }

    public LiveData<Student> getCourse(int studentId) {
        return studentDao.getStudent(studentId);
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
}
