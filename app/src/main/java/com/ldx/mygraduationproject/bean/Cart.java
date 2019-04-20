package com.ldx.mygraduationproject.bean;

/**
 * Created by freeFreAme on 2019/4/20.
 */

public class Cart {
    private Integer id;
    private String medicineName;
    private String medicinePrice;
    private String medicineNum;
    private String medicineImg;
    private Integer userId;

    public Cart(Integer id, String medicineName, String medicinePrice, String medicineNum, String medicineImg, Integer userId) {
        this.id = id;
        this.medicineName = medicineName;
        this.medicinePrice = medicinePrice;
        this.medicineNum = medicineNum;
        this.medicineImg = medicineImg;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getMedicinePrice() {
        return medicinePrice;
    }

    public void setMedicinePrice(String medicinePrice) {
        this.medicinePrice = medicinePrice;
    }

    public String getMedicineNum() {
        return medicineNum;
    }

    public void setMedicineNum(String medicineNum) {
        this.medicineNum = medicineNum;
    }

    public String getMedicineImg() {
        return medicineImg;
    }

    public void setMedicineImg(String medicineImg) {
        this.medicineImg = medicineImg;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
