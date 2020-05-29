package com.example.webproduction.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.webproduction.R;
import com.example.webproduction.actvity.SettingActivity;

public class HomeFragment extends BaseFragment {

    @Override
    public void onResume() {
        super.onResume();
        if (sp == null) return;
        if (sp.getInt("home_changed", 0) == 1) {
            sp.edit().putInt("home_changed", 0).commit();
//            activity.getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.fragment, new HomeFragment())
//                    .commit();
            navController.popBackStack();
            navController.navigate(R.id.homeFragment);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(sp == null) return;
        String url = sp.getString(getString(R.string.sp_default_url), getString(R.string.url_default));
        webView.loadUrl(url);
    }

    @Override
    public void onClick(View v) {

    }
}
