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

public class TermListActivity extends AppCompatActivity
        implements TermListFragment.OnTermSelectedListener {
    public static final String TERM_ID = "term_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Term List");
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        TermListFragment fragment  = new TermListFragment();
        fragmentTransaction.add(R.id.term_list_fragment_container, fragment);
        fragmentTransaction.commit();

        FloatingActionButton fab = findViewById(R.id.add_term_fab);
        fab.setOnClickListener(onAddTermClick);
    }

    @Override
    public void onTermSelected(int termId) {
        Intent intent = new Intent(getApplicationContext(), TermDetailActivity.class);
        intent.putExtra(TERM_ID, termId);
        startActivity(intent);
    }

    private final View.OnClickListener onAddTermClick = v -> {
        Intent intent = new Intent(getApplicationContext(), TermDetailActivity.class);
        startActivity(intent);
    };
}