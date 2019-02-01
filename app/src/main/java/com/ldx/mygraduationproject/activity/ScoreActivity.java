package com.ldx.mygraduationproject.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.utils.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class ScoreActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.score_score)
    TextView scoreScore;
    @BindView(R.id.score_description)
    TextView scoreDescription;
    @BindView(R.id.header_user_photo)
    ImageView headerUserPhoto;
    @BindView(R.id.score_user_name)
    TextView scoreUserName;
    @BindView(R.id.score_progress)
    ProgressBar scoreProgress;
    @BindView(R.id.score_health_number)
    TextView scoreHealthNumber;
    @BindView(R.id.score_health_state)
    TextView scoreHealthState;
    @BindView(R.id.score_suggest)
    TextView scoreSuggest;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_renew)
    TextView toolbarRenew;

    @Override
    protected void initView() {
//        UserBean userBean = MedicalCareApplication.getInstance().getUserBean();
        int score =90;
        scoreUserName.setText("ldx");
        scoreDescription.setText("身体健康超过 "+ (score+5) +"%的人");
        scoreScore.setText(String.valueOf(score));
        scoreHealthNumber.setText(String.valueOf(score));
        scoreHealthState.setText(StringUtils.getStateForScore(score));
        scoreProgress.setProgress(score);
        scoreSuggest.setText(StringUtils.getDescriptionForScore(score));
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_score;
    }


    @OnClick({R.id.toolbar_back, R.id.toolbar_renew})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_renew:
                startActivity(new Intent(ScoreActivity.this,ExaminationActivity.class));
                finish();
                break;
        }
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(toolbar).init();
    }
}
