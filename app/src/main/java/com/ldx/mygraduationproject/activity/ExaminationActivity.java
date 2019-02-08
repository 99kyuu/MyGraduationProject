package com.ldx.mygraduationproject.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.bean.Question;
import com.ldx.mygraduationproject.constant.AppConfig;
import com.ldx.mygraduationproject.utils.DialogUtils;
import com.ldx.mygraduationproject.utils.FileUtil;
import com.ldx.mygraduationproject.utils.LocalReceiver;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

public class ExaminationActivity extends BaseActivity {


    @BindView(R.id.examination_tab)
    TabLayout examinationTab;
    @BindView(R.id.examination_button1)
    Button examinationButton1;
    @BindView(R.id.examination_button2)
    Button examinationButton2;
    @BindView(R.id.examination_button3)
    Button examinationButton3;
    @BindView(R.id.examination_last)
    Button examinationLast;
    @BindView(R.id.examination_back)
    Button examinationBack;
    @BindView(R.id.examination_linear_bt)
    LinearLayout examinationLinearBt;
    @BindView(R.id.examination_text)
    TextView examinationText;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private DialogUtils dialogUtils;

    private int index = 0;

    private ArrayList<Question> questions = new ArrayList<>(10);
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                NotClickable(true);
                setQuestionText();
            }

            if (msg.what == 2) {
                dialogUtils.cancel();
                Intent intent = new Intent(AppConfig.ACTION);
                intent.putExtra("score",score);
                localBroadcastManager.sendBroadcast(intent);
                startActivity(new Intent(ExaminationActivity.this,ScoreActivity.class));
                finish();
            }
        }
    };

    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter ;
    private LocalReceiver localReceiver;

    @Override
    protected void initData() {

        //得到本地广播管理器的实例
        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        //动态注册本地广播接收器
        intentFilter = new IntentFilter();
        intentFilter.addAction(AppConfig.ACTION);
        localReceiver = new LocalReceiver(MainActivity.getInstance());
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);


        dialogUtils = new DialogUtils(ExaminationActivity.this,"分析中...");
        NotClickable(false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String json = FileUtil.getFromAssets("json/quesion.json");
                questions = new Gson().fromJson(json, new TypeToken<List<Question>>() {
                }.getType());
                Message message = handler.obtainMessage();
                message.what = 1;
                handler.sendMessage(message);
            }
        }).start();

        for (int i = 0; i < 10; i++) {
            examinationTab.addTab(examinationTab.newTab().setIcon(R.drawable.green_tab1));
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_examination;
    }

    private void setQuestionText() {
        examinationText.setText(questions.get(index).getQuestion());
        examinationTab.getTabAt(index).setIcon(R.drawable.green_tab2);
    }

    @OnClick({R.id.examination_button1, R.id.examination_button2,
            R.id.examination_button3, R.id.examination_last, R.id.examination_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.examination_button1:
                questions.get(index).setChoose(1);
                chooseAfter();
                break;
            case R.id.examination_button2:
                questions.get(index).setChoose(2);
                chooseAfter();
                break;
            case R.id.examination_button3:
                questions.get(index).setChoose(3);
                chooseAfter();
                break;
            case R.id.examination_last:
                if (index==0){
                    return;
                }
                index--;
                setQuestionText();
                break;
            case R.id.examination_back:
                if (index == 9) {
                    NotClickable(false);
                    GetScoreBefore();
                } else {
                    finish();
                }
                break;
        }
    }

    private int score ;
    private void GetScoreBefore() {
        dialogUtils.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                score = 0 ;
                for (int i = 0; i < questions.size(); i++ ){
                    score = score + questions.get(i).getChoose();
                }
                Random random = new Random();
                int ran = random.nextInt(10);
                score = score*3 + (ran-5);

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Message message = handler.obtainMessage();
                message.what = 2 ;
                handler.sendMessage(message);
            }
        }).start();

    }

    private void NotClickable(boolean isClickable) {
        examinationButton1.setEnabled(isClickable);
        examinationButton2.setEnabled(isClickable);
        examinationButton3.setEnabled(isClickable);
        examinationLast.setEnabled(isClickable);
    }


    private void chooseAfter() {
        if (index == 9) {
            examinationBack.setText("完成");
            NotClickable(false);
            GetScoreBefore();
            return;
        }
        index++;
        setQuestionText();
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(toolbar).init();
    }

}
