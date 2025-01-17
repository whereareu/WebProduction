package com.example.webproduction.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.NumberKeyListener;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.webproduction.R;
import com.example.webproduction.actvity.SettingActivity;

public class SettingFragment extends PreferenceFragmentCompat
        implements
        SharedPreferences.OnSharedPreferenceChangeListener {
    SettingActivity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SettingActivity)
            activity = (SettingActivity) context;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference_setting, rootKey);
        EditTextPreference section = findPreference(getString(R.string.sp_section));
        section.setOnBindEditTextListener(editText -> {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        });
        EditTextPreference duration = findPreference(getString(R.string.sp_duration));
        duration.setOnBindEditTextListener(editText -> {
            editText.setKeyListener(new NumberKeyListener() {
                @NonNull
                @Override
                protected char[] getAcceptedChars() {
                    char[] nums = {'1', '2', '3', '4', '5', '6', '7', '8', '9'} ;
                    return nums;
                }

                @Override
                public int getInputType() {
                    return InputType.TYPE_CLASS_NUMBER;
                }
            });
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        sp.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, String key) {
        Log.d("SettingFragment", "It's onChanged");
        if (key.equals(getString(R.string.sp_production_url)) || key.equals(getString(R.string.sp_key)) || key.equals(getString(R.string.sp_section))) {
            sharedPreferences.edit().putInt("projection_changed", 1).apply();
            if (key.equals(getString(R.string.sp_section))) {
                if (TextUtils.isEmpty(sharedPreferences.getString(key, getString(R.string.section)))) {
                    sharedPreferences.edit().putString(key, getString(R.string.section)).commit();
                }
            }
        }
        if (key.equals(getString(R.string.sp_default_url))) {
            sharedPreferences.edit().putInt("home_changed", 1).commit();
        }
        if (key.equals(getString(R.string.sp_duration))) {
            sharedPreferences.edit().putInt("duration_changed", 1).apply();
            if (TextUtils.isEmpty(sharedPreferences.getString(key, getString(R.string.duration)))) {
                sharedPreferences.edit().putString(key, getString(R.string.duration)).commit();
            }
        }
    }
}
