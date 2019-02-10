package com.ldx.mygraduationproject.activity;


import android.widget.CheckBox;
import android.widget.TextView;

import com.ldx.mygraduationproject.R;


import com.zkk.view.rulerview.RulerView;

import butterknife.BindView;

/**
 * Created by freeFreAme on 2019/2/8.
 */

public class WeightActivity extends BaseActivity {

    @BindView(R.id.ruler_height)
    RulerView ruler_height;
    @BindView(R.id.ruler_weight)
    RulerView ruler_weight;
    @BindView(R.id.height_value)
    TextView height_value;
    @BindView(R.id.weight_value)
    TextView weight_value;
    @BindView(R.id.btn_sex)
    CheckBox sex;
    @Override
    protected int setLayoutId() {
        return R.layout.activity_weight;
    }

    @Override
    protected void initView() {

        ruler_height.setOnValueChangeListener(new RulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                height_value.setText(value+"");

            }
        });
        ruler_height.setValue(175, 80, 250, 1);

        ruler_weight.setOnValueChangeListener(new RulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                weight_value.setText(value+"");
            }
        });
    ruler_weight.setValue(55,30,80,0.1f);
    }
}
