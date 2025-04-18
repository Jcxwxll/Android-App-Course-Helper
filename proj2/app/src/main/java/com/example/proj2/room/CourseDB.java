package com.example.proj2.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.proj2.dao.CourseDao;
import com.example.proj2.domain.Course;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Course.class}, version = 1, exportSchema = false)
public abstract class CourseDB extends RoomDatabase {
    public abstract CourseDao courseDao();

    private static volatile CourseDB INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static CourseDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CourseDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CourseDB.class, "course_db")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Initialises the DB with some courses [TODO: Remove when ready to be submitted]
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                CourseDao courseDao = INSTANCE.courseDao();
                courseDao.deleteAll();

                // TODO: "The main activity should be empty when the app is first launched."
                // This should be removed when ready to be submitted
                Course c1 = new Course("CO2124", "Software Architecture and System Development", "Muhammad Iftikhar");
                courseDao.insert(c1);
                Course c2 = new Course("CO2301", "Project Management", "Donovan A. Anderson");
                courseDao.insert(c2);
                Course c3 = new Course("CO2106", "Data Analytics", "Anand Sengodan");
                courseDao.insert(c3);
            });
        }
    };
}
