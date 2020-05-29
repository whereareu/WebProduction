package com.example.webproduction.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import com.example.webproduction.R;
import com.example.webproduction.actvity.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.error_view)
    LinearLayout errorView;
    @BindView(R.id.reBackbn)
    Button reloadBn;
    Unbinder unbinder;

    public Animation animation = new AlphaAnimation(1f, 0f);
    public MainActivity activity;
    public SharedPreferences sp;
    public NavController navController;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity)
            activity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = activity.getSp();
        navController = activity.getNavController();
        Log.d("MainActivity", "onCreate Fragment");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_web, container, false);
        Log.d("MainActivity", "onCreateView Fragment");
        unbinder = ButterKnife.bind(this, rootView);

        animation.setDuration(700);
        activity.setWebView(webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setSaveFormData(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDisplayZoomControls(true);
        webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);

        webView.setWebViewClient(new WebServiceViewClient());
        webView.setWebChromeClient(new WebServiceChromeClient());
        reloadBn.setOnClickListener(v -> activity.onBackPressed());
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private class WebServiceViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Log.d("WebView", "shouldOverrideUrlLoading");
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d("WebView", "onPageStarted");
            if(progressBar == null || errorView == null) return;
            progressBar.clearAnimation();
            progressBar.setProgress(0);
            progressBar.setVisibility(View.VISIBLE);
            errorView.setVisibility(View.GONE);
            Log.d("WebView", "onPageStarted" + url);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Log.d("WebView", "onReceivedError");
//            if (progressBar == null || progressBar == null || errorView == null) return;
//            progressBar.setVisibility(View.GONE);
//            webView.setVisibility(View.GONE);
//            errorView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String loadedUrl) {
            super.onPageFinished(view, loadedUrl);
            Log.d("WebView", "onPageFinished");
            if (progressBar == null) return;
            Log.d("WebView", "onPageFinished" + loadedUrl);
            progressBar.startAnimation(animation);
            progressBar.setVisibility(View.GONE);
        }
    }

    private class WebServiceChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int progress) {
            super.onProgressChanged(view, progress);
            Log.d("WebView", "onProgressChanged");
            if (progressBar != null) {
                progressBar.setProgress(progress);
            }
            if (webView != null) {
                if (webView.getVisibility() != View.VISIBLE && progress > 80) {
                    webView.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
