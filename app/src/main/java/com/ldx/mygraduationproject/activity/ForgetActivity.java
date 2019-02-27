package com.ldx.mygraduationproject.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.constant.AppConfig;
import com.ldx.mygraduationproject.utils.RegularUtils;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by freeFreAme on 2019/1/26.
 */
public class ForgetActivity extends BaseActivity {

    private final static boolean SEND = true;
    private final static boolean UNSEND = false;
    private  String token;
    private final static int COUNT_TIME=60;
    private Disposable disposable;

    @BindView(R.id.forget_email)
    EditText forgetEmail;
    @BindView(R.id.forget_code)
    EditText forgetCode;
    @BindView(R.id.forget_get_code)
    TextView forgetGetCode;
    @BindView(R.id.user_new_pwd)
    EditText newUserPwd;
    @BindView(R.id.user_new_pwd_confirm)
    EditText newUserPwdConfirm;

    @BindView(R.id.forget_button)
    Button forgetButton;
    @BindView(R.id.reset_button)
    Button resetButton;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_forget;
    }


    @OnClick({R.id.forget_get_code, R.id.forget_button, R.id.reset_button})
    public void onViewClicked(View view)  {
        switch (view.getId()) {
            case R.id.forget_get_code:
                    getCode();
                break;
            case R.id.forget_button:
                retrieve();
                break;
            case R.id.reset_button:
                resetEdit();
                break;
        }
    }


    private void resetEdit() {
        forgetEmail.setText("");
        forgetCode.setText("");
        newUserPwd.setText("");
        newUserPwdConfirm.setText("");
    }

    private boolean isSendCode = UNSEND;
    private String userEmail;

    @SuppressLint("ResourceAsColor")
    //发送验证码方法
    private void getCode() {

        userEmail = forgetEmail.getText().toString().trim();
//        if (!RegularUtils.isEmail(email)){
//            Toast.makeText(ForgetActivity.this,"邮箱格式不符合规范",Toast.LENGTH_LONG).show();
//            return;
//        }
        sendValidCode(userEmail);

    disposable= Flowable
            .intervalRange(1, COUNT_TIME, 0, 1,TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(new Consumer<Long>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void accept(Long aLong) throws Exception {
                    Log.d("fantasychongstatus", String.valueOf(120- aLong));
                    //此处有一个问题，当创建订阅，activity被销毁会造成空指针错误
                    if (ForgetActivity.this.isDestroyed() == true) {
                        if (disposable.isDisposed()==false) {
                            disposable.dispose();
                        }

                    }else {
                        forgetGetCode.setText((COUNT_TIME - aLong) + "秒后重发");
                        forgetGetCode.setClickable(false);
                        forgetGetCode.setTextColor(R.color.gray_1);
                        isSendCode = SEND;
                    }
                }
            })
            .doOnComplete(new Action() {
                @Override
                public void run() throws Exception {
                    forgetGetCode.setText("获取验证码");
                    forgetGetCode.setClickable(true);
                    isSendCode = UNSEND;
                }
            })
            .subscribe();
    }
    //向用户发送验证码
    public void sendValidCode(String userEmail){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("user_email",userEmail);
        final Request request = new Request.Builder()
                .url(AppConfig.RESET_VALID_CODE)
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
                Toast.makeText(ForgetActivity.this, r.get("msg")+"请前往邮箱查看验证码", Toast.LENGTH_SHORT).show();
                token=(String)r.get("token");

            }else { Toast.makeText(ForgetActivity.this,"发送失败,错误代码:"+r.get("code"),Toast.LENGTH_LONG).show();
                        }
        }
    };


    private String code ;
    private String userNewPwd;
    private String userNewPwdConfirm ;
    //确认重置
    private void retrieve() {
        code = forgetCode.getText().toString().trim();
        userNewPwd = newUserPwd.getText().toString().trim();
        userNewPwdConfirm = newUserPwd.getText().toString().trim();
        userEmail= forgetEmail.getText().toString().trim();
//        if (!RegularUtils.isEmail(userEmail)){
//            Toast.makeText(ForgetActivity.this,"邮箱格式不符合规范",Toast.LENGTH_LONG).show();
//            return;
//        }
//        if (!RegularUtils.isPassWord(userNewPwd )){
//            Toast.makeText(ForgetActivity.this,"密码格式不符合规范",Toast.LENGTH_LONG).show();
//            return;
//        }
//        if (!userNewPwd.equals(userNewPwdConfirm)){
//            Toast.makeText(ForgetActivity.this,"密码不一致",Toast.LENGTH_LONG).show();
//            return;
//        }
//        if (!isSendCode){
//            Toast.makeText(ForgetActivity.this,"请先获取验证码",Toast.LENGTH_LONG).show();
//            return;
//        }
        if (token == null) {
            Toast.makeText(this, "验证有误,请重新获取验证码", Toast.LENGTH_SHORT).show();
        }else
        resetUserPwd(userEmail,userNewPwdConfirm,code,token);

    }

    //重新设置用户的密码
    public void resetUserPwd(String userEmail,String userNewPwdConfirm,String emailValidCode,
                             String token){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("user_email",userEmail);
        builder.add("user_pwd",userNewPwdConfirm);
        builder.add("email_Validate",emailValidCode);
        builder.add("token",token);
        final Request request = new Request.Builder()
                .url(AppConfig.RESET_USER_PASSWORD)
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
                Message msg = mHandler2.obtainMessage();
                msg.obj = r;
                mHandler2.sendMessage(msg);
            }
        });
    }
    @SuppressLint("HandlerLeak")
    private Handler mHandler2 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Map<String,Object> r = (HashMap)msg.obj;
            if((Integer)r.get("code")==0){
                Toast.makeText(ForgetActivity.this, r.get("msg")+"", Toast.LENGTH_SHORT).show();
            }
            if((Integer)r.get("code")==1){
                Toast.makeText(ForgetActivity.this, r.get("msg")+"", Toast.LENGTH_SHORT).show();
//                Intent intent=new Intent(ForgetActivity.this, LoginActivity.class);
            }
        }
    };
    @Override
    protected void onSaveInstanceState(Bundle outState) {
                super.onSaveInstanceState(outState);
                 outState.putString("token",token);
             }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            token = savedInstanceState.getString("token");
        }
    }


}
