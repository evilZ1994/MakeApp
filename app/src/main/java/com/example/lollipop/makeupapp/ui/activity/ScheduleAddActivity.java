package com.example.lollipop.makeupapp.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TimePicker;

import com.example.lollipop.makeupapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduleAddActivity extends AppCompatActivity {

    @BindView(R.id.time_picker_start)
    TimePicker timePickerStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_add);

        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        timePickerStart.setIs24HourView(true);

    }
}
