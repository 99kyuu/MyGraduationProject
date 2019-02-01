package com.ldx.mygraduationproject.utils;



import android.content.Context;

import com.ldx.mygraduationproject.app.MyApplication;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author: ldx
 * @time: 2018/1/29
 * @version: 1.0
 * @exception: 无
 * @explain: 说明
 */
public class FileUtil {

    public static String getFromAssets(String fileName){
        try {

            InputStreamReader inputReader = new InputStreamReader(MyApplication.getInstance().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            StringBuffer result=new StringBuffer();
            while((line = bufReader.readLine()) != null)
                //Result += line;
                result.append(line);
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
