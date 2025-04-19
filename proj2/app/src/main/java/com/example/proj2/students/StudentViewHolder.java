package com.example.proj2.students;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proj2.CourseDetailsActivity;
import com.example.proj2.R;
import com.example.proj2.domain.Student;

// This is the view holder for "recyclerview_student.xml"
public class StudentViewHolder extends RecyclerView.ViewHolder {
    private final TextView studentNameTextView;
    private final TextView studentEmailTextView;
    private final TextView studentMatricTextView;
    private final CardView cardView;
    private final int courseId;

    public StudentViewHolder(View view, int courseId) {
        super(view);
        // Find the Card by its id
        cardView = (CardView) view.findViewById(R.id.card_view);
        // Find the TextView by their ids
        studentNameTextView = (TextView) view.findViewById(R.id.student_name);
        studentEmailTextView = (TextView) view.findViewById(R.id.student_email);
        studentMatricTextView = (TextView) view.findViewById(R.id.student_matric);
        this.courseId = courseId;
    }

    public void bind(Student student) {
        // Update to match student data
        studentNameTextView.setText(this.itemView.getContext().getString(R.string.student_name, student.getName()));
        studentEmailTextView.setText(this.itemView.getContext().getString(R.string.student_email, student.getEmail()));
        studentMatricTextView.setText(this.itemView.getContext().getString(R.string.student_matric, String.valueOf(student.getStudentId())));

        // When the card is long-press we will navigate to StudentDetailsActivity
        // WARNING: This is a slight deviation from feature 6, but it is required
        // as feature 6 and 7 conflict each other. An email to the professors has already
        // been made.
        cardView.setOnLongClickListener(v -> {
            // TODO: Navigate to StudentDetailsActivity
            return true;
        });
        cardView.setOnClickListener(v -> {
            ((CourseDetailsActivity) v.getContext()).showStudentOptionsDialog(student, courseId);
        });
    }

    static StudentViewHolder create(ViewGroup parent, int courseId) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_student, parent, false);
        return new StudentViewHolder(view, courseId);
    }
}
