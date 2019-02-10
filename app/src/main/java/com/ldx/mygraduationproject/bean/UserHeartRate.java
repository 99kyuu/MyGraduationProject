package com.ldx.mygraduationproject.bean;

/**
 * Created by freeFreAme on 2019/2/10.
 */

public class UserHeartRate {
    private Integer id;
    private String curDate;
    private String userHeartRate;

    public UserHeartRate(Integer id, String curDate, String userHeartRate) {
        this.id = id;
        this.curDate = curDate;
        this.userHeartRate = userHeartRate;
    }
    public UserHeartRate() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCurDate() {
        return curDate;
    }

    public void setCurDate(String curDate) {
        this.curDate = curDate;
    }

    public String getUserHeartRate() {
        return userHeartRate;
    }

    public void setUserHeartRate(String userHeartRate) {
        this.userHeartRate = userHeartRate;
    }
}
