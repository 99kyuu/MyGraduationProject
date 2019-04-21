package com.ldx.mygraduationproject.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.adapter.AdapterCart;
import com.ldx.mygraduationproject.bean.Cart;
import com.ldx.mygraduationproject.constant.AppConfig;
import com.ldx.mygraduationproject.utils.SPUtlis;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by freeFreAme on 2019/4/20.
 */

public class CartActivity extends BaseActivity implements View.OnClickListener
        , AdapterCart.CheckInterface, AdapterCart.ModifyCountInterface{
    AdapterCart adapterCart=new AdapterCart(CartActivity.this);
    private boolean flag = false;
    private Handler getCartHandler;
    private List<Cart> shoppingCartList = new ArrayList<>();
    @BindView(R.id.list_cart)
    ListView listCart;
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.ck_all)
    CheckBox ckAll;
    @BindView(R.id.tv_show_price)
    TextView tvShowPrice;
    @BindView(R.id.tv_settlement)
    TextView tvSettlement;
    @BindView(R.id.bt_header_right)
    TextView btnEdit;//tv_edit
    @Override
    protected int setLayoutId() {
        return R.layout.activity_cart;
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void initData() {
        super.initData();
        getCartByUserId((String) SPUtlis.get(CartActivity.this,
                AppConfig.AUTO_LOGIN_ID, ""));
        getCartHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                List<Cart> carts=(ArrayList) msg.obj;
                shoppingCartList=carts;
                if (carts.size()==0) {
                    Toast.makeText(CartActivity.this,"当前字段无数据",Toast.LENGTH_LONG).show();
                }else{
                    listCart.setAdapter(adapterCart);
                    adapterCart.setCartList(shoppingCartList);
                }
            }
        };
    }

    @Override
    protected void initView() {
        super.initView();
    }
    public void getCartByUserId(String userId) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("userId", userId);
        final Request request = new Request.Builder()
                .url(AppConfig.FIND_BY_USER_ID)
                .post(builder.build())
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseStr = response.body().string();
                List<Cart> carts = new ArrayList<>();
                carts = com.alibaba.fastjson.JSONArray.parseArray(responseStr, Cart.class);
                Message msg = getCartHandler.obtainMessage();
                msg.obj = carts;
                getCartHandler.sendMessage(msg);

            }
        });
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            //全选按钮
//            case R.id.ck_all:
//                if (shoppingCartList.size() != 0) {
//                    if (ckAll.isChecked()) {
//                        for (int i = 0; i < shoppingCartList.size(); i++) {
//                            shoppingCartList.get(i).setChoosed(true);
//                        }
//                        adapterCart.notifyDataSetChanged();
//                    } else {
//                        for (int i = 0; i < shoppingCartList.size(); i++) {
//                            shoppingCartList.get(i).setChoosed(false);
//                        }
//                        shoppingCartAdapter.notifyDataSetChanged();
//                    }
//                }
//                statistics();
//                break;
//            case R.id.bt_header_right:
//                flag = !flag;
//                if (flag) {
//                    btnEdit.setText("完成");
//                    shoppingCartAdapter.isShow(false);
//                } else {
//                    btnEdit.setText("编辑");
//                    shoppingCartAdapter.isShow(true);
//                }
//                break;
//            case R.id.tv_settlement: //结算
//                lementOnder();
//                break;
//            case R.id.btn_back:
//                finish();
//                break;
//        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void checkGroup(int position, boolean isChecked) {

    }

    @Override
    public void doIncrease(int position, View showCountView, boolean isChecked) {

    }

    @Override
    public void doDecrease(int position, View showCountView, boolean isChecked) {

    }

    @Override
    public void childDelete(int position) {

    }
}
