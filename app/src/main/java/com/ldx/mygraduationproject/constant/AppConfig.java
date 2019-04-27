package com.ldx.mygraduationproject.constant;

import android.content.Intent;

/**
 * Created by freeFreAme on 2019/1/22.
 */

public class AppConfig {
        //测试类
    public final static String BASE_URL_PATH = "http://192.168.1.104:8100"; //服务器基本地址
    public final static String DELETE_STUDENT_BY_NAME = BASE_URL_PATH.concat("/delete_by_name");
    public final static String INSERT_STUDENT = BASE_URL_PATH.concat("/insert");
    public final static String ADD_MORE_STUDENTS = BASE_URL_PATH.concat("/add_more_students");


//    192.168.124.9
    //192.168.1.104
//http://172.16.146.233学校
    //aliyun 106.14.204.72
    //用户类下功能 /userApi
    public final static String SERVER_URL_PATH = "http://192.168.1.102:8100/api";
    public final static String REGISTER_NEW_USER= SERVER_URL_PATH.concat("/security/register_new_user");
    public final static String USER_LOGIN= SERVER_URL_PATH.concat("/security/user_login");
    public final static String RESET_VALID_CODE= SERVER_URL_PATH.concat("/security/reset_valid_code");
    public final static String RESET_USER_PASSWORD= SERVER_URL_PATH.concat("/security/reset_user_password");
    public final static String FIND_BY_USERNAME=SERVER_URL_PATH.concat("/security/find_by_username");
    public static final String PAY_WALLET_FOR_CART=SERVER_URL_PATH.concat("/security/pay_wallet_for_cart");
    //步数类下的功能
    //计划类
    public final static String GET_USER_STEP_PLAN_BY_USERNAME=SERVER_URL_PATH.concat("/stepplan/get_user_step_plan_by_username");
    public final static String ADD_USER_PLAN=SERVER_URL_PATH.concat("/stepplan/add_user_plan");
    //步数类
    public final static String FIND_BY_CURDATE=SERVER_URL_PATH.concat("/stepcount/find_by_curDate");
    //心率类
    public final static String ADD_USER_HEART_RATE=SERVER_URL_PATH.concat("/heartrate/add_user_heart_rate");
    public final static String GET_USER_HEART_RATE_BY_USER_NAME=SERVER_URL_PATH.concat("/heartrate/get_user_heart_rate_by_user_name");
    public final static String GET_MAX_USER_HEART_RATE_BY_USER_NAME=SERVER_URL_PATH.concat("/heartrate/get_max_user_heart_rate_by_user_name");
    public final static String GET_MIN_USER_HEART_RATE_BY_USER_NAME=SERVER_URL_PATH.concat("/heartrate/get_min_user_heart_rate_by_user_name");
    //身体数据类
    public final static String GET_USER_PHYSICAL_BY_USER_NAME_FOR_ALL=SERVER_URL_PATH.concat("/physical/get_user_physical_by_user_name_for_all");
    public final static String ADD_USER_PHYSICAL=SERVER_URL_PATH.concat("/physical/add_user_physical");
    public final static String GET_USER_PHYSICAL_BY_USER_NAME=SERVER_URL_PATH.concat("/physical/get_user_physical_by_user_name");
    public final static String GET_MIN_USER_WEIGHT_BY_USER_NAME=SERVER_URL_PATH.concat("/physical/get_min_user_weight_by_user_name");
    public final static String GET_MAX_USER_WEIGHT_BY_USER_NAME=SERVER_URL_PATH.concat("/physical/get_max_user_weight_by_user_name");


    //计步类
    public static final int MSG_FROM_CLIENT = 0;
    public static final int MSG_FROM_SERVER = 1;
    //文章
    public static final String GET_ALL_ARTICLE=SERVER_URL_PATH.concat("/article/get_all_article");
    public static final String GET_CLASSES_ARTICLE=SERVER_URL_PATH.concat("/article/get_classes_article");
    public static final String GET_SEARCH_ARTICLE=SERVER_URL_PATH.concat("/article/get_search_article");
    //药品店
    public static final String GET_ALL_MEDICINE=SERVER_URL_PATH.concat("/medicine/get_all_medicine");
    public static final String FIND_BY_KEYWORD=SERVER_URL_PATH.concat("/medicine/find_by_keyword");
    public static final String FIND_BY_TYPE=SERVER_URL_PATH.concat("/medicine/find_by_type");
   //购物车
    public static final String FIND_BY_USER_ID=SERVER_URL_PATH.concat("/cart/find_by_user_id");
    public static final String ADD_MEDICINE_TO_CART=SERVER_URL_PATH.concat("/cart/add_medicine_to_cart");
    public static final String DELETE_MEDICINE_FROM_CART=SERVER_URL_PATH.concat("/cart/delete_medicine_from_cart");
    public static final String DELETE_MEDICINES_FROM_CART=SERVER_URL_PATH.concat("/cart/delete_medicines_from_cart");
    public static final String DELETE_MEDICINES_FROM_CART_BY_USER_ID=SERVER_URL_PATH.concat("/cart/delete_medicines_from_cart_by_user_id");
    //订单
    public static final String ADD_MEDICINE_TO_ORDER=SERVER_URL_PATH.concat("/order/add_medicine_to_order");
    public static final String FIND_ORDER_BY_USER_ID=SERVER_URL_PATH.concat("/order/find_order_by_user_id");
    public static final String DELETE_MEDICINE_FROM_ORDER=SERVER_URL_PATH.concat("/order/delete_medicine_from_order");
    /*是否自动登录*/
    public static String IS_AUTO_LOGIN = "IS_AUTO_LOGIN";
    /*自动登录用户名*/
    public static String AUTO_LOGIN_NAME = "AUTO_LOGIN_NAME";
    /*自动登录密码*/
    public static String AUTO_LOGIN_PASS = "AUTO_LOGIN_PASS";
    /*自动登录用户ID*/
    public static String AUTO_LOGIN_ID="AUTO_LOGIN_ID";
    /* web activity 传参数*/
    public static String WEB_INTENT = "WEB_INTENT_Bundle";
    /* web activity 传参数*/
    public static String WEB_INTENT_MODE = "WEB_INTENT_MODE_Bundle";
    /* web activity 传参数*/
    public static String WEB_INTENT_CONTENT = "WEB_INTENT_CONTENT_Bundle";
    /*百度百科前缀*/
    public static String BAIKE = "https://baike.baidu.com/item/";
    public static String ACTION = "com.ldx.mygraduationproject.LOCAL_BROADCAST";
    public static String DATE2 = "{\n" +
            "        \"id\": 15,\n" +
            "        \"createDate\": \"2018-08-31 01:02:29\",\n" +
            "        \"title\": \"冬季易上火，“灭火”之道你知道几个呢？\",\n" +
            "        \"content\": \"  前几天，黄先生和朋友小聚，喝了一些白酒。第二天，他感觉眼睛干涩，照镜子一看，眼睛里还有一些血丝，看上去白眼仁发红。那么冬季上火吃什么好呢? 对此，著名中医养生专家梅晓芳认为，...\",\n" +
            "        \"classes\": \"1\",\n" +
            "        \"articleDate\": null,\n" +
            "        \"articleUrl\": \"https://www.jianshu.com/p/9ed1b0b950fa\",\n" +
            "        \"author\": \"993\",\n" +
            "        \"img\": null,\n" +
            "        \"status\": \"1\"\n" +
            "      }";
}
