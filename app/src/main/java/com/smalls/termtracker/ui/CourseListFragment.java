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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smalls.termtracker.R;
import com.smalls.termtracker.entity.Course;
import com.smalls.termtracker.viewmodel.CourseDetailViewModel;

import java.util.List;
import java.util.stream.Collectors;


public class CourseListFragment extends Fragment {
    //implemented by CourseListActivity
    public interface OnCourseSelectedListener {
        void onCourseSelected(int courseId);
    }

    //reference to CourseListActivity
    private OnCourseSelectedListener mListener;

    private LiveData<List<Course>> mAllCourses;

    public CourseListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        CourseDetailViewModel mViewModel = new ViewModelProvider(
                requireActivity()
        ).get(CourseDetailViewModel.class);

        mAllCourses = mViewModel.getAllCourses();

        if (context instanceof OnCourseSelectedListener) {
            mListener = (OnCourseSelectedListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_course_list,
                container,
                false
        );
        Bundle mBundle = getArguments();
        int mTermId;
        if (mBundle != null) {
            mTermId = mBundle.getInt("TERM_ID");
        } else {
            mTermId = 0;
        }

        final CourseAdapter adapter = new CourseAdapter(mAllCourses, mListener, mTermId);
        RecyclerView recyclerView = view.findViewById(R.id.course_recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(
                        recyclerView.getContext(),
                        DividerItemDecoration.VERTICAL
                )
        );

        mAllCourses.observe(
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