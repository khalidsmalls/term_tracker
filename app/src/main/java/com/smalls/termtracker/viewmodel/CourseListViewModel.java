package com.smalls.termtracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.smalls.termtracker.database.Repository;
import com.smalls.termtracker.entity.Course;

import java.lang.reflect.Constructor;
import java.util.List;

public class CourseListViewModel extends AndroidViewModel {
    private final Repository mRepo;
    private final LiveData<List<Course>> mAllCourses;
    private final List<Course> mAssociatedCourses;

    public CourseListViewModel(@NonNull Application app, int termId) {
        super(app);
        mRepo = new Repository(app);
        mAllCourses = mRepo.getAllCourses();
        mAssociatedCourses = mRepo.getAssociatedCourses(termId);
    }

    public List<Course> getAssociatedCourses(int termId) {
        return mAssociatedCourses;
    }

    public LiveData<List<Course>> getAllCourses() {
        return mAllCourses;
    }

    public static class ViewModelFactory implements ViewModelProvider.Factory {
        private final Application mApplication;
        private final int mTermId;

        public ViewModelFactory(Application app, int termId) {
            mApplication = app;
            mTermId = termId;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new CourseListViewModel(mApplication, mTermId);
        }
    }

}
