package com.kxf.myweather;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 */
public class WebWeather extends Activity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        setContentView(webView);
        webView.loadUrl("http://m.weather.com.cn/mweather15d/101010100.shtml");
    }
}
