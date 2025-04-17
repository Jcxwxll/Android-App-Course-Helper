package com.example.cms.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.cms.dao.CourseDao;
import com.example.cms.domain.Course;
import com.example.cms.room.CourseDB;

import java.util.List;

public class CourseRepository {
    private CourseDao courseDao;
    private LiveData<List<Course>> allCourses;

    public CourseRepository(Application application) {
        CourseDB db = CourseDB.getDatabase(application);
        courseDao = db.courseDao();
        allCourses = courseDao.getAllCourses();
    }

    public LiveData<List<Course>> getAllCourses() {
        return allCourses;
    }

    public void insert(Course course) {
        CourseDB.databaseWriteExecutor.execute(() -> {
            courseDao.insert(course);
        });
    }

    public void deleteAll() {
        CourseDB.databaseWriteExecutor.execute(() -> {
            courseDao.deleteAll();
        });
    }
}
