package com.ldx.mygraduationproject.activity;

import android.app.TimePickerDialog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.gyf.barlibrary.OnKeyboardListener;
import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.bean.UserPlan;
import com.ldx.mygraduationproject.db.StepDataDao;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by yuandl on 2016-10-18.
 */

public class SetPlanActivity extends BaseActivity {

    private StepDataDao stepDataDao;


    @BindView(R.id.iv_left)
    ImageView iv_left;
    @BindView(R.id.iv_right)
    ImageView iv_right;
    @BindView(R.id.tv_step_number)
    EditText tv_step_number;
    @BindView(R.id.cb_remind)
    CheckBox cb_remind;
    @BindView(R.id.tv_remind_time)
    TextView tv_remind_time;
    @BindView(R.id.btn_save)
    Button btn_save;
    private String walk_qty;
    private String remind;
    private String achieveTime;


    @Override
    protected int setLayoutId() {
        return R.layout.activity_exercise_plan;
    }
//    @Override
//    public void initData() {//获取锻炼计划
//        UserPlan userPlan = null;
//        userPlan=stepDataDao.getPlanByTheLasted();
//        if (userPlan == null) {
//            Toast.makeText(this, "数据为空，请重新设置", Toast.LENGTH_SHORT).show();
//        }else {
//            String planSteps = userPlan.getPlanSteps();
//            String remind = userPlan.getIsRemind();
//            String achieveTime = userPlan.getRemindTime();
//            if (!planSteps.isEmpty()) {
//                if ("0".equals(planSteps)) {
//                    tv_step_number.setText("7000");//默认的计划是7000步
//                } else {
//                    tv_step_number.setText(planSteps);//如果数据库有数据则将数据取出
//                }
//            }
//            if (!remind.isEmpty()) {
//                if ("0".equals(remind)) {
//                    cb_remind.setChecked(false);
//                } else if ("1".equals(remind)) {
//                    cb_remind.setChecked(true);
//                }
//            }
//
//            if (!achieveTime.isEmpty()) {
//                tv_remind_time.setText(achieveTime);
//            }
//        }
//
//    }

    @OnClick({R.id.iv_left, R.id.btn_save, R.id.tv_remind_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.btn_save:
                save();
                break;
            case R.id.tv_remind_time:
                showTimeDialog1();
                break;
        }
    }




    private void save() {
        walk_qty = tv_step_number.getText().toString().trim();
        achieveTime = tv_remind_time.getText().toString().trim();
        if (cb_remind.isChecked()) {
            remind = "1";
        } else {
            remind = "0";
        }
        UserPlan userPlan = new UserPlan();
        if (walk_qty.isEmpty() || "0".equals(walk_qty)) {
            userPlan.setPlanSteps("7000");
        } else {
            userPlan.setPlanSteps(walk_qty);
        }
        userPlan.setPlanSteps(remind);
        if (achieveTime.isEmpty()) {
            userPlan.setRemindTime("7:00");
        } else {
            userPlan.setRemindTime(achieveTime);
        }
        stepDataDao.addNewPlan(userPlan);
                finish();
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
                String remaintime = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
                Date date = null;
                try {
                    date = df.parse(remaintime);
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

