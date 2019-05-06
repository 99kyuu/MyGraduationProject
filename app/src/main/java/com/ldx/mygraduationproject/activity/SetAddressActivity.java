package com.ldx.mygraduationproject.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.constant.AppConfig;
import com.ldx.mygraduationproject.utils.SPUtlis;
import com.squareup.okhttp.FormEncodingBuilder;
import com.ywp.addresspickerlib.AddressPickerView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by freeFreAme on 2019/5/6.
 */

public class SetAddressActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.setAddress)
    AddressPickerView addressPickerView;
    private Handler setAddressHandler;
    @Override
    protected int setLayoutId() {
        return R.layout.activity_addresset;

    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void initData() {
        super.initData();
        setAddressHandler= new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Map<String,Object> r = (HashMap)msg.obj;
                if((Integer)r.get("code")==200){
                    Toast.makeText(SetAddressActivity.this, ""+r.get("msg"),
                            Toast.LENGTH_SHORT).show();
                }

            }
        };
    }

    @Override
    protected void initView() {
        super.initView();
        addressPickerView.setOnAddressPickerSure(new AddressPickerView.OnAddressPickerSureListener() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onSureClick(String address, String provinceCode, String cityCode, String districtCode) {
               setAddress((String) SPUtlis.get(SetAddressActivity.this, AppConfig.AUTO_LOGIN_NAME,
                       ""),address);
            }
        });

    }
    public void setAddress(String userName,String userAddress) {
        com.squareup.okhttp.OkHttpClient mOkHttpClient = new com.squareup.okhttp.OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("user_name",userName);
        builder.add("user_address",userAddress);
        final com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                .url(AppConfig.SET_ADDRESS)
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
                Map<String,Object> r =new HashMap<>();
                r = com.alibaba.fastjson.JSONArray.parseObject(responseStr,HashMap.class);
                Message msg = setAddressHandler.obtainMessage();
                msg.obj = r;
                setAddressHandler.sendMessage(msg);

            }
        });
    }
    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(toolbar).init();
    }
}
