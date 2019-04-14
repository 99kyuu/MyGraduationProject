package com.ldx.mygraduationproject.activity;

/**
 * Created by freeFreAme on 2018/12/23.
 */
import android.content.Intent;
import android.os.Build;
import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.adapter.BaseFragmentStatePagerAdapter;
import com.ldx.mygraduationproject.constant.AppConfig;
import com.ldx.mygraduationproject.utils.LocalReceiver;
import com.ldx.mygraduationproject.utils.SPUtlis;
import com.ldx.mygraduationproject.utils.StringUtils;


import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
/**
 * Created by freeFreAme on 2019/2/21.
 */
public class MainActivity extends BaseActivity  implements
        RadioGroup.OnCheckedChangeListener,ViewPager.OnPageChangeListener,LocalReceiver.Callback
{


    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.rg_tab_bar)
    RadioGroup rg_tab_bar;
    @BindView(R.id.rss_reader)
    RadioButton rss_reader;
    @BindView(R.id.details_message)
    RadioButton details_message;
    @BindView(R.id.user_record)
    RadioButton user_record;

    private int score = 0;
    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;

    private static boolean isExit=false;
    private static MainActivity mainActivity ;
    public static int screenWidth, screenHeight;

    private BaseFragmentStatePagerAdapter baseFragmentStatePagerAdapter;
    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        mainActivity = MainActivity.this;
        //获得屏幕的宽高
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

    }



    @Override
    protected void initView() {
        super.initView();
        baseFragmentStatePagerAdapter=new BaseFragmentStatePagerAdapter(getSupportFragmentManager());

//        selectedFragment(0);
//        selected(mainItemHealth);
        rg_tab_bar.setOnCheckedChangeListener(mainActivity);
        viewPager.setAdapter(baseFragmentStatePagerAdapter);
        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(mainActivity);
        setHeader();
    }


    private ImageView myphoto;
    private TextView headerUserHealthNumber;
    private TextView headerUserHealthState;
    private TextView headerUserName;
    private ProgressBar headerProgress;
    private View view01 ;

    private void setHeader(){
        View view = navView.getHeaderView(0);

        view01 = view.findViewById(R.id.header_obtain);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT){
            view01.setVisibility(View.GONE);
        }
        headerUserHealthNumber = view.findViewById(R.id.header_user_health_number);
        headerUserHealthState = view.findViewById(R.id.header_user_health_state);
        headerUserName = view.findViewById(R.id.header_user_name);
        headerProgress = view.findViewById(R.id.header_progress);
        headerProgress.setProgress(score);
        headerUserName.setText((String) SPUtlis.get(MainActivity.this, AppConfig.AUTO_LOGIN_NAME,""));
        headerUserHealthState.setText("未知");
        headerUserHealthNumber.setText(String.valueOf(score));


        myphoto = view.findViewById(R.id.header_user_photo);
        myphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
            }
        });

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.toString()){
                    case "自我检测":
                        startActivity(new Intent(MainActivity.this,ExaminationActivity.class));
                        break;
                    case "体检单":
                        if (score == 0){
                            startActivity(new Intent(MainActivity.this,ExaminationActivity.class));
                        }else {
                            startActivity(new Intent(MainActivity.this,ScoreActivity.class));
                        }
                        break;
                    case"步数计划":
                        startActivity(new Intent(MainActivity.this,SetPlanActivity.class));
                        break;
                    case "身高体重":
                        startActivity(new Intent(MainActivity.this,PhysicalActivity.class));
                        break;
                    case "关注":
                        startActivity(new Intent(MainActivity.this,CollectionActivity.class));
                        break;
                    case "关于":
                        startActivity(new Intent(MainActivity.this,AboutActivity.class));
                        break;
                    case "设置":
                        startActivity(new Intent(MainActivity.this,SetActivity.class));
                        break;
                }
                return true;
            }
        });
    }
    
    public static MainActivity getInstance(){
        if (mainActivity != null && mainActivity instanceof MainActivity) {
            return mainActivity;
        }
        return null;
    }
    public void openDraw(){
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rss_reader:
                viewPager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.details_message:
                viewPager.setCurrentItem(PAGE_TWO);
                break;
            case R.id.user_record:
                viewPager.setCurrentItem(PAGE_THREE);
                break;

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 2) {
            switch (viewPager.getCurrentItem()) {
                case PAGE_ONE:
                    rss_reader.setChecked(true);
                    break;
                case PAGE_TWO:
                    details_message.setChecked(true);
                    break;
                case PAGE_THREE:
                    user_record.setChecked(true);
                    break;

            }
        }
    }
    Handler handlerforisExit=new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            isExit=false;
        }
    };
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){
            exit();
            return false;
        }
        return super.onKeyDown(keyCode,event);
    }

    private void exit(){
        if(!isExit){
            isExit=true;
            Toast.makeText(getApplicationContext(),"再按一次退出fourleafApp",Toast.LENGTH_SHORT).show();
                    //利用handler延迟发送更改状态信息
                    handlerforisExit.sendEmptyMessageDelayed(0,2000);
        }
        else{
            finish();
            System.exit(0);
        }
    }


    @Override
    public void onSuccess(int score) {
        headerUserHealthNumber.setText(String.valueOf(score));
        headerUserHealthState.setText(StringUtils.getStateForScore(score));
        headerProgress.setProgress(score);
    }
    public void goSearch() {
        startActivity(new Intent(MainActivity.this,SearchActivity.class));
    }
}