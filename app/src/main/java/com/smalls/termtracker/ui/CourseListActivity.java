package com.smalls.termtracker.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.smalls.termtracker.R;

public class CourseListActivity extends AppCompatActivity
        implements CourseListFragment.OnCourseSelectedListener {
    private int mTermId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        mTermId = getIntent().getIntExtra("TERM_ID", 0);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        CourseListFragment fragment = new CourseListFragment();
        transaction.add(R.id.course_list_fragment_container, fragment);

        Bundle mBundle = new Bundle();
        mBundle.putInt("TERM_ID", mTermId);
        fragment.setArguments(mBundle);

        transaction.commit();

        FloatingActionButton fab = findViewById(R.id.add_course_fab);
        fab.setOnClickListener(onAddCourseClick);
    }

    @Override
    public void onCourseSelected(int courseId) {
        Intent intent = new Intent(getApplicationContext(), CourseDetailActivity.class);
        intent.putExtra("COURSE_ID", courseId);
        intent.putExtra("TERM_ID", mTermId);
        startActivity(intent);
    }

    private final View.OnClickListener onAddCourseClick = v -> {
        Intent intent = new Intent(getApplicationContext(),  CourseDetailActivity.class);
        intent.putExtra("TERM_ID", mTermId);
        startActivity(intent);
    };

}