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
import com.ldx.mygraduationproject.bean.User;
import com.ldx.mygraduationproject.constant.AppConfig;
import com.ldx.mygraduationproject.utils.GlideUtils;
import com.ldx.mygraduationproject.utils.SPUtlis;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by freeFreAme on 2019/4/20.
 */

public class CartActivity extends BaseActivity implements View.OnClickListener
        , AdapterCart.CheckInterface, AdapterCart.ModifyCountInterface{
    AdapterCart adapterCart=new AdapterCart(CartActivity.this);
    private boolean flag = false;
    private Handler getCartHandler;
    private Handler addMedicineHandler;
    private Handler deleteMedicineHandler;
    private Handler delMedicinesHandler;
    private Handler buildOrderHandler;
    private Handler payMoneyHandler;
    private Handler clearAllHandler;
    private Handler getHandlerforUserId;
    private String orderAddress;

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
    private double totalPrice = 0.00;// 购买的商品总价
    private int totalCount = 0;// 购买的商品总数量

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
        findIdByName((String) SPUtlis.get(CartActivity.this,
                AppConfig.AUTO_LOGIN_NAME, ""));
        getCartHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                List<Cart> carts=(ArrayList) msg.obj;
                shoppingCartList=carts;
                if (carts.size()==0) {
                    Toast.makeText(CartActivity.this,"当前字段无数据",Toast.LENGTH_LONG).show();
                }else{
                    adapterCart.setCheckInterface(CartActivity.this);
                    adapterCart.setModifyCountInterface(CartActivity.this);
                    listCart.setAdapter(adapterCart);
                    adapterCart.setCartList(shoppingCartList);
                }
            }
        };
        getHandlerforUserId= new Handler() {
            @Override
            public void handleMessage(Message msg) {
                User userForId= (User) msg.obj;
                SPUtlis.put(CartActivity.this,
                        AppConfig.AUTO_LOGIN_ID,Integer.toString(userForId.getId()));
                orderAddress=userForId.getUserAddress();

            }
        };
    }

    @Override
    protected void initView() {
        super.initView();
        btnEdit.setOnClickListener(this);
        ckAll.setOnClickListener(this);
        tvSettlement.setOnClickListener(this);
        btnBack.setOnClickListener(this);
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
        switch (v.getId()) {
            //全选按钮
            case R.id.ck_all:
                if (shoppingCartList.size() != 0) {
                    if (ckAll.isChecked()) {
                        for (int i = 0; i < shoppingCartList.size(); i++) {
                            shoppingCartList.get(i).setChoosed(true);
                        }
                        adapterCart.notifyDataSetChanged();
                    } else {
                        for (int i = 0; i < shoppingCartList.size(); i++) {
                            shoppingCartList.get(i).setChoosed(false);
                        }
                        adapterCart.notifyDataSetChanged();
                    }
                }
                statistics();
                break;
            case R.id.bt_header_right:
                flag = !flag;
                if (flag) {
                    btnEdit.setText("完成");
                    adapterCart.isShow(false);
                } else {
                    btnEdit.setText("编辑");
                    adapterCart.isShow(true);
                }
                break;
            case R.id.tv_settlement: //结算
                lementOnder();
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }
    public void statistics() {
        totalCount = 0;
        totalPrice = 0.00;
        for (int i = 0; i < shoppingCartList.size(); i++) {
            Cart cart = shoppingCartList.get(i);
            if (ckAll.isChecked()) {
                totalCount++;
                totalPrice += (Double.parseDouble(cart.getMedicinePrice())) *Double.parseDouble(cart.getMedicineNum());
            }
        }
        tvShowPrice.setText("合计:" + totalPrice);
        tvSettlement.setText("结算(" + totalCount + ")");
    }
    /**
     * 结算订单、支付
     */
    @SuppressLint("HandlerLeak")
    private void lementOnder() {
        //选中的需要提交的商品清单

        for (Cart cart:shoppingCartList){
            boolean choosed = cart.getChoosed();

            if (choosed){
                String medicineName = cart.getMedicineName();
                int medicineNum = Integer.parseInt(cart.getMedicineNum());
                double medicinePrice = Double.parseDouble(cart.getMedicinePrice());
                String medicineImg=cart.getMedicineImg();

//                String orderAddress="x厦门";

                try {
                    buildOrder((String) SPUtlis.get(CartActivity.this, AppConfig.AUTO_LOGIN_ID,
                            ""),"1",medicineName,String.valueOf(medicinePrice),
                            medicineImg,String.valueOf(medicineNum),orderAddress);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        buildOrderHandler= new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Map<String,Object> r = (HashMap)msg.obj;

                if((Integer)r.get("code")==2){
                    Toast.makeText(CartActivity.this, ""+r.get("msg"),
                            Toast.LENGTH_SHORT).show();

                }

            }
        };
        try {
            payMoney((String) SPUtlis.get(CartActivity.this, AppConfig.AUTO_LOGIN_NAME,
                    ""),String.valueOf(totalPrice));
        } catch (IOException e) {
            e.printStackTrace();
        }
        payMoneyHandler= new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Map<String,Object> r = (HashMap)msg.obj;
                if((Integer)r.get("code")==1){
                    Toast.makeText(CartActivity.this, ""+r.get("msg"),
                            Toast.LENGTH_SHORT).show();
                }
                if((Integer)r.get("code")==2){
                    Toast.makeText(CartActivity.this, ""+r.get("msg"),
                            Toast.LENGTH_SHORT).show();
                }
            }
        };
        try {
            clearAllMed((String) SPUtlis.get(CartActivity.this, AppConfig.AUTO_LOGIN_ID,
                    ""));
        } catch (IOException e) {
            e.printStackTrace();
        }
        clearAllHandler= new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Map<String,Object> r = (HashMap)msg.obj;
                if((Integer)r.get("code")==1){
                    Toast.makeText(CartActivity.this, ""+r.get("msg"),
                            Toast.LENGTH_SHORT).show();
                }
            }
        };
        Toast.makeText(this, "支付成功："+totalPrice, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void checkGroup(int position, boolean isChecked) {
        shoppingCartList.get(position).setChoosed(isChecked);
        if (isAllCheck())
            ckAll.setChecked(true);
        else
            ckAll.setChecked(false);
        adapterCart.notifyDataSetChanged();
        statistics();
    }
    /**
     * 遍历list集合
     * @return
     */
    private boolean isAllCheck() {

        for (Cart group : shoppingCartList) {
            if (!group.getChoosed())
                return false;
        }
        return true;
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void doIncrease(int position, View showCountView, boolean isChecked) {
        Cart cart = shoppingCartList.get(position);
        int currentCount = Integer.parseInt(cart.getMedicineNum());
        currentCount++;
        try {
            addMedicineToCart(cart.getMedicineName(),cart.getMedicinePrice(),cart.getMedicineImg(),
                    (String) SPUtlis.get(CartActivity.this, AppConfig.AUTO_LOGIN_ID, ""));
        } catch (IOException e) {
            e.printStackTrace();
        }
        addMedicineHandler= new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Map<String,Object> r = (HashMap)msg.obj;

                if((Integer)r.get("code")==1){
                    Toast.makeText(CartActivity.this, ""+r.get("msg"),
                            Toast.LENGTH_SHORT).show();

                }

            }
        };
        cart.setMedicineNum(String.valueOf(currentCount));
        ((TextView) showCountView).setText(currentCount + "");
        adapterCart.notifyDataSetChanged();
        statistics();
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void doDecrease(int position, View showCountView, boolean isChecked) {
        Cart cart = shoppingCartList.get(position);
        int currentCount = Integer.parseInt(cart.getMedicineNum());
        if (currentCount == 1) {
            return;
        }
        currentCount--;
        try {
            delMedicineFromCart(cart.getMedicineName(), (String) SPUtlis.get(CartActivity.this, AppConfig.AUTO_LOGIN_ID, ""));
        } catch (IOException e) {
            e.printStackTrace();
        }
        deleteMedicineHandler= new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Map<String,Object> r = (HashMap)msg.obj;
                if((Integer)r.get("code")==1){
                    Toast.makeText(CartActivity.this, ""+r.get("msg"),
                            Toast.LENGTH_SHORT).show();
                    }
                if((Integer)r.get("code")==4){
                    Toast.makeText(CartActivity.this, ""+r.get("msg"),
                            Toast.LENGTH_SHORT).show();
                }

                }
            };


        cart.setMedicineNum(String.valueOf(currentCount));
        ((TextView) showCountView).setText(currentCount + "");
        adapterCart.notifyDataSetChanged();
        statistics();

    }

    @SuppressLint("HandlerLeak")
    @Override
    public void childDelete(int position) {
        Cart cart = shoppingCartList.get(position);
        shoppingCartList.remove(position);
        try {
            delMedicinesFromCart(cart.getMedicineName(), (String) SPUtlis.get(CartActivity.this, AppConfig.AUTO_LOGIN_ID, ""));
        } catch (IOException e) {
            e.printStackTrace();
        }
        delMedicinesHandler= new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Map<String,Object> r = (HashMap)msg.obj;
                if((Integer)r.get("code")==4){
                    Toast.makeText(CartActivity.this, ""+r.get("msg"),
                            Toast.LENGTH_SHORT).show();
                }
            }
        };
        adapterCart.notifyDataSetChanged();
        statistics();
    }
    private void addMedicineToCart(String medicineName,String medicinePrice,String medicineImg,
                                   String userId) throws IOException {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("medicine_name",medicineName);
        builder.add("medicine_price",medicinePrice);
        builder.add("medicine_img",medicineImg);
        builder.add("user_id",userId);
        final Request request = new Request.Builder()
                .url(AppConfig.ADD_MEDICINE_TO_CART)
                .post(builder.build())
                .build();
      /*  Response response = mOkHttpClient.newCall(request).execute();*/
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseStr = response.body().string();
                Map<String,Object> r =new HashMap<>();
                r = com.alibaba.fastjson.JSONArray.parseObject(responseStr,HashMap.class);
                Message msg = addMedicineHandler.obtainMessage();
                msg.obj = r;
                addMedicineHandler.sendMessage(msg);
            }
        });
    }
    private void delMedicineFromCart(String medicineName, String userId) throws IOException {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("medicine_name",medicineName);
        builder.add("user_id",userId);
        final Request request = new Request.Builder()
                .url(AppConfig.DELETE_MEDICINE_FROM_CART)
                .post(builder.build())
                .build();
      /*  Response response = mOkHttpClient.newCall(request).execute();*/
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseStr = response.body().string();
                Map<String,Object> r =new HashMap<>();
                r = com.alibaba.fastjson.JSONArray.parseObject(responseStr,HashMap.class);
                Message msg = deleteMedicineHandler.obtainMessage();
                msg.obj = r;
                deleteMedicineHandler.sendMessage(msg);
            }
        });
    }
    private void delMedicinesFromCart(String medicineName, String userId) throws IOException {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("medicine_name",medicineName);
        builder.add("user_id",userId);
        final Request request = new Request.Builder()
                .url(AppConfig.DELETE_MEDICINES_FROM_CART)
                .post(builder.build())
                .build();
      /*  Response response = mOkHttpClient.newCall(request).execute();*/
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseStr = response.body().string();
                Map<String,Object> r =new HashMap<>();
                r = com.alibaba.fastjson.JSONArray.parseObject(responseStr,HashMap.class);
                Message msg = delMedicinesHandler.obtainMessage();
                msg.obj = r;
                delMedicinesHandler.sendMessage(msg);
            }
        });
    }
    private void buildOrder(String userId, String orderId,String medicineName,
                            String medicinePrice,String medicineImg,String medicineNum,
                            String orderAddress) throws IOException {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("user_id",userId);
        builder.add("order_id",orderId);
        builder.add("medicine_name",medicineName);
        builder.add("medicine_price",medicinePrice);
        builder.add("medicine_img",medicineImg);
        builder.add("medicine_num",medicineNum);
        builder.add("order_address",orderAddress);
        final Request request = new Request.Builder()
                .url(AppConfig.ADD_MEDICINE_TO_ORDER)
                .post(builder.build())
                .build();
      /*  Response response = mOkHttpClient.newCall(request).execute();*/
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseStr = response.body().string();
                Map<String,Object> r =new HashMap<>();
                r = com.alibaba.fastjson.JSONArray.parseObject(responseStr,HashMap.class);
                Message msg = buildOrderHandler.obtainMessage();
                msg.obj = r;
                buildOrderHandler.sendMessage(msg);
            }
        });
    }
    private void payMoney(String userName,String wallet) throws IOException {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("user_name",userName);
        builder.add("wallet",wallet);
        final Request request = new Request.Builder()
                .url(AppConfig.PAY_WALLET_FOR_CART)
                .post(builder.build())
                .build();
      /*  Response response = mOkHttpClient.newCall(request).execute();*/
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseStr = response.body().string();
                Map<String,Object> r =new HashMap<>();
                r = com.alibaba.fastjson.JSONArray.parseObject(responseStr,HashMap.class);
                Message msg = payMoneyHandler.obtainMessage();
                msg.obj = r;
                payMoneyHandler.sendMessage(msg);
            }
        });
    }
    private void clearAllMed(String userId) throws IOException {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("user_id",userId);
        final Request request = new Request.Builder()
                .url(AppConfig.DELETE_MEDICINES_FROM_CART_BY_USER_ID)
                .post(builder.build())
                .build();
      /*  Response response = mOkHttpClient.newCall(request).execute();*/
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseStr = response.body().string();
                Map<String,Object> r =new HashMap<>();
                r = com.alibaba.fastjson.JSONArray.parseObject(responseStr,HashMap.class);
                Message msg = clearAllHandler.obtainMessage();
                msg.obj = r;
                clearAllHandler.sendMessage(msg);
            }
        });
    }
    public void findIdByName(String userName) {
        com.squareup.okhttp.OkHttpClient mOkHttpClient = new com.squareup.okhttp.OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("user_name",userName);
        final com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                .url(AppConfig.FIND_BY_USERNAME)
                .post(builder.build())
                .build();
        com.squareup.okhttp.Call call = mOkHttpClient.newCall(request);
        call.enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(com.squareup.okhttp.Request request, IOException e) {
            }
            @Override
            public void onResponse(com.squareup.okhttp.Response response) throws IOException {
                String responseStr = response.body().string();
                User user = new User();
                user = com.alibaba.fastjson.JSONArray.parseObject(responseStr, User.class);
                Message msg = getHandlerforUserId.obtainMessage();
                msg.obj = user;
                getHandlerforUserId.sendMessage(msg);

            }
        });
    }
}
