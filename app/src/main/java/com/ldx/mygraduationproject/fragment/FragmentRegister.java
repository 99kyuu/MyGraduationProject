package com.ldx.mygraduationproject.fragment;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.activity.LoginActivity;
import com.ldx.mygraduationproject.bean.User;
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

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by freeFreAme on 2019/1/3.
 */

public class FragmentRegister extends BaseFragment {

    @BindView(R.id.register_email)
    EditText registerEmail;
    @BindView(R.id.register_name)
    EditText registerName;
    @BindView(R.id.register_pass)
    EditText registerPass;
    @BindView(R.id.register_bt)
    Button registerBt;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    protected void initImmersionBar() {
    }

    @Override
    protected void initView() {
        Log.i("FragmentRegister", "activity：" + (mActivity == null));
    }

    private String userName;
    private String userPwd;
    private String userEmail;

    @OnClick(R.id.register_bt)
    public void onViewClicked() {
        userName = registerName.getText().toString().trim();
        userPwd = registerPass.getText().toString().trim();
        /* 测试不加密 */
        //pass = Md5Utlis.encryption(registerPass.getText().toString().trim());
        userEmail = registerEmail.getText().toString().trim();
//
//        if (!RegularUtils.isUsername(userName)){
//            Toast.makeText(mActivity,"用户名不符合规范",Toast.LENGTH_LONG).show();
//            return;
//        }
//        if (!RegularUtils.isPassWord(userPwd)){
//            Toast.makeText(mActivity,"密码不符合规范，最少需要6位字符",Toast.LENGTH_LONG).show();
//            return;
//        }
//        if (!RegularUtils.isEmail(userEmail)){
//            Toast.makeText(mActivity,"邮箱不符合规范",Toast.LENGTH_LONG).show();
//            return;
//        }
        try {
            registerNewUser(userName, userPwd, userEmail);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //注册用户方法
    private void registerNewUser(String userName, String userPwd, String userEmail) throws
            IOException {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("user_name", userName);
        builder.add("user_pwd", userPwd);
        builder.add("user_email", userEmail);
        final Request request = new Request.Builder()
                .url(AppConfig.REGISTER_NEW_USER)
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
                Toast.makeText(mActivity, "" + r.get("msg"), Toast.LENGTH_SHORT).show();

            }
            if ((Integer) r.get("code") == 0) {

                Toast.makeText(mActivity, "" + r.get("msg"), Toast.LENGTH_SHORT).show();
                ((LoginActivity)mActivity).setLoginTabIndex(0);
            }
        }
    };
}
