package com.example.lollipop.makeupapp.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.lollipop.makeupapp.R;
import com.example.lollipop.makeupapp.bean.bmob.Schedule;
import com.example.lollipop.makeupapp.bean.bmob.User;
import com.example.lollipop.makeupapp.bean.realm.ScheduleRealm;
import com.example.lollipop.makeupapp.ui.activity.ScheduleAddActivity;
import com.example.lollipop.makeupapp.ui.adapter.ScheduleItemRecyclerAdapter;
import com.example.lollipop.makeupapp.util.Codes;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class ScheduleClassificationFragment extends Fragment {
    private ScheduleItemRecyclerAdapter adapter;
    private List<ScheduleRealm> scheduleRealms;
    private Realm realm;
    private User currentUser;
    RealmResults<ScheduleRealm> results;

    private static final String CLASSIFICATION = "classification";
    private static final String POSITION = "position";

    private String classification;
    private int position;

    private OnItemNumChangeListener mListener;

    @BindView(R.id.text)
    AppCompatTextView text;
    @BindView(R.id.item_recycler)
    RecyclerView recyclerView;
    @OnClick(R.id.add_plan)
    void addPlan(){
        //添加监听器控制item的更改

        Intent intent = new Intent(getContext(), ScheduleAddActivity.class);
        intent.putExtra("tag", "insert");
        intent.putExtra("classification", classification);
        startActivityForResult(intent, Codes.SCHEDULE_ADD_REQUEST_CODE);
    }

    public ScheduleClassificationFragment() {
        // Required empty public constructor
    }

    public static ScheduleClassificationFragment newInstance(String classification, int position) {
        ScheduleClassificationFragment fragment = new ScheduleClassificationFragment();
        Bundle args = new Bundle();
        args.putString(CLASSIFICATION, classification);
        args.putInt(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            classification = getArguments().getString(CLASSIFICATION);
            position = getArguments().getInt(POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_classification, container, false);

        ButterKnife.bind(this, view);
        text.setText(classification);
        realm = Realm.getDefaultInstance();
        currentUser = User.getCurrentUser(User.class);
        initView();

        return view;
    }

    private void initView() {
        scheduleRealms = new ArrayList<>();
        adapter = new ScheduleItemRecyclerAdapter(getContext(), scheduleRealms);
        adapter.setOnItemClickListener(new OnScheduleItemClickListener());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        readSchedules();
    }

    /**
     * 从数据库读取计划表，并提醒adapter更新数据
     */
    private void readSchedules() {
        RealmQuery<ScheduleRealm> query = realm.where(ScheduleRealm.class);
        query.equalTo("userId", currentUser.getObjectId());
        query.equalTo("classification", classification);
        results = query.findAllAsync();
        results.addChangeListener(new RealmChangeListener<RealmResults<ScheduleRealm>>() {
            @Override
            public void onChange(RealmResults<ScheduleRealm> results) {
                scheduleRealms.clear();
                scheduleRealms.addAll(results);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void numChange(int num) {
        if (mListener != null) {
            mListener.onItemNumChange(position, num);
        }
    }

    public void setOnItemNumChangeListener(OnItemNumChangeListener listener){
        this.mListener = listener;
    }

    public interface OnItemNumChangeListener {
        void onItemNumChange(int position, int num);
    }

    @Override
    public void onStop() {
        super.onStop();
        results.removeAllChangeListeners();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Codes.SCHEDULE_ADD_REQUEST_CODE && resultCode == Codes.SCHEDULE_ADD_RESULT_OK){
            readSchedules();
            //新增一条计划
            numChange(1);
        }else if (requestCode == Codes.SCHEDULE_MODIFY_REQUEST_CODE && resultCode == Codes.SCHEDULE_MODIFY_RESULT_OK){
            adapter.notifyDataSetChanged();
        }
    }

    private class OnScheduleItemClickListener implements ScheduleItemRecyclerAdapter.OnItemClickListener{

        @Override
        public void onItemClick(Intent intent) {
            startActivityForResult(intent, Codes.SCHEDULE_MODIFY_REQUEST_CODE);
        }

        @Override
        public void onItemLongClick(View view, final int position) {
            //长按弹出删除选项
            PopupMenu popupMenu = new PopupMenu(getContext(), view);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu_delete, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    //删除操作
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            ScheduleRealm scheduleRealm = scheduleRealms.remove(position);
                            String objectId = scheduleRealm.getObjectId();
                            scheduleRealm.deleteFromRealm();
                            //从服务器删除
                            if (objectId != null && objectId.length()>0) {
                                Schedule schedule = new Schedule();
                                schedule.setObjectId(objectId);
                                schedule.delete(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {

                                    }
                                });
                            }
                            numChange(-1);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    return false;
                }
            });
            popupMenu.show();
        }
    }
}
