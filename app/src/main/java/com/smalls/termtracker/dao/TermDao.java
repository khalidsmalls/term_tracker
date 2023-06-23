package com.smalls.termtracker.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.smalls.termtracker.entity.Term;

import java.util.List;

@Dao
public interface TermDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Term term);

    @Update
    void update(Term term);

    @Query("DELETE FROM terms WHERE id = :termId")
    void deleteTerm(int termId);

    @Query("DELETE FROM courses WHERE id = :termId")
    void deleteAssociatedCourses(int termId);

    @Query("SELECT * FROM terms")
    LiveData<List<Term>> getAllTerms();

}
