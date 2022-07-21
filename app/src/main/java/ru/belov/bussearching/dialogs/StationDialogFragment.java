package ru.belov.bussearching.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import ru.belov.bussearching.R;

/**
 * Диалог добавления остановки.
 */
public class StationDialogFragment extends DialogFragment {

    public interface NoticeDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
    }

    NoticeDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder
                .setTitle("Добавить остановку")
                .setIcon(android.R.drawable.ic_input_add)
                .setView(R.layout.station_dialog)
                .setPositiveButton("ОК", (dialog, id) -> mListener.onDialogPositiveClick(StationDialogFragment.this))
                .setNegativeButton("Отмена", null)
                .create();
    }
}
