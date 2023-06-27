package com.smalls.termtracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.smalls.termtracker.database.Repository;
import com.smalls.termtracker.entity.Course;

import java.util.List;

public class CourseDetailViewModel extends AndroidViewModel {
    private final Repository mRepo;
    private final LiveData<List<Course>> mAssociatedCourses;

    public CourseDetailViewModel(@NonNull Application application, int termId) {
        super(application);
        mRepo = new Repository(application);
        mAssociatedCourses = mRepo.getAssociatedCourses(termId);
    }

    public LiveData<List<Course>> getAssociatedCourses() { return mAssociatedCourses; }
    public void insert(Course course) { mRepo.insert(course); }
    public void update(Course course) { mRepo.update(course); }
    public void delete(int courseId) { mRepo.deleteCourse(courseId); }
    public void deleteAssociatedAssessments(int courseId) {
        mRepo.deleteAssociatedAssessments(courseId);
    }

    public static class ViewModelFactory implements ViewModelProvider.Factory {
        private final Application mApplication;
        private final int mTermId;

        public ViewModelFactory(Application application, int termId) {
            mApplication = application;
            mTermId = termId;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new CourseDetailViewModel(mApplication, mTermId);
        }
    }

}
