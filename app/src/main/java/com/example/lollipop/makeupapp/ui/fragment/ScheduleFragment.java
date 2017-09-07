package com.example.lollipop.makeupapp.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.ui.activity.ScheduleAddActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {

    @OnClick(R.id.btn)
    void btn(){
        startActivity(new Intent(getContext(), ScheduleAddActivity.class));
    }


    public ScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

}
