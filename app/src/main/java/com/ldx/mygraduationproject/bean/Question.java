package com.ldx.mygraduationproject.bean;

/**
 * Created by freeFreAme on 2019/1/22.
 */
public class Question {

    private String question ;
    private int choose;

    public Question(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getChoose() {
        return choose;
    }

    public void setChoose(int choose) {
        this.choose = choose;
    }
}
