package com.example.lollipop.makeupapp.broadcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.bean.realm.ScheduleRealm;
import com.example.lollipop.makeupapp.util.AlarmUtil;
import com.example.lollipop.makeupapp.util.DateFormatUtil;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmQuery;

/**
 * Created by Lollipop on 2017/9/15.
 */

public class AlarmReceiver extends BroadcastReceiver {
    private NotificationManager notificationManager;
    private Notification notification;
    private Realm realm;

    public AlarmReceiver(){
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("receiver", "received");
        String title = intent.getStringExtra("title");
        String time = intent.getStringExtra("time");
        int requestCode = intent.getIntExtra("requestCode", 0);
        String remindWay = intent.getStringExtra("remindWay");
        String repeatMode = intent.getStringExtra("repeatMode");

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setWhen(System.currentTimeMillis())
                .setContentTitle("计划")
                .setContentText(title+"  "+time);
        switch (remindWay){
            case "响铃":
                builder.setDefaults(Notification.DEFAULT_SOUND);
                break;
            case "振动":
                builder.setDefaults(Notification.DEFAULT_VIBRATE);
                break;
            default:
                builder.setDefaults(Notification.DEFAULT_ALL);
                break;
        }

        notification = builder.build();
        notificationManager.notify(requestCode, notification);

        if (repeatMode.equals("仅一次")){
            String objectId = intent.getStringExtra("objectId");
            if (objectId != null){
                RealmQuery<ScheduleRealm> query = realm.where(ScheduleRealm.class);
                query.equalTo("objectId", objectId);
                ScheduleRealm scheduleRealm = query.findFirst();
                realm.beginTransaction();
                scheduleRealm.setOpen(false);
                realm.commitTransaction();
            }else {
                String userId = intent.getStringExtra("userId");
                String createTime = intent.getStringExtra("createTime");
                RealmQuery<ScheduleRealm> query = realm.where(ScheduleRealm.class);
                query.equalTo("userId", userId);
                query.equalTo("createTime", DateFormatUtil.toDate(createTime));
                ScheduleRealm scheduleRealm = query.findFirst();
                realm.beginTransaction();
                scheduleRealm.setOpen(false);
                realm.commitTransaction();
            }
        }
    }
}
