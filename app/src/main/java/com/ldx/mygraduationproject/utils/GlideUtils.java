package com.ldx.mygraduationproject.utils;

import android.content.Context;
import android.widget.ImageView;

import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.app.GlideApp;


/**
 * Created by freeFreAme on 2019/2/1.
 */
public class GlideUtils {
    //默认加载
    public static void loadImageView(Context mContext, String path, ImageView mImageView) {
        GlideApp.with(mContext)
                .load(path)
                .placeholder(R.drawable.pic_loading)
                .error(R.drawable.pic_loading_error)
                .into(mImageView);
    }
}
