package com.ldx.mygraduationproject.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.bean.Cash;
import com.ldx.mygraduationproject.bean.User;
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
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class WallteActivity extends BaseActivity {

    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.wallet_card)
    CardView walletCard;
    @BindView(R.id.money)
    TextView money;
    private Handler getHandlerforUserId;
    private Handler addMoneyHandler;
    private Handler cashHandler;
    @Override
    protected int setLayoutId() {
        return R.layout.activity_wallte;
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void initData() {
        super.initData();
        findIdByName((String) SPUtlis.get(WallteActivity.this,
                AppConfig.AUTO_LOGIN_NAME, ""));
        getHandlerforUserId= new Handler() {
            @Override
            public void handleMessage(Message msg) {
                User userForId= (User) msg.obj;
                SPUtlis.put(WallteActivity.this,
                        AppConfig.AUTO_LOGIN_ID,Integer.toString(userForId.getId()));
                money.setText(userForId.getUserWallet());
            }
        };
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(toolbar).init();
    }

    @OnClick({R.id.toolbar_back, R.id.wallet_card,R.id.cash,R.id.addcash})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.wallet_card:
                break;
            case R.id.addcash:

                final EditText et = new EditText(this);
                new AlertDialog.Builder(this).setTitle("输入充值的金额")
                        .setIcon(R.drawable.launch_icon)
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @SuppressLint("HandlerLeak")
                            public void onClick(DialogInterface dialog, int which) {
                                String input = et.getText().toString();
                                if (input.equals("")) {
                                    Toast.makeText(getApplicationContext(), "输入数额不能为空！" , Toast.LENGTH_LONG).show();
                                }
                                else {
                                    AddMoney((String) SPUtlis.get(WallteActivity.this, AppConfig.AUTO_LOGIN_NAME,
                                            ""),input);
                                    addMoneyHandler= new Handler() {
                                        @Override
                                        public void handleMessage(Message msg) {
                                            Map<String,Object> r = (HashMap)msg.obj;
                                            if((Integer)r.get("code")==1){
                                                Toast.makeText(WallteActivity.this, ""+r.get("msg"),
                                                        Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    };
                                }
                            }
                        }).setNegativeButton("取消", null)
                        .show();
                break;
            case R.id.cash:
                final EditText et2 = new EditText(this);
                new AlertDialog.Builder(this).setTitle("输入提现的金额")
                        .setIcon(R.drawable.launch_icon)
                        .setView(et2)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @SuppressLint("HandlerLeak")
                            public void onClick(DialogInterface dialog, int which) {
                                String input = et2.getText().toString();
                                if (input.equals("")) {
                                    Toast.makeText(getApplicationContext(), "输入数额不能为空！" , Toast.LENGTH_LONG).show();
                                }else if (Double.parseDouble(input) >Double.parseDouble(money.getText().toString())) {
                                    Toast.makeText(getApplicationContext(), "输入数额不能大于钱包额度！" , Toast.LENGTH_LONG).show();
                                } else {
                                    CashMoney((String) SPUtlis.get(WallteActivity.this, AppConfig.AUTO_LOGIN_NAME,
                                            ""),input);
                                    cashHandler= new Handler() {
                                        @Override
                                        public void handleMessage(Message msg) {
                                            Map<String,Object> r = (HashMap)msg.obj;
                                            if((Integer)r.get("code")==1){
                                                Toast.makeText(WallteActivity.this, ""+r.get("msg"),
                                                        Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    };
                                }
                            }
                        }).setNegativeButton("取消", null)
                        .show();
                break;
        }
    }
    public void findIdByName(String userName) {
        com.squareup.okhttp.OkHttpClient mOkHttpClient = new com.squareup.okhttp.OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("user_name",userName);
        final com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                .url(AppConfig.FIND_BY_USERNAME)
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
                User user = new User();
                user = com.alibaba.fastjson.JSONArray.parseObject(responseStr, User.class);
                Message msg = getHandlerforUserId.obtainMessage();
                msg.obj = user;
                getHandlerforUserId.sendMessage(msg);

            }
        });
    }
    public void AddMoney(String userName,String much) {
        com.squareup.okhttp.OkHttpClient mOkHttpClient = new com.squareup.okhttp.OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("user_name",userName);
        builder.add("much",much);
        final com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                .url(AppConfig.ADD_WALLET_FOR_USER)
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
                Message msg = addMoneyHandler.obtainMessage();
                msg.obj = r;
                addMoneyHandler.sendMessage(msg);

            }
        });
    }
    public void CashMoney(String userName,String much) {
        com.squareup.okhttp.OkHttpClient mOkHttpClient = new com.squareup.okhttp.OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("user_name",userName);
        builder.add("much",much);
        final com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                .url(AppConfig.CASH_FOR_USER)
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
                Message msg = cashHandler.obtainMessage();
                msg.obj = r;
                cashHandler.sendMessage(msg);

            }
        });
    }
}
