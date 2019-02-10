package com.ldx.mygraduationproject.constant;

/**
 * Created by Administrator on 2018/2/1.
 */

public class AppConfig {
        //测试类
    public final static String BASE_URL_PATH = "http://192.168.1.104:8100"; //服务器基本地址
    public final static String DELETE_STUDENT_BY_NAME = BASE_URL_PATH.concat("/delete_by_name");
    public final static String INSERT_STUDENT = BASE_URL_PATH.concat("/insert");
    public final static String ADD_MORE_STUDENTS = BASE_URL_PATH.concat("/add_more_students");


//    192.168.124.9
    //192.168.1.104
    //用户类下功能 /userApi
    public final static String SERVER_URL_PATH = "http://192.168.1.104:8100/api";
    public final static String REGISTER_NEW_USER= SERVER_URL_PATH.concat("/security/register_new_user");
    public final static String USER_LOGIN= SERVER_URL_PATH.concat("/security/user_login");
    public final static String RESET_VALID_CODE= SERVER_URL_PATH.concat("/security/reset_valid_code");
    public final static String RESET_USER_PASSWORD= SERVER_URL_PATH.concat("/security/reset_user_password");
    //步数类下的功能
    //计划类
    public final static String GET_USER_STEP_PLAN_BY_USERNAME=SERVER_URL_PATH.concat("/stepplan/get_user_step_plan_by_username");
    public final static String ADD_USER_PLAN=SERVER_URL_PATH.concat("/stepplan/add_user_plan");
    //步数类
    public final static String FIND_BY_CURDATE=SERVER_URL_PATH.concat("/stepcount/find_by_curDate");
    //心率类
    public final static String ADD_USER_HEART_RATE=SERVER_URL_PATH.concat("/heartrate/add_user_heart_rate");
    public final static String GET_USER_HEART_RATE_BY_USER_NAME=SERVER_URL_PATH.concat("/heartrate/get_user_heart_rate_by_user_name");
    //计步类
    public static final int MSG_FROM_CLIENT = 0;
    public static final int MSG_FROM_SERVER = 1;
    //文章
    public static final String GET_ALL_ARTICLE=SERVER_URL_PATH.concat("/article/get_all_article");
    /*是否自动登录*/
    public static String IS_AUTO_LOGIN = "IS_AUTO_LOGIN";
    /*自动登录用户名*/
    public static String AUTO_LOGIN_NAME = "AUTO_LOGIN_NAME";
    /*自动登录密码*/
    public static String AUTO_LOGIN_PASS = "AUTO_LOGIN_PASS";


    /* web activity 传参数*/
    public static String WEB_INTENT = "WEB_INTENT_Bundle";
    /* web activity 传参数*/
    public static String WEB_INTENT_MODE = "WEB_INTENT_MODE_Bundle";
    /* web activity 传参数*/
    public static String WEB_INTENT_CONTENT = "WEB_INTENT_CONTENT_Bundle";
    /*百度百科前缀*/
    public static String BAIKE = "https://baike.baidu.com/item/";

    public static String DATE = "{\n" +
            "        \"id\": 14,\n" +
            "        \"createDate\": \"2018-09-08 14:12:27\",\n" +
            "        \"title\": \"高血压的危害与预防你知道多少？\",\n" +
            "        \"content\": \"   中风：高血压脑卒中的发生率是正常血压的7.76倍。    心力衰竭：高血压常见的并发症，40%-50%的心衰起因是高血压造成。    高血压导致左心室肥厚和心肌梗塞，可...\",\n" +
            "        \"classes\": \"1\",\n" +
            "        \"articleDate\": null,\n" +
            "        \"articleUrl\": \"https://www.jianshu.com/p/82d47b02a033\",\n" +
            "        \"author\": \"993\",\n" +
            "        \"img\": null,\n" +
            "        \"status\": \"1\"\n" +
            "      }";
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
