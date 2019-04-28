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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;

/**
 * Created by freeFreAme on 2019/4/15.
 */

public class FragmentBuy extends BaseFragment implements OnRefreshListener {

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
    @BindView(R.id.srl_simple)
    SwipeRefreshLayout srl_simple;
//    @BindView(R.id.fragment_main_use_rv)
//    RecyclerView fragmentMainUseRv;
    @BindView(R.id.fragment_main_tab)
    TabLayout fragmentMainTab;
    private ArrayList<Medicine> beans0 = null;
    private ArrayList<Medicine> beans1 = null;
    private ArrayList<Medicine> beans2 = null;
    private ArrayList<Medicine> beans3 = null;
    private ArrayList<Medicine> beans4 = null;
    private AdapterMedicine adapterMedicine;
    private AdapterMedicineUse adapterMedicineUse;
    private Handler getMedicinesHandler;
    private Handler getMedicinesByKeyHandler;
    private Handler getMedicinesByType0Handler;
    private Handler getMedicinesByType1Handler;
    private Handler getMedicinesByType2Handler;
    private Handler getMedicinesByType3Handler;
    private Handler getMedicinesByType4Handler;
    private List<Medicine> medicineArrayList;
    private List<Medicine> medicineArrayListForType;
    @Override
    protected int setLayoutId() {
        return R.layout.fragment_buy;
    }

    @Override
    protected void initData() {
        getMedicineFromNet();
        getTop100MedicineRankPage();
         findByType1("发烧");
         findByType0("风热感冒");
         findByType2("上呼吸道感染");
         findByType3("清热解毒");
         findByType4("支气管炎");
    }

    @SuppressLint("HandlerLeak")
    public void getTop100MedicineRankPage() {
        getMedicinesHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                medicineArrayList = (List<Medicine>) msg.obj;
                adapterMedicine.refreshData(medicineArrayList);
            }
        };
    }

    @SuppressLint("HandlerLeak")
    public void getMedicineByNamePage( ) {
        getMedicinesByType0Handler= new Handler() {
            @Override
            public void handleMessage(Message msg) {
                medicineArrayListForType = (List<Medicine>) msg.obj;
                beans0 = (ArrayList<Medicine>) medicineArrayListForType;
                adapterMedicineUse.refreshData(beans0);
            }
        };
        getMedicinesByType1Handler= new Handler() {
            @Override
            public void handleMessage(Message msg) {
                medicineArrayListForType = (List<Medicine>) msg.obj;
                beans1 = (ArrayList<Medicine>) medicineArrayListForType;

            }
        };
        getMedicinesByType2Handler= new Handler() {
            @Override
            public void handleMessage(Message msg) {
                medicineArrayListForType = (List<Medicine>) msg.obj;
                beans2 = (ArrayList<Medicine>) medicineArrayListForType;

            }
        };
        getMedicinesByType3Handler= new Handler() {
            @Override
            public void handleMessage(Message msg) {
                medicineArrayListForType = (List<Medicine>) msg.obj;
                beans3 = (ArrayList<Medicine>) medicineArrayListForType;

            }
        };
        getMedicinesByType4Handler= new Handler() {
            @Override
            public void handleMessage(Message msg) {
                medicineArrayListForType = (List<Medicine>) msg.obj;
                beans4 = (ArrayList<Medicine>) medicineArrayListForType;

            }
        };
        fragmentMainRecommendRv.setLayoutManager(new GridLayoutManager(mActivity, 3));
        adapterMedicine = new AdapterMedicine(mActivity);
        fragmentMainRecommendRv.setAdapter(adapterMedicine);
        fragmentMainTab.addTab(fragmentMainTab.newTab().setText("风热感冒"));
        fragmentMainTab.addTab(fragmentMainTab.newTab().setText("发烧"));
        fragmentMainTab.addTab(fragmentMainTab.newTab().setText("上呼吸道感染"));
        fragmentMainTab.addTab(fragmentMainTab.newTab().setText("清热解毒"));
        fragmentMainTab.addTab(fragmentMainTab.newTab().setText("支气管炎"));
        fragmentMainTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int index = tab.getPosition();
                switch (index) {
                    case 0:
                        adapterMedicineUse.refreshData(beans0);
                        break;
                    case 1:
                        adapterMedicineUse.refreshData(beans1);
                        break;
                    case 2:
                        adapterMedicineUse.refreshData(beans2);
                        break;
                    case 3:
                        adapterMedicineUse.refreshData(beans3);
                        break;
                    case 4:
                        adapterMedicineUse.refreshData(beans4);
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
        fragmentMainHotRv.setLayoutManager(new GridLayoutManager(mActivity, 2));
        adapterMedicineUse = new AdapterMedicineUse(mActivity);
        fragmentMainHotRv.setAdapter(adapterMedicineUse);
        setBanner();
//        fragmentMainUseRv.setLayoutManager(new GridLayoutManager(mActivity, 2));
//        fragmentMainUseRv.setAdapter(adapterMedicineUse);
    }
    protected void initView() {
        srl_simple.setOnRefreshListener(this);
        srl_simple.setColorSchemeResources(R.color.green);
        getMedicineByNamePage();
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
    public void findByType0(String type0) {
        com.squareup.okhttp.OkHttpClient mOkHttpClient = new com.squareup.okhttp.OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("type", type0);
        final com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                .url(AppConfig.FIND_BY_TYPE)
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
                Message msg = getMedicinesByType0Handler.obtainMessage();
                msg.obj = articles;
                getMedicinesByType0Handler.sendMessage(msg);

            }
        });
    }
    public void findByType1(String type1) {
        com.squareup.okhttp.OkHttpClient mOkHttpClient = new com.squareup.okhttp.OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("type", type1);
        final com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                .url(AppConfig.FIND_BY_TYPE)
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
                Message msg = getMedicinesByType1Handler.obtainMessage();
                msg.obj = articles;
                getMedicinesByType1Handler.sendMessage(msg);

            }
        });
    }
    public void findByType2(String type2) {
        com.squareup.okhttp.OkHttpClient mOkHttpClient = new com.squareup.okhttp.OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("type", type2);
        final com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                .url(AppConfig.FIND_BY_TYPE)
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
                Message msg = getMedicinesByType2Handler.obtainMessage();
                msg.obj = articles;
                getMedicinesByType2Handler.sendMessage(msg);

            }
        });
    }
    public void findByType3(String type3) {
        com.squareup.okhttp.OkHttpClient mOkHttpClient = new com.squareup.okhttp.OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("type", type3);
        final com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                .url(AppConfig.FIND_BY_TYPE)
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
                Message msg = getMedicinesByType3Handler.obtainMessage();
                msg.obj = articles;
                getMedicinesByType3Handler.sendMessage(msg);

            }
        });
    }

    public void findByType4(String type4) {
        com.squareup.okhttp.OkHttpClient mOkHttpClient = new com.squareup.okhttp.OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("type", type4);
        final com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                .url(AppConfig.FIND_BY_TYPE)
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
                Message msg = getMedicinesByType4Handler.obtainMessage();
                msg.obj = articles;
                getMedicinesByType4Handler.sendMessage(msg);

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
                ((MainActivity) mActivity).goSearchForMedicine();
                break;
        }
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(mRefresh, 2000);
    }
    private Handler mHandler = new Handler();
    private Runnable mRefresh = new Runnable() {
        @Override
        public void run() {
            initData();
            getMedicineByNamePage();
            srl_simple.setRefreshing(false);
        }
    };

}