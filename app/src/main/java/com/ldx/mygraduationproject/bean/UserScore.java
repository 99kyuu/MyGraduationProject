package com.ldx.mygraduationproject.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * @author: ldx
 * @time: 2019/2/1
 * @version: 1.0
 * @exception: 无
 * @explain: 用于在本地数据库存储体检单成绩
 */
public class UserScore extends LitePalSupport {
    @Column(unique = true,nullable = false)
    private String username;
    @Column(nullable = false)
    private int score;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "UserScore{" +
                "username=" + username +
                ", score=" + score +
                '}';
    }


}
