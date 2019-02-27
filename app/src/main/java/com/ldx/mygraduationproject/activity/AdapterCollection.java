package com.ldx.mygraduationproject.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.adapter.BaseViewHolder;
import com.ldx.mygraduationproject.adapter.SimpleAdapter;
import com.ldx.mygraduationproject.bean.Article;
import com.ldx.mygraduationproject.constant.AppConfig;
import com.ldx.mygraduationproject.utils.GlideUtils;

import org.litepal.LitePal;

import java.util.List;
/**
 * Created by freeFreAme on 2019/1/23.
 */
public class AdapterCollection extends SimpleAdapter<Article> {

    private ImageView itemCollectionImg;
    private TextView itemCollectionTitle;
    private LinearLayout itemCollectionLinear;
    private TextView itemCollectionDelete;

    public AdapterCollection(Context context) {
        super(context, R.layout.item_collection);
    }

    public AdapterCollection(Context context, List<Article> data) {
        super(context, data, R.layout.item_collection);
    }

    @Override
    protected void change(BaseViewHolder viewHolder, final Article article, int position) {
        itemCollectionImg = viewHolder.findView(R.id.item_collection_img);
        itemCollectionLinear = viewHolder.findView(R.id.item_collection_linear);
        itemCollectionTitle = viewHolder.findView(R.id.item_collection_title);
        itemCollectionDelete = viewHolder.findView(R.id.item_collection_delete);

        GlideUtils.loadImageView(mContext,"https://" + article.getImg(),itemCollectionImg);
        itemCollectionTitle.setText(article.getTitle());
        itemCollectionLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(AppConfig.WEB_INTENT_MODE, WebActivity.MODE_ARTICLE);
                bundle.putString(AppConfig.WEB_INTENT_CONTENT, article.getArticleUrl());
                intent.putExtra(AppConfig.WEB_INTENT, bundle);
                mContext.startActivity(intent);
            }
        });
        itemCollectionDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LitePal.delete(Article.class, article.getId());
                removeItem(article);
            }
        });
    }

}
