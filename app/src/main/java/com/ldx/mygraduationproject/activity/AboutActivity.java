package com.ldx.mygraduationproject.activity;

import android.widget.ImageView;

import com.ldx.mygraduationproject.R;

import butterknife.BindView;
import butterknife.OnClick;
/**
 * Created by freeFreAme on 2019/1/24.
 */
public class AboutActivity extends BaseActivity {

    @BindView(R.id.about_back)
    ImageView aboutBack;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.fitsSystemWindows(true).barColor(R.color.green).init();
    }

    @OnClick(R.id.about_back)
    public void onViewClicked() {
        finish();
    }
}
