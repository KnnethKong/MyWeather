package com.kxf.myweather.drawview;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.kxf.myweather.LocationService;
import com.kxf.myweather.R;
import com.kxf.myweather.SignListActivity;
import com.kxf.myweather.Utils;
import com.kxf.myweather.WeatherApplication;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description:[功能描述]
 * @Author:孔祥发
 */
public class SignActivity extends Activity implements View.OnClickListener {
    private Button signOn;
    private String longTime, shorttime, addr;
    private ListView listMan;
    private LocationService locationService;
    private Utils utils;
    public TextView localAdr;
    private ProgressDialog progressDialog;
    private double latitude, longitude, officelat, officelong;

    private class MyHandler extends Handler {
        private final WeakReference<SignActivity> mActivity;

        public MyHandler(SignActivity activity) {
            mActivity = new WeakReference<SignActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 13:
                    progressDialog.dismiss();
                    String[] str = msg.obj.toString().split("-");
                    localAdr.setText(str[0]);
                    saveData();
                    boolean isDis = GetDistanceTwo(officelat, officelong, latitude, longitude);
                    if (isDis) {
                        Toast.makeText(SignActivity.this, "成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignActivity.this, "超出范围,打卡失败", Toast.LENGTH_SHORT).show();

                    }

                    break;
            }
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        onCreate(null);
//
//    }

    private final MyHandler mHandler = new MyHandler(this);

    @Override
    protected void onStart() {
        super.onStart();
        locationService = ((WeatherApplication) getApplication()).locationService;
        locationService.registerListener(mListener);
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_layout);

        signOn = (Button) findViewById(R.id.sign_signbtn);
        listMan = (ListView) findViewById(R.id.sign_allman);
        TextView txtOder = (TextView) findViewById(R.id.sing_txt_older);
        txtOder.setOnClickListener(this);
        signOn.setOnClickListener(this);
        localAdr = (TextView) findViewById(R.id.sign_txt_map);
        TextView tDate = (TextView) findViewById(R.id.sign_txt_date);
        Date date = new Date();
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制
        longTime = sdformat.format(date);
        shorttime = longTime.substring(0, 10);
        tDate.setText(shorttime);
        utils = new Utils();
        officelat = 39.9892250000;
        officelong = 116.3935690000;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_signbtn:
                showMap();
                break;
            case R.id.sing_txt_older:
                startActivity(new Intent(SignActivity.this, SignListActivity.class));
                break;
        }
    }

    /***
     * Stop location service
     */
    @Override
    protected void onStop() {
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
        utils = null;
        listMan = null;
        longTime = null;
        shorttime = null;
    }

    private void checkNet() {
        Utils utils = new Utils();
        int code = utils.NetworkType(this);
        String str = "走错路了";
        if (code == 3) {
            str = "没有网，你还想打卡做梦呢啊";
        }
        if (code == 1) {
            str = "你的钱真多，用流量打卡";
        }
        if (code == 2) {
            WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            str = "连接的wifi" + info.toString();
        }

    }

    private void showMap() {
        int code = utils.NetworkType(this);
        if (code == 2) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("转啊转啊转...");
            progressDialog.show();
            locationService.start();// 定位SDK
        } else {
            Toast.makeText(this, "请连接wifi", Toast.LENGTH_SHORT).show();
        }

    }

    private void saveData() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.sign_addlinear);
        TextView addTxt = new TextView(this);
        addTxt.setText(longTime + "：" + addr);
        linearLayout.addView(addTxt);
        ContentValues contentValues = null;
        SQLiteDatabase sqldb = openOrCreateDatabase("kxfzjy.db", Context.MODE_PRIVATE, null);
//      sqldb.execSQL("DROP TABLE IF EXISTS person");
        sqldb.execSQL("create table if not exists  signdb (_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, stime VARCHAR,sadress VARCHAR)");
        for (int i = 0; i < 6; i++) {
            contentValues = new ContentValues();
            contentValues.put("name", "kong");
            contentValues.put("stime", longTime);
            contentValues.put("sadress", addr);
            sqldb.insert("signdb", null, contentValues);
        }
        sqldb.close();
    }

    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                int rcode = location.getLocType();
                if (rcode == 161 || rcode == 61) {
                    String city = location.getCity();
                }
                String city = location.getCity();
                addr = location.getAddrStr();
                addr = addr.substring(2, addr.length());
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Log.i("kxf", "latitude:" + latitude + "longitude:" + longitude);
                Poi poi = null;
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    poi = (Poi) location.getPoiList().get(0);
                }
                Message message = new Message();
                message.obj = city + "·" + poi.getName() + "-" + addr;
                message.what = 13;
                mHandler.sendMessage(message);
                if (location.getLocType() == BDLocation.TypeServerError) {
                    //服务端网络定位失败
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    //请检查网络是否通畅
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    //重启手机
                }
            }
            locationService.stop();

        }

    };
    private double EARTH_RADIUS = 6378137;//赤道半径(单位m)

    private double rad(double d) {
        return d * Math.PI / 180.0;
    }

    private void checkNetIP() {


    }

    private boolean GetDistanceTwo(double lon1, double lat1, double lon2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        Log.i("kxf", "s:" + s);
//        s = s / 1000;
        if (s > 100) {
            return false;
        } else {
            return true;
        }
//        DecimalFormat df = new DecimalFormat("#.00");
//        df.format(s);

    }


}
