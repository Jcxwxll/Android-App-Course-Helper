package com.example.proj2.courses;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proj2.CourseDetailsActivity;
import com.example.proj2.R;
import com.example.proj2.domain.Course;

// This is the view holder for "recyclerview_course.xml"
public class CourseViewHolder extends RecyclerView.ViewHolder {
    private final TextView courseCodeTextView;
    private final TextView courseNameTextView;
    private final TextView lecturerNameTextView;
    private final CardView cardView;

    public CourseViewHolder(View view) {
        super(view);
        // Find the Card by its id
        cardView = (CardView) view.findViewById(R.id.card_view);
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

        // Set up the click listener for the card view so when you tap on it
        // it should direct you to CourseDetailsActivity
        cardView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), CourseDetailsActivity.class);
            intent.putExtra("courseId", course.getCourseID());
            v.getContext().startActivity(intent);
        });
    }

    static CourseViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_course, parent, false);
        return new CourseViewHolder(view);
    }
}
