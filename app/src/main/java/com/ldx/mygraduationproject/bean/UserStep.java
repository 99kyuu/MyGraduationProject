package com.ldx.mygraduationproject.bean;

import org.litepal.LitePal;
import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * Created by fySpring
 * Date : 2017/3/24
 * To do :
 */

public class UserStep extends LitePalSupport{
    @Column(unique = true,nullable = false)
    private Integer id;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserStep{" +
                "curDate='" + curDate + '\'' +
                ", steps=" + steps +
                '}';
    }
}
