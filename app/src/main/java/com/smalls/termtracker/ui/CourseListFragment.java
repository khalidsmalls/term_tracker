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
import com.smalls.termtracker.entity.Course;
import com.smalls.termtracker.viewmodel.CourseListViewModel;

import java.util.List;


public class CourseListFragment extends Fragment {

    public interface OnCourseSelectedListener {
        void onCourseSelected(int courseId);
    }
    //reference to CourseListActivity
    private OnCourseSelectedListener mListener;
    private LiveData<List<Course>> mAssociatedCourses;
    private int mTermId;
    private CourseListViewModel mViewModel;

    public CourseListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        Bundle mBundle = getArguments();
        if (mBundle != null) {
            String TERM_ID = "term_id";
            mTermId = mBundle.getInt(TERM_ID);
        } else {
            mTermId = 0;
        }

        mViewModel = new ViewModelProvider(
                requireActivity(),
                new CourseListViewModel.ViewModelFactory(
                    this.requireActivity().getApplication(),
                    mTermId
                )
        ).get(CourseListViewModel.class);

        mAssociatedCourses = mViewModel.getAssociatedCourses();

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

        final CourseAdapter adapter = new CourseAdapter(mListener);
        RecyclerView recyclerView = view.findViewById(R.id.course_recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(
                        recyclerView.getContext(),
                        DividerItemDecoration.VERTICAL
                )
        );

        mAssociatedCourses.observe(
                getViewLifecycleOwner(),
                courses -> {
                    mAssociatedCourses = mViewModel.getAssociatedCourses();
                    List<Course> associatedCourses = mAssociatedCourses.getValue();
                    adapter.submitList(associatedCourses);
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