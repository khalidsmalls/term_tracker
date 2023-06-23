package com.smalls.termtracker.ui;

import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.smalls.termtracker.R;
import com.smalls.termtracker.entity.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

public class CourseAdapter extends ListAdapter<Course, CourseAdapter.ViewHolder> {
    private static CourseListFragment.OnCourseSelectedListener mListener;
    private final LiveData<List<Course>> mAllCourses;
    private final int mTermId;
    private int mCourseId;
    private Executor executor;

    public CourseAdapter(
            LiveData<List<Course>> courses,
            CourseListFragment.OnCourseSelectedListener listener,
            int termId
    ) {
        super(new CourseDiff());
        mAllCourses = courses;
        mListener = listener;
        mTermId = termId;
    }

    /**
     * nested ViewHolder class
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.course_list_item);
        }

        public TextView getTextView() { return textView; }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_course, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mAllCourses.getValue() != null) {
            List<Course> associatedCourses = mAllCourses.getValue().stream()
                    .filter(c -> c.getTermId() == mTermId)
                    .collect(Collectors.toList());

            String text = associatedCourses.get(position).getTitle();
            mCourseId = associatedCourses.get(position).getId();
            holder.getTextView().setText(text);
            holder.getTextView().setOnClickListener(CourseClickListener);
        }
    }

    @Override
    public int getItemCount() {
        if (mAllCourses.getValue() != null) {
            return (int) mAllCourses.getValue()
                    .stream()
                    .filter(c -> c.getTermId() == mTermId)
                    .count();
        } else {
            return 0;
        }
    }

    private static class CourseDiff extends DiffUtil.ItemCallback<Course> {
        @Override
        public boolean areItemsTheSame(
                @NonNull Course oldItem,
                @NonNull Course newItem
        ) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(
                @NonNull Course oldItem,
                @NonNull Course newItem
        ) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getStart().equals(newItem.getStart()) &&
                    oldItem.getEnd().equals(newItem.getEnd()) &&
                    oldItem.getInstructorName().equals(newItem.getInstructorName()) &&
                    oldItem.getInstructorPhone().equals(newItem.getInstructorPhone()) &&
                    oldItem.getInstructorEmail().equals(newItem.getInstructorEmail()) &&
                    oldItem.getOptionalNote().equals(newItem.getOptionalNote());
        }
    }

    private final View.OnClickListener CourseClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mListener.onCourseSelected(mCourseId);
        }
    };




}
