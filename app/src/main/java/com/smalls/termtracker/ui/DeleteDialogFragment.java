package com.smalls.termtracker.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.smalls.termtracker.R;


public class DeleteDialogFragment extends DialogFragment {
    String mMessage;
    public interface onDeleteDialogListener {
        void onDelete();
    }

    private onDeleteDialogListener mListener;

    public DeleteDialogFragment(String message) {
        mMessage = message;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                requireActivity(),
                R.style.alertDialog
        );
        builder.setMessage(mMessage);
        builder.setPositiveButton(
                "Delete",
                (dialog, which) -> mListener.onDelete()
        );
        builder.setNegativeButton(
                "Cancel",
                null
        );
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (onDeleteDialogListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}

