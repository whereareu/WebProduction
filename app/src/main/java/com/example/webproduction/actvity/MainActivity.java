package com.example.webproduction.actvity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import com.example.webproduction.fragment.HomeFragment;
import com.example.webproduction.view.MDToast;
import com.example.webproduction.fragment.ProjectionFragment;
import com.example.webproduction.R;
import com.example.webproduction.view.MDialogFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MDialogFragment.IDialogBn {
    @BindView(R.id.view_left_top)
    View left_top;
    @BindView(R.id.view_right_top)
    View right_top;
    @BindView(R.id.view_bottom_left)
    View bottom_left;
    @BindView(R.id.view_bottom_right)
    View bottom_right;

    public static String[][] TECHLISTS;
    public static IntentFilter[] FILTERS;
    static {
        try {
            TECHLISTS = new String[][] {
                    {IsoDep.class.getName()}, {NfcV.class.getName()}, { NfcF.class.getName()},
                    {NfcA.class.getName()}, {NfcB.class.getName()}, {NdefFormatable.class.getName(),
                    MifareClassic.class.getName()}
            };

            FILTERS = new IntentFilter[] {
                    new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED, "*/*")
            };
        } catch (Exception e) { }
    }
    public NfcAdapter nfcAdapter;
    public PendingIntent pendingIntent;

    private SharedPreferences sp;
    private long duration;
    private int section;

    long futureTime;
    final long interval = 1 * 1000;
    LogoutTimerTask logoutTimerTask;

    private WebView webView;
    private NavController navController;

    private int count;

    private MDialogFragment dialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "onCreate 0");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setNfcSetting();
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.edit().putInt("home_changed", 0).apply();
        sp.edit().putInt("projection_changed", 0).apply();
        sp.edit().putInt("duration_changed", 0).apply();
        sp.edit().putInt("logOut", 0).apply();

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        navController = Navigation.findNavController(this, R.id.fragment);
        navController.navigate(R.id.homeFragment);
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.fragment, new HomeFragment())
//                .commit();

        left_top.setOnClickListener(v -> {
            count = 1;
            Log.d("MainActivity", "left_top");
        });

        right_top.setOnClickListener(v -> {
            Log.d("MainActivity", "right_top");
        });

        bottom_left.setOnClickListener(v -> {
            if (count == 1) {
                count = 2;
            }
            Log.d("MainActivity", "bottom_left");
        });

        bottom_right.setOnClickListener(v -> {
            if (count == 2) {
                count = 0;
                dialogFragment = new MDialogFragment();
                dialogFragment.setiDialogBn(this);
                dialogFragment.show(getSupportFragmentManager(), "dialog");
            }
            Log.d("MainActivity", "bottom_right");
        });

        Log.d("MainActivity", "onCreate 1");
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (navController.getCurrentDestination().getId() == R.id.homeFragment) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
                if (tag != null) {
                    Log.d("MainActivity", "onNewIntent");
                    MifareClassic mifareClassic = MifareClassic.get(tag);
                    String value = getData(mifareClassic, section);
                    showSnackBar(left_top, value);
                    showToast(getString(R.string.logged), MDToast.TYPE_SUCCESS);
                    navController.popBackStack();
                    navController.navigate(R.id.projectionFragment);
//                            getSupportFragmentManager()
//                            .beginTransaction()
//                            .replace(R.id.fragment, new ProjectionFragment())
//                            .commit();
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MainActivity", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "onResume");
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, FILTERS, TECHLISTS);
        }
        duration = Long.parseLong(sp.getString(getString(R.string.sp_duration), getString(R.string.duration)));
        futureTime = interval * duration * 60;
        if (sp.getInt("duration_changed", 0) == 1) {
            sp.edit().putInt("duration_changed", 0).commit();
            //重新创建新的TimerTask需要先将之前的实例计时取消
            if (logoutTimerTask != null) {
                logoutTimerTask.cancel();
            }
            logoutTimerTask = new LogoutTimerTask(futureTime, interval);
        }
        onUserInteraction();
        section = Integer.parseInt(sp.getString(getString(R.string.sp_section), getString(R.string.section)));
        if (sp.getInt("exit", 0) == 1) {
            sp.edit().putInt("exit", 0).commit();
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
        Log.d("MainActivity", "onPause");
    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            if (navController.getCurrentDestination().getId() == R.id.projectionFragment) {
                popUpChoose(getString(R.string.loggingOut), dialog -> {
                    showToast(getString(R.string.loggedOut), MDToast.TYPE_INFO);
                    navController.popBackStack();
                    navController.navigate(R.id.homeFragment);
//                    getSupportFragmentManager()
//                            .beginTransaction()
//                            .replace(R.id.fragment, new HomeFragment())
//                            .commit();
                });
            } else {
                logoutTimerTask.cancel();
                finish();
            }
        }
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        Log.d("MainActivity", "onUserInteraction");
        if (logoutTimerTask != null) {
            logoutTimerTask.cancel();
            logoutTimerTask.start();
        } else {
            logoutTimerTask = new LogoutTimerTask(futureTime, interval);
            logoutTimerTask.start();
        }
    }

    protected String ByteArrayToHexString(byte[] data) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            String plainText = Integer.toHexString(0xff & data[i]);
            if (plainText.length() < 2)
                plainText = "0" + plainText;
            hexString.append(plainText);
        }
        return hexString.toString().toUpperCase();
    }

    public String getData(MifareClassic mfc, int sector) {
        String rep = "";
        int bIndex;
        byte[] data;
        try {
            mfc.connect();
            boolean auth;
            auth = mfc.authenticateSectorWithKeyB(sector, MifareClassic.KEY_DEFAULT);
            if(auth) {
                bIndex = mfc.sectorToBlock(sector);
                // 6.3) Read the block
                data = mfc.readBlock(bIndex);
                rep= ByteArrayToHexString(data);
            } else {
                Log.d("MainActivity", "nfc no auth");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rep;
    }

    @Override
    public void onPositiveClick(String txt) {
        if(txt.equals("123456")) {
            Intent settings = new Intent(this, SettingActivity.class);
            startActivity(settings);
        } else {
            showToast(getString(R.string.pass_error), MDToast.TYPE_ERROR);
        }
    }


    class LogoutTimerTask extends CountDownTimer {
        public LogoutTimerTask(long futureTime, long interval) {
            super(futureTime, interval);
        }

        @Override
        public void onFinish() {
            if (navController.getCurrentDestination().getId() == R.id.projectionFragment) {
                showToast(getString(R.string.loggedOut), MDToast.TYPE_INFO);
                navController.popBackStack();
                navController.navigate(R.id.homeFragment);
            }
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.fragment, new HomeFragment())
//                    .commit();
            onUserInteraction();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Log.e("MilliSec", " - " + millisUntilFinished);
        }
    }

    private void setNfcSetting() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if (nfcAdapter != null) {
            if (!nfcAdapter.isEnabled()) {
                showToast(getString(R.string.nfcBeClosed), MDToast.TYPE_WARNING);
            }
        } else showToast(getString(R.string.nfcNoFuture), MDToast.TYPE_WARNING);
    }


    public SharedPreferences getSp() {
        return sp;
    }

    public NavController getNavController() {
        return navController;
    }

    public void setWebView(WebView webView) {
        this.webView = webView;
    }
}
