package com.ldx.mygraduationproject.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * Created by freeFreAme on 2019/1/28.
 */
public class UserPlan extends LitePalSupport {
    @Column(unique = true,nullable = false)
    private Integer id;
    @Column(nullable = false)
    private String remindTime; //提醒的事件的时间
    @Column(nullable = false)
    private String planSteps;   //当天的步数
    @Column(nullable = false)
    private String isRemind; //是否提醒

    public UserPlan(String remindTime, String isRemind,String planSteps) {
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    //    @Override
//    public String toString() {
//        return "UserPlan{" +
//                "remindTime='" + remindTime + '\'' +
//                ", isRemind=" + isRemind + '\'' +
//                ",planSteps="+
//                '}';
//    }
}
