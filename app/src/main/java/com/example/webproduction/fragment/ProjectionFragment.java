package com.example.webproduction.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.webproduction.view.MDToast;
import com.example.webproduction.R;
import com.example.webproduction.actvity.SettingActivity;

public class ProjectionFragment extends BaseFragment {

    @Override
    public void onResume() {
        super.onResume();
        Log.d("OnResume", "HomeFragment");
        if (sp.getInt("projection_changed", 0) == 1) {
            sp.edit().putInt("projection_changed", 0).commit();
            activity.showToast(getString(R.string.setting_changed), MDToast.TYPE_INFO);
            navController.popBackStack();
            navController.navigate(R.id.homeFragment);
//            activity.getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.fragment, new HomeFragment())
//                    .commit();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView.loadUrl(sp.getString(getString(R.string.sp_production_url), getString(R.string.url_projection)));
    }

    @Override
    public void onClick(View v) {

    }
}
