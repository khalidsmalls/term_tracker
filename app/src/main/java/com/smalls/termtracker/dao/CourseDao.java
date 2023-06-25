package com.smalls.termtracker.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.smalls.termtracker.entity.Course;

import java.util.List;

@Dao
public interface CourseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Course course);

    @Update
    void update(Course course);

    @Query("DELETE FROM courses WHERE id = :courseId")
    void deleteCourse(int courseId);

    @Query("DELETE FROM courses")
    void deleteAllCourses();

    @Query("SELECT * FROM courses")
    LiveData<List<Course>> getAllCourses();

    @Query("SELECT * FROM courses WHERE termId = :termId")
    LiveData<List<Course>> getAssociatedCourses(int termId);

}
