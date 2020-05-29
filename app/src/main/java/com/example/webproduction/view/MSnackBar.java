package com.example.webproduction.view;

import android.content.Context;

import com.example.webproduction.App;
import com.example.webproduction.R;
import com.google.android.material.snackbar.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class MSnackBar {

    public void showSnackBar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view,"",Snackbar.LENGTH_SHORT);
        snackbar.setAction(App.getContext().getString(R.string.ok), v -> {

        });
        Snackbar.SnackbarLayout snackBarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        LayoutInflater inflater =  (LayoutInflater) App.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.view_snackbar,null);
        TextView snack_tv = (TextView) customView.findViewById(R.id.snackBar_tv);
        snack_tv.setText(message);
        snackBarLayout.addView(customView,0);
        snackbar.show();
    }
}
