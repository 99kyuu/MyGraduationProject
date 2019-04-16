package com.ldx.mygraduationproject.bean;

/**
 * Created by freeFreAme on 2018/12/26.
 */
public class Medicine {

    /**
     * id : 422
     * createDate : 2018-09-05 22:39:41
     * medicineName : 感冒清热颗粒(胶囊)
     * medicinePrice : 6.2
     * medicineType : /
     * usedNum: 5655395,
     * usedRank: 2
     */

    private int id;
    private String createDate;
    private String medicineName;
    private double medicinePrice;
    private String medicineType;
    private String usedNum;
    private String usedRank;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public double getMedicinePrice() {
        return medicinePrice;
    }

    public void setMedicinePrice(double medicinePrice) {
        this.medicinePrice = medicinePrice;
    }

    public String getMedicineType() {
        return medicineType;
    }

    public void setMedicineType(String medicineType) {
        this.medicineType = medicineType;
    }

    public String getUsedNum() {
        return usedNum;
    }

    public void setUsedNum(String usedNum) {
        this.usedNum = usedNum;
    }

    public String getUsedRank() {
        return usedRank;
    }

    public void setUsedRank(String usedRank) {
        this.usedRank = usedRank;
    }

    @Override
    public String toString() {
        return "Medicine{" +
                "id=" + id +
                ", createDate='" + createDate + '\'' +
                ", medicineName='" + medicineName + '\'' +
                ", medicinePrice=" + medicinePrice +
                ", medicineType='" + medicineType + '\'' +
                ", usedNum='" + usedNum + '\'' +
                ", usedRank='" + usedRank + '\'' +
                '}';
    }
}
