package com.ldx.mygraduationproject.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.ldx.mygraduationproject.R;
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
    @Override
    protected int setLayoutId() {
        return R.layout.activity_set;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(toolbar).init();
    }


    @OnClick({R.id.toolbar_back, R.id.set_finish,R.id.edit_pwd,R.id.version})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.set_finish:
                finish();
                //这里6.0闪退报错
                System.exit(0);
                break;
            case R.id.edit_pwd:
                startActivity(new Intent(SetActivity.this,ForgetActivity.class));
            case R.id.version:
                startActivity(new Intent(SetActivity.this,SearchActivity.class));
        }
    }
}
