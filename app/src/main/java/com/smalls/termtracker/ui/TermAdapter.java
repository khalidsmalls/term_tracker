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
import com.smalls.termtracker.entity.Term;

import java.util.List;

public class TermAdapter extends ListAdapter<Term, TermAdapter.ViewHolder> {
    private static TermListFragment.OnTermSelectedListener mListener;

    private final LiveData<List<Term>> mTerms;

    public TermAdapter(
            LiveData<List<Term>> terms,
            TermListFragment.OnTermSelectedListener listener
    ) {
        super(new TermDiff());
        mTerms = terms;
        mListener = listener;
    }

    /**
     * nested ViewHolder class
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.term_list_item);
        }
        public TextView getTextView() {
            return textView;
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_term, parent, false);

        return new ViewHolder(view);
    }

    /**
     * specify  recyclerView ViewHolders
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mTerms.getValue() != null) {
            String text = mTerms.getValue().get(position).getTitle();
            holder.getTextView().setText(text);
            holder.getTextView().setTag(mTerms.getValue().get(position).getId());
            holder.getTextView().setOnClickListener(TermClickListener);
        }
    }

    @Override
    public int getItemCount() {
        if (mTerms.getValue() != null) {
            return mTerms.getValue().size();
        } else {
            return 0;
        }
    }

    static class TermDiff extends DiffUtil.ItemCallback<Term> {
        @Override
        public boolean areItemsTheSame(
                @NonNull Term oldItem,
                @NonNull Term newItem
        ) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(
                @NonNull Term oldItem,
                @NonNull Term newItem
        ) {
            return oldItem.getTitle().equals(newItem.getTitle());
        }
    }

    private final View.OnClickListener TermClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int termId = (int) v.getTag();
            mListener.onTermSelected(termId);
        }
    };

}
