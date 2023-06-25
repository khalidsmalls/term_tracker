package com.smalls.termtracker.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.smalls.termtracker.R;
import com.smalls.termtracker.entity.Term;
import com.smalls.termtracker.viewmodel.TermDetailViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TermDetailActivity extends AppCompatActivity
        implements DeleteDialogFragment.onDeleteDialogListener {
    private final String TERM_ID = "term_id";
    private EditText mTitleEditText;
    private EditText mStartDateEditText;
    private EditText mEndDateEditText;
    private SimpleDateFormat mDateFormatter;
    private Calendar mCalendar;
    private Date mStartDate;
    private Date mEndDate;
    private int mTermId;
    private TermDetailViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Term Detail");
        }

        //the id of the term to be updated or default, 0, for a new term
        mTermId = getIntent().getIntExtra(TERM_ID, 0);

        mViewModel = new ViewModelProvider(this).get(TermDetailViewModel.class);
        LiveData<List<Term>> mAllTerms = mViewModel.getAllTerms();

        mStartDateEditText = findViewById(R.id.term_start_edittext);
        mEndDateEditText = findViewById(R.id.term_end_edittext);
        mTitleEditText = findViewById(R.id.term_title_edittext);
        mStartDateEditText.setTag("START_DATE");
        mEndDateEditText.setTag("END_DATE");
        Button mSaveBtn = findViewById(R.id.new_term_save_btn);
        Button mViewCoursesBtn = findViewById(R.id.view_courses_btn);
        mCalendar = Calendar.getInstance();
        mDateFormatter = new SimpleDateFormat("MM/dd/yy", Locale.US);

        mStartDateEditText.setOnClickListener(onDateFieldClicked);
        mEndDateEditText.setOnClickListener(onDateFieldClicked);
        mSaveBtn.setOnClickListener(onSaveClicked);
        mViewCoursesBtn.setOnClickListener(onViewCoursesClick);

        if (mTermId == 0) {
            mViewCoursesBtn.setVisibility(View.GONE);
        }

        mAllTerms.observe(
                this,
                terms -> {
                    for (Term term : terms) {
                       if (term.getId() == mTermId) {
                           mTitleEditText.setText(term.getTitle());
                           mStartDateEditText.setText(
                                   mDateFormatter.format(term.getStart())
                           );
                           mEndDateEditText.setText(
                                   mDateFormatter.format(term.getEnd())
                           );
                       }
                    }
                }
        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //show delete option if term is in the database
        if (mTermId != 0) {
            getMenuInflater().inflate(R.menu.delete, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            FragmentManager manager = getSupportFragmentManager();
            DeleteDialogFragment dialog = new DeleteDialogFragment(
                    getString(R.string.delete_term)
            );
            dialog.show(manager, "deleteDialog");
        }
        return true;
    }

    @Override
    public void onDelete() {
        mViewModel.deleteAssociatedCourses(mTermId);
        mViewModel.delete(mTermId);
        finish();
        Toast.makeText(
                this,
                getString(R.string.term_deleted),
                Toast.LENGTH_SHORT
        ).show();
    }

    private final View.OnClickListener onDateFieldClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCalendar.setTime(new Date());
            EditText editText = (EditText) v;

            new DatePickerDialog(
                    TermDetailActivity.this,
                    R.style.datepicker,
                    (view, year, month, dayOfMonth) -> {
                       mCalendar.set(year, month, dayOfMonth);
                       if (editText.getTag().equals("START_DATE")) {
                           mStartDate = new Date(mCalendar.getTimeInMillis());
                       }
                       if (editText.getTag().equals("END_DATE")) {
                           mEndDate = new Date(mCalendar.getTimeInMillis());
                       }
                       editText.setText(
                               mDateFormatter.format(
                                       new Date(mCalendar.getTimeInMillis())
                               )
                       );
                    },
                    mCalendar.get(Calendar.YEAR),
                    mCalendar.get(Calendar.MONTH),
                    mCalendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        }
    };

    private final View.OnClickListener onViewCoursesClick = v -> {
        Intent intent = new Intent(getApplicationContext(), CourseListActivity.class);
        intent.putExtra(TERM_ID, mTermId);
        startActivity(intent);
    };

    private final View.OnClickListener onSaveClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (validateFields()) {
                Term term;
                if (mTermId == 0) {
                    //new term
                    term = new Term(
                            mTermId,
                            mTitleEditText.getText().toString(),
                            mStartDate,
                            mEndDate
                    );
                    mViewModel.insert(term);
                    Toast.makeText(
                            getApplicationContext(),
                            getString(R.string.term_added),
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    //update term
                    try {
                        mStartDate = mDateFormatter.parse(
                                mStartDateEditText.getText().toString()
                        );
                        mEndDate = mDateFormatter.parse(
                                mEndDateEditText.getText().toString()
                        );
                        term = new Term(
                                mTermId,
                                mTitleEditText.getText().toString(),
                                mStartDate,
                                mEndDate
                        );
                        mViewModel.update(term);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    Toast.makeText(
                            getApplicationContext(),
                            getString(R.string.term_updated),
                            Toast.LENGTH_SHORT
                    ).show();
                }
                finish();
            }
        }
    };

    private boolean validateFields() {
        return !TextUtils.isEmpty(mTitleEditText.getText()) &&
                !TextUtils.isEmpty(mStartDateEditText.getText()) &&
                !TextUtils.isEmpty(mEndDateEditText.getText());
    }
}