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
import com.smalls.termtracker.entity.Term;
import com.smalls.termtracker.viewmodel.TermListViewModel;

import java.util.List;

public class TermListFragment extends Fragment {

    //for TermListActivity to implement
    public interface OnTermSelectedListener {
        void onTermSelected(int termId);
    }
    //reference to TermListActivity
    private OnTermSelectedListener mListener;

    private LiveData<List<Term>> mAllTerms;

    public TermListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnTermSelectedListener) {
            mListener = (OnTermSelectedListener) context;
            TermListViewModel viewModel =
                    new ViewModelProvider(requireActivity()).get(TermListViewModel.class);
            mAllTerms = viewModel.getAllTerms();
        } else {
            throw new RuntimeException(
                    context + " must implement OnTermSelectedListener"
            );
        }
    }

    /**
     * initialize adapter and recyclerview. set observer on mAllTerms
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(
                R.layout.fragment_term_list,
                container,
                false
        );
        final TermAdapter adapter = new TermAdapter(mListener);
        RecyclerView recyclerView = view.findViewById(R.id.term_recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(
                        recyclerView.getContext(),
                        DividerItemDecoration.VERTICAL
                )
        );

        mAllTerms.observe(
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