package com.example.proj2.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.proj2.dao.EnrollmentDao;
import com.example.proj2.domain.Enrollment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Enrollment.class}, version = 1, exportSchema = false)
public abstract class EnrollmentDB extends RoomDatabase {
    public abstract EnrollmentDao enrollmentDao();

    private static volatile EnrollmentDB INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static EnrollmentDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (EnrollmentDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            EnrollmentDB.class, "enrollment_db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
