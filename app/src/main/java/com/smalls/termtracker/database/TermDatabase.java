package com.smalls.termtracker.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.smalls.termtracker.dao.AssessmentDao;
import com.smalls.termtracker.dao.CourseDao;
import com.smalls.termtracker.dao.TermDao;
import com.smalls.termtracker.entity.Assessment;
import com.smalls.termtracker.entity.Course;
import com.smalls.termtracker.entity.Term;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {
                Assessment.class,
                Course.class,
                Term.class
        },
        version = 35,
        exportSchema = false
)
@TypeConverters({DateConverter.class, AssessmentTypeConverter.class})
public abstract class TermDatabase extends RoomDatabase {

    public abstract AssessmentDao assessmentDao();
    public abstract CourseDao courseDao();
    public abstract TermDao termDao();

    private static volatile TermDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static TermDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TermDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            TermDatabase.class,
                                    "term_database"
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
