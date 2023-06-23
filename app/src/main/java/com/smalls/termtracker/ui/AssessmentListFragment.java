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

    //for AssessmentListActivity to implement
    public interface OnAssessmentSelectedListener {
        void onAssessmentSelected(int assessmentId);
    }

    //reference to AssessmentListActivity
    private OnAssessmentSelectedListener mListener;

    private LiveData<List<Assessment>> mAllAssessments;

    public AssessmentListFragment() {
        // Required empty public constructor
    }

   @Override
   public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnAssessmentSelectedListener) {
            mListener = (OnAssessmentSelectedListener) context;
            AssessmentListViewModel viewModel =
                    new ViewModelProvider(requireActivity()).get(AssessmentListViewModel.class);
            mAllAssessments = viewModel.getAllAssessments();
        } else {
            throw new RuntimeException(
                    context + " must implement OnAssessmentSelectedListener"
            );
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
        final AssessmentAdapter adapter = new AssessmentAdapter(mAllAssessments, mListener);
        RecyclerView recyclerView = view.findViewById(R.id.assessment_recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(
                        recyclerView.getContext(),
                        DividerItemDecoration.VERTICAL
                )
        );

        mAllAssessments.observe(
                getViewLifecycleOwner(),
                adapter::submitList
        );
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}