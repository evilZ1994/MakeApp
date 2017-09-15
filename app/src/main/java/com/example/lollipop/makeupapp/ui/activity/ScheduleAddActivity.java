package com.example.lollipop.makeupapp.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.app.AppManager;
import com.example.lollipop.makeupapp.bean.bmob.Schedule;
import com.example.lollipop.makeupapp.bean.bmob.User;
import com.example.lollipop.makeupapp.bean.realm.ScheduleRealm;
import com.example.lollipop.makeupapp.ui.base.BaseActivity;
import com.example.lollipop.makeupapp.util.AlarmUtil;
import com.example.lollipop.makeupapp.util.BmobRealmTransUtil;
import com.example.lollipop.makeupapp.util.Codes;
import com.example.lollipop.makeupapp.util.DateFormatUtil;
import com.example.lollipop.makeupapp.util.ScheduleTimeUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class ScheduleAddActivity extends BaseActivity {
    private User currentUser;
    private ProgressDialog progressDialog;

    private String tag;
    private String classification;
    private String title;
    private String startTime;
    private String endTime;
    private String repeatWay;
    private String remindWay;
    private ScheduleRealm scheduleRealm;
    private Realm realm;

    private ArrayAdapter<String> repeatAdapter;
    private ArrayAdapter<String> remindAdapter;
    private String[] repeatWays = {
            "仅一次", "每天", "自定义"
    };
    private String[] remindWays = {
            "响铃", "振动", "响铃+振动"
    };

    @BindView(R.id.content_edit)
    EditText contentEdit;
    @BindView(R.id.time_picker_start)
    TimePicker timePickerStart;
    @BindView(R.id.time_picker_end)
    TimePicker timePickerEnd;
    @BindView(R.id.spinner_repeat)
    AppCompatSpinner spinnerRepeat;
    @BindView(R.id.repeat_way_text)
    AppCompatTextView repeatWayText;
    @BindView(R.id.spinner_remind)
    AppCompatSpinner spinnerRemind;
    @BindView(R.id.remind_way_text)
    AppCompatTextView remindWayText;
    @OnClick(R.id.back)
    void back(){
        AppManager.getInstance().finishActivity();
    }
    @OnClick(R.id.finish)
    void done(){
        progressDialog.show();

        //先保存到服务器，得到objectId后再保存到本地
        title = contentEdit.getText().toString();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            startTime = ScheduleTimeUtil.getStr(timePickerStart.getHour()) + ":" + ScheduleTimeUtil.getStr(timePickerStart.getMinute());
            endTime = ScheduleTimeUtil.getStr(timePickerEnd.getHour()) + ":" + ScheduleTimeUtil.getStr(timePickerEnd.getMinute());
        }else {
            startTime = ScheduleTimeUtil.getStr(timePickerStart.getCurrentHour()) + ":" + ScheduleTimeUtil.getStr(timePickerStart.getCurrentMinute());
            endTime = ScheduleTimeUtil.getStr(timePickerEnd.getCurrentHour()) + ":" + ScheduleTimeUtil.getStr(timePickerEnd.getCurrentMinute());
        }

        if (!tag.equals("modify")){
            //添加数据
            //查找现有的计划表，将计划表的数量作为requestCode
            RealmQuery<ScheduleRealm> query = realm.where(ScheduleRealm.class);
            query.equalTo("userId", currentUser.getObjectId());
            RealmResults<ScheduleRealm> results = query.findAll();
            int requestCode = results.size();

            scheduleRealm.setTitle(title);
            scheduleRealm.setStartTime(startTime);
            scheduleRealm.setEndTime(endTime);
            scheduleRealm.setRepeatMode(repeatWay);
            scheduleRealm.setRemindWay(remindWay);
            scheduleRealm.setClassification(classification);
            scheduleRealm.setUserId(currentUser.getObjectId());
            scheduleRealm.setOpen(true);
            scheduleRealm.setRequestCode(requestCode);
            scheduleRealm.setCreateTime(DateFormatUtil.toDate(DateFormatUtil.toStr(new Date(System.currentTimeMillis()))));
            //保存到服务器
            Schedule schedule = BmobRealmTransUtil.schedule(scheduleRealm);
            schedule.save(new SaveListener<String>() {
                @Override
                public void done(String objectId, BmobException e) {
                    if (e == null) {
                        scheduleRealm.setObjectId(objectId);
                        scheduleRealm.setNeedUpdate(false);
                    }else {
                        //保存失败，记录一下，下次检查时更新到服务器
                        scheduleRealm.setNeedUpdate(true);
                    }
                    //插入新数据
                    realm.beginTransaction();
                    realm.copyToRealm(scheduleRealm);
                    realm.commitTransaction();

                    Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    //开启闹钟
                    AlarmUtil.openAlarm(ScheduleAddActivity.this, scheduleRealm);

                    setResult(Codes.SCHEDULE_ADD_RESULT_OK);
                    AppManager.getInstance().finishActivity();
                }
            });
        }else {
            //修改数据
            realm.beginTransaction();
            scheduleRealm.setTitle(title);
            scheduleRealm.setStartTime(startTime);
            scheduleRealm.setEndTime(endTime);
            scheduleRealm.setRepeatMode(repeatWay);
            scheduleRealm.setRemindWay(remindWay);
            scheduleRealm.setClassification(classification);
            scheduleRealm.setUserId(currentUser.getObjectId());
            scheduleRealm.setOpen(true);
            scheduleRealm.setCreateTime(new Date(System.currentTimeMillis()));
            scheduleRealm.setNeedUpdate(true);
            realm.commitTransaction();
            //修改闹钟
            if (scheduleRealm.isOpen()) {
                AlarmUtil.closeAlarm(this, scheduleRealm.getRequestCode());
                AlarmUtil.openAlarm(this, scheduleRealm);
            }

            String objectId = scheduleRealm.getObjectId();
            //如果服务器有记录，则更新到服务器，否则等检查更新时保存到服务器
            if (objectId != null && objectId.length()>0){
                //保存更新到服务器
                Schedule schedule = BmobRealmTransUtil.schedule(scheduleRealm);
                schedule.setObjectId(objectId);
                schedule.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null){
                            realm.beginTransaction();
                            scheduleRealm.setNeedUpdate(false);
                            realm.commitTransaction();
                        }
                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    }
                });
            }
            Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            setResult(Codes.SCHEDULE_MODIFY_RESULT_OK);
            AppManager.getInstance().finishActivity();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_add);

        ButterKnife.bind(this);

        currentUser = User.getCurrentUser(User.class);

        realm = Realm.getDefaultInstance();
        Intent intent = getIntent();
        classification = intent.getStringExtra("classification");
        tag = intent.getStringExtra("tag");

        initView();

        if (tag.equals("modify")){
            String jsonStr = intent.getStringExtra("schedule");
            Gson gson = new GsonBuilder().setDateFormat(DateFormatUtil.format1).create();
            ScheduleRealm scheduleTemp = gson.fromJson(jsonStr, ScheduleRealm.class);
            RealmQuery<ScheduleRealm> query = realm.where(ScheduleRealm.class);
            if (scheduleTemp.getObjectId()!=null && scheduleTemp.getObjectId().length()>0){
                query.equalTo("objectId", scheduleTemp.getObjectId());
            }else {
                query.equalTo("userId", scheduleTemp.getUserId());
                query.equalTo("createTime", scheduleTemp.getCreateTime());
            }
            scheduleRealm = query.findFirst();
            //填充页面内容
            setupContent();
        }else {
            scheduleRealm = new ScheduleRealm();
        }
    }

    private void initView() {
        progressDialog = new ProgressDialog(this, R.style.ProgressDialogTheme);

        timePickerStart.setIs24HourView(true);
        timePickerEnd.setIs24HourView(true);

        repeatAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        repeatAdapter.addAll(repeatWays);
        repeatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRepeat.setAdapter(repeatAdapter);
        spinnerRepeat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                repeatWay = repeatWays[position];
                repeatWayText.setText(repeatWay);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                repeatWay = repeatWays[0];
                repeatWayText.setText(repeatWay);
            }
        });
        remindAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        remindAdapter.addAll(remindWays);
        remindAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRemind.setAdapter(remindAdapter);
        spinnerRemind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                remindWay = remindWays[position];
                remindWayText.setText(remindWay);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                remindWay = remindWays[0];
                remindWayText.setText(remindWay);
            }
        });
    }

    private void setupContent() {
        contentEdit.setText(scheduleRealm.getTitle());

        String[] startTimes = scheduleRealm.getStartTime().split(":");
        String[] endTimes = scheduleRealm.getEndTime().split(":");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            timePickerStart.setHour(ScheduleTimeUtil.getInt(startTimes[0]));
            timePickerStart.setMinute(ScheduleTimeUtil.getInt(startTimes[1]));
            timePickerEnd.setHour(ScheduleTimeUtil.getInt(endTimes[0]));
            timePickerEnd.setMinute(ScheduleTimeUtil.getInt(endTimes[1]));
        }else {
            timePickerStart.setCurrentHour(ScheduleTimeUtil.getInt(startTimes[0]));
            timePickerStart.setCurrentMinute(ScheduleTimeUtil.getInt(startTimes[1]));
            timePickerEnd.setCurrentHour(ScheduleTimeUtil.getInt(startTimes[0]));
            timePickerEnd.setCurrentMinute(ScheduleTimeUtil.getInt(startTimes[1]));
        }

        spinnerRepeat.setSelection(Arrays.asList(repeatWays).indexOf(scheduleRealm.getRepeatMode()), true);
        spinnerRemind.setSelection(Arrays.asList(remindWays).indexOf(scheduleRealm.getRemindWay()), true);
    }
}
