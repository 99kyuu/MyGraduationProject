package com.ldx.mygraduationproject.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ldx.mygraduationproject.app.MyApplication;
import com.ldx.mygraduationproject.bean.UserScore;


/**
 * @author: ldx
 * @time: 2019/1/29
 */
public class LocalReceiver extends BroadcastReceiver {

    private Callback callback ;
    public LocalReceiver() {
    }

    public LocalReceiver(Callback callback) {
        this.callback = callback;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        int score = intent.getIntExtra("score",0);
        MyApplication.getInstance().setScore(score);
        UserScore userScore = new UserScore();
        userScore.setScorre(score);
        userScore.setUserid(1);
        if (userScore.save()){
            Log.i("LocalReceiver","存储成功");
        }
        if (callback != null){
            callback.onSuccess(score);
        }
    }

    public interface Callback{
        void onSuccess(int score);
    }

}
