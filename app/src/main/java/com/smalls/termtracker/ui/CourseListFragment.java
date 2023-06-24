package com.smalls.termtracker.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
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
import com.smalls.termtracker.viewmodel.CourseListViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class CourseListFragment extends Fragment {

    public interface OnCourseSelectedListener {
        void onCourseSelected(int courseId);
    }
    //reference to CourseListActivity
    private OnCourseSelectedListener mListener;
    private LiveData<List<Course>> mAllCoursesLiveData;
    private List<Course> mAllCourses;
    private List<Course> associatedCourses;
    private CourseAdapter adapter;
    private CourseListViewModel mViewModel;
    private int mTermId;

    public CourseListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mViewModel = new ViewModelProvider(
                requireActivity()
        ).get(CourseListViewModel.class);

        mAllCoursesLiveData = mViewModel.getAllCoursesLiveData();
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
        if (mBundle != null) {
            mTermId = mBundle.getInt("TERM_ID");
        } else {
            mTermId = 0;
        }

        associatedCourses = new ArrayList<>();
        adapter = new CourseAdapter(associatedCourses, mListener, mTermId);
        RecyclerView recyclerView = view.findViewById(R.id.course_recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(
                        recyclerView.getContext(),
                        DividerItemDecoration.VERTICAL
                )
        );

        mAllCoursesLiveData.observe(getViewLifecycleOwner(), courseObserver);

//        mAllCourses.observe(
//                getViewLifecycleOwner(),
//                adapter::submitList
//        );

        return view;
    }

    final Observer<List<Course>> courseObserver = new Observer<List<Course>>() {
        @Override
        public void onChanged(List<Course> associatedCourses) {
            List<Course> updatedList = associatedCourses.stream()
                    .filter(c -> c.getTermId() == mTermId)
                    .collect(Collectors.toList());
            adapter.submitList(updatedList);
        }
    };

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}