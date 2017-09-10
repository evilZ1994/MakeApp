package com.example.lollipop.makeupapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by R2D2 on 2017/9/6.
 */

public class DateFormatUtil {
    public static String format1 = "yyyy-MM-dd HH:mm:ss";

    public static Date toDate(String s){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format1);
        try {
            return dateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toStr(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format1);
        return dateFormat.format(date);
    }
}
