package com.example.lollipop.makeupapp.util;

/**
 * Created by R2D2 on 2017/9/10.
 */

public class ScheduleTimeUtil {

    /**
     * 格式化时间格式，比如0或者2格式化为00或者02
     * @param value hour或者minute
     * @return
     */
    public static String getStr(int value){
        if(value < 10){
            return new StringBuilder().append(0).append(value).toString();
        }else{
            return String.valueOf(value);
        }
    }

    /**
     * 将时间字符串转换成int型， "02"转成2;
     * @param value
     * @return
     */
    public static int getInt(String value){
        if (value.startsWith("0")){
            return Integer.valueOf((String) value.subSequence(0, value.length()));
        }else {
            return Integer.valueOf(value);
        }
    }
}
