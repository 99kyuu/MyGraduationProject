package com.ldx.mygraduationproject.bean;

/**
 * Created by fySpring
 * Date : 2017/3/24
 * To do :
 */

public class UserStep {

    private String curDate; //当天的日期
    private String steps;   //当天的步数

    public UserStep() {
    }

    public UserStep(String curDate, String steps) {
        this.curDate = curDate;
        this.steps = steps;
    }

    public String getCurDate() {
        return curDate;
    }

    public void setCurDate(String curDate) {
        this.curDate = curDate;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }


    @Override
    public String toString() {
        return "UserStep{" +
                "curDate='" + curDate + '\'' +
                ", steps=" + steps +
                '}';
    }
}
