package com.smalls.termtracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.smalls.termtracker.database.Repository;
import com.smalls.termtracker.entity.Course;
import com.smalls.termtracker.entity.Term;

import java.util.List;


public class TermDetailViewModel extends AndroidViewModel {
    private final Repository mRepo;
    private final LiveData<List<Term>> mAllTerms;

    public TermDetailViewModel(@NonNull Application application) {
        super(application);
        mRepo = new Repository(application);
        mAllTerms = mRepo.getAllTerms();
    }

    public LiveData<List<Term>> getAllTerms() {
        return mAllTerms;
    }

    public void insert(Term term) { mRepo.insert(term); }
    public void update(Term term) { mRepo.update(term); }
    public void delete(int termId) { mRepo.deleteTerm(termId); }
    public void deleteAssociatedCourses(int termId) {
        mRepo.deleteAssociatedCourses(termId);
    }
}
