package com.ldx.mygraduationproject.bean;

/**
 * Created by freeFreAme on 2019/1/28.
 */

public class UserPlan {
    private String remindTime; //提醒的事件的时间
    private String planSteps;   //当天的步数
    private String isRemind; //是否提醒

    public UserPlan(String remindTime, String planSteps, String isRemind) {
        this.remindTime = remindTime;
        this.planSteps = planSteps;
        this.isRemind = isRemind;
    }

    public UserPlan() {

    }

    public String getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(String remindTime) {
        this.remindTime = remindTime;
    }

    public String getPlanSteps() {
        return planSteps;
    }

    public void setPlanSteps(String planSteps) {
        this.planSteps = planSteps;
    }

    public String getIsRemind() {
        return isRemind;
    }

    public void setIsRemind(String isRemind) {
        this.isRemind = isRemind;
    }
}
