package com.ldx.mygraduationproject.bean;

import org.litepal.LitePal;
import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * Created by freeFreAme on 2019/1/29.
 */

public class UserStep extends LitePalSupport{
    @Column(unique = true,nullable = false)
    private Integer id;
    private String curDate; //当天的日期
    private String totalSteps;   //当天的步数
    public UserStep() {
    }

    public UserStep(String curDate, String totalSteps) {
        this.curDate = curDate;
        this.totalSteps = totalSteps;
    }

    public String getCurDate() {
        return curDate;
    }

    public void setCurDate(String curDate) {
        this.curDate = curDate;
    }

    public String getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(String totalSteps) {
        this.totalSteps = totalSteps;
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
                ", steps=" + totalSteps +
                '}';
    }
}
