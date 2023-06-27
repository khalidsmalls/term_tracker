package com.smalls.termtracker.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.smalls.termtracker.R;
import com.smalls.termtracker.entity.Assessment;
import com.smalls.termtracker.viewmodel.AssessmentDetailViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AssessmentDetailActivity extends AppCompatActivity
        implements DeleteDialogFragment.onDeleteDialogListener {
    private int mCourseId;
    private int mAssessmentId;
    private RadioGroup mRadioGroup;
    private RadioButton mPerformance;
    private RadioButton mObjective;
    private EditText mStartDateEditText;
    private EditText mEndDateEditText;
    private AssessmentDetailViewModel mViewModel;
    private Calendar mCalendar;
    private SimpleDateFormat mDateFormatter;
    private Date mStartDate;
    private Date mEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Assessment Detail");
        }

        String COURSE_ID = "course_id";
        String ASSESSMENT_ID = "assessment_id";
        mCourseId = getIntent().getIntExtra(COURSE_ID, 0);
        mAssessmentId = getIntent().getIntExtra(ASSESSMENT_ID, 0);

        mViewModel = new ViewModelProvider(
                this,
                new AssessmentDetailViewModel.ViewModelFactory(
                        getApplication(),
                        mCourseId
                )
        ).get(AssessmentDetailViewModel.class);

        LiveData<List<Assessment>> mAssociatedAssessments = mViewModel.getAssociatedAssessments();
        mRadioGroup = findViewById(R.id.assessment_radio_group);
        mPerformance = findViewById(R.id.radio_performance);
        mObjective = findViewById(R.id.radio_objective);
        mStartDateEditText = findViewById(R.id.start_date_edittext);
        mEndDateEditText = findViewById(R.id.end_date_edittext);
        Button mSaveButton = findViewById(R.id.new_assessment_save_button);
        mStartDateEditText.setOnClickListener(onDateFieldClicked);
        mEndDateEditText.setOnClickListener(onDateFieldClicked);
        mSaveButton.setOnClickListener(onSaveClicked);
        mCalendar = Calendar.getInstance();
        mDateFormatter = new SimpleDateFormat("MM/dd/yy", Locale.US);
        mStartDateEditText.setTag("START_DATE");
        mEndDateEditText.setTag("END_DATE");

        mAssociatedAssessments.observe(
                this,
                assessments -> {
                    for (Assessment assessment : assessments) {
                        if (assessment.getId() == mAssessmentId) {
                            if (assessment.getType() == Assessment.MType.PERFORMANCE) {
                                mPerformance.setChecked(true);
                            } else {
                                mObjective.setChecked(true);
                            }
                            mStartDateEditText.setText(assessment.getStart().toString());
                            mEndDateEditText.setText(assessment.getEnd().toString());
                        }
                    }
                }
        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mAssessmentId != 0) {
            getMenuInflater().inflate(R.menu.delete, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            FragmentManager manager = getSupportFragmentManager();
            DeleteDialogFragment dialog = new DeleteDialogFragment(
                    "Delete assessment?"
            );
            dialog.show(manager, "deleteDialog");
        }
        return true;
    }

    @Override
    public void onDelete() {
        mViewModel.delete(mAssessmentId);
        finish();
        Toast.makeText(
                this,
                "Assessment deleted",
                Toast.LENGTH_SHORT
        ).show();
    }

    private final View.OnClickListener onDateFieldClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCalendar.setTime(new Date());
            EditText editText = (EditText) v;

            new DatePickerDialog(
                    AssessmentDetailActivity.this,
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

    private final View.OnClickListener onSaveClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (validateFields()) {
                Assessment assessment;
                Assessment.MType mType;
                if (mRadioGroup.getCheckedRadioButtonId() != -1) {
                    if (mRadioGroup.getCheckedRadioButtonId() == R.id.radio_objective) {
                        mType = Assessment.MType.OBJECTIVE;
                    } else {
                        mType = Assessment.MType.PERFORMANCE;
                    }
                } else {
                    mType = null;
                }

                if (mAssessmentId == 0) {
                    //new assessment
                    assessment = new Assessment(
                            mStartDate,
                            mEndDate,
                            mType,
                            mCourseId
                    );
                    mViewModel.insert(assessment);
                    Toast.makeText(
                            getApplicationContext(),
                            "Assessment added",
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    //update assessment
                    try {
                        mStartDate = mDateFormatter.parse(
                                mStartDateEditText.getText().toString()
                        );
                        mEndDate = mDateFormatter.parse(
                                mEndDateEditText.getText().toString()
                        );
                        assessment = new Assessment(
                                mAssessmentId,
                                mStartDate,
                                mEndDate,
                                mType,
                                mCourseId
                        );
                        mViewModel.update(assessment);
                        Toast.makeText(
                                getApplicationContext(),
                                "Assessment updated",
                                Toast.LENGTH_SHORT
                        ).show();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
                finish();
            }
        }
    };

    private boolean validateFields() {
        return mRadioGroup.getCheckedRadioButtonId() != -1 &&
                !TextUtils.isEmpty(mStartDateEditText.getText()) &&
                !TextUtils.isEmpty(mEndDateEditText.getText());
    }
}