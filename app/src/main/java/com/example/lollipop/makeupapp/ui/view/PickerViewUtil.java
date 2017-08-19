package com.example.lollipop.makeupapp.ui.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bigkoo.pickerview.OptionsPickerView;
import com.example.lollipop.makeupapp.app.App;
import com.example.lollipop.makeupapp.bean.bmob.User;
import com.example.lollipop.makeupapp.bean.local.AreaBean;
import com.example.lollipop.makeupapp.bean.local.CityBean;
import com.example.lollipop.makeupapp.bean.local.ProvinceBean;
import com.example.lollipop.makeupapp.util.DBManager;

import java.util.ArrayList;

import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Lollipop on 2017/8/16.
 */

public class PickerViewUtil {
    private  OptionsPickerView pvOptions;//地址选择器
    private  ArrayList<ProvinceBean> options1Items = new ArrayList<>();//省
    private  ArrayList<ArrayList<CityBean>> options2Items = new ArrayList<>();//市
    private  ArrayList<ArrayList<ArrayList<AreaBean>>> options3Items = new ArrayList<>();//区
    private  ArrayList<String> Provincestr = new ArrayList<>();//省
    private  ArrayList<ArrayList<String>> Citystr = new ArrayList<>();//市
    private  ArrayList<ArrayList<ArrayList<String>>> Areastr = new ArrayList<>();//区

    public void showAddressPickerView(Context context, final UpdateListener listener, final ProgressDialog progressDialog){
        //选项选择器
        pvOptions = new OptionsPickerView(context);
        // 获取数据库
        SQLiteDatabase db = DBManager.getdb(App.getApplication());
        //省
        Cursor cursor = db.query("province", null, null, null, null, null,
                null);
        while (cursor.moveToNext()) {
            int pro_id = cursor.getInt(0);
            String pro_code = cursor.getString(1);
            String pro_name = cursor.getString(2);
            String pro_name2 = cursor.getString(3);
            ProvinceBean provinceBean = new ProvinceBean(pro_id, pro_code, pro_name, pro_name2);
            options1Items.add(provinceBean);//添加一级目录
            Provincestr.add(cursor.getString(2));
            //查询二级目录，市区
            Cursor cursor1 = db.query("city", null, "province_id=?", new String[]{pro_id + ""}, null, null,
                    null);
            ArrayList<CityBean> cityBeanList = new ArrayList<>();
            ArrayList<String> cityStr = new ArrayList<>();
            //地区集合的集合（注意这里要的是当前省份下面，当前所有城市的地区集合我去）
            ArrayList<ArrayList<AreaBean>> options3Items_03 = new ArrayList<>();
            ArrayList<ArrayList<String>> options3Items_str = new ArrayList<>();
            while (cursor1.moveToNext()) {
                int cityid = cursor1.getInt(0);
                int province_id = cursor1.getInt(1);
                String code = cursor1.getString(2);
                String name = cursor1.getString(3);
                String provincecode = cursor1.getString(4);
                CityBean cityBean = new CityBean(cityid, province_id, code, name, provincecode);
                //添加二级目录
                cityBeanList.add(cityBean);
                cityStr.add(cursor1.getString(3));
                //查询三级目录
                Cursor cursor2 = db.query("area", null, "city_id=?", new String[]{cityid + ""}, null, null,
                        null);
                ArrayList<AreaBean> areaBeanList = new ArrayList<>();
                ArrayList<String> areaBeanstr = new ArrayList<>();
                while (cursor2.moveToNext()) {
                    int areaid = cursor2.getInt(0);
                    int city_id = cursor2.getInt(1);
//                    String code0=cursor2.getString(2);
                    String areaname = cursor2.getString(3);
                    String citycode = cursor2.getString(4);
                    AreaBean areaBean = new AreaBean(areaid, city_id, areaname, citycode);
                    areaBeanList.add(areaBean);
                    areaBeanstr.add(cursor2.getString(3));
                }
                cursor2.close();
                options3Items_str.add(areaBeanstr);//本次查询的存储内容
                options3Items_03.add(areaBeanList);
            }
            cursor1.close();
            options2Items.add(cityBeanList);//增加二级目录数据
            Citystr.add(cityStr);
            options3Items.add(options3Items_03);//添加三级目录
            Areastr.add(options3Items_str);
        }
        cursor.close();
        //设置三级联动效果
        pvOptions.setPicker(Provincestr, Citystr, Areastr, true);
        //设置选择的三级单位
//        pvOptions.setLabels("省", "市", "区");
        pvOptions.setTitle("选择城市");
        //设置是否循环滚动
        pvOptions.setCyclic(false, false, false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        pvOptions.setSelectOptions(0, 0, 0);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                progressDialog.show();
                //返回的分别是三个级别的选中位置
                String province = options1Items.get(options1).getPro_name();
                String city = "";
                //四大直辖市city为空
                if (!province.equals("北京市") && !province.equals("天津市") && !province.equals("上海市") && !province.equals("重庆市")){
                    city = options2Items.get(options1).get(option2).getName();
                }
                String area = options3Items.get(options1).get(option2).get(options3).getName();
                if (area.equals("市辖区")){
                    area = "";
                }
                String location = province+city+area;
                User currentUser = User.getCurrentUser(User.class);
                if (currentUser.getLocation()!=null && currentUser.getLocation().equals(location)){
                    //do nothing
                }else {
                    User newUser = new User();
                    newUser.setLocation(location);
                    newUser.update(currentUser.getObjectId(), listener);
                }
            }
        });
        pvOptions.show();
    }
}
