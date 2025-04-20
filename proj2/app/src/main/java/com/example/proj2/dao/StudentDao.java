package com.example.proj2.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.proj2.domain.Student;

import java.util.List;

@Dao
public interface StudentDao {
    @Insert
    void insert(Student student);

    @Update
    void update(Student student);

    @Delete
    void delete(Student student);
    @Query("SELECT * FROM students")
    LiveData<List<Student>> getAllStudents();
    @Query("SELECT * FROM students WHERE studentId = :studentId")
    LiveData<Student> getStudent(int studentId);
    @Query("SELECT * FROM students WHERE studentId = :studentId")
    Student getStudentSync(int studentId);
    @Query("SELECT * FROM students WHERE studentId IN (:studentIds)")
    LiveData<List<Student>> getStudentsByIds(List<Integer> studentIds);

    @Query("SELECT s.* FROM students AS s JOIN enrollments AS e ON s.studentId = e.studentId WHERE e.courseId = :courseId")
    LiveData<List<Student>> getStudentsForCourse(int courseId);
    @Query("DELETE FROM students")
    void deleteAll();

}
