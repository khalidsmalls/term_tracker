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

public class AssessmentAdapter extends ListAdapter<Assessment, AssessmentAdapter.ViewHolder> {

    private static AssessmentListFragment.OnAssessmentSelectedListener mListener;
    private final LiveData<List<Assessment>> mAllAssessments;

    public AssessmentAdapter(
            LiveData<List<Assessment>> assessments,
            AssessmentListFragment.OnAssessmentSelectedListener listener
    ) {
        super(new AssessmentDiff());
        mAllAssessments = assessments;
        mListener = listener;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.assessment_list_item);
        }

        public TextView getTextView() { return textView; }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_assessment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //TODO check out enum
        if (mAllAssessments != null) {
            String text = String.valueOf(mAllAssessments.getValue().get(position).getType());
            holder.getTextView().setText(text);
            holder.getTextView().setTag(mAllAssessments.getValue().get(position).getId());
            holder.getTextView().setOnClickListener(AssessmentClickListener);
        }

    }

    @Override
    public int getItemCount() {
        if (mAllAssessments.getValue() != null) {
            return mAllAssessments.getValue().size();
        } else {
            return 0;
        }
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
            String assessmentId = v.getTag().toString();
            mListener.onAssessmentSelected(Integer.parseInt(assessmentId));
        }
    };
}
