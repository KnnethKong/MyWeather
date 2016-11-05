package com.kxf.myweather;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:[功能描述]
 * @Author:孔祥发
 */
public class SignListActivity extends Activity {
    ListView listView;
    List<String> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout llayout = new LinearLayout(this);
        llayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        listView = new ListView(this);
        llayout.addView(listView, layoutParams);
        setContentView(llayout);
        listData = new ArrayList<String>();
        for (int i=0;i<10;i++){
            listData.add("2016-07-0"+i+" 峻峰华亭");
        }
        MyAdapter myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        listData = null;
        listView = null;
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            viewHolder viewHolder = null;
            if (view == null) {
                viewHolder = new viewHolder();
                view = getLayoutInflater().inflate(R.layout.sign_item, null);
                viewHolder.txt = (TextView) view.findViewById(R.id.sign_item_txt);
                view.setTag(viewHolder);
            } else {
                viewHolder = (SignListActivity.viewHolder) view.getTag();
            }
            viewHolder.txt.setText(listData.get(position));
            return view;
        }
    }

    class viewHolder {
        TextView txt;

    }
}
