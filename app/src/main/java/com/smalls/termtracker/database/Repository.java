package com.smalls.termtracker.database;

import android.app.Application;

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
    private final LiveData<List<Term>> mAllTerms;
    //private final LiveData<List<Course>> mAllCourses;
    private LiveData<List<Course>> mAssociatedCourses;
    private LiveData<List<Assessment>> mAssociatedAssessments;

    public Repository(Application application) {
        TermDatabase db = TermDatabase.getDatabase(application);
        mAssessmentDao = db.assessmentDao();
        mCourseDao = db.courseDao();
        mTermDao = db.termDao();
        mAllTerms = mTermDao.getAllTerms();
        //mAllCourses = mCourseDao.getAllCourses();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public LiveData<List<Term>> getAllTerms() {
        return mAllTerms;
    }

//    public LiveData<List<Course>> getAllCourses() {
//        return mAllCourses;
//    }

    /**
     * filter courses by term id
     *
     * @param termId the id of the term to filter courses
     * @return the courses associated with the term
     */
    public LiveData<List<Course>> getAssociatedCourses(int termId) {
        TermDatabase.databaseWriteExecutor.execute(() -> {
            mAssociatedCourses = mCourseDao.getAssociatedCourses(termId);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAssociatedCourses;
    }

    /**
     * filter assessments by course id
     *
     * @param courseId the id of the course to filter
     *                 assessments by
     * @return the assessments associated with the course
     */
    public LiveData<List<Assessment>> getAssociatedAssessments(int courseId) {
        TermDatabase.databaseWriteExecutor.execute(() -> {
            mAssociatedAssessments =
                    mAssessmentDao.getAssociatedAssessments(courseId);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAssociatedAssessments;
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

    public void deleteAssociatedCourses(int termId) {
        TermDatabase.databaseWriteExecutor.execute(() -> {
                    mTermDao.deleteAssociatedCourses(termId);
                }
        );
    }

    public void deleteAssociatedAssessments(int courseId) {
        TermDatabase.databaseWriteExecutor.execute(() -> {
            mAssessmentDao.deleteAssociatedAssessments(courseId);
        });
    }

}
