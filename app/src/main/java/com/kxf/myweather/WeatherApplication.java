package com.kxf.myweather;


import android.app.Application;
import android.app.Service;
import android.os.Vibrator;

import com.baidu.mapapi.SDKInitializer;

import org.xutils.x;

/**
 */
public class WeatherApplication extends Application {
    public LocationService locationService;
    public Vibrator mVibrator;

    @Override
    public void onCreate() {
        super.onCreate();
        /***
         * 初始化定位sdk，建议在Application中创建//http://wthrcdn.etouch.cn/weather_mini?city=北京

         */
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        WriteLog.getInstance().init(); // 初始化日志
        SDKInitializer.initialize(getApplicationContext());
//xutils

        x.Ext.init(this);
        x.Ext.setDebug(true); // 是否输出debug日志
    }
}
