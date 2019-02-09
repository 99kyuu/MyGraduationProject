package com.ldx.mygraduationproject.activity;


import android.widget.TextView;

import com.ldx.mygraduationproject.R;


import com.zkk.view.rulerview.RulerView;

import butterknife.BindView;

/**
 * Created by freeFreAme on 2019/2/8.
 */

public class WeightActivty extends BaseActivity {
//    @BindView(R.id.ruler)
//    RulerView ruler;
    @BindView(R.id.ruler_height)
    RulerView ruler_height;
//    RulerView rulerView;


    @BindView(R.id.height_value)
    TextView height_value;
    @Override
    protected int setLayoutId() {
        return R.layout.activity_weight;
    }

    @Override
    protected void initView() {
//        super.initView();
//        ruler.setOnValueChangeListener(new NumberPicker.OnValueChangeListener() {
//            @Override
//            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//
//            }
//
////            @Override
////            public void onValueChange(int value) {
//////                tv_pressure_1.setText(value+"");
//////                diastolicPressure=value;
////            }
//        });
//        ruler_height=(RulerView)findViewById(R.id.ruler_height);
        ruler_height.setOnValueChangeListener(new RulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                height_value.setText(value+"");
            }
        });

        ruler_height.setValue(165, 80, 250, 1);
    }
}
