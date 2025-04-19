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
        MediatorLiveData<List<Student>> studentsInCourse = new MediatorLiveData<>();
        LiveData<List<Enrollment>> enrollmentsForCourse = enrollmentDao.getEnrollmentsByCourseId(courseId);

        studentsInCourse.addSource(enrollmentsForCourse, enrollments -> {
            if (enrollments != null) {
                List<Integer> studentIds = new ArrayList<>();
                for (Enrollment enrollment : enrollments) {
                    studentIds.add(enrollment.getStudentId());
                }
                if (!studentIds.isEmpty()) {
                    LiveData<List<Student>> studentsLiveData = studentDao.getStudentsByIds(studentIds);
                    studentsInCourse.addSource(studentsLiveData, students -> {
                        if (students != null) {
                            studentsInCourse.setValue(students);
                            // This prevents recursion errors
                            studentsInCourse.removeSource(studentsLiveData);
                        } else {
                            studentsInCourse.setValue(new ArrayList<>());
                        }
                    });
                } else {
                    studentsInCourse.setValue(new ArrayList<>());
                }
            } else {
                studentsInCourse.setValue(new ArrayList<>());
            }
        });

        return studentsInCourse;
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
