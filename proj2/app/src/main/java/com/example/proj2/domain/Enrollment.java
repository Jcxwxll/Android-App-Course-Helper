package com.example.proj2.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

/*
    The enrollment Entity class,
    links the Course and Student entities
 */
@Entity(tableName = "enrollments",
        primaryKeys = {"studentId", "courseId"},  // Composite primary key
        foreignKeys = {
                @ForeignKey(entity = Student.class,
                        parentColumns = "studentId",
                        childColumns = "studentId",
                        onDelete = ForeignKey.CASCADE),  // Enrollment is deleted if Student is deleted
                @ForeignKey(entity = Course.class,
                        parentColumns = "courseId",
                        childColumns = "courseId",
                        onDelete = ForeignKey.CASCADE)   // Enrollment is deleted if Course is deleted
        }
)
public class Enrollment {
    @ColumnInfo(name = "studentId")
    private int studentId;
    @ColumnInfo(name = "courseId")
    private int courseId;

    // Constructor
    public Enrollment(int studentId, int courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }

    // Getters
    public int getStudentId() {
        return studentId;
    }

    public int getCourseId() {
        return courseId;
    }
}
