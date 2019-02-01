package com.ldx.mygraduationproject.activity;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;



import com.ldx.mygraduationproject.R;

import butterknife.BindView;
import butterknife.OnClick;

public class QuestionActivity extends BaseActivity {


    @BindView(R.id.web_view)
    WebView webView;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_question;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void initView() {
        super.initView();
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.loadUrl(request.getUrl().toString());
                } else {
                    view.loadUrl(request.toString());
                }
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                view.loadUrl("file:///android_asset/html/error.html");
            }
        });
        webView.loadUrl("https://m.drmed.cn/self-diagnosis");

    }
    //是否在webview内加载页面



    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();

    }
}
