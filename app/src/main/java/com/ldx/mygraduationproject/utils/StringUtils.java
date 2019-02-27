package com.ldx.mygraduationproject.utils;


/**
 * Created by freeFreAme on 2019/1/31.
 */
public class StringUtils {

    /**
     * 删除后台传递的某些值的特定前缀
     * @param s
     * @return
     */
    public static String deleteBlankPrefix(String s){
        if (s.charAt(0) == 65279){
            s = s.substring(1);
        }
        return s;
    }

    public static String getStateForScore(int score) {
        if (score == 0){
            return "未评测";
        }else if (score > 0 && score <= 33 ){
            return "健康不佳";
        }else if (score > 33 && score <= 66  ){
            return "亚健康";
        }else if (score > 66 && score <= 80){
            return "健康";
        }else if (score > 80){
            return "很健康";
        }
        return "未评测";
    }

    public static String getDescriptionForScore(int score) {
        if (score == 0){
            return "未评测";
        }else if (score > 0 && score <= 33 ){
            return "建议，身体不适要马上检查喔";
        }else if (score > 33 && score <= 66  ){
            return "亚健康，多注意休息和锻炼身体";
        }else if (score > 66 && score <= 80){
            return "健康，很不错喔";
        }else if (score > 80){
            return "很健康，加油保持";
        }
        return "未评测";
    }
}
