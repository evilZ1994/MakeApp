package com.example.lollipop.makeupapp.ui.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.app.AppManager;
import com.example.lollipop.makeupapp.bean.bmob.Schedule;
import com.example.lollipop.makeupapp.bean.bmob.User;
import com.example.lollipop.makeupapp.bean.realm.ScheduleDelete;
import com.example.lollipop.makeupapp.bean.realm.ScheduleRealm;
import com.example.lollipop.makeupapp.ui.adapter.MyFragmentPagerAdapter;
import com.example.lollipop.makeupapp.ui.base.BaseActivity;
import com.example.lollipop.makeupapp.ui.fragment.CommunityFragment;
import com.example.lollipop.makeupapp.ui.fragment.HomeFragment;
import com.example.lollipop.makeupapp.ui.fragment.ScheduleFragment;
import com.example.lollipop.makeupapp.util.BmobRealmTransUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity {
    private List<Fragment> fragmentList = new ArrayList<>();
    private FragmentPagerAdapter fragmentPagerAdapter;
    private MenuItem menuItem;

    private CommunityFragment communityFragment;
    private HomeFragment homeFragment;
    private ScheduleFragment scheduleFragment;

    private NotificationManager syncNotificationManager;
    private Notification syncNotification;
    private Realm realm;
    private User currentUser;

    //点击返回键时的时间，用于控制双击退出
    private long mPressedTime = 0;

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        currentUser = User.getCurrentUser(User.class);

        communityFragment = new CommunityFragment();
        homeFragment = new HomeFragment();
        scheduleFragment = new ScheduleFragment();
        fragmentList.add(communityFragment);
        fragmentList.add(scheduleFragment);
        fragmentList.add(homeFragment);

        fragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(fragmentPagerAdapter);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.community:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.schedule:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.me:
                        viewPager.setCurrentItem(2);
                        break;
                }
                return false;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null){
                    menuItem.setChecked(false);
                }else {
                    menuItem = bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //同步数据
        syncData();
    }

    /**
     * 目前主要是同步计划表
     */
    private void syncData() {
        syncNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        syncNotification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setWhen(System.currentTimeMillis())
                .setContentTitle("同步")
                .setContentText("正在同步数据...")
                .setOngoing(true)//无法手动取消
                .build();
        syncNotificationManager.notify("sync", 1, syncNotification);

        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                realm = Realm.getDefaultInstance();
                RealmQuery<ScheduleRealm> query = realm.where(ScheduleRealm.class);
                query.equalTo("userId", currentUser.getObjectId());
                query.equalTo("needUpdate", true);
                RealmResults<ScheduleRealm> scheduleRealms1 = query.findAll();
                for (ScheduleRealm scheduleRealm : scheduleRealms1){
                    if (scheduleRealm.getObjectId() == null){
                        //添加到服务器
                        Schedule schedule = BmobRealmTransUtil.schedule(scheduleRealm);
                        String objectId = schedule.saveSync();
                        if (objectId != null){
                            realm.beginTransaction();
                            scheduleRealm.setObjectId(objectId);
                            realm.commitTransaction();
                        }
                    }else {
                        //更新服务器数据
                        Schedule schedule = BmobRealmTransUtil.schedule(scheduleRealm);
                        schedule.setObjectId(scheduleRealm.getObjectId());
                        schedule.updateSync(scheduleRealm.getObjectId());
                    }
                }
                //检查是否有本地已删除，但服务器未删除的计划
                RealmQuery<ScheduleDelete> deleteQuery = realm.where(ScheduleDelete.class);
                query.equalTo("userId", currentUser.getObjectId());
                RealmResults<ScheduleDelete> deletes = deleteQuery.findAll();
                for (final ScheduleDelete delete : deletes){
                    Schedule schedule = new Schedule();
                    schedule.setObjectId(delete.getObjectId());
                    schedule.delete(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                realm.beginTransaction();
                                delete.deleteFromRealm();
                                realm.commitTransaction();
                            }
                        }
                    });
                }
                //同步服务器数据到本地
                BmobQuery<Schedule> bmobQuery = new BmobQuery<Schedule>();
                bmobQuery.addWhereEqualTo("user", currentUser);
                List<Schedule> schedules = bmobQuery.findObjectsSync(Schedule.class);
                List<String> bmobObjectIds = new ArrayList<String>();
                RealmQuery<ScheduleRealm> realmQuery = realm.where(ScheduleRealm.class);
                realmQuery.equalTo("userId", currentUser.getObjectId());
                RealmResults<ScheduleRealm> scheduleRealms = realmQuery.findAll();
                List<String> realmObjectIds = new ArrayList<String>();
                for (Schedule schedule : schedules){
                    bmobObjectIds.add(schedule.getObjectId());
                }
                for (ScheduleRealm scheduleRealm : scheduleRealms){
                    realmObjectIds.add(scheduleRealm.getObjectId());
                }
                for (String objectId : bmobObjectIds){
                    if(!realmObjectIds.contains(objectId)){
                        ScheduleRealm scheduleRealm = BmobRealmTransUtil.schedule(schedules.get(bmobObjectIds.indexOf(objectId)));
                        realm.beginTransaction();
                        realm.copyToRealm(scheduleRealm);
                        realm.commitTransaction();
                    }
                }
                subscriber.onNext("done");
                subscriber.onCompleted();
            }
        });

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(String s) {
                syncNotificationManager.cancel("sync", 1);
            }
        };

        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    /**
     * 重写onBackPressed()方法，设置双击退出，时间差500毫秒
     */
    @Override
    public void onBackPressed() {
        long mNowTime = System.currentTimeMillis();
        if (mNowTime - mPressedTime > 500){
            Toast.makeText(this, "再次点击退出", Toast.LENGTH_SHORT).show();
            mPressedTime = mNowTime;
        } else {
            //做一些退出操作
            AppManager.getInstance().AppExit(this);
        }
    }
}
