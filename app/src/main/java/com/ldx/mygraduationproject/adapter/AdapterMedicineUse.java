package com.ldx.mygraduationproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.bean.Medicine;
import com.ldx.mygraduationproject.constant.AppConfig;
import com.ldx.mygraduationproject.utils.GlideUtils;
import com.ldx.mygraduationproject.utils.SPUtlis;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by freeFreAme on 2018/12/26.
 */
public class AdapterMedicineUse extends SimpleAdapter<Medicine> {

    private ImageView itemMainCurrencyImg;
    private ImageView itemMainCurrencyAdd;
    private TextView itemMainCurrencyName;
    private TextView itemMainCurrencyUse;
    private TextView itemMainCurrencyPrice;
    private Handler addMedicineHandler;
    public AdapterMedicineUse(Context context) {
        super(context, R.layout.item_main_currency);
    }

    public AdapterMedicineUse(Context context, List<Medicine> data) {
        super(context, data, R.layout.item_main_currency);
    }

    @Override
    protected void change(BaseViewHolder viewHolder, final Medicine medicine, int position) {
        itemMainCurrencyImg = viewHolder.findView(R.id.item_main_currency_img);
        Log.i("AdapterMedicine",medicine.getMedicineImg());
        GlideUtils.loadImageView(mContext,"https://" + medicine.getMedicineImg(),itemMainCurrencyImg);
        itemMainCurrencyAdd = viewHolder.findView(R.id.item_main_currency_add);
        itemMainCurrencyName = viewHolder.findView(R.id.item_main_currency_name);
        itemMainCurrencyUse = viewHolder.findView(R.id.item_main_currency_use);
        itemMainCurrencyPrice = viewHolder.findView(R.id.item_main_currency_price);
        itemMainCurrencyUse.setText(medicine.getMedicineType());
        itemMainCurrencyName.setText(medicine.getMedicineName());
        itemMainCurrencyPrice.setText(medicine.getMedicinePrice() + "￥");
        //itemMainCurrencyUse.setText(medicineBean.getMedicineType());
        itemMainCurrencyAdd.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onClick(View v) {
                try {
                    addMedicineToCart(medicine.getMedicineName(),String.valueOf(medicine.getMedicinePrice()),
                            medicine.getMedicineImg(), (String) SPUtlis.get(mContext, AppConfig.AUTO_LOGIN_ID, ""));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                addMedicineHandler= new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        Map<String,Object> r = (HashMap)msg.obj;
                        if((Integer)r.get("code")==1){
                            Toast.makeText(mContext, ""+r.get("msg"),
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                };
            }
        });
    }
    private void addMedicineToCart(String medicineName,String medicinePrice,String medicineImg,
                                   String userId) throws IOException {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("medicine_name",medicineName);
        builder.add("medicine_price",medicinePrice);
        builder.add("medicine_img",medicineImg);
        builder.add("user_id",userId);
        final Request request = new Request.Builder()
                .url(AppConfig.ADD_MEDICINE_TO_CART)
                .post(builder.build())
                .build();
      /*  Response response = mOkHttpClient.newCall(request).execute();*/
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseStr = response.body().string();
                Map<String,Object> r =new HashMap<>();
                r = com.alibaba.fastjson.JSONArray.parseObject(responseStr,HashMap.class);
                Message msg = addMedicineHandler.obtainMessage();
                msg.obj = r;
                addMedicineHandler.sendMessage(msg);
            }
        });
    }
}
