package com.example.lollipop.makeupapp.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.lollipop.makeupapp.bean.realm.ScheduleRealm;
import com.example.lollipop.makeupapp.broadcast.AlarmReceiver;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Lollipop on 2017/9/15.
 */

public class AlarmUtil {

    public static void openAlarm(Context context, ScheduleRealm scheduleRealm){
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("title", scheduleRealm.getTitle());
        intent.putExtra("time", scheduleRealm.getStartTime()+"-"+scheduleRealm.getEndTime());
        intent.putExtra("requestCode", scheduleRealm.getRequestCode());
        intent.putExtra("repeatMode", scheduleRealm.getRepeatMode());
        intent.putExtra("remindWay", scheduleRealm.getRemindWay());
        intent.putExtra("objectId", scheduleRealm.getObjectId());
        if (scheduleRealm.getObjectId() == null) {
            intent.putExtra("userId", scheduleRealm.getUserId());
            intent.putExtra("createTime", DateFormatUtil.toStr(scheduleRealm.getCreateTime()));
        }
        PendingIntent sender = PendingIntent.getBroadcast(context, scheduleRealm.getRequestCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        String timeStr = scheduleRealm.getStartTime();
        String[] times = timeStr.split(":");
        int hour = ScheduleTimeUtil.getInt(times[0]);
        int minute = ScheduleTimeUtil.getInt(times[1]);
        Calendar cal = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), hour, minute, 0);
        Log.i("calender", calendar.getTime().toString());
        switch (scheduleRealm.getRepeatMode()){
            case "每天":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    Log.i("mode", "每天");
                    alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), null), sender);
                }
                break;
            case "仅一次":
                Log.i("mode", "仅一次");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), null), sender);
                }
                break;
            default:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), null), sender);
                }
                break;
        }
    }

    public static void closeAlarm(Context context, int requestCode){
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
