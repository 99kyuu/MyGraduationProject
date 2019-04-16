package com.ldx.mygraduationproject.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.activity.MainActivity;
import com.ldx.mygraduationproject.activity.WebActivity;
import com.ldx.mygraduationproject.adapter.AdapterMedicine;
import com.ldx.mygraduationproject.adapter.AdapterMedicineUse;
import com.ldx.mygraduationproject.bean.Medicine;
import com.ldx.mygraduationproject.constant.AppConfig;
import com.ldx.mygraduationproject.utils.GlideImageLoader;
import com.ldx.mygraduationproject.utils.StringUtils;
import com.squareup.okhttp.FormEncodingBuilder;
import com.youth.banner.Banner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


import static android.support.constraint.Constraints.TAG;

/**
 * Created by freeFreAme on 2019/4/15.
 */

public class FragmentBuy extends BaseFragment {

    @BindView(R.id.toolbar_user)
    ImageView toolbarUser;
    @BindView(R.id.toolbar_search)
    ImageView toolbarSearch;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_main_recommend_rv)
    RecyclerView fragmentMainRecommendRv;
    @BindView(R.id.fragment_main_hot_rv)
    RecyclerView fragmentMainHotRv;
    @BindView(R.id.fragment_main_banner)
    Banner fragmentMainBanner;
    @BindView(R.id.fragment_main_use_rv)
    RecyclerView fragmentMainUseRv;
    @BindView(R.id.fragment_main_tab)
    TabLayout fragmentMainTab;

    private ArrayList<Medicine> list;
    private AdapterMedicine adapterMedicine;
    private AdapterMedicineUse adapterMedicineUse;
    private Handler getMedicinesHandler;
    private Handler getMedicinesByKeyHandler;
    private List<Medicine> medicineArrayList;
    @Override
    protected int setLayoutId() {
        return R.layout.fragment_buy;
    }

    @Override
    protected void initData() {
//        userBean = AppConfig.getInstance().getUserBean();
        getMedicineFromNet();
        getTop100MedicineRankPage();
//        getMedicineByNamePage();
    }

    @SuppressLint("HandlerLeak")
    public void getTop100MedicineRankPage() {
        getMedicinesHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
               medicineArrayList = (List<Medicine>) msg.obj;
               adapterMedicineUse.refreshData(medicineArrayList);
            }
        };
    }

    public void getMedicineByNamePage() {
//        disposable = NetworkClient.getUserApi()
//                .getMedicineByNamePage(userBean.getAutoSessionToken(), "0", "10","感")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<ResponseBean<DateBean<MedicineBean>>>() {
//                    @Override
//                    public void accept(@NonNull ResponseBean<DateBean<MedicineBean>> bean) throws Exception {
//                        adapterMedicine.refreshData(bean.getData().getContent());
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//                        Log.d("getTop100Medicine", throwable.toString());
//                    }
//                });
    }

    @Override
    protected void initView() {
        fragmentMainRecommendRv.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        adapterMedicine = new AdapterMedicine(mActivity);
        fragmentMainRecommendRv.setAdapter(adapterMedicine);

        fragmentMainTab.addTab(fragmentMainTab.newTab().setText("风热感冒"));
        fragmentMainTab.addTab(fragmentMainTab.newTab().setText("发烧"));
        fragmentMainTab.addTab(fragmentMainTab.newTab().setText("上呼吸道感染"));
        fragmentMainTab.addTab(fragmentMainTab.newTab().setText("清热解毒"));
        fragmentMainTab.addTab(fragmentMainTab.newTab().setText("支气管炎"));


        fragmentMainHotRv.setLayoutManager(new GridLayoutManager(mActivity, 2));
        adapterMedicineUse = new AdapterMedicineUse(mActivity);
        fragmentMainHotRv.setAdapter(adapterMedicineUse);

        setBanner();

        fragmentMainUseRv.setLayoutManager(new GridLayoutManager(mActivity, 2));
        fragmentMainUseRv.setAdapter(adapterMedicineUse);

    }

    private void setBanner() {
        fragmentMainBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.ad_1);
        images.add(R.drawable.ad_2);
        fragmentMainBanner.setImages(images);
        //banner设置方法全部调用完毕时最后调用
        fragmentMainBanner.start();
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar
                .titleBar(toolbar)
                .init();
    }
    public void getMedicineFromNet() {
        com.squareup.okhttp.OkHttpClient mOkHttpClient = new com.squareup.okhttp.OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        final com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                .url(AppConfig.GET_ALL_MEDICINE)
                .build();
        com.squareup.okhttp.Call call = mOkHttpClient.newCall(request);
        call.enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(com.squareup.okhttp.Request request, IOException e) {

            }

            @Override
            public void onResponse(com.squareup.okhttp.Response response) throws IOException {
                String responseStr = response.body().string();
                List<Medicine> articles = new ArrayList<>();
                articles = com.alibaba.fastjson.JSONArray.parseArray(responseStr, Medicine.class);
                Message msg = getMedicinesHandler.obtainMessage();
                msg.obj = articles;
                getMedicinesHandler.sendMessage(msg);

            }
        });
    }
    public void getArticleFromNetByClasses4(String classes) {
        com.squareup.okhttp.OkHttpClient mOkHttpClient = new com.squareup.okhttp.OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("classes", classes);
        final com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                .url(AppConfig.GET_ALL_MEDICINE)
                .post(builder.build())
                .build();
        com.squareup.okhttp.Call call = mOkHttpClient.newCall(request);
        call.enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(com.squareup.okhttp.Request request, IOException e) {

            }

            @Override
            public void onResponse(com.squareup.okhttp.Response response) throws IOException {
                String responseStr = response.body().string();
                List<Medicine> articles = new ArrayList<>();
                articles = com.alibaba.fastjson.JSONArray.parseArray(responseStr, Medicine.class);
                Message msg = getMedicinesByKeyHandler.obtainMessage();
                msg.obj = articles;
                getMedicinesByKeyHandler.sendMessage(msg);

            }
        });
    }

    @OnClick({R.id.toolbar_user, R.id.toolbar_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_user:
                ((MainActivity) mActivity).openDraw();
                break;
            case R.id.toolbar_search:
                ((MainActivity) mActivity).goSearch();
                break;
        }
    }
}