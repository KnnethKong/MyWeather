package com.kxf.myweather;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//ExcelFace
public class ExcelActivity extends Activity implements OnClickListener {
    TextView mMessageView;
    EditText editText;
    private ProgressDialog progressDialog;
    ListView listView;
    List<ExcelEntiy> excelEntiys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.excel_layout);
        Button btn = (Button) findViewById(R.id.excel_btn);
        btn.setOnClickListener(this);
        mMessageView = (TextView) findViewById(R.id.excel_txt);
        editText = (EditText) findViewById(R.id.excel_edip);
        listView = (ListView) findViewById(R.id.excel_list);
        excelEntiys = new ArrayList<ExcelEntiy>();
        getDta();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.excel_btn:
                try {
                    String sdUrl = getSDPath(2);
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Log.i("kxf", "url:" + sdUrl);
//                    Uri uri = Uri.fromFile(new File("file:///android_asset/sixmonths.xls"));
                    Uri uri = Uri.fromFile(new File(sdUrl));
                    intent.setDataAndType(uri, "application/vnd.ms-excel");
                    this.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("kxf", "------>exception");
                }
                break;
        }
    }

    public void getDta() {
        RequestParams requestParams = new RequestParams("http://192.168.0.35:8080/joa/mobileController/getFiles");
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                checkJson(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(ExcelActivity.this, "失败", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }


    public String getSDPath(int index) {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString() + "/kxfxls/" + index + ".xls";
    }


    public void donExcel(View view) {
        progressDialog = new ProgressDialog(this);
        String re_url = "http://192.168.0.35:8080/joa/file/5月考勤.xls";
//        re_url = "http://113.11.209.148/ZJYKXF/img/kxf.xls";
        re_url = "http://192.168.0.35:8080/joa/file/6.xls";
//        try {
//            re_url = URLEncoder.encode(re_url, "utf-8");
//            Log.i("kxf", "onSuccess file----->" + re_url);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        String url_s = getSDPath(1);
        RequestParams requestParams = new RequestParams(re_url);
        requestParams.setCharset("UTF-8");
        requestParams.setSaveFilePath(url_s);
        Log.i("kxf", "onSuccess file----->" + re_url);
        x.http().post(requestParams,
                new Callback.ProgressCallback<File>() {
                    @Override
                    public void onSuccess(File result) {
                        progressDialog.dismiss();
                        mMessageView.setText("下载成功");
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
                        mMessageView.setText(current + "///");
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

    private void checkJson(String result) {
        Log.i("kxf", "result:" + result);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            String code = jsonObject.optString("code");
            if (code.equals("0")) {
                JSONArray twoArray = jsonObject.optJSONArray("dataDictionary");
                for (int i = 0; i < twoArray.length(); i++) {
                    ExcelEntiy excelEntiy = new ExcelEntiy();
                    JSONObject twoObj = twoArray.optJSONObject(i);
                    String ename = twoObj.getString("fileName");
                    String time = twoObj.getString("createTime");
                    String eurl = twoObj.getString("path");
                    excelEntiy.setExcelname(ename);
                    excelEntiy.setExcelurl(eurl);
                    excelEntiy.setExceltime(time);
                    excelEntiys.add(excelEntiy);
                }
            }

        } catch (JSONException e) {

            e.printStackTrace();
        }
        ExcelAdpter adpter = new ExcelAdpter(excelEntiys, ExcelActivity.this);
//        adpter.setExcelFace(this);
        listView.setAdapter(adpter);
        jsonObject = null;
    }

//    @Override
//    public void donFile(int index) {
//        progressDialog = new ProgressDialog(this);
//        String fileurl = excelEntiys.get(index).getExcelurl();
//        String url_s = getSDPath(index);
//        RequestParams requestParams = new RequestParams(fileurl);
//        requestParams.setCharset("UTF-8");
//        requestParams.setSaveFilePath(url_s);
//        Log.i("kxf", "onSuccess file----->" + fileurl);
//        x.http().post(requestParams,
//                new Callback.ProgressCallback<File>() {
//                    @Override
//                    public void onSuccess(File result) {
//                        progressDialog.dismiss();
//                        mMessageView.setText("下载成功");
//
//                    }
//
//                    @Override
//                    public void onError(Throwable ex, boolean isOnCallback) {
//                        Log.i("kxf", "onError file----->");
//                        progressDialog.dismiss();
//
//                    }
//
//                    @Override
//                    public void onCancelled(CancelledException cex) {
//                        progressDialog.dismiss();
//
//                    }
//
//                    @Override
//                    public void onFinished() {
//                        progressDialog.dismiss();
//
//                    }
//
//                    @Override
//                    public void onWaiting() {
//
//                    }
//
//                    @Override
//                    public void onStarted() {
//
//                    }
//                    @Override
//                    public void onLoading(long total, long current, boolean isDownloading) {
//                        mMessageView.setText(current + "///");
//                        Log.i("kxf", "current file----->" + current);
//                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                        progressDialog.setMessage("亲，努力下载中。。。");
//                        progressDialog.show();
//                        progressDialog.setMax((int) total);
//                        progressDialog.setProgress((int) current);
//                    }
//                }
//        );
//
//
//    }

//    @Override
//    public void openFile(int index) {
//        String url_s = getSDPath(index);
//        try {
//            Intent intent = new Intent("android.intent.action.VIEW");
//            intent.addCategory("android.intent.category.DEFAULT");
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            Log.i("kxf", "openFile url:" + url_s);
////                    Uri uri = Uri.fromFile(new File("file:///android_asset/sixmonths.xls"));
//            Uri uri = Uri.fromFile(new File(url_s));
//            intent.setDataAndType(uri, "application/vnd.ms-excel");
//            this.startActivity(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
}