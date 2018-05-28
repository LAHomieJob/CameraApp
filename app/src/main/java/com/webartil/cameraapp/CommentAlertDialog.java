package com.webartil.cameraapp;

import android.app.Dialog;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.arch.lifecycle.LifecycleOwner;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

public class CommentAlertDialog extends DialogFragment implements LifecycleOwner {

    private static final String TAG = "CANCEL";
    DialogListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the DialogListener so we can send events to the host
            mListener = (DialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement DialogListener");
        }
    }

    @NonNull
    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.dialog_fragment, null);
        builder.setView(dialoglayout);
        builder.setMessage(R.string.comment_photo)
                .setPositiveButton(R.string.comment, (dialog, id) ->
                        mListener.onDialogPositiveClick(this)
                )
                .setNegativeButton(R.string.cancel, (dialog, id) ->
                        mListener.onDialogNegativeClick(this));
        return builder.create();
    }

    public interface DialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        default void onDialogNegativeClick(DialogFragment dialog) {
            Log.i(TAG, "Cancel button in comment dialog.");
            dialog.dismiss();
        }
    }
}
