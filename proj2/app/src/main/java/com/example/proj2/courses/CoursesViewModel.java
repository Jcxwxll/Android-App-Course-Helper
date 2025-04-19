package com.example.proj2.courses;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.proj2.domain.Course;
import com.example.proj2.repository.CourseRepository;

import java.util.List;

public class CoursesViewModel extends AndroidViewModel {
    public CourseRepository courseRepository;
    private final LiveData<List<Course>> allCourses;

    public CoursesViewModel(Application application) {
        super(application);
        courseRepository = new CourseRepository(application);
        allCourses = courseRepository.getAllCourses();
    }

    public LiveData<List<Course>> getAllCourses() {
        return allCourses;
    }

    public void insert(Course course) {
        courseRepository.insert(course);
    }

    public void deleteAll() {
        courseRepository.deleteAll();
    }
}
