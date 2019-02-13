package com.ldx.mygraduationproject.activity;


import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.ldx.mygraduationproject.R;


import com.ldx.mygraduationproject.constant.AppConfig;
import com.ldx.mygraduationproject.utils.TimeUtil;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zkk.view.rulerview.RulerView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by freeFreAme on 2019/2/8.
 */

public class PhysicalActivity extends BaseActivity {

    @BindView(R.id.ruler_height)
    RulerView ruler_height;
    @BindView(R.id.ruler_weight)
    RulerView ruler_weight;
    @BindView(R.id.height_value)
    TextView height_value;
    @BindView(R.id.weight_value)
    TextView weight_value;
    @BindView(R.id.btn_sex)
    CheckBox sex;
    private String userSex;
    //具体时间
    private String curSelDate = TimeUtil.getCurrentDate();
    @Override
    protected int setLayoutId() {
        return R.layout.activity_weight;
    }

    @Override
    protected void initView() {

        ruler_height.setOnValueChangeListener(new RulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                height_value.setText(value+"");

            }
        });
        ruler_height.setValue(175, 80, 250, 1);

        ruler_weight.setOnValueChangeListener(new RulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                weight_value.setText(value+"");
            }
        });
    ruler_weight.setValue(55,30,80,0.1f);
        if (sex.isChecked() == true) {
            userSex="女";
        }else{
            userSex="男";
        }

    }

    public void savePhysical(View view){
        AddUserPhysicalToNet("ldx",height_value.getText().toString(),
                weight_value.getText().toString(),userSex,curSelDate);
    }
    public void AddUserPhysicalToNet(String userName,String userHeight,String userWeight,
                                     String userSex,String curDate){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("user_name",userName);
        builder.add("cur_date",curDate);
        builder.add("user_weight",userWeight);
        builder.add("user_height",userHeight);
        builder.add("user_sex",userSex);
        final Request request = new Request.Builder()
                .url(AppConfig.ADD_USER_PHYSICAL)
                .post(builder.build())
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseStr = response.body().string();
                Map<String, Object> r = new HashMap<>();
                r = com.alibaba.fastjson.JSONArray.parseObject(responseStr, HashMap.class);
                Message msg = addUserPhysicalHandler.obtainMessage();
                msg.obj = r;
                addUserPhysicalHandler.sendMessage(msg);

            }
        });
    }
    @SuppressLint("HandlerLeak")
    private Handler addUserPhysicalHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Map<String, Object> r = (HashMap) msg.obj;
            if ((Integer) r.get("code") == 1) {
                Toast.makeText(PhysicalActivity.this, "" + r.get("msg"), Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    };
}
