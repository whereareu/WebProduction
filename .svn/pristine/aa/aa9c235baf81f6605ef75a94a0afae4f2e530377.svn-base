package com.example.webproduction.actvity;

import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.webproduction.view.MDToast;
import com.example.webproduction.view.MSnackBar;
import com.example.webproduction.view.PopUpDialog;
import com.example.webproduction.R;

public class BaseActivity extends AppCompatActivity {
    public MDToast mdToast;
    public MSnackBar mSnackBar;
    public PopUpDialog dialog;

    public void showSnackBar(View v, String m) {
        if (mSnackBar != null) mSnackBar.showSnackBar(v,m);
        else {
            mSnackBar = new MSnackBar();
            mSnackBar.showSnackBar(v, m);
        }
    }

    public void showToast(String m, int t) {
        if (mdToast != null) mdToast.cancel();
        mdToast = MDToast.makeText(this, m, Toast.LENGTH_SHORT, t);
        mdToast.setGravity(Gravity.CENTER, 0, 0);
        mdToast.show();
    }

    public void popUpChoose(String message, PopUpDialog.OnPositiveListener listener){
        if (dialog != null) dialog.dismiss();
        dialog = new PopUpDialog(this)
                .setDialogType(PopUpDialog.DIALOG_TYPE_INFO)
                .setAnimationEnable(true)
                .setTitleText(getString(R.string.prompt))
                .setContentText(message)
                .setNegativeListener(getString(R.string.close), new PopUpDialog.OnNegativeListener() {
                    @Override
                    public void onClick(PopUpDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .setPositiveListener(R.string.ok, listener);
        dialog.show();
    }
}
