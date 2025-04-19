package com.example.proj2.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.proj2.dao.StudentDao;
import com.example.proj2.domain.Student;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Student.class}, version = 1, exportSchema = false)
public abstract class StudentDB extends RoomDatabase {
    public abstract StudentDao studentDao();

    private static volatile StudentDB INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static StudentDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (StudentDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            StudentDB.class, "student_db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
