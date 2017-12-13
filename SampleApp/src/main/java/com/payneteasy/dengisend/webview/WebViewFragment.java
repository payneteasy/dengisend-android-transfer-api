package com.payneteasy.dengisend.webview;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.payneteasy.dengisend.MainContract;
import com.payneteasy.dengisend.R;

/**
 * Dengisend
 * <p>
 * Created by Alex Oleynyak on 19/09/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

public class WebViewFragment extends Fragment implements WebViewContract.View {

    WebView mWebView;

    ProgressBar pageLoadingProgress;

    String mUrl;
    String loadedUrl;

    boolean doSetInitialScale = false;

    public WebViewFragment() {
        // Requires empty public constructor
    }

    public static WebViewFragment newInstance() {
        return new WebViewFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(doSetInitialScale){
            mWebView.getSettings().setBuiltInZoomControls(true);
            mWebView.getSettings().setSupportZoom(true);
            mWebView.setInitialScale(getScale());
        }

        if (mUrl != null) {
            // Maybe we already loaded the url
            if(loadedUrl == null || !loadedUrl.equals(mUrl)) {
                mWebView.loadUrl(mUrl);
                loadedUrl = mUrl;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void setUrl(String url) {

        mUrl = url;

        String filenameArray[] = url.split("\\.");
        String extension = filenameArray[filenameArray.length - 1];

        if (extension.equals("pdf")) {
            mUrl = "https://docs.google.com/gview?embedded=true&url=" + url;
        }
    }

    public void setInitialScale(boolean setInitialScale) {
        doSetInitialScale = setInitialScale;
    }

    @Override
    public void setMainActivity(@NonNull MainContract.Activity activity) {
        //mainActivity = checkNotNull(activity);
    }

    @Override
    public void setPresenter(@NonNull WebViewContract.Presenter presenter) {
        //mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.webview_frag, container, false);

        pageLoadingProgress = (ProgressBar) root.findViewById(R.id.page_loading_progress);

        mWebView = (WebView) root.findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                // SHOW LOADING
                pageLoadingProgress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // HIDE LOADING
                pageLoadingProgress.setVisibility(View.GONE);
            }
        });

        return root;
    }

    private int getScale() {
        final DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int) ((dm.widthPixels / (1024d)) * 100d);
    }
}
