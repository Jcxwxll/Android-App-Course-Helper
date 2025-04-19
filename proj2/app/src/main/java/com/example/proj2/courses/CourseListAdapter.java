package com.example.proj2.courses;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.proj2.domain.Course;

public class CourseListAdapter extends ListAdapter<Course, CourseViewHolder> {
    private final CourseViewHolder.OnCourseDeleteListener deleteListener;
    public CourseListAdapter(@NonNull DiffUtil.ItemCallback<Course> diffCallback, CourseViewHolder.OnCourseDeleteListener listener) {
        super(diffCallback);
        this.deleteListener = listener;
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return CourseViewHolder.create(parent, deleteListener);
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {
        Course current = getItem(position);
        holder.bind(current);
    }

    public static class CourseDiff extends DiffUtil.ItemCallback<Course> {
        @Override
        public boolean areItemsTheSame(@NonNull Course oldCourse, @NonNull Course newCourse) {
            return oldCourse == newCourse;
        }
        @Override
        public boolean areContentsTheSame(@NonNull Course oldCourse, @NonNull Course newCourse) {
            return oldCourse.getCourseName().equals(newCourse.getCourseName());
        }
    }
}
