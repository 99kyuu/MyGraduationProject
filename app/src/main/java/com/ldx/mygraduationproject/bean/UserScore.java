package com.ldx.mygraduationproject.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * @author: tao
 * @time: 2018/9/7
 * @e-mail: 1462320178@qq.com
 * @version: 1.0
 * @exception: 无
 * @explain: 用于在本地数据库存储体检单成绩
 */
public class UserScore extends LitePalSupport {
    @Column(unique = true,nullable = false)
    private int userid;
    @Column(nullable = false)
    private int scorre;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getScorre() {
        return scorre;
    }

    public void setScorre(int scorre) {
        this.scorre = scorre;
    }

    @Override
    public String toString() {
        return "UserScore{" +
                "userid=" + userid +
                ", scorre=" + scorre +
                '}';
    }


}
