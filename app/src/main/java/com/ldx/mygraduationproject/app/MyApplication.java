package com.ldx.mygraduationproject.app;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Created by freeFreAme on 2019/1/26.
 */

public class MyApplication extends Application {
    private static MyApplication application ;

    public static Context applicationContext;

//    private UserBean userBean ;


    private int Score = 0 ;

    /**
     * 获取用户
     * @return
     */
//    public UserBean getUserBean() {
//        if (userBean!=null)
//            return userBean;
//        else
//            return null;
//    }

//    /**
//     * 设置用户
//     * @param userBean
//     */
////    public void setUserBean(UserBean userBean) {
////        this.userBean = userBean;
////    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        applicationContext = this;
        LitePal.initialize(this);
    }


    /**
     * 获取MyApplication实例
     * @return
     */
    public static MyApplication getInstance(){
        if (application != null && application instanceof MyApplication) {
            return application;
        } else {
            application = new MyApplication();
            application.onCreate();
            return application;
        }
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }
}
