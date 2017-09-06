package com.example.lollipop.makeupapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by R2D2 on 2017/9/6.
 */

public class DateFormatUtil {
    private static String format1 = "yyyy-MM-dd HH:mm:ss";

    public static Date format1(String s){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format1);
        try {
            return dateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
