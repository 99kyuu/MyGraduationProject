package com.ldx.mygraduationproject.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.adapter.AdapterArticle;
import com.ldx.mygraduationproject.bean.Article;
import com.ldx.mygraduationproject.constant.AppConfig;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.search_ed)
    EditText searchEd;
    @BindView(R.id.search_tx)
    TextView searchTx;
    @BindView(R.id.flow_layout)
    TagFlowLayout flowLayout;
    @BindView(R.id.search_toolbar)
    Toolbar searchToolbar;
    @BindView(R.id.search_rv)
    RecyclerView searchRv;

    private LayoutInflater mInflater = null;
    private ArrayList<String> list = new ArrayList<>();
    private AdapterArticle adapterArticle = null ;
    private Handler getArticlesHandler;
    @Override
    protected int setLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initData() {
        getArticleFromNet();
        list.add("二手烟的危害");
        list.add("感冒");
        list.add("发烧");
        list.add("早餐的选择");
        list.add("基因检测");
        list.add("DNA 测序");
        list.add("酒精");
        list.add("企鹅");
        list.add("扎堆取暖");
        list.add("抵御严寒");

    }

    @Override
    protected void initView() {
        mInflater = getLayoutInflater();
        flowLayout.setAdapter(new TagAdapter(list) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                TextView tv = (TextView) mInflater.inflate(R.layout.item_flow,
                        flowLayout, false);
                tv.setText(list.get(position));
                return tv;
            }
        });
        flowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Log.d("00",position+ "**");
                searchEd.setText(list.get(position));
                return false;
            }
        });

        searchRv.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        adapterArticle = new AdapterArticle(SearchActivity.this);
        searchRv.setAdapter(adapterArticle);
    }

    @OnClick(R.id.search_tx)
    public void onViewClicked() {
        getData();
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(searchToolbar).init();
    }

    @SuppressLint("HandlerLeak")
    private void getData(){
        String key = searchEd.getText().toString().trim();
        if (key.isEmpty()){
            Toast.makeText(SearchActivity.this,"输入为空",Toast.LENGTH_LONG).show();
            return;
        }
        getArticlesHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                List<Article> articles=(ArrayList) msg.obj;
                adapterArticle.refreshData(articles);
            }
            };
//        UserBean bean = MedicalCareApplication.getInstance().getUserBean();
//        disposable = NetworkClient.getUserApi()
//                .searchAllArticleByTitlePage(bean.getAutoSessionToken(),"0","10",key)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<ResponseBean<DateBean<ArticleBean>>>() {
//                    @Override
//                    public void accept(@NonNull ResponseBean<DateBean<ArticleBean>> bean) throws Exception {
//                        Log.d("userRegister成功",bean.toString());
//                        if (bean.getData().getContent().size() == 0){
//                            Toast.makeText(SearchActivity.this,"无数据",Toast.LENGTH_LONG).show();
//                        }
//
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//                        Log.d("userRegister错误",throwable.toString());
//                    }
//                });
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
