package com.smalls.termtracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.smalls.termtracker.R;
import com.smalls.termtracker.database.Repository;
import com.smalls.termtracker.entity.Assessment;

import java.util.List;

public class AssessmentListViewModel extends AndroidViewModel {
    private final Repository mRepo;
    private final LiveData<List<Assessment>> mAllAssessments;

    public AssessmentListViewModel(@NonNull Application application) {
        super(application);
        mRepo = new Repository(application);
        mAllAssessments = mRepo.getAllAssessments();
    }

    public LiveData<List<Assessment>> getAllAssessments() { return mAllAssessments; }
}
