package com.kxf.myweather;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

/**
 * @Description:[功能描述]
 * @Author:孔祥发
 */
public class WifiActivity extends Activity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);
        textView = (TextView) findViewById(R.id.textView1);
        WifiManage wifiManage = new WifiManage();
        try {
            List<MyWifiInfo> wifiInfos = wifiManage.Read();
            textView.setText(wifiInfos.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
