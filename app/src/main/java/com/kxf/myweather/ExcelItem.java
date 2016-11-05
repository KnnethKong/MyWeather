package com.kxf.myweather;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * @Description:[功能描述]
 * @Author:孔祥发
 */
public class ExcelItem extends Activity {
    TextView eName, eMessage;
    Button openEls, doEls;
    String eUrl = "", file_url = "";
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xlsitem_layout);
        eName = (TextView) findViewById(R.id.xlsitem_name);
        eMessage = (TextView) findViewById(R.id.xlsitem_message);
        openEls = (Button) findViewById(R.id.xlsitem_open);
        doEls = (Button) findViewById(R.id.xlsitem_don);
        String ename = getIntent().getStringExtra("ename");
        eName.setText(ename);
        eUrl = getIntent().getStringExtra("eurl");
        index = getIntent().getIntExtra("index", 1);
        int check = checkFile(index);
        if (check == 1) {
            eMessage.setText("已存在文件，无需下载。");
            doEls.setEnabled(false);
            openEls.setEnabled(true);
        } else {
            doEls.setEnabled(true);
            openEls.setEnabled(false);
        }
    }

    public void donElx(View view) {
//        String url_s = getSDPath(index);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        RequestParams requestParams = new RequestParams(eUrl);
        requestParams.setCharset("UTF-8");
        requestParams.setSaveFilePath(file_url);
        Log.i("kxf", "onSuccess file----->" + file_url);
        x.http().post(requestParams,
                new Callback.ProgressCallback<File>() {
                    @Override
                    public void onSuccess(File result) {
                        progressDialog.dismiss();
                        eMessage.setText("下载成功");
                        openEls.setEnabled(true);
                        doEls.setEnabled(false);


                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.i("kxf", "onError file----->");
                        progressDialog.dismiss();

                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        progressDialog.dismiss();

                    }

                    @Override
                    public void onFinished() {
                        progressDialog.dismiss();

                    }

                    @Override
                    public void onWaiting() {

                    }

                    @Override
                    public void onStarted() {

                    }

                    @Override
                    public void onLoading(long total, long current, boolean isDownloading) {
                        eMessage.setText(current + "///");
                        Log.i("kxf", "current file----->" + current);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setMessage("亲，努力下载中。。。");
                        progressDialog.show();
                        progressDialog.setMax((int) total);
                        progressDialog.setProgress((int) current);
                    }
                }
        );


    }

    public void openElx(View view) {
//        String url_s = getSDPath(index);
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.i("kxf", "openFile url:" + file_url);
//                    Uri uri = Uri.fromFile(new File("file:///android_asset/sixmonths.xls"));
            Uri uri = Uri.fromFile(new File(file_url));
            intent.setDataAndType(uri, "application/vnd.ms-excel");
            this.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private String getSDPath(int index) {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString() + "/kxfxls/" + index + ".xls";
    }

    public int checkFile(int index) {
        file_url = getSDPath(index);
        File f = new File(file_url);
        if (!f.exists()) {
            return 0;
        } else {
            return 1;
        }
    }

}
