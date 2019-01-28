package com.ldx.mygraduationproject.fragment;

import android.app.FragmentManager;
import android.content.Context;
import android.view.Display;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.view.BeforeOrAfterCalendarView;

import butterknife.BindView;

/**
 * Created by freeFreAme on 2019/1/22.
 */

public class FragmentMedicalRecords extends BaseFragment {


    private BeforeOrAfterCalendarView calenderView1;


    private TextView totalKmTv;
    private TextView stepsTimeTv;
    private TextView totalStepsTv;
    private TextView supportTv;
    @BindView(R.id.movement_records_calender_l2)
    LinearLayout movementCalenderL2;
    @BindView(R.id.movement_total_km_time_tv)
    TextView kmTimeTv;
    /**
     * 屏幕长度和宽度
     */
    public static int screenWidth, screenHeight;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_record;
    }

    protected void initData() {
        WindowManager windowManager = (WindowManager)mActivity
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

//        放到获取宽度之后
        calenderView1 = new BeforeOrAfterCalendarView(getContext());
        movementCalenderL2.addView(calenderView1);

    }
}
