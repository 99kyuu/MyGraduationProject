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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.activity.WebActivity;
import com.ldx.mygraduationproject.adapter.AdapterArticle;
import com.ldx.mygraduationproject.bean.Article;
import com.ldx.mygraduationproject.bean.UserPlan;
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

/**
 * Created by freeFreAme on 2019/1/22.
 */
public class FragmentReader extends BaseFragment {
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

    private Handler getArticlesHandler;
    private List<Article> articleArrayList;
    private AdapterArticle adapterArticle;
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
//        setFragmentHealthArticleRv();
    }



    @Override
    protected void setListener() {
        super.setListener();
    }

    /**
     * 设置轮播图
     */
    @SuppressLint("HandlerLeak")
    private void setBanner() {
        fragmentHealthBanner.setImageLoader(new GlideImageLoader());

        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.news_image_1);
        images.add(R.drawable.news_image_2);
        images.add(R.drawable.news_image_3);
        images.add(R.drawable.news_image_4);
        images.add(R.drawable.news_image_5);
        images.add(R.drawable.news_image_6);
        fragmentHealthBanner.setImages(images);
        fragmentHealthBanner.start();
        getArticleFromNet();
        getArticlesHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                 articleArrayList = (List<Article>) msg.obj;
                adapterArticle = new AdapterArticle(mActivity, null);
                fragmentHealthArticleRv.setLayoutManager(new LinearLayoutManager(mActivity));
                fragmentHealthArticleRv.setAdapter(adapterArticle);
                getData( );
                fragmentHealthBanner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(final int position) {
                        switch (position) {
                            case 0:
                                article = (articleArrayList.get(0));
                                break;
                            case 1:
                                article = (articleArrayList.get(1));
                                break;
                            case 2:
                                article = com.alibaba.fastjson.JSONArray.parseObject(AppConfig.DATE2, Article.class);
                                break;
                            case 3:
                                article = com.alibaba.fastjson.JSONArray.parseObject(AppConfig.DATE2, Article.class);
                                break;
                            case 4:
                                article = com.alibaba.fastjson.JSONArray.parseObject(AppConfig.DATE2, Article.class);
                                break;
                            case 5:
                                article = com.alibaba.fastjson.JSONArray.parseObject(AppConfig.DATE2, Article.class);
                                break;

                        }

                        //banner设置方法全部调用完毕时最后调用

                        Intent intent = new Intent(mActivity, WebActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt(AppConfig.WEB_INTENT_MODE, WebActivity.MODE_ARTICLE);
                        bundle.putString(AppConfig.WEB_INTENT_CONTENT, article.getArticleUrl());
                        intent.putExtra(AppConfig.WEB_INTENT, bundle);
                        mActivity.startActivity(intent);
                    }
                });
            }
        };
    }

//    private void setFragmentHealthArticleRv() {
////        adapterArticle = new AdapterArticle(mActivity, null);
////        fragmentHealthArticleRv.setLayoutManager(new LinearLayoutManager(mActivity));
////        fragmentHealthArticleRv.setAdapter(adapterArticle);
////        getData( );
//    }

    public void getData( ) {
        adapterArticle.setData(articleArrayList);
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
}
