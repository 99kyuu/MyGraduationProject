package com.ldx.mygraduationproject.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * Created by freeFreAme on 2019/1/28.
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
