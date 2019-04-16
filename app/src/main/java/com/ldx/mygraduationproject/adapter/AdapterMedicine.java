package com.ldx.mygraduationproject.adapter;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.bean.Medicine;

import java.util.List;

/**
 * Created by freeFreAme on 2018/12/26.
 */
public class AdapterMedicine extends SimpleAdapter<Medicine> {

    private ImageView itemMainRecommendImg;
    private TextView itemMainCurrencyName;
    private TextView itemMainCurrencyUse;
    private ImageView itemMainRecommendAdd;

    public AdapterMedicine(Context context) {
        super(context, R.layout.item_buy_recommend);
    }

    public AdapterMedicine(Context context, List<Medicine> data) {
        super(context, data, R.layout.item_buy_recommend);
    }

    @Override
    protected void change(BaseViewHolder viewHolder, Medicine medicine, int position) {
        Log.i("AdapterMedicine",medicine.toString());
        itemMainRecommendImg = viewHolder.findView(R.id.item_main_recommend_img);
        itemMainCurrencyName = viewHolder.findView(R.id.item_main_recommend_name);
        itemMainCurrencyUse = viewHolder.findView(R.id.item_main_recommend_use);
        itemMainRecommendAdd = viewHolder.findView(R.id.item_main_recommend_add);
        itemMainCurrencyName.setText(medicine.getMedicineName());
        itemMainCurrencyUse.setText(medicine.getMedicinePrice() + "￥");
    }
}
