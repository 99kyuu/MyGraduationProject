package com.ldx.mygraduationproject.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.bean.StepEntity;
import com.ldx.mygraduationproject.constant.AppConfig;
import com.ldx.mygraduationproject.db.StepDataDao;
import com.ldx.mygraduationproject.service.StepService;
import com.ldx.mygraduationproject.utils.StepCountCheckUtil;
import com.ldx.mygraduationproject.utils.TimeUtil;
import com.ldx.mygraduationproject.view.BeforeOrAfterCalendarView;
import com.ldx.mygraduationproject.view.LineChartView;
import com.ldx.mygraduationproject.view.RunningView;
import com.ldx.mygraduationproject.view.WeightView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;


/**
 * @author: tao
 * @time: 2018/9/3
 * @e-mail: 1462320178@qq.com
 * @version: 1.0
 * @exception: 无
 * @explain: 说明
 */
public class FragmentDetails extends BaseFragment  implements android.os.Handler.Callback{
    //历史记录组件
    private BeforeOrAfterCalendarView calenderView;

//    private TextView totalKmTv;
//    private TextView stepsTimeTv;
//    private TextView totalStepsTv;
//    private TextView supportTv;
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

    /**
     * 屏幕长度和宽度
     */

    private String curSelDate;
    private DecimalFormat df = new DecimalFormat("#.##");
    private List<StepEntity> stepEntityList = new ArrayList<>();
    private StepDataDao stepDataDao;


    @Override
    protected int setLayoutId() {
        return R.layout.fragment_details;

    }
    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        super.initData();
        //跑步数据
        runningView.setCurrentCount(0, 10000, 1799);
        curSelDate = TimeUtil.getCurrentDate();
        //体重数据
        weightView.setPercent(66.45f);
        //  初始化折线数据
        mItems = new ArrayList<>();
        mItems.add(new LineChartView.ItemBean(1489507200, 23));
        mItems.add(new LineChartView.ItemBean(1489593600, 88));
        mItems.add(new LineChartView.ItemBean(1489680000, 60));
        mItems.add(new LineChartView.ItemBean(1489766400, 50));
        mItems.add(new LineChartView.ItemBean(1489852800, 70));
        mItems.add(new LineChartView.ItemBean(1489939200, 10));
        mItems.add(new LineChartView.ItemBean(1490025600, 33));
        mItems.add(new LineChartView.ItemBean(1490112000, 44));
        mItems.add(new LineChartView.ItemBean(1490198400, 99));
        mItems.add(new LineChartView.ItemBean(1490284800, 17));

        shadeColors= new int[]{
                Color.argb(100, 161,216,139), Color.argb(55, 183,235,139),
                Color.argb(20, 221,249,197)};

        //  设置折线数据
        lineChartView.setItems(mItems);
        //  设置渐变颜色
        lineChartView.setShadeColors(shadeColors);
        //  开启动画
        lineChartView.startAnim(lineChartView,2000);
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
        Intent intent = new Intent(getContext(), StepService.class);
        isBind = getContext().bindService(intent, conn, Context.BIND_AUTO_CREATE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            getContext().startForegroundService(intent);
        } else {
            getContext().startService(intent);
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
     *
     */
    private void setDatas() {
        StepEntity stepEntity = stepDataDao.getCurDataByDate(curSelDate);

        if (stepEntity != null) {
            int steps = Integer.parseInt(stepEntity.getSteps());

            //获取全局的步数
            totalStepsTv.setText(String.valueOf(steps));
            //计算总公里数
            totalKmTv.setText(countTotalKM(steps));
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
        stepEntityList.clear();
        stepEntityList.addAll(stepDataDao.getAllDatas());
        if (stepEntityList.size() >= 7) {
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        //记得解绑Service，不然多次绑定Service会异常
        if (isBind) mActivity.unbindService(conn);
    }


}

