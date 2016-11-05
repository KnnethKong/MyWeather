package com.kxf.myweather;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Utils {
    public final static String CoorType_GCJ02 = "gcj02";
    public final static String CoorType_BD09LL = "bd09ll";
    public final static String CoorType_BD09MC = "bd09";
    /***
     * 61 ： GPS定位结果，GPS定位成功。
     * 62 ： 无法获取有效定位依据，定位失败，请检查运营商网络或者wifi网络是否正常开启，尝试重新请求定位。
     * 63 ： 网络异常，没有成功向服务器发起请求，请确认当前测试手机网络是否通畅，尝试重新请求定位。
     * 65 ： 定位缓存的结果。
     * 66 ： 离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果。
     * 67 ： 离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果。
     * 68 ： 网络连接失败时，查找本地离线定位时对应的返回结果。
     * 161： 网络定位结果，网络定位定位成功。
     * 162： 请求串密文解析失败。
     * 167： 服务端定位失败，请您检查是否禁用获取位置信息权限，尝试重新请求定位。
     * 502： key参数错误，请按照说明文档重新申请KEY。
     * 505： key不存在或者非法，请按照说明文档重新申请KEY。
     * 601： key服务被开发者自己禁用，请按照说明文档重新申请KEY。
     * 602： key mcode不匹配，您的ak配置过程中安全码设置有问题，请确保：sha1正确，“;”分号是英文状态；且包名是您当前运行应用的包名，请按照说明文档重新申请KEY。
     * 501～700：key验证失败，请按照说明文档重新申请KEY。
     */

    public static float[] EARTH_WEIGHT = {0.1f, 0.2f, 0.4f, 0.6f, 0.8f}; // 推算计算权重_地球
    //public static float[] MOON_WEIGHT = {0.0167f,0.033f,0.067f,0.1f,0.133f};
    //public static float[] MARS_WEIGHT = {0.034f,0.068f,0.152f,0.228f,0.304f};

    /**
     * 关闭加载对话框
     */
    public void hideDialogForLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.cancel();
        }
    }


    /**
     * 加载数据对话框
     */
    private Dialog mLoadingDialog;

    /**
     * 显示加载对话框
     */
    public void showDialogForLoading(Activity context, String msg,
                                     boolean cancelable) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.layout_loading_dialog, null);
        TextView loadingText = (TextView) view
                .findViewById(R.id.id_tv_loading_dialog_text);
        loadingText.setText(msg);
        // mLoadingDialog.setCancelable(false);
        mLoadingDialog = new Dialog(context, R.style.loading_dialog_style);
        mLoadingDialog.setCancelable(cancelable);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        mLoadingDialog.show();
    }

    public boolean isNetworkConnected(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // 移动网络
        NetworkInfo mobNetInfo = connectMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        // wifi网络
        NetworkInfo wifiNetInfo = connectMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
            return false;
        } else {
            return true;
        }

    }

    public int NetworkType(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // 移动网络
        NetworkInfo mobNetInfo = connectMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        // wifi网络
        NetworkInfo wifiNetInfo = connectMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        int back = 0;
        if (mobNetInfo.isConnected())
            back = 1;
        if (wifiNetInfo.isConnected())//wifi连接名字外网
            back = 2;
        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
            back = 3;
        }
        return back;
    }

    /**
     * 检测当的网络（WLAN、3G/2G）状态
     *
     * @param context Context
     * @return true 表示网络可用
     */
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * wifi is use
     *
     * @param context
     * @return boolean
     */
    public boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * gprs is use
     *
     * @param context
     * @return
     */
    public boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * NetWork is type
     *
     * @param context
     * @return int
     */
    public int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }

}
