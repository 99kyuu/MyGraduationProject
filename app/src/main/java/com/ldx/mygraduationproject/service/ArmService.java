package com.ldx.mygraduationproject.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ldx.mygraduationproject.activity.RemindActivity;

/**
 * Created by freeFreAme on 2019/5/2.
 */

public class ArmService  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("com.ldx.steplan.service".equals(intent.getAction())){
            Intent intent1=new Intent(context,RemindActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
    }
}
