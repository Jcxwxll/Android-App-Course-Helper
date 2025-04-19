package com.example.proj2.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.proj2.dao.CourseDao;
import com.example.proj2.dao.EnrollmentDao;
import com.example.proj2.dao.StudentDao;
import com.example.proj2.domain.Course;
import com.example.proj2.domain.Enrollment;
import com.example.proj2.domain.Student;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Student.class, Course.class, Enrollment.class}, version = 1, exportSchema = false)
public abstract class CMSDB extends RoomDatabase {
    public abstract StudentDao studentDao();
    public abstract CourseDao courseDao();
    public abstract EnrollmentDao enrollmentDao();

    private static volatile CMSDB INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static CMSDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CMSDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    CMSDB.class, "cms_database")
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