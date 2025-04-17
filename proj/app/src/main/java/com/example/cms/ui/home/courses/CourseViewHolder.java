package com.example.cms.ui.home.courses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cms.R;
import com.example.cms.domain.Course;

// This is the view holder for "recyclerview_course.xml"
public class CourseViewHolder extends RecyclerView.ViewHolder {
    private final TextView courseCodeTextView;
    private final TextView courseNameTextView;
    private final TextView lecturerNameTextView;

    public CourseViewHolder(View view) {
        super(view);
        // Find the TextView by their ids
        courseNameTextView = (TextView) view.findViewById(R.id.course_name);
        lecturerNameTextView = (TextView) view.findViewById(R.id.lecturer_name);
        courseCodeTextView = (TextView) view.findViewById(R.id.course_code);
    }

    public void bind(Course course) {
        // Update to match course data
        courseNameTextView.setText(course.getCourseName());
        lecturerNameTextView.setText(course.getLecturerName());
        courseCodeTextView.setText(course.getCourseCode());
    }

    static CourseViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_course, parent, false);
        return new CourseViewHolder(view);
    }
}
