package com.ldx.mygraduationproject.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.bean.UserStep;
import com.ldx.mygraduationproject.constant.AppConfig;
import com.ldx.mygraduationproject.utils.SPUtlis;
import com.ldx.mygraduationproject.view.LineChartViewForStep;
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
 * Created by freeFreAme on 2019/3/28.
 */

public class Last7DayStepActivity extends BaseActivity {
    private List<LineChartViewForStep.ItemBean> mItems;
    private int[] shadeColors;
    private List<UserStep> userStepslList;
    private Handler getUserStepsHandler;
    @BindView(R.id.LineChartViewForStep)
    LineChartViewForStep lineChartViewForStep;
    @Override
    protected int setLayoutId() {
        return R.layout.activity_last7step;
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void initData() {
        super.initData();
        getUser7DayStepsFromNet((String) SPUtlis.get(Last7DayStepActivity.this, AppConfig.AUTO_LOGIN_NAME, ""));
        getUserStepsHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
//
                userStepslList = (List<UserStep>) msg.obj;
                if (userStepslList.size() == 0) {
                    Toast.makeText(Last7DayStepActivity.this, "用户暂时未拥有数据", Toast.LENGTH_SHORT).show();
                } else {
                    mItems = new ArrayList<>();
                        Log.e("asdasd",""+userStepslList.get(6).getTotalSteps());
                    Log.e("asdasd",""+userStepslList.get(0).getTotalSteps());
                    Log.e("asdasd",""+userStepslList.get(0).getCurDate());
                    Log.e("asdasd",""+userStepslList.size());
                    mItems.add(new LineChartViewForStep.ItemBean(1489507200, Integer.parseInt(userStepslList.get(6).getTotalSteps())));
                    mItems.add(new LineChartViewForStep.ItemBean(1489593600, Integer.parseInt(userStepslList.get(5).getTotalSteps())));
                    mItems.add(new LineChartViewForStep.ItemBean(1489680000, Integer.parseInt(userStepslList.get(4).getTotalSteps())));
                    mItems.add(new LineChartViewForStep.ItemBean(1489766400, Integer.parseInt(userStepslList.get(3).getTotalSteps())));
                    mItems.add(new LineChartViewForStep.ItemBean(1489852800, Integer.parseInt(userStepslList.get(2).getTotalSteps())));
                    mItems.add(new LineChartViewForStep.ItemBean(1489939200, Integer.parseInt(userStepslList.get(1).getTotalSteps())));
                    mItems.add(new LineChartViewForStep.ItemBean(1490025600, Integer.parseInt(userStepslList.get(0).getTotalSteps())));
                    shadeColors = new int[]{
                            Color.argb(100, 161, 216, 139), Color.argb(55, 183, 235, 139),
                            Color.argb(20, 221, 249, 197)};

                    //  设置折线数据
                    lineChartViewForStep.setItems(mItems);
                    //  设置渐变颜色
                    lineChartViewForStep.setShadeColors(shadeColors);
                    //  开启动画
                    lineChartViewForStep.startAnim(lineChartViewForStep, 2000);
                }
            }
        };
    }

    /***
     * 从云端获取历史体质数据的部分*/
    private void getUser7DayStepsFromNet(String userName) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("user_name", userName);
        final Request request = new Request.Builder()
                .url(AppConfig.HISTORY_STEPS)
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
                List<UserStep> userSteps = new ArrayList<>();
                userSteps = com.alibaba.fastjson.JSONArray.parseArray(responseStr, UserStep.class);
                Message msg = getUserStepsHandler.obtainMessage();
                msg.obj = userSteps;
                getUserStepsHandler.sendMessage(msg);

            }
        });
    }
}
