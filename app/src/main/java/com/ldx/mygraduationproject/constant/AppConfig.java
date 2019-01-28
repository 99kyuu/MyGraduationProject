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



    //用户类下功能 /userApi
    public final static String SERVER_URL_PATH = "http://192.168.1.104:8100/api/security";
    public final static String REGISTER_NEW_USER= SERVER_URL_PATH.concat("/register_new_user");
    public final static String USER_LOGIN= SERVER_URL_PATH.concat("/user_login");
    public final static String RESET_VALID_CODE= SERVER_URL_PATH.concat("/reset_valid_code");
    public final static String RESET_USER_PASSWORD= SERVER_URL_PATH.concat("/reset_user_password");


    //计步类
    public static final int MSG_FROM_CLIENT = 0;
    public static final int MSG_FROM_SERVER = 1;
}
