package com.example.proj2.domain;

import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/*
    The student Entity class
 */
@Entity(tableName = "students", indices = {@Index(value = "UserName", unique = true)})
public class Student {
    @PrimaryKey(autoGenerate = true)
    private int studentId;
    @NonNull
    @ColumnInfo(name = "name")
    private String name;
    @NonNull
    @ColumnInfo(name = "email")
    private String email;
    // For some reason in the assignment specification the UserName field
    // deviates from the camelCase formatting
    @NonNull
    @ColumnInfo(name = "UserName")
    private String UserName;

    // Constructor
    public Student(@NonNull String name, @NonNull String email, @NonNull String UserName) {
        this.name = name;
        this.email = email;
        this.UserName = UserName;
    }

    @Ignore
    public Student(int studentId, @NonNull String name, @NonNull String email) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        // The UserName was not described in full detail in the assignment so I am
        // generating it
        this.UserName = UUID.randomUUID().toString().replace("-", "");
    }

    @Ignore
    public Student(@NonNull String name, @NonNull String email) {
        // The UserName was not described in full detail in the assignment so I am
        // generating it
        this.name = name;
        this.email = email;
        this.UserName = UUID.randomUUID().toString().replace("-", "");
    }

    // Getters and setters
    @NonNull
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
    public String getUserName() {
        return UserName;
    }

    public void setUserName(@NonNull String userName) {
        UserName = userName;
    }
}
