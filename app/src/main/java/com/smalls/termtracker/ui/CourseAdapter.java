package com.smalls.termtracker.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.smalls.termtracker.R;
import com.smalls.termtracker.entity.Course;

import java.util.List;

public class CourseAdapter extends ListAdapter<Course, CourseAdapter.ViewHolder> {
    private static CourseListFragment.OnCourseSelectedListener mListener;

    public CourseAdapter(
            CourseListFragment.OnCourseSelectedListener listener
    ) {
        super(new CourseDiff());
        mListener = listener;
    }

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
        Course current = getItem(position);
        holder.getTextView().setText(current.getTitle());
        holder.getTextView().setTag(current.getId());
        holder.getTextView().setOnClickListener(CourseClickListener);
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
            int mCourseId = (int) v.getTag();
            mListener.onCourseSelected(mCourseId);
        }
    };

}
