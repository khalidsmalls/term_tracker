package com.smalls.termtracker.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.smalls.termtracker.entity.Assessment;

import java.util.List;

@Dao
public interface AssessmentDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Assessment assessment);

    @Update
    void update(Assessment assessment);

    @Query("DELETE FROM assessments WHERE id = :assessmentId")
    void deleteAssessment(int assessmentId);

    @Query("DELETE FROM assessments")
    void deleteAll();

    @Query("DELETE FROM assessments WHERE courseId = :courseId")
    void deleteAssociatedAssessments(int courseId);

    @Query("SELECT * FROM assessments WHERE courseId = :courseId")
    LiveData<List<Assessment>> getAssociatedAssessments(int courseId);
}
