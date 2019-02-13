package com.ldx.mygraduationproject.service;

/**
 * Created by freeFreAme on 2019/2/13.
 */

public class PhysicalService {
    private Double userHeight;
    private Double userWeight;
    private String userSex;


    public String getBodyRate(double height,double weight,String sex){
        if(sex.equals("男")){
            if(height>=165){
                if(weight<=(height-100)*1.02&&weight>=(height-100)*0.98){
                    return"体重标准";
                }
                else if(weight>(height-100)*1.02){
                    return"体重太重";
                }
                else if(weight<(height-100)*0.98){
                    return"体重太轻";
                }
            }
            else if(height>0&&height<165){
                if(weight<=(height-105)*1.02&&weight>=(height-105)*0.98){
                    return"体重标准";
                }
                else if(weight>(height-105)*1.02){
                    return"体重太重";
                }
                else if(weight<(height-105)*0.98){
                    return"体重太轻";
                }
            }
        }
        else{
            if(weight<=(height-100)*1.02&&weight>=(height-100)*0.98){
                return"体重标准";
            }
            else if(weight>(height-100)*1.02){
                return"体重太重";
            }
            else if(weight<(height-100)*0.98){
                return"体重太轻";
            }
        }
        return "输入无效的值";
    }

}
