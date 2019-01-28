package com.ldx.mygraduationproject.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.gyf.barlibrary.OnKeyboardListener;
import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.adapter.AdapterTab;

import butterknife.BindView;

/**
 * Created by freeFreAme on 2019/1/3.
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.login_tab)
    TabLayout loginTab;
    @BindView(R.id.login_viewpager)
    ViewPager loginViewpager;
    @Override
    protected int setLayoutId() {
        return R.layout.activity_login;
    }
    @Override
    protected void initView() {
        loginViewpager.setAdapter(new AdapterTab(getSupportFragmentManager()));
        loginTab.setupWithViewPager(loginViewpager);

    }

    public void setLoginTabIndex(int index){
        loginViewpager.setCurrentItem(index);
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar
                .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题
                .setOnKeyboardListener(new OnKeyboardListener() {
                    @Override
                    public void onKeyboardChange(boolean isPopup, int keyboardHeight) {
//                Log.i("setOnKeyboardListener","isPopup=" + isPopup + " keyboardHeight=" + keyboardHeight);
                    }
                }).init();
    }
}
