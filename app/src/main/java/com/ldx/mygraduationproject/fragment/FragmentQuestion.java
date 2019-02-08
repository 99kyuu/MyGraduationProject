package com.ldx.mygraduationproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.activity.WebActivity;
import com.ldx.mygraduationproject.adapter.AdapterArticle;
import com.ldx.mygraduationproject.bean.Article;
import com.ldx.mygraduationproject.constant.AppConfig;
import com.ldx.mygraduationproject.utils.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by freeFreAme on 2019/1/22.
 */

public class FragmentQuestion extends BaseFragment{
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
    private ArrayList<Article> beans0 = null;
    private ArrayList<Article> beans1 = null;
    private ArrayList<Article> beans2 = null;
    private ArrayList<Article> beans3 = null;

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
        setFragmentHealthArticleRv();
        setFragmentHealthTab();
    }

    private void setFragmentHealthTab() {
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
                        if (beans0 == null) {
                            //获取数据的操作
                            getData("0", "10", "0", 0);
                        } else {
//                            Log.i("FragmentHeath测试",beans0.toString());
                            adapterArticle.setData(beans0);
                        }
                        break;
                    case 1:
                        if (beans1 == null) {
                            //获取数据的操作
                            getData("0", "10", "1", 1);
                        } else {
                            adapterArticle.setData(beans1);
                        }
                        break;
                    case 2:
                        if (beans2 == null) {
                            getData("0", "10", "2", 2);
                        } else {
                            adapterArticle.setData(beans2);
                        }
                        break;
                    case 3:
                        if (beans3 == null) {
                            getData("0", "10", "3", 3);
                        } else {
                            adapterArticle.setData(beans3);
                        }
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
//                Article bean =com.alibaba.fastjson.JSONArray.parseObject(AppConfig.DATE,Article.class);
                Article article=null;
                switch (position){
                    case 0:
                         article = com.alibaba.fastjson.JSONArray.parseObject(AppConfig.DATE,Article.class);
                        break;
                    case 1:
                        article =com.alibaba.fastjson.JSONArray.parseObject(AppConfig.DATE2,Article.class);
                        break;
                    case 2:
                        article =com.alibaba.fastjson.JSONArray.parseObject(AppConfig.DATE2,Article.class);
                        break;
                    case 3:
                        article =com.alibaba.fastjson.JSONArray.parseObject(AppConfig.DATE2,Article.class);
                        break;
                    case 4:
                        article =com.alibaba.fastjson.JSONArray.parseObject(AppConfig.DATE2,Article.class);
                        break;
                    case 5:
                        article =com.alibaba.fastjson.JSONArray.parseObject(AppConfig.DATE2,Article.class);
                        break;
                }
                Intent intent = new Intent(mActivity, WebActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(AppConfig.WEB_INTENT_MODE, WebActivity.MODE_ARTICLE);
                bundle.putString(AppConfig.WEB_INTENT_CONTENT, article.getArticleUrl());
                intent.putExtra(AppConfig.WEB_INTENT, bundle);
                mActivity.startActivity(intent);
            }
        });
    }

    private void setFragmentHealthArticleRv() {
        adapterArticle = new AdapterArticle(mActivity, null);
        fragmentHealthArticleRv.setLayoutManager(new LinearLayoutManager(mActivity));
        fragmentHealthArticleRv.setAdapter(adapterArticle);
        getData("0", "10", "0", 4);
    }

    public void getData(String page, String size, final String clazz, final int mode) {
//        UserBean bean = MedicalCareApplication.getInstance().getUserBean();
//        disposable = NetworkClient.getUserApi()
//                .getArticleByClazzPage(bean.getAutoSessionToken(), page, size, clazz)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<ArticleResponseBean>() {
//                    @Override
//                    public void accept(@NonNull ArticleResponseBean bean) throws Exception {
//                        switch (mode) {
//                            case 0:
//                                adapterArticle.setData(beans0);
//                                break;
//                            case 1:
//                                beans1 = (ArrayList<Article>) bean.getData().getContent();
//                                adapterArticle.setData(beans1);
//                                break;
//                            case 2:
//                                beans2 = (ArrayList<Article>) bean.getData().getContent();
//                                adapterArticle.setData(beans2);
//                                break;
//                            case 3:
//                                beans3 = (ArrayList<Article>) bean.getData().getContent();
//                                adapterArticle.setData(beans3);
//                                break;
//                            case 4:
//                                beans0 = (ArrayList<Article>) bean.getData().getContent();
//                                adapterArticle.setData(beans0);
//                                break;
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//                        Log.d("getArticleByClazzPage错误", throwable.toString());
//                    }
//                });
//    }
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(fragmentHealthToolbar).init();
    }

//
//    @OnClick({R.id.fragment_health_toolbar_user, R.id.fragment_health_toolbar_search})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.fragment_health_toolbar_user:
//                ((MainActivity)mActivity).openDraw();
//                break;
//            case R.id.fragment_health_toolbar_search:
//                ((MainActivity)mActivity).goSearch();
//                break;
//        }
//    }
}
