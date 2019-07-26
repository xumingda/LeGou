package com.xq.LegouShop.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xq.LegouShop.R;
import com.xq.LegouShop.base.BaseActivity;
import com.xq.LegouShop.base.MyVolley;
import com.xq.LegouShop.bean.City;
import com.xq.LegouShop.bean.MyRegion;
import com.xq.LegouShop.protocol.GetProvinceListProtocol;
import com.xq.LegouShop.protocol.GetRegionListProtocol;
import com.xq.LegouShop.request.GetRegionListRequest;
import com.xq.LegouShop.response.GetAreaListResponse;
import com.xq.LegouShop.response.GetCityListResponse;
import com.xq.LegouShop.response.GetProvinceListResponse;
import com.xq.LegouShop.response.GetRegionListResponse;
import com.xq.LegouShop.util.DialogUtils;
import com.xq.LegouShop.util.LogUtils;


import java.util.ArrayList;

/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/8/16 11:40
 * 城市选择页面
 */
public class RegionSelectActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_top_back;
    private View rootView;
    private TextView tv_ensure;
    private ListView lv_city;
    private ArrayList<MyRegion> regions;

    private CityAdapter adapter;
    private static int PROVINCE = 0x00;
    private static int CITY = 0x01;
    private static int DISTRICT = 0x02;

    private TextView[] tvs = new TextView[3];
    private int[] ids = {R.id.rb_province, R.id.rb_city, R.id.rb_district};

    private City city;
    int last, current;
    private String url;
    private Dialog loadingDialog;
    private GetRegionListResponse getRegionListResponse;
    private String updatecity;
    private String provincecode;
    private String citycode;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.city_select_layout, null);
        setContentView(rootView);
        tv_top_back = (TextView) rootView.findViewById(R.id.tv_top_back);
        tv_ensure = (TextView) rootView. findViewById(R.id.tv_ensure);
        lv_city=(ListView) rootView.findViewById(R.id.lv_city);
        initData();
        return null;
    }


    public void initData() {
        loadingDialog = DialogUtils.createLoadDialog(RegionSelectActivity.this, true);
        tv_top_back.setOnClickListener(this);
        tv_ensure.setOnClickListener(this);
        viewInit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ensure:{
                if(!TextUtils.isEmpty(city.getProvinceCode())&&!TextUtils.isEmpty(city.getCityCode())){
                    updatecity=city.getProvince()+city.getCity()+city.getDistrict();
                    Intent intent=getIntent();
                    intent.putExtra("province", city.getProvinceCode());
                    intent.putExtra("city", city.getCityCode());
                    intent.putExtra("district", city.getDistrictCode());
                    intent.putExtra("region", updatecity);
                    setResult(1, intent);
                    finish();
                    overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                }
                else{
                    if (city.getProvinceCode() == null || city.getProvinceCode().equals("")) {
                        Toast.makeText(RegionSelectActivity.this, "您还没有选择省份",
                                Toast.LENGTH_SHORT).show();
                    }else if (city.getCityCode() == null
                            || city.getCityCode().equals("")) {
                        Toast.makeText(RegionSelectActivity.this, "您还没有选择城市",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
            case R.id.tv_top_back: {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
        }
        if (ids[0] == v.getId()) {
            current = 0;
//            util.initProvince();
//            region_id="1";
            getProvinceList();
            tvs[last].setBackgroundColor(Color.WHITE);
            tvs[current].setBackgroundColor(Color.GRAY);
            last = current;
        } else if (ids[1] == v.getId()) {
            if (city.getProvinceCode() == null || city.getProvinceCode().equals("")) {
                current = 0;
                Toast.makeText(RegionSelectActivity.this, "您还没有选择省份",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            current = 1;
            tvs[last].setBackgroundColor(Color.WHITE);
            tvs[current].setBackgroundColor(Color.GRAY);
            last = current;
        } else if (ids[2] == v.getId()) {
            if (city.getProvinceCode() == null
                    || city.getProvinceCode().equals("")) {
                Toast.makeText(RegionSelectActivity.this, "您还没有选择省份",
                        Toast.LENGTH_SHORT).show();
                current = 0;
                return;
            } else if (city.getCityCode() == null
                    || city.getCityCode().equals("")) {
                Toast.makeText(RegionSelectActivity.this, "您还没有选择城市",
                        Toast.LENGTH_SHORT).show();
                current = 1;
                provincecode=city.getRegionId();
                getCityList();
                return;
            }
            current = 2;
            citycode=city.getRegionId();
            getAreaList();
            tvs[last].setBackgroundColor(Color.WHITE);
            tvs[current].setBackgroundColor(Color.GRAY);
            last = current;
        }

    }

    class CityAdapter extends ArrayAdapter<MyRegion> {

        LayoutInflater inflater;

        public CityAdapter(Context con) {
            super(con, 0);
            inflater = LayoutInflater.from(RegionSelectActivity.this);
        }

        @Override
        public View getView(int arg0, View v, ViewGroup arg2) {
            v = inflater.inflate(R.layout.city_item, null);
            TextView tv_city = (TextView) v.findViewById(R.id.tv_city);
            tv_city.setText(getItem(arg0).getName());
            return v;
        }

        public void update() {
            this.notifyDataSetChanged();
        }
    }

    private void viewInit() {

        city = new City();
        Intent in = getIntent();
        city = in.getParcelableExtra("city");


        for (int i = 0; i < tvs.length; i++) {
            tvs[i] = (TextView) findViewById(ids[i]);
            tvs[i].setOnClickListener(this);
        }

        if (city == null) {
            city = new City();
            city.setProvince("");
            city.setCity("");
            city.setDistrict("");
        } else {
            if (city.getProvince() != null && !city.getProvince().equals("")) {
                tvs[0].setText(city.getProvince());
            }
            if (city.getCity() != null && !city.getCity().equals("")) {
                tvs[1].setText(city.getCity());
            }
            if (city.getDistrict() != null && !city.getDistrict().equals("")) {
                tvs[2].setText(city.getDistrict());
            }
        }


        findViewById(R.id.scrollview).setVisibility(View.GONE);


        getProvinceList();
        tvs[current].setBackgroundColor(0xff999999);
        lv_city = (ListView) findViewById(R.id.lv_city);

        regions = new ArrayList<MyRegion>();
        adapter = new CityAdapter(this);
        lv_city.setAdapter(adapter);

    }

    protected void onStart() {
        super.onStart();
        lv_city.setOnItemClickListener(onItemClickListener);
    }

    ;



    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {

            if (current == PROVINCE) {
                String newProvince = regions.get(arg2).getName();
                if (!newProvince.equals(city.getProvince())) {
                    city.setProvince(newProvince);
                    tvs[0].setText(regions.get(arg2).getName());
                    city.setRegionId(regions.get(arg2).getId());
                    city.setProvinceCode(regions.get(arg2).getCode());
                    city.setCityCode("");
                    city.setDistrictCode("");
                    tvs[1].setText("市");
                    tvs[2].setText("区 ");
                }

                current = 1;
                //点击省份列表中的省份就初始化城市列表
                provincecode=city.getProvinceCode();
                LogUtils.e("sendVerificationCodeResponse  provincecode:"+provincecode);
                getCityList();
            } else if (current == CITY) {
                String newCity = regions.get(arg2).getName();
                if (!newCity.equals(city.getCity())) {
                    city.setCity(newCity);
                    tvs[1].setText(regions.get(arg2).getName());
                    city.setRegionId(regions.get(arg2).getId());
                    city.setCityCode(regions.get(arg2).getCode());
                    city.setDistrictCode("");
                    tvs[2].setText("区 ");
                }

                //点击城市列表中的城市就初始化区县列表
                citycode=city.getCityCode();
                getAreaList();
                current = 2;

            } else if (current == DISTRICT) {
                current = 2;
                city.setDistrictCode(regions.get(arg2).getCitycode());
                city.setRegionId(regions.get(arg2).getId());
                city.setDistrict(regions.get(arg2).getName());
                tvs[2].setText(regions.get(arg2).getName());

            }
            tvs[last].setBackgroundColor(Color.WHITE);
            tvs[current].setBackgroundColor(Color.GRAY);
            last = current;
        }
    };
    public void getProvinceList() {
        loadingDialog.show();
        GetProvinceListProtocol getProvinceListProtocol = new GetProvinceListProtocol();
        GetRegionListRequest getRegionListRequest = new GetRegionListRequest();
        url = getProvinceListProtocol.getApiFun();

        MyVolley.uploadNoFile(MyVolley.POST, url, getRegionListRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                GetProvinceListResponse  getProvinceListResponse = gson.fromJson(json, GetProvinceListResponse.class);
                LogUtils.e("sendVerificationCodeResponse:" + json.toString());
                if (getProvinceListResponse.code.equals("0")) {
                    regions.clear();
                    for(int i=0;i<getProvinceListResponse.dataList.size();i++){
                        MyRegion myRegion=new MyRegion();
                        myRegion.setId(getProvinceListResponse.dataList.get(i).id);
                        myRegion.setName(getProvinceListResponse.dataList.get(i).name);
                        myRegion.setCode(getProvinceListResponse.dataList.get(i).code);
                        regions.add(myRegion);
                    }
                    adapter.clear();
                    adapter.addAll(regions);
                    adapter.update();
                    loadingDialog.dismiss();
                } else {
                    loadingDialog.dismiss();
                    DialogUtils.showAlertDialog(RegionSelectActivity.this,
                            getRegionListResponse.getMsg());
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(RegionSelectActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }
    public void getCityList() {
        loadingDialog.show();

        GetRegionListRequest getRegionListRequest = new GetRegionListRequest();
        getRegionListRequest.map.put("provincecode",provincecode);
        url ="/common/getCityList";

        MyVolley.uploadNoFile(MyVolley.POST, url, getRegionListRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                GetCityListResponse getCityListResponse = gson.fromJson(json, GetCityListResponse.class);
                LogUtils.e("getCityListResponseResponse:" + json.toString());
                if (getCityListResponse.code.equals("0")) {
                    regions.clear();
                    for(int i=0;i<getCityListResponse.dataList.size();i++){
                        MyRegion myRegion=new MyRegion();
                        myRegion.setId(getCityListResponse.dataList.get(i).id);
                        myRegion.setName(getCityListResponse.dataList.get(i).name);
                        myRegion.setCode(getCityListResponse.dataList.get(i).code);
                        myRegion.setProvincecode(getCityListResponse.dataList.get(i).provincecode);
                        regions.add(myRegion);
                    }
                    adapter.clear();
                    adapter.addAll(regions);
                    adapter.update();
                    loadingDialog.dismiss();
                } else {
                    loadingDialog.dismiss();
                    DialogUtils.showAlertDialog(RegionSelectActivity.this,
                            getRegionListResponse.getMsg());
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(RegionSelectActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }

    public void getAreaList() {
        loadingDialog.show();

        GetRegionListRequest getRegionListRequest = new GetRegionListRequest();
        getRegionListRequest.map.put("citycode",citycode);
        url ="/common/getAreaList";

        MyVolley.uploadNoFile(MyVolley.POST, url, getRegionListRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                GetAreaListResponse getAreaListResponse = gson.fromJson(json, GetAreaListResponse.class);
                LogUtils.e("getAreaListResponse:" + json.toString());
                if (getAreaListResponse.code.equals("0")) {
                    regions.clear();
                    for(int i=0;i<getAreaListResponse.dataList.size();i++){
                        MyRegion myRegion=new MyRegion();
                        myRegion.setId(getAreaListResponse.dataList.get(i).id);
                        myRegion.setName(getAreaListResponse.dataList.get(i).name);
                        myRegion.setCode(getAreaListResponse.dataList.get(i).code);
                        myRegion.setCitycode(getAreaListResponse.dataList.get(i).citycode);
                        regions.add(myRegion);
                    }
                    adapter.clear();
                    adapter.addAll(regions);
                    adapter.update();
                    loadingDialog.dismiss();
                } else {
                    loadingDialog.dismiss();
                    DialogUtils.showAlertDialog(RegionSelectActivity.this,
                            getRegionListResponse.getMsg());
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(RegionSelectActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }
}
