package com.example.lollipop.makeupapp.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.ArrayAdapter;
import android.widget.TimePicker;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.app.AppManager;
import com.example.lollipop.makeupapp.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScheduleAddActivity extends BaseActivity {

    private ArrayAdapter<String> repeatAdapter;

    private String[] repeatWays = {
            "每天", "仅一次", "周一至周五", "自定义"
    };

    @BindView(R.id.time_picker_start)
    TimePicker timePickerStart;
    @BindView(R.id.time_picker_end)
    TimePicker timePickerEnd;
    @BindView(R.id.spinner_repeat)
    AppCompatSpinner spinnerRepeat;
    @OnClick(R.id.back)
    void back(){
        AppManager.getInstance().finishActivity();
    }
    @OnClick(R.id.finish)
    void done(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_add);

        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        timePickerStart.setIs24HourView(true);
        timePickerEnd.setIs24HourView(true);

        repeatAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        repeatAdapter.addAll(repeatWays);
        repeatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRepeat.setAdapter(repeatAdapter);
    }
}
