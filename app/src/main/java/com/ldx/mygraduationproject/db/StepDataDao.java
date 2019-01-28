package com.ldx.mygraduationproject.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.ldx.mygraduationproject.bean.UserStep;
import com.ldx.mygraduationproject.bean.UserPlan;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ldx
 * Date : 2019/3/24
 * To do :
 */

public class StepDataDao {
    private DBOpenHelper stepHelper;
    private SQLiteDatabase stepDb;

    public StepDataDao(Context context) {
        stepHelper = new DBOpenHelper(context);
    }

    /**
     * 添加一条新记录
     *
     * @param userStep
     */
    public void addNewData(UserStep userStep) {
        stepDb = stepHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put("curDate", userStep.getCurDate());
        values.put("totalSteps", userStep.getSteps());
        stepDb.insert("step", null, values);

        stepDb.close();
    }

    /**
     * 根据日期查询记录
     *
     * @param curDate
     * @return
     */
    public UserStep getCurDataByDate(String curDate) {
        stepDb = stepHelper.getReadableDatabase();
        UserStep userStep = null;
        Cursor cursor = stepDb.query("step", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndexOrThrow("curDate"));
            if (curDate.equals(date)) {
                String steps = cursor.getString(cursor.getColumnIndexOrThrow("totalSteps"));
                userStep = new UserStep(date, steps);
                //跳出循环
                break;
            }
        }
        //关闭
        stepDb.close();
        cursor.close();
        return userStep;
    }

    /**
     * 查询所有的记录
     *
     * @return
     */
    public List<UserStep> getAllDatas() {
        List<UserStep> dataList = new ArrayList<>();
        stepDb = stepHelper.getReadableDatabase();
        Cursor cursor = stepDb.rawQuery("select * from step", null);

        while (cursor.moveToNext()) {
            String curDate = cursor.getString(cursor.getColumnIndex("curDate"));
            String totalSteps = cursor.getString(cursor.getColumnIndex("totalSteps"));
            UserStep entity = new UserStep(curDate, totalSteps);
            dataList.add(entity);
        }

        //关闭数据库
        stepDb.close();
        cursor.close();
        return dataList;
    }

    /**
     * 更新数据
     *
     * @param userStep
     */
    public void updateCurData(UserStep userStep) {
        stepDb = stepHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put("curDate", userStep.getCurDate());
        values.put("totalSteps", userStep.getSteps());
        stepDb.update("step", values, "curDate=?", new String[]{userStep.getCurDate()});

        stepDb.close();
    }


    /**
     * 删除指定日期的记录
     *
     * @param curDate
     */
    public void deleteCurData(String curDate) {
        stepDb = stepHelper.getReadableDatabase();

        if (stepDb.isOpen())
            stepDb.delete("step", "curDate", new String[]{curDate});
        stepDb.close();
    }

    /**
     * 记入用户设置的计划步数
     *
     * @param userPlan
     */
    public void addNewPlan(UserPlan userPlan) {
        stepDb = stepHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("remindTime", userPlan.getRemindTime());
        values.put("isRemind", userPlan.getIsRemind());
        values.put("planSteps", userPlan.getPlanSteps());
        stepDb.insert("step_plan", null, values);

        stepDb.close();
    }

    /**
     * 根据日期查询记录
     *
     * @param
     * @return
     */
    public UserPlan getPlanByTheLasted() {
        UserPlan userPlan =null;
        stepDb = stepHelper.getReadableDatabase();
        Cursor cursor = stepDb.rawQuery(
                "select * from step_plan order by id", null);

        while (cursor.moveToNext()) {
            String remindTime = cursor.getString(cursor.getColumnIndex("remindTime"));
            String isRemind = cursor.getString(cursor.getColumnIndex("isRemind"));
            String planSteps=cursor.getString(cursor.getColumnIndex("planSteps"));
            userPlan =new UserPlan(remindTime,isRemind,planSteps);
        }
        //关闭
        stepDb.close();
        cursor.close();
        return userPlan;

    }
}
