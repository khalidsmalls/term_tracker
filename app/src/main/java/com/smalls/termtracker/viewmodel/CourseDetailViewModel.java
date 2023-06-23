package com.smalls.termtracker.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.smalls.termtracker.database.Repository;
import com.smalls.termtracker.entity.Course;

import java.util.List;

public class CourseDetailViewModel extends AndroidViewModel {
    private final Repository mRepo;
    private final LiveData<List<Course>> mAllCourses;

    public CourseDetailViewModel(@NonNull Application application) {
        super(application);
        mRepo = new Repository(application);
        mAllCourses = mRepo.getAllCourses();
    }

    public LiveData<List<Course>> getAllCourses() { return mAllCourses; }

    public void insert(Course course) { mRepo.insert(course); }
    public void update(Course course) { mRepo.update(course); }
    public void delete(int courseId) { mRepo.deleteCourse(courseId); }
    public void deleteAssociatedAssessments(int courseId) {
        mRepo.deleteAssociatedAssessmentes(courseId);
    }

}
