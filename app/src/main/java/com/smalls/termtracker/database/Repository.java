package com.smalls.termtracker.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.smalls.termtracker.dao.AssessmentDao;
import com.smalls.termtracker.dao.CourseDao;
import com.smalls.termtracker.dao.TermDao;
import com.smalls.termtracker.entity.Assessment;
import com.smalls.termtracker.entity.Course;
import com.smalls.termtracker.entity.Term;

import java.util.List;

public class Repository {

    private final AssessmentDao mAssessmentDao;
    private final CourseDao mCourseDao;
    private final TermDao mTermDao;
    private LiveData<List<Term>> mAllTerms;
    private LiveData<List<Course>> mAllCourses;
    private LiveData<List<Assessment>> mAllAssessments;
    private List<Course> associatedCourses;

    public Repository(Application application) {
        TermDatabase db = TermDatabase.getDatabase(application);
        mAssessmentDao = db.assessmentDao();
        mCourseDao = db.courseDao();
        mTermDao = db.termDao();
        mAllTerms = mTermDao.getAllTerms();
        mAllCourses = mCourseDao.getAllCourses();
        mAllAssessments = mAssessmentDao.getAllAssessments();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public LiveData<List<Term>> getAllTerms() {
        TermDatabase.databaseWriteExecutor.execute(() -> {
           mAllTerms = mTermDao.getAllTerms();
        });
        return mAllTerms;
    }

    public LiveData<List<Course>> getAllCourses() {
        TermDatabase.databaseWriteExecutor.execute(() -> {
            mAllCourses = mCourseDao.getAllCourses();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllCourses;
    }

   public LiveData<List<Assessment>> getAllAssessments() {
        TermDatabase.databaseWriteExecutor.execute(() -> {
            mAllAssessments = mAssessmentDao.getAllAssessments();
        });
        return mAllAssessments;
   }

    public void insert(Term term) {
        TermDatabase.databaseWriteExecutor.execute(() -> {
            mTermDao.insert(term);
        });
    }

    public void update(Term term) {
        TermDatabase.databaseWriteExecutor.execute(() -> {
            mTermDao.update(term);
        });
    }

    public void deleteTerm(int termId) {
        TermDatabase.databaseWriteExecutor.execute(() -> {
            mTermDao.deleteTerm(termId);
        });
    }

    public void insert(Course course) {
        TermDatabase.databaseWriteExecutor.execute(() -> {
            mCourseDao.insert(course);
        });
    }

    public void update(Course course) {
        TermDatabase.databaseWriteExecutor.execute(() -> {
            mCourseDao.update(course);
        });
    }

    public void deleteCourse(int courseId) {
        TermDatabase.databaseWriteExecutor.execute(() -> {
            mCourseDao.deleteCourse(courseId);
        });
    }

    public void deleteAllCourses() {
        TermDatabase.databaseWriteExecutor.execute(mCourseDao::deleteAllCourses);
    }

    public void insert(Assessment assessment) {
        TermDatabase.databaseWriteExecutor.execute(() -> {
            mAssessmentDao.insert(assessment);
        });
    }

    public void update(Assessment assessment) {
        TermDatabase.databaseWriteExecutor.execute(() -> {
            mAssessmentDao.update(assessment);
        });
    }

    public void deleteAssessment(int assessmentId) {
        TermDatabase.databaseWriteExecutor.execute(() -> {
            mAssessmentDao.deleteAssessment(assessmentId);
        });
    }

    public void deleteAllAssessments() {
        TermDatabase.databaseWriteExecutor.execute(mAssessmentDao::deleteAll);
    }

    public void deleteAssociatedCourses(int termId) {
        TermDatabase.databaseWriteExecutor.execute(() -> {
                    mTermDao.deleteAssociatedCourses(termId);
                }
        );
    }

    public void deleteAssociatedAssessmentes(int courseId) {
        TermDatabase.databaseWriteExecutor.execute(() -> {
            mAssessmentDao.deleteAssociatedAssessments(courseId);
        });
    }

}
