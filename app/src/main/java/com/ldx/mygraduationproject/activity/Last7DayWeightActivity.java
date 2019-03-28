package com.ldx.mygraduationproject.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.bean.UserPhysical;
import com.ldx.mygraduationproject.constant.AppConfig;
import com.ldx.mygraduationproject.utils.SPUtlis;
import com.ldx.mygraduationproject.view.LineChartViewForWeight;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by freeFreAme on 2019/3/18.
 */

public class Last7DayWeightActivity extends BaseActivity {

    private List<LineChartViewForWeight.ItemBean> mItems;
    private int[] shadeColors;
    private List<UserPhysical> userPhysicalList;
    private Handler getUserPhysicalHandler;
    private Handler getMinPhysicalHandler;
    private Handler getMaxPhysicalHandler;
    @BindView(R.id.LineChartViewForWeight)
    LineChartViewForWeight lineChartViewForWeight;
    @BindView(R.id.min_weight_data)
    TextView min_weight_data;
    @BindView(R.id.max_weight_data)
    TextView max_weight_data;
    @Override
    protected int setLayoutId() {
        return R.layout.activity_last7weight;
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void initData() {
        super.initData();
        getUser7DayPhysicalFromNet((String) SPUtlis.get(Last7DayWeightActivity.this, AppConfig.AUTO_LOGIN_NAME, ""));
        getMinPhysicalFromNet((String) SPUtlis.get(Last7DayWeightActivity.this, AppConfig.AUTO_LOGIN_NAME, ""));
        getMaxPhysicalFromNet((String) SPUtlis.get(Last7DayWeightActivity.this, AppConfig.AUTO_LOGIN_NAME, ""));
        
        getMinPhysicalHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                UserPhysical userHeartRate = (UserPhysical) msg.obj;
                if ((UserPhysical) msg.obj == null) {
                    Toast.makeText(Last7DayWeightActivity.this, "用户暂时未拥有数据", Toast.LENGTH_SHORT).show();
                } else {
                    min_weight_data.setText(userHeartRate.getUserWeight());
                }
            }
        };
        getMaxPhysicalHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                UserPhysical userHeartRate = (UserPhysical) msg.obj;
                if ((UserPhysical) msg.obj == null) {
                    Toast.makeText(Last7DayWeightActivity.this, "用户暂时未拥有数据", Toast.LENGTH_SHORT).show();
                } else {
                    max_weight_data.setText(userHeartRate.getUserWeight());
                }
            }
        };
        getUserPhysicalHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
//                if (msg.obj == null) {
//                    Toast.makeText(mActivity, "用户暂时未拥有数据", Toast.LENGTH_SHORT).show();
//                }else {
                userPhysicalList = (List<UserPhysical>) msg.obj;
                if (userPhysicalList.size() == 0) {
                    Toast.makeText(Last7DayWeightActivity.this, "用户暂时未拥有数据", Toast.LENGTH_SHORT).show();
                } else {
                    mItems = new ArrayList<>();
                    mItems.add(new LineChartViewForWeight.ItemBean(1489507200, Integer.parseInt(userPhysicalList.get(0).getUserWeight())));
                    mItems.add(new LineChartViewForWeight.ItemBean(1489593600, Integer.parseInt(userPhysicalList.get(1).getUserWeight())));
                    mItems.add(new LineChartViewForWeight.ItemBean(1489680000, Integer.parseInt(userPhysicalList.get(2).getUserWeight())));
                    mItems.add(new LineChartViewForWeight.ItemBean(1489766400, Integer.parseInt(userPhysicalList.get(3).getUserWeight())));
                    mItems.add(new LineChartViewForWeight.ItemBean(1489852800, Integer.parseInt(userPhysicalList.get(4).getUserWeight())));
                    mItems.add(new LineChartViewForWeight.ItemBean(1489939200, Integer.parseInt(userPhysicalList.get(5).getUserWeight())));
                    mItems.add(new LineChartViewForWeight.ItemBean(1490025600, Integer.parseInt(userPhysicalList.get(6).getUserWeight())));
                    shadeColors = new int[]{
                            Color.argb(100, 161, 216, 139), Color.argb(55, 183, 235, 139),
                            Color.argb(20, 221, 249, 197)};

                    //  设置折线数据
                    lineChartViewForWeight.setItems(mItems);
                    //  设置渐变颜色
                    lineChartViewForWeight.setShadeColors(shadeColors);
                    //  开启动画
                    lineChartViewForWeight.startAnim(lineChartViewForWeight, 2000);
                }
            }
        };
    }


    /***
     * 从云端获取历史体质数据的部分*/
    private void getUser7DayPhysicalFromNet(String userName) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("user_name", userName);
        final Request request = new Request.Builder()
                .url(AppConfig.GET_USER_PHYSICAL_BY_USER_NAME)
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
                List<UserPhysical> userHeartPhysicals = new ArrayList<>();
                userHeartPhysicals = com.alibaba.fastjson.JSONArray.parseArray(responseStr, UserPhysical.class);
                Message msg = getUserPhysicalHandler.obtainMessage();
                msg.obj = userHeartPhysicals;
                getUserPhysicalHandler.sendMessage(msg);

            }
        });
    }

    private void getMinPhysicalFromNet(String userName) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("user_name", userName);
        final Request request = new Request.Builder()
                .url(AppConfig.GET_MIN_USER_WEIGHT_BY_USER_NAME)
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
                UserPhysical userPhysical = new UserPhysical();
                userPhysical = com.alibaba.fastjson.JSONArray.parseObject(responseStr, UserPhysical.class);
                Message msg = getMinPhysicalHandler.obtainMessage();
                msg.obj = userPhysical;
                getMinPhysicalHandler.sendMessage(msg);

            }
        });
    }
    private void getMaxPhysicalFromNet(String userName) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("user_name", userName);
        final Request request = new Request.Builder()
                .url(AppConfig.GET_MAX_USER_WEIGHT_BY_USER_NAME)
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
                UserPhysical userPhysical = new UserPhysical();
                userPhysical = com.alibaba.fastjson.JSONArray.parseObject(responseStr, UserPhysical.class);
                Message msg = getMaxPhysicalHandler.obtainMessage();
                msg.obj = userPhysical;
                getMaxPhysicalHandler.sendMessage(msg);

            }
        });
    }

}
