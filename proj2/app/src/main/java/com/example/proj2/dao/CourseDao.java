package com.example.proj2.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.proj2.domain.Course;

import java.util.List;

@Dao
public interface CourseDao {
    @Insert
    void insert(Course course);

    @Update
    void update(Course course);

    @Delete
    void delete(Course course);
    @Query("SELECT * FROM courses")
    LiveData<List<Course>> getAllCourses();
    @Query("SELECT * FROM courses WHERE courseId = :courseId")
    LiveData<Course> getCourse(int courseId);
    @Query("SELECT * FROM courses WHERE code = :courseCode")
    LiveData<Course> getCourseByCode(String courseCode);
    @Query("DELETE FROM courses")
    void deleteAll();
}
