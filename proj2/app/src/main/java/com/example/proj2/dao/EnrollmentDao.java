package com.example.proj2.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.proj2.domain.Enrollment;

import java.util.List;

@Dao
public interface EnrollmentDao {
    @Insert
    void insert(Enrollment enrollment);

    @Update
    void update(Enrollment enrollment);

    @Delete
    void delete(Enrollment enrollment);
    @Query("SELECT * FROM enrollments")
    LiveData<List<Enrollment>> getAllEnrollments();

    @Query("SELECT * FROM enrollments WHERE studentId = :studentId")
    LiveData<List<Enrollment>> getEnrollmentsByStudentId(int studentId);

    @Query("SELECT * FROM enrollments WHERE courseId = :courseId")
    LiveData<List<Enrollment>> getEnrollmentsByCourseId(int courseId);

    @Query("SELECT * FROM enrollments WHERE courseId = :courseId AND studentId = :studentId")
    LiveData<Enrollment> getEnrollmentByCourseIdAndStudentId(int courseId, int studentId);

    @Query("DELETE FROM enrollments")
    void deleteAll();

    @Query("DELETE FROM enrollments WHERE studentId = :studentId")
    void deleteEnrollmentsByStudentId(int studentId);

    @Query("DELETE FROM enrollments WHERE courseId = :courseId")
    void deleteEnrollmentsByCourseId(int courseId);

    @Query("DELETE FROM enrollments WHERE courseId = :courseId AND studentId = :studentId")
    void deleteEnrollmentByCourseIdAndStudentId(int courseId, int studentId);
}
