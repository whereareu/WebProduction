package com.example.webproduction.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.webproduction.R;
import com.example.webproduction.actvity.MainActivity;

public class MDialogFragment extends DialogFragment {
    public MainActivity activity;
    public EditText editText;
    public IDialogBn iDialogBn;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof  MainActivity) {
            activity = (MainActivity) context;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog, null);
        editText = (EditText) view.findViewById(R.id.et_dialog);
        builder.setView(view).setTitle(getString(R.string.password))
                .setPositiveButton(getString(R.string.ok), (dialog, which) -> {
                    if (TextUtils.isEmpty(editText.getText())) return;
                    iDialogBn.onPositiveClick(editText.getText().toString());
                })
                .setNegativeButton(getString(R.string.close), null);
        return builder.show();
    }

    public interface IDialogBn {
        void onPositiveClick(String txt);
    }

    public void setiDialogBn(IDialogBn i) {
        this.iDialogBn = i;
    }
}
