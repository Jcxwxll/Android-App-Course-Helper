package com.example.proj2.domain;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
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
    public Student(@NonNull String name, @NonNull String email, @NonNull String userName) {
        this.name = name;
        this.email = email;
        this.UserName = userName;
    }

    // Getters and setters
    @NonNull
    public int getStudentId() {
        return studentId;
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
