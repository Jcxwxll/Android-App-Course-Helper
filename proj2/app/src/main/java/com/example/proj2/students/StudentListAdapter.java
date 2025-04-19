package com.example.proj2.students;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.proj2.students.StudentViewHolder;
import com.example.proj2.domain.Student;

public class StudentListAdapter extends ListAdapter<Student, StudentViewHolder> {
    private final int courseId;

    public StudentListAdapter(@NonNull DiffUtil.ItemCallback<Student> diffCallback, int courseId) {
        super(diffCallback);
        this.courseId = courseId;
    }

    @Override
    public StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return StudentViewHolder.create(parent, courseId);
    }

    @Override
    public void onBindViewHolder(StudentViewHolder holder, int position) {
        Student current = getItem(position);
        holder.bind(current);
    }

    public static class StudentDiff extends DiffUtil.ItemCallback<Student> {
        @Override
        public boolean areItemsTheSame(@NonNull Student oldStudent, @NonNull Student newStudent) {
            return oldStudent == newStudent;
        }
        @Override
        public boolean areContentsTheSame(@NonNull Student oldStudent, @NonNull Student newStudent) {
            return oldStudent.getStudentId() == newStudent.getStudentId();
        }
    }
}
