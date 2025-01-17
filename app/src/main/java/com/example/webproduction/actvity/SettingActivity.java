package com.example.webproduction.actvity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.example.webproduction.R;
import com.example.webproduction.fragment.ProjectionFragment;
import com.example.webproduction.fragment.SettingFragment;

import java.util.Objects;

public class SettingActivity extends BaseActivity {
    private SharedPreferences sp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.setting));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        toolbar.setOnClickListener(v -> {
            Log.d("SettingActivity", "toolbar");
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, new SettingFragment())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_back:
                sp.edit().putInt("exit", 1).commit();
                finish();
                break;
        }
        return true;
    }
}
