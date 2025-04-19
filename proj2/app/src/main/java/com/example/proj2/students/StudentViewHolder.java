package com.example.proj2.students;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.proj2.R;
import com.example.proj2.domain.Student;

// This is the view holder for "recyclerview_student.xml"
public class StudentViewHolder extends RecyclerView.ViewHolder {

    public StudentViewHolder(View view) {
        super(view);
    }

    public void bind(Student student) {
    }

    static StudentViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_student, parent, false);
        return new StudentViewHolder(view);
    }
}
