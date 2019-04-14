package com.ldx.mygraduationproject.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;


import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.constant.AppConfig;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.KeyEvent.KEYCODE_BACK;

public class WebActivity extends BaseActivity {

    public static final int MODE_ARTICLE = 1;
    public static final int MODE_IDSEASE = 2;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    /**
     * mode: 模式
     * 1.知乎文章
     * 2.百度百科-病症
     */
    private int mode;

    /**
     * url:地址
     * 如果为知乎文章则为地址
     * 如果为病症则为病症名
     */
    private String content;


    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.web_view)
    WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mode = getIntent().getBundleExtra(AppConfig.WEB_INTENT).getInt(AppConfig.WEB_INTENT_MODE);
        content = getIntent().getBundleExtra(AppConfig.WEB_INTENT).getString(AppConfig.WEB_INTENT_CONTENT);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void initView() {
        if (mode == MODE_ARTICLE){
            toolbarTitle.setText("文章详情");
        }else if (mode == MODE_IDSEASE){
            toolbarTitle.setText("病症详情");
            content = content.trim();
            String s = "上呼吸道感染";
            content = AppConfig.BAIKE + content.trim();

        }else {

        }

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setAppCacheEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(false);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.toString());
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                view.loadUrl("file:///android_asset/html/error.html");
            }
        });
        webView.loadUrl(content);
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(toolbar).init();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_web;
    }


    @OnClick(R.id.toolbar_back)
    public void onViewClicked() {
        finish();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (webView != null) {
                webView.removeAllViews();
                webView.destroy();
                webView = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
