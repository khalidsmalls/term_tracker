package com.smalls.termtracker.ui;

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
import com.smalls.termtracker.entity.Assessment;

import java.util.List;

public class AssessmentAdapter extends ListAdapter<Assessment, AssessmentAdapter.AssessmentViewHolder> {

    private static AssessmentListFragment.OnAssessmentSelectedListener mListener;

    public AssessmentAdapter(
            AssessmentListFragment.OnAssessmentSelectedListener listener
    ) {
        super(new AssessmentDiff());
        mListener = listener;
    }

    public static class AssessmentViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public AssessmentViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.assessment_list_item);
        }

        public TextView getTextView() { return textView; }
    }

    @NonNull
    @Override
    public AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_assessment, parent, false);
        return new AssessmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentViewHolder holder, int position) {
        Assessment current = getItem(position);
        holder.getTextView().setTag(current.getId());
        holder.getTextView().setText(current.getType().toString());
        holder.getTextView().setOnClickListener(AssessmentClickListener);
    }

    private static class AssessmentDiff extends DiffUtil.ItemCallback<Assessment> {

        @Override
        public boolean areItemsTheSame(
                @NonNull Assessment oldItem,
                @NonNull Assessment newItem
        ) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(
                @NonNull Assessment oldItem,
                @NonNull Assessment newItem
        ) {
            return oldItem.getStart().equals(newItem.getStart()) &&
                    oldItem.getEnd().equals(newItem.getEnd()) &&
                    oldItem.getType().equals(newItem.getType());
        }
    }

    private final View.OnClickListener AssessmentClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int assessmentId = (int) v.getTag();
            mListener.onAssessmentSelected(assessmentId);
        }
    };
}
