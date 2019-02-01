package com.ldx.mygraduationproject.utils;

import android.content.Context;
import android.graphics.Color;

import com.cazaea.sweetalert.SweetAlertDialog;

/**
 * @author: tao
 * @time: 2018/9/6
 * @e-mail: 1462320178@qq.com
 * @version: 1.0
 * @exception: 无
 * @explain: 说明
 */
public class DialogUtils {

    private SweetAlertDialog pDialog;

    public DialogUtils(Context context, String s) {
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(s);
        pDialog.setCancelable(false);
    }

    public void show(){
        pDialog.show();
    }

    public void cancel(){
        pDialog.cancel();
    }
}
