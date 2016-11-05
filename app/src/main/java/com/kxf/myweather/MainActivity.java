package com.kxf.myweather;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

import net.youmi.android.AdManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class MainActivity extends Activity implements View.OnClickListener {
    private LinearLayout addressChange;
    private LocationService locationService;
    private TextView wendu, txtSubject, fengXiang, fengLi, jinWen, jinType, mingWen, mingType;
    private TextView showAdd, houType, houWen;

    @Override
    protected void onStart() {
        super.onStart();
        locationService = ((WeatherApplication) getApplication()).locationService;
        locationService.registerListener(bdLocationListener);
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
    }

    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AdManager.getInstance(this).init("f3f2ca49023ea743","69ae437c07870eeb",false);

        setContentView(R.layout.activity_main);

        utils = new Utils();
        if (utils.isNetworkConnected(this)) {
            utils.showDialogForLoading(this, "正在获取天气信息", true);
            city = "朝阳区";
            logMsg("朝阳区");
        } else {
            Toast.makeText(this, "当前无网络连接", Toast.LENGTH_SHORT).show();
        }
        wendu = (TextView) findViewById(R.id.main_du);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/shaonv.ttf");
        wendu.setTypeface(face);
        wendu.setText("29°");
        TextView textView = (TextView) findViewById(R.id.main_go_fif);
        addressChange = (LinearLayout) findViewById(R.id.main_address);
        jinType = (TextView) findViewById(R.id.jin_type);
        jinWen = (TextView) findViewById(R.id.jin_wendu);
        mingType = (TextView) findViewById(R.id.ming_type);
        mingWen = (TextView) findViewById(R.id.ming_wendu);
        houType = (TextView) findViewById(R.id.hou_type);
        houWen = (TextView) findViewById(R.id.hou_wendu);
        fengLi = (TextView) findViewById(R.id.fengli);
        fengXiang = (TextView) findViewById(R.id.fengxiang);
        txtSubject = (TextView) findViewById(R.id.main_subject);
        addressChange.setOnClickListener(this);
        showAdd = (TextView) findViewById(R.id.mian_add_txt);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WebWeather.class));

            }
        });

    }

    /***
     * Stop location service
     */
    @Override
    protected void onStop() {
        locationService.unregisterListener(bdLocationListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_address:
                if (utils.isNetworkConnected(this)) {
                    utils.showDialogForLoading(this, "正在获取天气信息", true);
                    txtSubject.setText("正在获取....");
                    locationService.start();// 定位SDK

                } else {
                    if (locationService != null)
                        locationService.stop();
                    Toast.makeText(this, "当前无网络连接", Toast.LENGTH_SHORT).show();
                }

                break;
        }

    }

    String city;
    BDLocationListener bdLocationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                int rcode = location.getLocType();
                if (rcode == 161 || rcode == 61) {
                    city = location.getDistrict();
                    logMsg(city);
                }
            }


        }
    };

    /**
     * 显示请求字符串
     */
    public void logMsg(String str) {
        str = str.substring(0, str.length() - 1);
        RequestParams params = new RequestParams("http://wthrcdn.etouch.cn/weather_mini");
        params.addQueryStringParameter("city", str);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                utils.hideDialogForLoading();
//                Log.i("kxf", resu lt);
                try {
                    showData(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                utils.hideDialogForLoading();
                Toast.makeText(MainActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                Log.i("kxf", "onError");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                utils.hideDialogForLoading();
                Log.i("kxf", "onCancelled");
            }

            @Override
            public void onFinished() {
                utils.hideDialogForLoading();
                Log.i("kxf", "onFinished");
            }
        });
        if (locationService != null)
            locationService.stop();
//        locationService.stop();
    }

    public void showData(String str) throws Exception {
        String maxDegrees, minDegrees, nowDegrees, subject, wend, fengli, wetherType;

        JSONObject jsonObject = new JSONObject(str);
        int code = jsonObject.optInt("status");
        if (code != 1000)
            return;
        JSONObject twoObj = jsonObject.optJSONObject("data");
        JSONArray threArray = twoObj.optJSONArray("forecast");
        nowDegrees = twoObj.optString("wendu");
        subject = twoObj.optString("ganmao");
        JSONObject fourObj;
        fourObj = threArray.getJSONObject(0);
        str = fourObj.getString("type");
        maxDegrees = fourObj.getString("high");
        maxDegrees = maxDegrees.substring(3, 6);
        minDegrees = fourObj.getString("low");
        minDegrees = minDegrees.substring(3, 6);
        wetherType = fourObj.getString("type");
        wend = fourObj.getString("fengxiang");
        fengli = fourObj.getString("fengli");
        txtSubject.setText(subject);
        wendu.setText(nowDegrees + "°");
        showAdd.setText(city + " / " + str);
        fengXiang.setText(wend);
        fengLi.setText(fengli);
        jinWen.setText(minDegrees + "/" + maxDegrees);
        jinType.setText(wetherType);
        fourObj = threArray.getJSONObject(1);
        maxDegrees = fourObj.getString("high");
        maxDegrees = maxDegrees.substring(3, 6);
        minDegrees = fourObj.getString("low");
        minDegrees = minDegrees.substring(3, 6);
        wetherType = fourObj.getString("type");
        mingWen.setText(minDegrees + "/" + maxDegrees);
        mingType.setText(wetherType);
        fourObj = threArray.getJSONObject(2);
        maxDegrees = fourObj.getString("high");
        maxDegrees = maxDegrees.substring(3, 6);
        minDegrees = fourObj.getString("low");
        minDegrees = minDegrees.substring(3, 6);
        wetherType = fourObj.getString("type");
        houWen.setText(minDegrees + "/" + maxDegrees);
        houType.setText(wetherType);

    }


}
