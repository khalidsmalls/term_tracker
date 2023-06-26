package com.smalls.termtracker.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smalls.termtracker.R;
import com.smalls.termtracker.entity.Assessment;
import com.smalls.termtracker.viewmodel.AssessmentListViewModel;

import java.util.List;

public class AssessmentListFragment extends Fragment {
    public interface OnAssessmentSelectedListener {
        void onAssessmentSelected(int assessmentId);
    }
    //reference to AssessmentListActivity
    private OnAssessmentSelectedListener mListener;
    private LiveData<List<Assessment>> mAssociatedAssessments;
    private AssessmentListViewModel mViewModel;

    public AssessmentListFragment() {
        // Required empty public constructor
    }

   @Override
   public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        Bundle mBundle = getArguments();
       int mCourseId;
       if (mBundle != null) {
            String COURSE_ID = "course_id";
            mCourseId = mBundle.getInt(COURSE_ID);
        } else {
            mCourseId = 0;
        }

        mViewModel = new ViewModelProvider(
                requireActivity(),
                new AssessmentListViewModel.ViewModelFactory(
                        this.requireActivity().getApplication(),
                        mCourseId
                )
        ).get(AssessmentListViewModel.class);

        mAssociatedAssessments = mViewModel.getAssociatedAssessments();

        if (context instanceof OnAssessmentSelectedListener) {
            mListener = (OnAssessmentSelectedListener) context;
        }
   }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(
                R.layout.fragment_assessment_list,
                container,
                false
        );

        final AssessmentAdapter adapter = new AssessmentAdapter(mListener);
        RecyclerView recyclerView = view.findViewById(R.id.assessment_recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(
                        recyclerView.getContext(),
                        DividerItemDecoration.VERTICAL
                )
        );

        mAssociatedAssessments.observe(
                getViewLifecycleOwner(),
                assessments -> {
                    mAssociatedAssessments = mViewModel.getAssociatedAssessments();
                    List<Assessment> associatedAssessments = mAssociatedAssessments.getValue();
                    adapter.submitList(associatedAssessments);
                }
        );

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}