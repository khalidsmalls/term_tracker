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

public class AssessmentDetailViewModel extends AndroidViewModel {
    private final LiveData<List<Assessment>> mAssociatedAssessments;
    private final Repository mRepo;

    public AssessmentDetailViewModel(@NonNull Application application, int courseId) {
        super(application);
        mRepo = new Repository(application);
        mAssociatedAssessments = mRepo.getAssociatedAssessments(courseId);
    }

    public LiveData<List<Assessment>> getAssociatedAssessments() {
        return mAssociatedAssessments;
    }

    public void insert(Assessment assessment) {
        mRepo.insert(assessment);
    }

    public void update(Assessment assessment) {
        mRepo.update(assessment);
    }

    public void delete(int assessmentId) {
        mRepo.deleteAssessment(assessmentId);
    }

    public static class ViewModelFactory implements ViewModelProvider.Factory {
        private final Application mApplication;
        private final int mCourseId;

        public ViewModelFactory(Application application, int courseId) {
            mApplication = application;
            mCourseId = courseId;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new AssessmentDetailViewModel(mApplication, mCourseId);
        }
    }
}
