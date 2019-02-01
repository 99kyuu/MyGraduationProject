package com.ldx.mygraduationproject.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.activity.ForgetActivity;
import com.ldx.mygraduationproject.activity.MainActivity;
import com.ldx.mygraduationproject.bean.User;
import com.ldx.mygraduationproject.constant.AppConfig;
import com.ldx.mygraduationproject.utils.RegularUtils;
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

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by freeFreAme on 2019/1/3.
 */

public class FragmentLogin extends BaseFragment {

    @BindView(R.id.login_username)
    EditText loginUsername;
    @BindView(R.id.login_pass)
    EditText loginPass;
    @BindView(R.id.forget_pass)
    LinearLayout forgetPass;
    @BindView(R.id.auto_login_img)
    ImageView autoLoginImg;
    @BindView(R.id.auto_login)
    LinearLayout autoLogin;
    @BindView(R.id.login_bt)
    Button loginBt;



    private boolean isAuto = false;

    private String userName;
    private String userPwd;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initImmersionBar() {
    }

    @Override
    protected void initData() {
//        isAuto = (Boolean) SPUtlis.get(mActivity, MedicalCareConstant.IS_AUTO_LOGIN,isAuto);
//        setImageSelect();
    }

    private void setImageSelect(){
        if (isAuto){
            autoLoginImg.setImageResource(R.drawable.selected);
        }else {
            autoLoginImg.setImageResource(R.drawable.select);
        }
    }

    @Override
    protected void initView() {
//        if (isAuto){
//            username = (String) SPUtlis.get(mActivity,MedicalCareConstant.AUTO_LOGIN_NAME,"");
//            loginUsername.setText(username);
//        }
    }


    @OnClick({R.id.forget_pass, R.id.auto_login, R.id.login_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.forget_pass:
                mActivity.startActivity(new Intent(mActivity,ForgetActivity.class));
                break;
            case R.id.auto_login:
                isAuto = !isAuto;
                setImageSelect();
                break;
            case R.id.login_bt:
                doLogin();
                break;
        }
    }

    private void doLogin(){

        userName = loginUsername.getText().toString().trim();

//        if (!RegularUtils.isUsername(username)){
//            Toast.makeText(mActivity,"用户名不符合规范",Toast.LENGTH_LONG).show();
//            return;
//        }
        userPwd = loginPass.getText().toString().trim();
//        /* 测试不加密 */
//        //pass = Md5Utlis.encryption(registerPass.getText().toString().trim());
//        if (!RegularUtils.isPassWord(pass)){
//            Toast.makeText(mActivity,"密码不符合规范",Toast.LENGTH_LONG).show();
//            return;
        try {
            userLogin(userName,userPwd);
//            afterLogin(userName,userPwd,isAuto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //注册用户方法
    private void userLogin(String userName,String userPwd) throws
            IOException {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("user_name",userName);
        builder.add("user_pwd",userPwd);
        final Request request = new Request.Builder()
                .url(AppConfig.USER_LOGIN)
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
                Message msg = mHandler.obtainMessage();
                msg.obj = r;
                mHandler.sendMessage(msg);
            }
        });
    }
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Map<String,Object> r = (HashMap)msg.obj;
            if((Integer)r.get("code")==0){

                Toast.makeText(mActivity, ""+r.get("msg"), Toast.LENGTH_SHORT).show();
            }
            if((Integer)r.get("code")==1){
                Toast.makeText(mActivity, ""+r.get("msg"), Toast.LENGTH_SHORT).show();

            }
            if((Integer)r.get("code")==2){

                Toast.makeText(mActivity, ""+r.get("msg"), Toast.LENGTH_SHORT).show();
            }
        }
    };




}
