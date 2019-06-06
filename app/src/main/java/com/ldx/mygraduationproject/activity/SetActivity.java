package com.ldx.mygraduationproject.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.bean.User;
import com.ldx.mygraduationproject.constant.AppConfig;
import com.ldx.mygraduationproject.utils.GlideUtils;
import com.ldx.mygraduationproject.utils.SPUtlis;
import com.squareup.okhttp.FormEncodingBuilder;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
/**
 * Created by freeFreAme on 2019/1/29.
 */
public class SetActivity extends BaseActivity {


    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.set_user_name)
    TextView setUserName;
    @BindView(R.id.set_finish)
    TextView setFinish;
    @BindView(R.id.edit_pwd)
    TextView editPwd;
    @BindView(R.id.version)
    TextView version;
    @BindView(R.id.set_user_photo)
    ImageView setUserPhoto;
    @BindView(R.id.set_address)
    TextView setAddress;
    private Handler getHandlerforUserId;
    @Override
    protected int setLayoutId() {
        return R.layout.activity_set;
    }

    @Override
    protected void initView() {

    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void initData() {
        super.initData();
        findIdByName((String) SPUtlis.get(SetActivity.this,
                AppConfig.AUTO_LOGIN_NAME, ""));
        getHandlerforUserId= new Handler() {
            @Override
            public void handleMessage(Message msg) {
                User userForId= (User) msg.obj;
                SPUtlis.put(SetActivity.this,
                        AppConfig.AUTO_LOGIN_ID,Integer.toString(userForId.getId()));
                setUserName.setText(userForId.getUserName());
                GlideUtils.loadImageView(SetActivity.this,"https://" + userForId.getUserImg()
                        ,setUserPhoto);

            }
        };
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(toolbar).init();
    }


    @OnClick({R.id.toolbar_back, R.id.set_finish,R.id.edit_pwd,R.id.version,R.id.set_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.set_finish:
                SPUtlis.put(SetActivity.this,AppConfig.IS_AUTO_LOGIN,false);
                startActivity(new Intent(SetActivity.this,LoginActivity.class));
                //这里6.0闪退报错

                break;
            case R.id.edit_pwd:
                startActivity(new Intent(SetActivity.this,ForgetActivity.class));
                break;
            case R.id.version:
                Toast.makeText(this, "最新版本", Toast.LENGTH_SHORT).show();
                break;
            case R.id.set_address:
                startActivity(new Intent(SetActivity.this,SetAddressActivity.class));
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
}
