package com.smalls.termtracker.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
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
import com.smalls.termtracker.entity.Course;
import com.smalls.termtracker.viewmodel.CourseDetailViewModel;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CourseDetailActivity extends AppCompatActivity
        implements DeleteDialogFragment.onDeleteDialogListener {
    private final String COURSE_ID = "course_id";
    private EditText mTitleEditText;
    private EditText mStartDateEditText;
    private EditText mEndDateEditText;
    private EditText mInstructorNameEditText;
    private EditText mInstructorPhoneEditText;
    private EditText mInstructorEmailEditText;
    private EditText mOptionalNoteEditText;
    private RadioGroup mRadioGroup;
    private RadioButton mInProgress;
    private RadioButton mCompleted;
    private RadioButton mDropped;
    private RadioButton mPlanToTake;
    private int mCourseId;
    private int mTermId;
    private CourseDetailViewModel mViewModel;
    private Calendar mCalendar;
    private SimpleDateFormat mDateFormatter;
    private Date mStartDate;
    private Date mEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Course Detail");
        }

        //intent will come from onCourseClickListener in the course list in the
        //term detail view
        mCourseId = getIntent().getIntExtra(COURSE_ID, 0);
        String TERM_ID = "term_id";
        mTermId = getIntent().getIntExtra(TERM_ID, 0);

        mViewModel = new ViewModelProvider(this).get(CourseDetailViewModel.class);
        LiveData<List<Course>> mAssociatedCourses = mViewModel.getAssociatedCourses();

        mDateFormatter = new SimpleDateFormat("MM/dd/yy", Locale.US);
        mTitleEditText = findViewById(R.id.course_title_edittext);
        mStartDateEditText = findViewById(R.id.course_start_edittext);
        mEndDateEditText = findViewById(R.id.course_end_edittext);
        mInstructorNameEditText = findViewById(R.id.course_instructor_edittext);
        mInstructorPhoneEditText = findViewById(R.id.instructor_phone_edittext);
        mInstructorEmailEditText = findViewById(R.id.instructor_email_edittext);
        mOptionalNoteEditText = findViewById(R.id.optional_note_edittext);
        Button mSaveBtn = findViewById(R.id.new_term_save_btn);
        Button mViewAssessmentsBtn = findViewById(R.id.view_assessments_btn);
        mRadioGroup = findViewById(R.id.course_radio_group);
        mInProgress = findViewById(R.id.radio_in_progress);
        mCompleted = findViewById(R.id.radio_completed);
        mDropped = findViewById(R.id.radio_dropped);
        mPlanToTake = findViewById(R.id.radio_plan_to_take);
        mCalendar = Calendar.getInstance();
        mStartDateEditText.setTag("START_DATE");
        mEndDateEditText.setTag("END_DATE");

        mStartDateEditText.setOnClickListener(onDateFieldClicked);
        mEndDateEditText.setOnClickListener(onDateFieldClicked);
        mSaveBtn.setOnClickListener(onSaveClicked);
        mViewAssessmentsBtn.setOnClickListener(onViewAssessmentsClick);

        if (mCourseId == 0) {
            mViewAssessmentsBtn.setVisibility(View.GONE);
        }

        mAssociatedCourses.observe(
                this,
                courses -> {
                    for (Course course : courses) {
                        if (course.getId() == mCourseId) {
                            switch (course.getStatus()) {
                                case IN_PROGRESS:
                                    mInProgress.setChecked(true);
                                    break;
                                case COMPLETED:
                                    mCompleted.setChecked(true);
                                    break;
                                case DROPPED:
                                    mDropped.setChecked(true);
                                    break;
                                case PLAN_TO_TAKE:
                                    mPlanToTake.setChecked(true);
                                    break;
                            }
                            mTitleEditText.setText(course.getTitle());
                            mStartDateEditText.setText(
                                    mDateFormatter.format(course.getStart())
                            );
                            mEndDateEditText.setText(
                                    mDateFormatter.format(course.getEnd())
                            );
                            mInstructorNameEditText.setText(course.getInstructorName());
                            mInstructorPhoneEditText.setText(course.getInstructorPhone());
                            mInstructorEmailEditText.setText(course.getInstructorEmail());
                            mOptionalNoteEditText.setText(course.getOptionalNote());
                        }
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mCourseId != 0) {
            getMenuInflater().inflate(R.menu.course_detail, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            FragmentManager manager = getSupportFragmentManager();
            DeleteDialogFragment dialog = new DeleteDialogFragment(
                    getString(R.string.delete_course)
            );
            dialog.show(manager, "deleteDialog");
        } else if (item.getItemId() == R.id.action_share_note) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Optional Note");
            intent.putExtra(Intent.EXTRA_TEXT, mOptionalNoteEditText.getText());

            if (intent.resolveActivity(getPackageManager()) != null) {
                Intent chooser = Intent.createChooser(intent, "Share Optional Note");
                startActivity(chooser);
            }
        }
        return true;
    }

    @Override
    public void onDelete() {
        mViewModel.deleteAssociatedAssessments(mCourseId);
        mViewModel.delete(mCourseId);
        finish();
        Toast.makeText(
                this,
                getString(R.string.course_deleted),
                Toast.LENGTH_SHORT
        ).show();
    }

    private final View.OnClickListener onDateFieldClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCalendar.setTime(new Date());
            EditText editText = (EditText) v;

            new DatePickerDialog(
                    CourseDetailActivity.this,
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
                Course course;
                Course.MStatus mStatus = null;
                int mRadioId = mRadioGroup.getCheckedRadioButtonId();
                if (mRadioId != -1) {
                    if (mRadioId == R.id.radio_in_progress) {
                        mStatus = Course.MStatus.IN_PROGRESS;
                    } else if (mRadioId == R.id.radio_completed) {
                        mStatus = Course.MStatus.COMPLETED;
                    } else if (mRadioId == R.id.radio_dropped) {
                        mStatus = Course.MStatus.DROPPED;
                    } else if (mRadioId == R.id.radio_plan_to_take) {
                        mStatus = Course.MStatus.PLAN_TO_TAKE;
                    }
                }
                if (mCourseId == 0) {
                    //new course
                    course = new Course(
                            mTitleEditText.getText().toString(),
                            mStartDate,
                            mEndDate,
                            mInstructorNameEditText.getText().toString(),
                            mInstructorPhoneEditText.getText().toString(),
                            mInstructorEmailEditText.getText().toString(),
                            mOptionalNoteEditText.getText().toString(),
                            mTermId,
                            mStatus
                    );
                    mViewModel.insert(course);
                    Toast.makeText(
                            getApplicationContext(),
                            getString(R.string.course_added),
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
                        course = new Course(
                                mCourseId,
                                mTitleEditText.getText().toString(),
                                mStartDate,
                                mEndDate,
                                mInstructorNameEditText.getText().toString(),
                                mInstructorPhoneEditText.getText().toString(),
                                mInstructorEmailEditText.getText().toString(),
                                mOptionalNoteEditText.getText().toString(),
                                mTermId,
                                mStatus
                        );
                        mViewModel.update(course);
                        Toast.makeText(
                                getApplicationContext(),
                                getString(R.string.course_updated),
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

   private final View.OnClickListener onViewAssessmentsClick = v -> {
        Intent intent = new Intent(getApplicationContext(), AssessmentListActivity.class);
        intent.putExtra(COURSE_ID, mCourseId);
        startActivity(intent);
   };

    private boolean validateFields() {
        return !TextUtils.isEmpty(mTitleEditText.getText()) &&
                !TextUtils.isEmpty(mStartDateEditText.getText()) &&
                !TextUtils.isEmpty(mEndDateEditText.getText()) &&
                !TextUtils.isEmpty(mInstructorNameEditText.getText()) &&
                !TextUtils.isEmpty(mInstructorPhoneEditText.getText()) &&
                !TextUtils.isEmpty(mInstructorEmailEditText.getText());
    }
}