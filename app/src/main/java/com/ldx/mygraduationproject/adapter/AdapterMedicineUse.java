package com.ldx.mygraduationproject.adapter;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.bean.Medicine;
import com.ldx.mygraduationproject.utils.GlideUtils;

import java.util.List;

/**
 * Created by freeFreAme on 2018/12/26.
 */
public class AdapterMedicineUse extends SimpleAdapter<Medicine> {

    private ImageView itemMainCurrencyImg;
    private ImageView itemMainCurrencyAdd;
    private TextView itemMainCurrencyName;
    private TextView itemMainCurrencyUse;
    private TextView itemMainCurrencyPrice;

    public AdapterMedicineUse(Context context) {
        super(context, R.layout.item_main_currency);
    }

    public AdapterMedicineUse(Context context, List<Medicine> data) {
        super(context, data, R.layout.item_main_currency);
    }

    @Override
    protected void change(BaseViewHolder viewHolder, Medicine medicine, int position) {
        itemMainCurrencyImg = viewHolder.findView(R.id.item_main_currency_img);
        Log.i("AdapterMedicine",medicine.getMedicineImg());
        GlideUtils.loadImageView(mContext,"https://" + medicine.getMedicineImg(),itemMainCurrencyImg);
        itemMainCurrencyAdd = viewHolder.findView(R.id.item_main_currency_add);
        itemMainCurrencyName = viewHolder.findView(R.id.item_main_currency_name);
        itemMainCurrencyUse = viewHolder.findView(R.id.item_main_currency_use);
        itemMainCurrencyPrice = viewHolder.findView(R.id.item_main_currency_price);

        itemMainCurrencyName.setText(medicine.getMedicineName());
        itemMainCurrencyPrice.setText(medicine.getMedicinePrice() + "￥");
        //itemMainCurrencyUse.setText(medicineBean.getMedicineType());

    }
}
