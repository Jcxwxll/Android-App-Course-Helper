package com.example.proj2.students;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.proj2.domain.Student;
import com.example.proj2.repository.StudentRepository;

import java.util.List;

public class StudentsViewModel extends AndroidViewModel {
    public StudentRepository studentRepository;
    private final LiveData<List<Student>> allStudents;

    public StudentsViewModel(Application application) {
        super(application);
        studentRepository = new StudentRepository(application);
        allStudents = studentRepository.getAllStudents();
    }

    public LiveData<List<Student>> getAllStudents() {
        return allStudents;
    }

    public LiveData<List<Student>> getStudentsByCourse(int courseId) {
        return studentRepository.getStudentsByCourse(courseId);
    }

    public void insert(Student student) {
        studentRepository.insert(student);
    }

    public void deleteAll() {
        studentRepository.deleteAll();
    }

    public LiveData<Student> getStudent(int studentId) {
        return studentRepository.getStudent(studentId);
    }

    public void update(Student student) {
        studentRepository.update(student);
    }


}
