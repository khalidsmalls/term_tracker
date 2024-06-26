package com.smalls.termtracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.smalls.termtracker.database.Repository;
import com.smalls.termtracker.entity.Assessment;

import java.util.List;

public class AssessmentListViewModel extends AndroidViewModel {
    private final LiveData<List<Assessment>> mAssociatedAssessments;

    public AssessmentListViewModel(@NonNull Application application, int courseId) {
        super(application);
        Repository mRepo = new Repository(application);
        mAssociatedAssessments = mRepo.getAssociatedAssessments(courseId);
    }

    public LiveData<List<Assessment>> getAssociatedAssessments() {
        return mAssociatedAssessments;
    }

    public static class ViewModelFactory implements ViewModelProvider.Factory {
        private final Application mApplication;
        private final int mCourseId;

        public ViewModelFactory(Application app, int courseId) {
            mApplication = app;
            mCourseId = courseId;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new AssessmentListViewModel(mApplication, mCourseId);
        }
    }
}
