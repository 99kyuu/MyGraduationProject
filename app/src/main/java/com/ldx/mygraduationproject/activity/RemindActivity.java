package com.ldx.mygraduationproject.activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.bean.UserPlan;
import com.ldx.mygraduationproject.constant.AppConfig;
import com.ldx.mygraduationproject.utils.SPUtlis;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import butterknife.BindView;

/**
 * Created by freeFreAme on 2019/5/2.
 */

public class RemindActivity extends BaseActivity {

    private MediaPlayer mediaPlayer;
    private Handler getPlanHandler;
    @SuppressLint("HandlerLeak")
    @Override
    protected void initData() {
        super.initData();
        mediaPlayer = MediaPlayer.create(this, R.raw.ard);
        mediaPlayer.start();
        getUserPlanByNet((String) SPUtlis.get(RemindActivity.this, AppConfig.AUTO_LOGIN_NAME,""));
        getPlanHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                UserPlan userPlanNet = (UserPlan) msg.obj;
                if (userPlanNet == null) {
                    Toast.makeText(RemindActivity.this, "当前无计划", Toast.LENGTH_SHORT).show();
                } else {
                    //实例化通知管理器
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    //实例化通知
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(RemindActivity.this);
                    builder.setDefaults(NotificationCompat.DEFAULT_ALL);//默认的通知手机声音
                    builder.setContentTitle("计划提醒!");
                    builder.setSmallIcon(android.R.drawable.ic_lock_idle_alarm);
                    builder.setContentText("今日目标："+userPlanNet.getPlanSteps()+"步");
                    Intent intent = new Intent();
                    intent.setAction("com.ldx.steplan.service");
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(RemindActivity.this, 0x105, intent, 0);
                    builder.setContentIntent(pendingIntent);

                    Notification notification = builder.build();
                    //发送通知
                    notificationManager.notify(0x104, notification);
                }
            }
        };

    }



    @Override
    protected int setLayoutId() {
        return R.layout.activity_remind;
    }

    public void stop(View view) {
        mediaPlayer.stop();
        finish();
    }

    public void getUserPlanByNet(String userName) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("user_name",userName);
        final Request request = new Request.Builder()
                .url(AppConfig.GET_USER_STEP_PLAN_BY_USERNAME)
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
                UserPlan userPlan = new UserPlan();
                userPlan = com.alibaba.fastjson.JSONArray.parseObject(responseStr, UserPlan.class);
                Message msg = getPlanHandler.obtainMessage();
                msg.obj = userPlan;
                getPlanHandler.sendMessage(msg);

            }
        });
    }


}
