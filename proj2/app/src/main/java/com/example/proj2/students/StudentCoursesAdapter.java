// StudentCoursesAdapter.java
package com.example.proj2.students;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proj2.R;
import com.example.proj2.domain.Course;

import java.util.ArrayList;
import java.util.List;

public class StudentCoursesAdapter extends RecyclerView.Adapter<StudentCoursesAdapter.ViewHolder> {
    private List<Course> courses = new ArrayList<>();

    public void setCourses(List<Course> newCourses) {
        this.courses = newCourses;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView courseName;

        public ViewHolder(View view) {
            super(view);
            courseName = view.findViewById(R.id.courseItemName);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Course course = courses.get(position);
        holder.courseName.setText(course.getCourseName() + " (" + course.getCourseCode() + ")");
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }
}
