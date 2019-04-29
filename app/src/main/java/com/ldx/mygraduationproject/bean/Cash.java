package com.ldx.mygraduationproject.bean;

/**
 * Created by freeFreAme on 2019/4/29.
 */

public class Cash {

    private Integer id;
    private String isOK;
    private String userName;
    private String howMuch;
    private String createTime;

    public Cash() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIsOK() {
        return isOK;
    }

    public void setIsOK(String isOK) {
        this.isOK = isOK;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHowMuch() {
        return howMuch;
    }

    public void setHowMuch(String howMuch) {
        this.howMuch = howMuch;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
