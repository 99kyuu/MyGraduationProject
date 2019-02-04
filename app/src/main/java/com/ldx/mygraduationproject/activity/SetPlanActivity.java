package com.ldx.mygraduationproject.activity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gyf.barlibrary.OnKeyboardListener;
import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.bean.UserPlan;
import com.ldx.mygraduationproject.constant.AppConfig;
import com.ldx.mygraduationproject.db.StepDataDao;
import com.ldx.mygraduationproject.utils.NetUtils;
import com.ldx.mygraduationproject.utils.SPUtlis;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by yuandl on 2016-10-18.
 */

public class SetPlanActivity extends BaseActivity {

//    private StepDataDao stepDataDao;
    private Handler getPlanHandler;
    private UserPlan userPlan = new UserPlan();

    @BindView(R.id.tv_step_number)
    EditText tv_step_number;
    @BindView(R.id.cb_remind)
    CheckBox cb_remind;
    @BindView(R.id.tv_remind_time)
    TextView tv_remind_time;
    @BindView(R.id.btn_save)
    Button btn_save;
    private String planSteps;
    private String isRemind;
    private String remindTime;

    public void getUserPlanByNet(String userName) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("user_name",userName);
        final Request request = new Request.Builder()
                .url(AppConfig.GET_USER_STEP_PLAN_BY_USERNAME)
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

    //-----------
    public void SaveUserPlanToNet(String userName, String remindTime, String isRemind, String planSteps) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("user_name", userName);
        builder.add("remind_time", remindTime);
        builder.add("plan_steps", planSteps);
        builder.add("is_remind", isRemind);
        final Request request = new Request.Builder()
                .url(AppConfig.ADD_USER_PLAN)
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
                Message msg = addPlanHandler.obtainMessage();
                msg.obj = r;
                addPlanHandler.sendMessage(msg);

            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler addPlanHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Map<String, Object> r = (HashMap) msg.obj;
            if ((Integer) r.get("code") == 1) {
                Toast.makeText(SetPlanActivity.this, "" + r.get("msg"), Toast.LENGTH_SHORT).show();
            }
        }
    };
    //-----------

    @Override
    protected int setLayoutId() {
        return R.layout.activity_exercise_plan;
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void initData() {//获取锻炼计划
//        if (NetUtils.isConnected(this) == true || NetUtils.isWifi(this) == true) {
//            getUserPlanByNet("ldx");
//            getPlanHandler = new Handler() {
//                @Override
//                public void handleMessage(Message msg) {
//                    UserPlan userPlanNet = (UserPlan) msg.obj;
////                    UserPlan userPlan = new UserPlan();
//                    userPlan = userPlanNet;
//                }
//            };
//        } else {
            UserPlan userPlan = new UserPlan();
            if (LitePal.findLast(UserPlan.class) == null) {
                Toast.makeText(this, "计划为空，已设为默认设置", Toast.LENGTH_SHORT).show();
            } else {
                userPlan = LitePal.findLast(UserPlan.class);
                String planSteps = userPlan.getPlanSteps();
                String isRemind = userPlan.getIsRemind();
                String remindTime = userPlan.getRemindTime();
                if (!planSteps.isEmpty()) {
                    if ("0".equals(planSteps)) {
                        tv_step_number.setText("7000");//默认的计划是7000步
                    } else {
                        tv_step_number.setText(planSteps);//如果数据库有数据则将数据取出
                    }
                }
                if (!isRemind.isEmpty()) {
                    if ("0".equals(isRemind)) {
                        cb_remind.setChecked(false);
                    } else if ("1".equals(isRemind)) {
                        cb_remind.setChecked(true);
                    }
                }
                if (!remindTime.isEmpty()) {
                    tv_remind_time.setText(remindTime);
                }
            }
    }

    @OnClick({R.id.btn_save, R.id.tv_remind_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                save();
                break;
            case R.id.tv_remind_time:
                showTimeDialog1();
                break;
        }
    }


    private void save() {

        UserPlan userPlan = new UserPlan();
        planSteps = tv_step_number.getText().toString().trim();
        remindTime = tv_remind_time.getText().toString().trim();
        if (cb_remind.isChecked()) {
            isRemind = "1";
            userPlan.setIsRemind("1");
        } else {
            isRemind = "0";
            userPlan.setIsRemind("0");
        }
        if (planSteps.isEmpty() || "0".equals(planSteps)) {
            userPlan.setPlanSteps("7000");
        } else {
            userPlan.setPlanSteps(planSteps);
        }
        if (remindTime.isEmpty()) {
            userPlan.setRemindTime("7:00");
        } else {
            userPlan.setRemindTime(remindTime);
        }
        if (NetUtils.isConnected(this) == true) {
            SaveUserPlanToNet("ldx", remindTime, isRemind, planSteps);
        } else {
            userPlan.save();
            Toast.makeText(this, ""+"计划本地存储成功", Toast.LENGTH_SHORT).show();
        }

    }

    private void showTimeDialog1() {
        final Calendar calendar = Calendar.getInstance(Locale.CHINA);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
//        String time = tv_remind_time.getText().toString().trim();
        final DateFormat df = new SimpleDateFormat("HH:mm");
//        Date date = null;
//        try {
//            date = df.parse(time);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        if (null != date) {
//            calendar.setTime(date);
//        }
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                String remindtime = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
                Date date = null;
                try {
                    date = df.parse(remindtime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (null != date) {
                    calendar.setTime(date);
                }
                tv_remind_time.setText(df.format(date));
            }
        }, hour, minute, true).show();
    }

}

