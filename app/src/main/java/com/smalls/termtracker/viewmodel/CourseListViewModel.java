package com.smalls.termtracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.smalls.termtracker.database.Repository;
import com.smalls.termtracker.entity.Course;

import java.util.List;

public class CourseListViewModel extends AndroidViewModel {
    private final Repository mRepo;
    private final LiveData<List<Course>> mAllCoursesLiveData;
    private final List<Course> mAllCourses;

    public CourseListViewModel(@NonNull Application application) {
        super(application);
        mRepo = new Repository(application);
        mAllCoursesLiveData = mRepo.getAllCourses();
        mAllCourses = mRepo.getCourses();
    }

    public LiveData<List<Course>> getAllCoursesLiveData() {
        return mAllCoursesLiveData;
    }

    public List<Course> getAllCourses() {
        return mAllCourses;
    }


}
