package com.ldx.mygraduationproject.fragment;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.activity.HeartRateActivity;
import com.ldx.mygraduationproject.activity.Last7DayStepActivity;
import com.ldx.mygraduationproject.activity.MainActivity;
import com.ldx.mygraduationproject.activity.SetPlanActivity;
import com.ldx.mygraduationproject.activity.PhysicalActivity;
import com.ldx.mygraduationproject.bean.UserHeartRate;
import com.ldx.mygraduationproject.bean.UserPhysical;
import com.ldx.mygraduationproject.bean.UserPlan;
import com.ldx.mygraduationproject.bean.UserStep;
import com.ldx.mygraduationproject.constant.AppConfig;
import com.ldx.mygraduationproject.db.StepDataDao;
import com.ldx.mygraduationproject.service.PhysicalService;
import com.ldx.mygraduationproject.service.StepService;
import com.ldx.mygraduationproject.utils.SPUtlis;
import com.ldx.mygraduationproject.utils.StepCountCheckUtil;
import com.ldx.mygraduationproject.utils.TimeUtil;
import com.ldx.mygraduationproject.view.BeforeOrAfterCalendarView;
import com.ldx.mygraduationproject.view.LineChartView;
import com.ldx.mygraduationproject.view.RunningView;
import com.ldx.mygraduationproject.view.WeightView;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by freeFreAme on 2019/1/18.
 */
public class FragmentDetails extends BaseFragment implements android.os.Handler.Callback {
    //历史记录组件
    private BeforeOrAfterCalendarView calenderView;

    @BindView(R.id.movement_total_steps_tv)
    TextView totalStepsTv;
    @BindView(R.id.is_support_tv)
    TextView supportTv;
    @BindView(R.id.movement_total_steps_time_tv)
    TextView stepsTimeTv;
    @BindView(R.id.movement_total_km_tv)
    TextView totalKmTv;
    @BindView(R.id.movement_records_calender_ll)
    LinearLayout movementCalenderLl;
    @BindView(R.id.movement_total_km_time_tv)
    TextView kmTimeTv;

    //CardView组件
    private List<LineChartView.ItemBean> mItems;
    private int[] shadeColors;
    @BindView(R.id.running_view)
    RunningView runningView;
    @BindView(R.id.LineChartView)
    LineChartView lineChartView;
    @BindView(R.id.weightView)
    WeightView weightView;
    @BindView(R.id.weight_data_text)
    TextView weight_data_text;
    @BindView(R.id.mile_data)
    TextView mile_data;
    @BindView(R.id.max_heart_rate_data)
    TextView max_heart_rate_data;
    @BindView(R.id.min_heart_rate_data)
    TextView min_heart_rate_data;
    @BindView(R.id.survey_data1)
    TextView user_height;
    @BindView(R.id.survey_data2)
    TextView user_sex;
    @BindView(R.id.survey_data3)
    TextView user_survey_data;
    /**
     * 屏幕长度和宽度
     */

    private String curSelDate;
    private DecimalFormat df = new DecimalFormat("#.##");
    private List<UserStep> userStepList = new ArrayList<>();
    private StepDataDao stepDataDao;
    private List<UserHeartRate> userHeartRateList;
    private Handler getHeartRateHandler;
    private Handler getMaxHeartRateHandler;
    private Handler getMinHeartRateHandler;
    private Handler getUserPhysicalHandler;
    private Handler getPlanHandler;
    private Integer totalStepNum;
    PhysicalService physicalService = new PhysicalService();

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_details;

    }

    @Override
    protected void initView() {

    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void initData() {
        super.initData();
        //具体时间
        curSelDate = TimeUtil.getCurrentDate();
        getHeartRateFromNet((String) SPUtlis.get(mActivity, AppConfig.AUTO_LOGIN_NAME, ""));
        getMaxHeartRateFromNet((String) SPUtlis.get(mActivity, AppConfig.AUTO_LOGIN_NAME, ""));
        getMinHeartRateFromNet((String) SPUtlis.get(mActivity, AppConfig.AUTO_LOGIN_NAME, ""));
        getUserPhysicalFromNet((String) SPUtlis.get(mActivity, AppConfig.AUTO_LOGIN_NAME, ""));
        getUserPlanByNet((String) SPUtlis.get(mActivity, AppConfig.AUTO_LOGIN_NAME, ""));
        getMinHeartRateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                UserHeartRate userHeartRate = (UserHeartRate) msg.obj;
                if ((UserHeartRate) msg.obj == null) {
                    Toast.makeText(mActivity, "用户暂时未拥有数据", Toast.LENGTH_SHORT).show();
                } else {
                    min_heart_rate_data.setText(userHeartRate.getUserHeartRate());
                }
            }
        };
        getMaxHeartRateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                UserHeartRate userHeartRate = (UserHeartRate) msg.obj;
                if ((UserHeartRate) msg.obj == null) {
                    Toast.makeText(mActivity, "用户暂时未拥有数据", Toast.LENGTH_SHORT).show();
                } else {
                    max_heart_rate_data.setText(userHeartRate.getUserHeartRate());
                }
            }
        };

        //体重身高数据
        getUserPhysicalHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                UserPhysical userPhysical = (UserPhysical) msg.obj;
                if ((UserPhysical) msg.obj == null) {
                    Toast.makeText(mActivity, "用户暂时未拥有数据", Toast.LENGTH_SHORT).show();
                } else {
                    weightView.setPercent(Float.parseFloat(userPhysical.getUserWeight()));
                    weight_data_text.setText(userPhysical.getUserWeight() + "kg");
                    user_sex.setText(userPhysical.getUserSex());
                    user_height.setText(userPhysical.getUserHeight() + "cm");
                    String userBodyRate = physicalService.getBodyRate(Double.parseDouble(userPhysical.getUserHeight()),
                            Double.parseDouble(userPhysical.getUserHeight()), userPhysical.getUserSex());
                    user_survey_data.setText(userBodyRate);
                }
            }
        };


        //  初始化心率数据

        getHeartRateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
//                if (msg.obj == null) {
//                    Toast.makeText(mActivity, "用户暂时未拥有数据", Toast.LENGTH_SHORT).show();
//                }else {
                userHeartRateList = (List<UserHeartRate>) msg.obj;
                if (userHeartRateList.size() == 0) {
                    Toast.makeText(mActivity, "用户暂时未拥有数据", Toast.LENGTH_SHORT).show();
                } else {
                    mItems = new ArrayList<>();
                    mItems.add(new LineChartView.ItemBean(1489507200, Integer.parseInt(userHeartRateList.get(0).getUserHeartRate())));
                    mItems.add(new LineChartView.ItemBean(1489593600, Integer.parseInt(userHeartRateList.get(1).getUserHeartRate())));
                    mItems.add(new LineChartView.ItemBean(1489680000, Integer.parseInt(userHeartRateList.get(2).getUserHeartRate())));
                    mItems.add(new LineChartView.ItemBean(1489766400, Integer.parseInt(userHeartRateList.get(3).getUserHeartRate())));
                    mItems.add(new LineChartView.ItemBean(1489852800, Integer.parseInt(userHeartRateList.get(4).getUserHeartRate())));
                    mItems.add(new LineChartView.ItemBean(1489939200, Integer.parseInt(userHeartRateList.get(5).getUserHeartRate())));
                    mItems.add(new LineChartView.ItemBean(1490025600, Integer.parseInt(userHeartRateList.get(6).getUserHeartRate())));
//                mItems.add(new LineChartView.ItemBean(1490112000, Integer.parseInt(userHeartRateList.get(6).getUserHeartRate())));
//                mItems.add(new LineChartView.ItemBean(1490198400, Integer.parseInt(userHeartRateList.get(1).getUserHeartRate())));
//                mItems.add(new LineChartView.ItemBean(1490284800, Integer.parseInt(userHeartRateList.get(1).getUserHeartRate())));
                    shadeColors = new int[]{
                            Color.argb(100, 161, 216, 139), Color.argb(55, 183, 235, 139),
                            Color.argb(20, 221, 249, 197)};

                    //  设置折线数据
                    lineChartView.setItems(mItems);
                    //  设置渐变颜色
                    lineChartView.setShadeColors(shadeColors);
                    //  开启动画
                    lineChartView.startAnim(lineChartView, 2000);
                }
            }
        };
        //
        getPlanHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                UserPlan userPlanNet = (UserPlan) msg.obj;
                if ((UserPlan) msg.obj == null) {
                    Toast.makeText(mActivity, "用户暂时未拥有数据", Toast.LENGTH_SHORT).show();
                } else {
                    UserStep userStep = stepDataDao.getCurDataByDate(curSelDate);
                    String isRemind = userPlanNet.getIsRemind();
                    String remindTime = userPlanNet.getRemindTime();
                    Log.e("aa",""+userPlanNet.getPlanSteps());
                    totalStepNum = Integer.parseInt(userPlanNet.getPlanSteps());
                    if (userStep != null) {
                        int steps = Integer.parseInt(userStep.getSteps());
                        //获取全局的步数
                        runningView.setCurrentCount(0, totalStepNum, steps);
                        Log.e("aaaaaaa", "" + totalStepNum);
                    } else {
                        runningView.setCurrentCount(0, totalStepNum, 0);
                    }
                }
            }
        };


        //用户历史步数

//        放到获取宽度之后
        calenderView = new BeforeOrAfterCalendarView(mActivity);
        movementCalenderLl.addView(calenderView);
        if (StepCountCheckUtil.isSupportStepCountSensor(mActivity)) {
            getRecordList();
            supportTv.setVisibility(View.GONE);
            setDatas();
            setupService();
        } else {
            totalStepsTv.setText("0");
            supportTv.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.running_count_card, R.id.heart_rate_card,R.id.weight_card})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.running_count_card:
                mActivity.startActivity(new Intent(mActivity, Last7DayStepActivity.class));
                break;
            case R.id.heart_rate_card:
                mActivity.startActivity(new Intent(mActivity, HeartRateActivity.class));
                break;
            case R.id.weight_card:
                mActivity.startActivity(new Intent(mActivity, Last7DayStepActivity.class));
                break;
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        calenderView.setOnBoaCalenderClickListener(new BeforeOrAfterCalendarView.BoaCalenderClickListener() {
            @Override
            public void onClickToRefresh(int position, String curDate) {
                //获取当前选中的时间
                curSelDate = curDate;
                //根据日期去取数据
                setDatas();
            }
        });
    }

    private boolean isBind = false;
    private Messenger mGetReplyMessenger = new Messenger(new Handler((Handler.Callback) mActivity));
    private Messenger messenger;

    /**
     * 开启计步服务
     */
    private void setupService() {
        Intent intent = new Intent(mActivity, StepService.class);
        isBind = getContext().bindService(intent, conn, Context.BIND_AUTO_CREATE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mActivity.startForegroundService(intent);
        } else {
            mActivity.startService(intent);
        }

    }

    /**
     * 定时任务
     */
    private TimerTask timerTask;
    private Timer timer;
    /**
     * 用于查询应用服务（application Service）的状态的一种interface，
     * 更详细的信息可以参考Service 和 context.bindService()中的描述，
     * 和许多来自系统的回调方式一样，ServiceConnection的方法都是进程的主线程中调用的。
     */
    private ServiceConnection conn = new ServiceConnection() {
        /**
         * 在建立起于Service的连接时会调用该方法，目前Android是通过IBind机制实现与服务的连接。
         * @param name 实际所连接到的Service组件名称
         * @param service 服务的通信信道的IBind，可以通过Service访问对应服务
         */
        @Override
        public void onServiceConnected(ComponentName name, final IBinder service) {
            /**
             * 设置定时器，每个三秒钟去更新一次运动步数
             */
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        messenger = new Messenger(service);
                        Message msg = Message.obtain();
                        msg.replyTo = mGetReplyMessenger;
                        messenger.send(msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            };
            timer = new Timer();
            timer.schedule(timerTask, 0, 3000);
        }

        /**
         * 当与Service之间的连接丢失的时候会调用该方法，
         * 这种情况经常发生在Service所在的进程崩溃或者被Kill的时候调用，
         * 此方法不会移除与Service的连接，当服务重新启动的时候仍然会调用 onServiceConnected()。
         * @param name 丢失连接的组件名称
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    /**
     * 设置记录数据
     */

    private void setDatas() {
        UserStep userStep = stepDataDao.getCurDataByDate(curSelDate);

        if (userStep != null) {
            int steps = Integer.parseInt(userStep.getSteps());

            //获取全局的步数
            totalStepsTv.setText(String.valueOf(steps));
            //计算总公里数
            totalKmTv.setText(countTotalKM(steps));
            //跑步数据
            mile_data.setText(countTotalKM(steps));
        } else {
            //获取全局的步数
            totalStepsTv.setText("0");
            //计算总公里数
            totalKmTv.setText("0");
        }

        //设置时间
        String time = TimeUtil.getWeekStr(curSelDate);
        kmTimeTv.setText(time);
        stepsTimeTv.setText(time);
    }

    /**
     * 简易计算公里数，假设一步大约有0.7米
     *
     * @param steps 用户当前步数
     * @return
     */
    private String countTotalKM(int steps) {
        double totalMeters = steps * 0.7;
        //保留两位有效数字
        return df.format(totalMeters / 1000);
    }


    /**
     * 获取全部运动历史纪录
     */
    private void getRecordList() {
        //获取数据库
        stepDataDao = new StepDataDao(mActivity);
        userStepList.clear();
        userStepList.addAll(stepDataDao.getAllDatas());
        if (userStepList.size() >= 7) {
            // TODO: 2019/1/24在这里获取历史记录条数，当条数达到7条或以上时，就开始删除第七天之前的数据,暂未实现

        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            //这里用来获取到Service发来的数据
            case AppConfig.MSG_FROM_SERVER:

                //如果是今天则更新数据
                if (curSelDate.equals(TimeUtil.getCurrentDate())) {
                    //记录运动步数
                    int steps = msg.getData().getInt("steps");
                    //设置的步数
                    totalStepsTv.setText(String.valueOf(steps));
                    //计算总公里数
                    totalKmTv.setText(countTotalKM(steps));
                }
                break;
        }
        return false;
    }

    /***
     * 从云端获取历史心率数据的部分*/
    private void getHeartRateFromNet(String userName) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("user_name", userName);
        final Request request = new Request.Builder()
                .url(AppConfig.GET_USER_HEART_RATE_BY_USER_NAME)
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
                List<UserHeartRate> userHeartRates = new ArrayList<>();
                userHeartRates = com.alibaba.fastjson.JSONArray.parseArray(responseStr, UserHeartRate.class);
                Message msg = getHeartRateHandler.obtainMessage();
                msg.obj = userHeartRates;
                getHeartRateHandler.sendMessage(msg);

            }
        });
    }

    private void getMaxHeartRateFromNet(String userName) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("user_name", userName);
        final Request request = new Request.Builder()
                .url(AppConfig.GET_MAX_USER_HEART_RATE_BY_USER_NAME)
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
                UserHeartRate userHeartRates = new UserHeartRate();
                userHeartRates = com.alibaba.fastjson.JSONArray.parseObject(responseStr, UserHeartRate.class);
                Message msg = getMaxHeartRateHandler.obtainMessage();
                msg.obj = userHeartRates;
                getMaxHeartRateHandler.sendMessage(msg);

            }
        });
    }

    private void getMinHeartRateFromNet(String userName) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("user_name", userName);
        final Request request = new Request.Builder()
                .url(AppConfig.GET_MIN_USER_HEART_RATE_BY_USER_NAME)
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
                UserHeartRate userHeartRates = new UserHeartRate();
                userHeartRates = com.alibaba.fastjson.JSONArray.parseObject(responseStr, UserHeartRate.class);
                Message msg = getMinHeartRateHandler.obtainMessage();
                msg.obj = userHeartRates;
                getMinHeartRateHandler.sendMessage(msg);

            }
        });
    }

    public void getUserPhysicalFromNet(String userName) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("user_name", userName);
        final Request request = new Request.Builder()
                .url(AppConfig.GET_USER_PHYSICAL_BY_USER_NAME_FOR_ALL)
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
                UserPhysical userPhysical = new UserPhysical();
                userPhysical = com.alibaba.fastjson.JSONArray.parseObject(responseStr, UserPhysical.class);
                Message msg = getUserPhysicalHandler.obtainMessage();
                msg.obj = userPhysical;
                getUserPhysicalHandler.sendMessage(msg);

            }
        });
    }

    public void getUserPlanByNet(String userName) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("user_name", userName);
        final Request request = new Request.Builder()
                .url(AppConfig.GET_USER_STEP_PLAN_BY_USERNAME)
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
                UserPlan userPlan = new UserPlan();
                userPlan = com.alibaba.fastjson.JSONArray.parseObject(responseStr, UserPlan.class);
                Message msg = getPlanHandler.obtainMessage();
                msg.obj = userPlan;
                getPlanHandler.sendMessage(msg);

            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //记得解绑Service，不然多次绑定Service会异常
        if (isBind) mActivity.unbindService(conn);
    }


}

