package com.ldx.mygraduationproject.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.activity.MainActivity;
import com.ldx.mygraduationproject.activity.WebActivity;
import com.ldx.mygraduationproject.adapter.AdapterArticle;
import com.ldx.mygraduationproject.bean.Article;
import com.ldx.mygraduationproject.constant.AppConfig;
import com.ldx.mygraduationproject.utils.GlideImageLoader;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by freeFreAme on 2018/12/26.
 */
public class FragmentHeath extends BaseFragment {

    @BindView(R.id.fragment_health_banner)
    Banner fragmentHealthBanner;
    @BindView(R.id.fragment_health_toolbar_user)
    ImageView fragmentHealthToolbarUser;
    @BindView(R.id.fragment_health_toolbar_search)
    ImageView fragmentHealthToolbarSearch;
    @BindView(R.id.fragment_health_toolbar)
    Toolbar fragmentHealthToolbar;
    @BindView(R.id.fragment_health_article_rv)
    RecyclerView fragmentHealthArticleRv;
    @BindView(R.id.fragment_health_tab)
    TabLayout fragmentHealthTab;

    private AdapterArticle adapterArticle;
    private ArrayList<Article> beans1 = null;
    private ArrayList<Article> beans2 = null;
    private ArrayList<Article> beans3 = null;
    private ArrayList<Article> beans4 = null;
    private Handler getArticlesHandler;
    private Handler getGetArticlesHandlerByClasses1;
    private Handler getGetArticlesHandlerByClasses2;
    private Handler getGetArticlesHandlerByClasses3;
    private Handler getGetArticlesHandlerByClasses4;
    private List<Article> articleArrayList;
    private Article article = null;


    @Override
    protected int setLayoutId() {
        return R.layout.fragment_health;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initView() {
        super.initView();
        setBanner();
//        getArticleFromNet();
        getArticleFromNetByClasses1("1");
        getArticleFromNetByClasses2("2");
        getArticleFromNetByClasses3("3");
        getArticleFromNetByClasses4("4");
        setFragmentHealthArticleRv();

    }


    @Override
    protected void setListener() {
        super.setListener();
    }

    /**
     * 设置轮播图
     */
    private void setBanner() {
        fragmentHealthBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.news_image_1);
        images.add(R.drawable.news_image_2);
        images.add(R.drawable.news_image_3);
        images.add(R.drawable.news_image_4);
        images.add(R.drawable.news_image_5);
        images.add(R.drawable.news_image_6);
        fragmentHealthBanner.setImages(images);
        //banner设置方法全部调用完毕时最后调用
        fragmentHealthBanner.start();
        fragmentHealthBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                switch (position) {
                }
                Article bean = new Gson().fromJson(AppConfig.DATE2, Article.class);
                Intent intent = new Intent(mActivity, WebActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(AppConfig.WEB_INTENT_MODE, WebActivity.MODE_ARTICLE);
                bundle.putString(AppConfig.WEB_INTENT_CONTENT, bean.getArticleUrl());
                intent.putExtra(AppConfig.WEB_INTENT, bundle);
                mActivity.startActivity(intent);
            }
        });
    }

    private void setFragmentHealthArticleRv() {
        adapterArticle = new AdapterArticle(mActivity, null);
        fragmentHealthArticleRv.setLayoutManager(new LinearLayoutManager(mActivity));
        fragmentHealthArticleRv.setAdapter(adapterArticle);
        getData();
    }

    @SuppressLint("HandlerLeak")
    public void getData() {
        getGetArticlesHandlerByClasses1 = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                articleArrayList = (List<Article>) msg.obj;
                beans1 = (ArrayList<Article>) articleArrayList;
                adapterArticle.setData(beans1);
            }
        };
        getGetArticlesHandlerByClasses2 = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                articleArrayList = (List<Article>) msg.obj;
                beans2 = (ArrayList<Article>) articleArrayList;
            }
        };
        getGetArticlesHandlerByClasses3 = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                articleArrayList = (List<Article>) msg.obj;
                beans3 = (ArrayList<Article>) articleArrayList;
            }
        };
        getGetArticlesHandlerByClasses4 = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                articleArrayList = (List<Article>) msg.obj;
                beans4 = (ArrayList<Article>) articleArrayList;
            }
        };
//                switch (mode) {
//                    case 1:

//                    case 2:
//                        beans2 = (ArrayList<Article>) bean.getData().getContent();
//                        adapterArticle.setData(beans2);
//                        break;
//                }
        fragmentHealthTab.addTab(fragmentHealthTab.newTab().setText("健康养生"));
        fragmentHealthTab.addTab(fragmentHealthTab.newTab().setText("流行疾病"));
        fragmentHealthTab.addTab(fragmentHealthTab.newTab().setText("医学热点"));
        fragmentHealthTab.addTab(fragmentHealthTab.newTab().setText("疾病预防"));
        fragmentHealthTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int index = tab.getPosition();
                switch (index) {
                    case 0:
                        adapterArticle.setData(beans1);
                        break;
                    case 1:
                        adapterArticle.setData(beans2);
                        break;
                    case 2:
                        adapterArticle.setData(beans3);
                        break;
                    case 3:
                        adapterArticle.setData(beans4);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//            }
//        };
    }


    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(fragmentHealthToolbar).init();
    }

    public void getArticleFromNet() {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        final Request request = new Request.Builder()
                .url(AppConfig.GET_ALL_ARTICLE)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseStr = response.body().string();
                List<Article> articles = new ArrayList<>();
                articles = com.alibaba.fastjson.JSONArray.parseArray(responseStr, Article.class);
                Message msg = getArticlesHandler.obtainMessage();
                msg.obj = articles;
                getArticlesHandler.sendMessage(msg);

            }
        });
    }

    public void getArticleFromNetByClasses1(String classes) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("classes", classes);
        final Request request = new Request.Builder()
                .url(AppConfig.GET_CLASSES_ARTICLE)
                .post(builder.build())
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseStr = response.body().string();
                List<Article> articles = new ArrayList<>();
                articles = com.alibaba.fastjson.JSONArray.parseArray(responseStr, Article.class);
                Message msg = getGetArticlesHandlerByClasses1.obtainMessage();
                msg.obj = articles;
                getGetArticlesHandlerByClasses1.sendMessage(msg);

            }
        });
    }
    public void getArticleFromNetByClasses2(String classes) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("classes", classes);
        final Request request = new Request.Builder()
                .url(AppConfig.GET_CLASSES_ARTICLE)
                .post(builder.build())
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseStr = response.body().string();
                List<Article> articles = new ArrayList<>();
                articles = com.alibaba.fastjson.JSONArray.parseArray(responseStr, Article.class);
                Message msg = getGetArticlesHandlerByClasses2.obtainMessage();
                msg.obj = articles;
                getGetArticlesHandlerByClasses2.sendMessage(msg);

            }
        });
    }
    public void getArticleFromNetByClasses3(String classes) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("classes", classes);
        final Request request = new Request.Builder()
                .url(AppConfig.GET_CLASSES_ARTICLE)
                .post(builder.build())
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseStr = response.body().string();
                List<Article> articles = new ArrayList<>();
                articles = com.alibaba.fastjson.JSONArray.parseArray(responseStr, Article.class);
                Message msg = getGetArticlesHandlerByClasses3.obtainMessage();
                msg.obj = articles;
                getGetArticlesHandlerByClasses3.sendMessage(msg);

            }
        });
    }
    public void getArticleFromNetByClasses4(String classes) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("classes", classes);
        final Request request = new Request.Builder()
                .url(AppConfig.GET_CLASSES_ARTICLE)
                .post(builder.build())
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseStr = response.body().string();
                List<Article> articles = new ArrayList<>();
                articles = com.alibaba.fastjson.JSONArray.parseArray(responseStr, Article.class);
                Message msg = getGetArticlesHandlerByClasses4.obtainMessage();
                msg.obj = articles;
                getGetArticlesHandlerByClasses4.sendMessage(msg);

            }
        });
    }
    @OnClick({R.id.fragment_health_toolbar_user, R.id.fragment_health_toolbar_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_health_toolbar_user:
                ((MainActivity)mActivity).openDraw();
                break;
            case R.id.fragment_health_toolbar_search:
                ((MainActivity)mActivity).goSearch();
                break;
        }
    }
}

