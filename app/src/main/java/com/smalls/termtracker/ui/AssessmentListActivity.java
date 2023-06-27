package com.smalls.termtracker.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.smalls.termtracker.R;

public class AssessmentListActivity extends AppCompatActivity
        implements AssessmentListFragment.OnAssessmentSelectedListener {

    private final String COURSE_ID = "course_id";
    private int mCourseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Assessment List");
        }

        FloatingActionButton fab = findViewById(R.id.add_assessment_fab);
        fab.setOnClickListener(onAddAssessmentClick);

        mCourseId = getIntent().getIntExtra(COURSE_ID, 0);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        AssessmentListFragment fragment = new AssessmentListFragment();
        transaction.add(R.id.assessment_list_fragment_container, fragment);

        Bundle mBundle = new Bundle();
        mBundle.putInt(COURSE_ID, mCourseId);
        fragment.setArguments(mBundle);

        transaction.commit();
    }

    @Override
    public void onAssessmentSelected(int assessmentId) {
        Intent intent = new Intent(getApplicationContext(), AssessmentDetailActivity.class);
        String ASSESSMENT_ID = "assessment_id";
        intent.putExtra(ASSESSMENT_ID, assessmentId);
        startActivity(intent);
    }

    private final View.OnClickListener onAddAssessmentClick = v -> {
        Intent intent = new Intent(getApplicationContext(), AssessmentDetailActivity.class);
        intent.putExtra(COURSE_ID, mCourseId);
        startActivity(intent);
    };
}