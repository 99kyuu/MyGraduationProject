package com.ldx.mygraduationproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.activity.WebActivity;
import com.ldx.mygraduationproject.bean.Article;
import com.ldx.mygraduationproject.constant.AppConfig;
import com.ldx.mygraduationproject.utils.GlideUtils;

import java.util.List;

/**
 * Created by freeFreAme on 2019/1/24.
 */
public class AdapterArticle extends SimpleAdapter<Article> {

    public AdapterArticle(Context context){
        super(context, R.layout.item_article);
    }

    private ImageView itemArticleImage;
    private TextView itemArticleLabel;

    private TextView itemArticleTitle;
    private TextView itemArticleContent;
    private TextView itemArticleTime;
    private CheckBox itemArticleMore;
    private RelativeLayout itemArticleRelative;

    public AdapterArticle(Context context, List<Article> data) {
        super(context, data, R.layout.item_article);
    }


    @Override
    protected void change(BaseViewHolder viewHolder, final Article article, int position) {
        itemArticleContent = viewHolder.findView(R.id.item_article_content);
        itemArticleImage = viewHolder.findView(R.id.item_article_image);
        GlideUtils.loadImageView(mContext,"https://" + article.getImg(),itemArticleImage);
        itemArticleTitle = viewHolder.findView(R.id.item_article_title);
        itemArticleTitle.setText(article.getTitle());
        itemArticleTime = viewHolder.findView(R.id.item_article_time);
        itemArticleTime.setText(article.getCreateDate());
        itemArticleLabel = viewHolder.findView(R.id.item_article_label);

        itemArticleRelative = viewHolder.findView(R.id.item_article_relative);
        itemArticleRelative.setOnClickListener(new View.OnClickListener() {
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
        itemArticleContent.setText(article.getContent());

        itemArticleMore = viewHolder.findView(R.id.item_article_more);
        if (article.isSaved()==true) {
            itemArticleMore.setChecked(true);
        }else{
            itemArticleMore.setChecked(false);
        }
        itemArticleMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (article.save()){
                    Toast.makeText(mContext,"收藏成功",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(mContext,"已收藏",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void setData(List<Article> data){
        refreshData(data);
    }
}
