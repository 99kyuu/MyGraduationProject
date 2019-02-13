package com.ldx.mygraduationproject.activity;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;


import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.bean.Article;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CollectionActivity extends BaseActivity {

    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collection_rv)
    RecyclerView collectionRv;


    @Override
    protected int setLayoutId() {
        return R.layout.activity_collection;
    }

    @Override
    protected void initData() {
        List<Article> list = LitePal.findAll(Article.class);
        AdapterCollection adapterCollection = new AdapterCollection(CollectionActivity.this, list);
        collectionRv.setLayoutManager(new LinearLayoutManager(CollectionActivity.this));
        collectionRv.setAdapter(adapterCollection);
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(toolbar).init();
    }


    @OnClick(R.id.toolbar_back)
    public void onViewClicked() {
        finish();
    }
}
