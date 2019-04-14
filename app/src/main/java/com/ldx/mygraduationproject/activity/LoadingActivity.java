package com.ldx.mygraduationproject.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.app.MyApplication;
import com.ldx.mygraduationproject.constant.AppConfig;
import com.ldx.mygraduationproject.utils.NetUtils;
import com.ldx.mygraduationproject.utils.SPUtlis;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

/**
 * Created by freeFreAme on 2019/3/28.
 */

public class LoadingActivity extends BaseActivity {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_loading;
    }

    private static final int SLEEP = 1;

    private static final int IS_LOGIN = 0;
    private static final int UN_LOGIN = 1;

    private static final int TIME = 2000;
    private long time1;
    private long time2;


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == IS_LOGIN) {
                startActivity(new Intent(LoadingActivity.this, LoadingActivity.class));
                finish();
            }
            if (msg.what == UN_LOGIN) {
                startActivity(new Intent(LoadingActivity.this, LoginActivity.class));
                finish();
            }
        }
    };
    private Message message;


    @Override
    protected void initData() {
        time1 = System.currentTimeMillis();
        boolean isAuto = (boolean) SPUtlis.get(LoadingActivity.this, AppConfig
                .IS_AUTO_LOGIN, false);
        if (isAuto) {
            if (NetUtils.isConnected(this)) {
                String userName = (String) SPUtlis.get(LoadingActivity.this, AppConfig
                        .AUTO_LOGIN_NAME, "");
                String userPwd = (String) SPUtlis.get(LoadingActivity.this, AppConfig
                        .AUTO_LOGIN_PASS, "");
                try {
                    userLogin(userName, userPwd);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(LoadingActivity.this, "网络未连接，请连接",
                        Toast.LENGTH_LONG).show();
                NetUtils.openSetting(LoadingActivity.this);
            }
        } else {
            if (NetUtils.isConnected(this)) {
                Toast.makeText(LoadingActivity.this, "未设置自动登录,请登录",
                        Toast.LENGTH_LONG).show();
                sleep(UN_LOGIN);
            }else{
                Toast.makeText(LoadingActivity.this, "网络未连接，请连接",
                        Toast.LENGTH_LONG).show();
                NetUtils.openSetting(LoadingActivity.this);
            }
        }
    }

    private void userLogin(String userName, String userPwd) throws
            IOException {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("user_name", userName);
        builder.add("user_pwd", userPwd);
        final Request request = new Request.Builder()
                .url(AppConfig.USER_LOGIN)
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
                Message msg = mHandler.obtainMessage();
                msg.obj = r;
                mHandler.sendMessage(msg);
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Map<String, Object> r = (HashMap) msg.obj;

            if ((Integer) r.get("code") == 1) {
                Toast.makeText(LoadingActivity.this, "" + r.get("msg"), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoadingActivity.this, MainActivity.class));
            }

        }
    };

    private void sleep(final int MODE) {
        time2 = System.currentTimeMillis();
        final long result = time2 - time1;
        if (TIME > result) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep((long) TIME - result);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sendMessage(MODE);
                }

            }).start();
        } else {
            sendMessage(MODE);

        }
    }

    private void sendMessage(int mes) {
        message = handler.obtainMessage();
        message.what = mes;
        handler.sendMessage(message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unsubscribe();
    }

}
