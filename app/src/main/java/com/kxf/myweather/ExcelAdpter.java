package com.kxf.myweather;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * @Description:[功能描述]
 * @Author:孔祥发
 */
public class ExcelAdpter extends BaseAdapter {
//    ExcelFace excelFace;
    List<ExcelEntiy> excelEntiys;
    Context context;
//
//    public void setExcelFace(ExcelFace excelFace) {
//        this.excelFace = excelFace;
//    }

    public ExcelAdpter(List<ExcelEntiy> excelEntiys, Context context) {
        this.context = context;
        this.excelEntiys = excelEntiys;
    }

    @Override
    public int getCount() {
        return excelEntiys.size();
    }

    @Override
    public Object getItem(int position) {
        return excelEntiys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        viewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.excel_item_layout, null);
            viewHolder = new viewHolder();
            viewHolder.txt = (TextView) convertView.findViewById(R.id.excel_item_tname);
            viewHolder.time = (TextView) convertView.findViewById(R.id.excel_item_time);
//            viewHolder.btnOpen = (Button) convertView.findViewById(R.id.excel_item_open);
//            viewHolder.btnDon = (Button) convertView.findViewById(R.id.excel_item_don);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ExcelAdpter.viewHolder) convertView.getTag();
        }
        final String e_name = excelEntiys.get(position).getExcelname();
        final String e_url = excelEntiys.get(position).getExcelurl();

        String e_time = excelEntiys.get(position).getExceltime();
        viewHolder.time.setText(e_time);
        viewHolder.txt.setText(e_name);
        viewHolder.txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context,ExcelItem.class);
                intent.putExtra("index",position);
                intent.putExtra("ename",e_name);
                intent.putExtra("eurl",e_url);
                context.startActivity(intent);
            }
        });
//        viewHolder.btnDon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                excelFace.donFile(position);
//
//            }
//        });
//        viewHolder.btnOpen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                excelFace.openFile(position);
//            }
//        });
        return convertView;
    }

    class viewHolder {
        TextView txt;
        TextView time;

//        Button btnOpen;
//        Button btnDon;
    }
}
