package com.kxf.myweather;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

/**
 */
public class FifteenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fifteenprediction);
        LineChart lineChart = (LineChart) findViewById(R.id.fofteen_liner);
        LineData lineData = getLineData(15, 10);//
        showChart(lineChart, lineData, Color.parseColor("#336699"));
        BarChart barChart = (BarChart) findViewById(R.id.fofteen_bar);
        BarData mBarData = getBarData(4, 100);
        showBarChart(barChart, mBarData);
    }

    private LineData getLineData(int count, float range) {

        ArrayList<String> xValues = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xValues.add("2/" + (i + 1));
        }
        ArrayList<Entry> yValues = new ArrayList<Entry>();
        yValues.add(new Entry(32, 1));
        yValues.add(new Entry(28, 2));
        yValues.add(new Entry(23, 3));
        yValues.add(new Entry(28, 4));
        yValues.add(new Entry(34, 5));
        yValues.add(new Entry(30, 6));
        yValues.add(new Entry(25, 7));
        yValues.add(new Entry(26, 8));
        yValues.add(new Entry(27, 9));
        yValues.add(new Entry(25, 10));
        yValues.add(new Entry(32, 11));
        yValues.add(new Entry(31, 12));
        yValues.add(new Entry(27, 13));
        yValues.add(new Entry(28, 14));
        yValues.add(new Entry(33, 15));
        LineDataSet lineDataSet = new LineDataSet(yValues, "天气" /* 显示在比例图上 */);
        // 用y轴的集合来设置参数
        lineDataSet.setLineWidth(1.75f); // 线宽
        lineDataSet.setCircleSize(3f);// 显示的圆形大小
        lineDataSet.setColor(Color.WHITE);// 显示颜色
        lineDataSet.setCircleColor(Color.WHITE);// 圆形的颜色
        lineDataSet.setHighLightColor(Color.WHITE); // 高亮的线的颜色

        ArrayList<Entry> yValues1 = new ArrayList<Entry>();
        yValues1.add(new Entry(18.34f, 1));
        yValues1.add(new Entry(18.14f, 2));
        yValues1.add(new Entry(18.39f, 3));
        yValues1.add(new Entry(18.56f, 4));
        yValues1.add(new Entry(18.22f, 5));
        yValues1.add(new Entry(18.18f, 6));
        yValues1.add(new Entry(18.29f, 7));
        yValues1.add(new Entry(18.45f, 8));
        yValues1.add(new Entry(18.00f, 9));
        yValues1.add(new Entry(17.46f, 10));
        yValues1.add(new Entry(18.18f, 11));
        yValues1.add(new Entry(18.22f, 12));
        yValues1.add(new Entry(18.31f, 13));
        yValues1.add(new Entry(18.55f, 14));
        yValues1.add(new Entry(18.10f, 15));
        yValues1.add(new Entry(18.08f, 16));
        yValues1.add(new Entry(18.01f, 17));
        yValues1.add(new Entry(18.22f, 18));
        yValues1.add(new Entry(18.42f, 19));
        yValues1.add(new Entry(17.27f, 20));
        LineDataSet lineDataSet1 = new LineDataSet(yValues1, "下班" /* 显示在比例图上 */);

        lineDataSet1.setLineWidth(1.75f); // 线宽
        lineDataSet1.setCircleSize(3f);// 显示的圆形大小
        lineDataSet1.setColor(Color.BLUE);// 显示颜色
        lineDataSet1.setCircleColor(Color.WHITE);// 圆形的颜色
        lineDataSet1.setHighLightColor(Color.GREEN); // 高亮的线的颜色

        ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
        lineDataSets.add(lineDataSet); // add the datasets
//        lineDataSets.add(lineDataSet1); // add the datasets

        // create a data object with the datasets
        LineData lineData = new LineData(xValues, lineDataSets);

        return lineData;
    }
    private void showChart(LineChart lineChart, LineData lineData, int color) {
        lineChart.setDrawBorders(false); // 是否在折线图上添加边框

        lineChart.setDescription("二月份打卡记录");// 数据描述
        lineChart.setNoDataTextDescription("没有查到相应数据");

        // enable / disable grid background
        lineChart.setDrawGridBackground(false); // 是否显示表格颜色
        lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度

        // enable touch gestures
        lineChart.setTouchEnabled(true); // 设置是否可以触摸

        // enable scaling and dragging
        lineChart.setDragEnabled(true);// 是否可以拖拽
        lineChart.setScaleEnabled(true);// 是否可以缩放

        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(false);//

        lineChart.setBackgroundColor(color);// 设置背景

        // add data
        lineChart.setData(lineData); // 设置数据

        Legend mLegend = lineChart.getLegend(); // 设置比例图标示，就是那个一组y的value的

        // modify the legend ...
        // mLegend.setPosition(LegendPosition.LEFT_OF_CHART);
        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
        mLegend.setFormSize(6f);// 字体
        mLegend.setTextColor(Color.WHITE);// 颜色
        // mLegend.setTypeface(mTf);// 字体

        lineChart.animateX(2500); // 立即执行的动画,x轴
    }

    private void showBarChart(BarChart barChart, BarData barData) {
        barChart.setDrawBorders(false); //

        barChart.setDescription("前四月报销统计");// 数据描述

        // 如果没有数据的时候，会显示这个，类似ListView的EmptyView
        barChart.setNoDataTextDescription("没有相应的数据.");

        barChart.setDrawGridBackground(false); // 是否显示表格颜色
        barChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度

        barChart.setTouchEnabled(true); // 设置是否可以触摸

        barChart.setDragEnabled(true);// 是否可以拖拽
        barChart.setScaleEnabled(true);// 是否可以缩放

        barChart.setPinchZoom(false);//

        // barChart.setBackgroundColor();// 设置背景

        barChart.setDrawBarShadow(true);

        barChart.setData(barData); // 设置数据

        Legend mLegend = barChart.getLegend(); // 设置比例图标示

        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
        mLegend.setFormSize(6f);// 字体
        mLegend.setTextColor(Color.BLACK);// 颜色

        // X轴设定
        // XAxis xAxis = barChart.getXAxis();
        // xAxis.setPosition(XAxisPosition.BOTTOM);

        barChart.animateX(2500); // 立即执行的动画,x轴
    }

    private BarData getBarData(int count, float range) {
        ArrayList<String> xValues = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xValues.add("" + (i + 1) + "月份");
        }
        // -----交通午餐
        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
        yValues.add(new BarEntry(352.65f, 1));
        yValues.add(new BarEntry(378.65f, 2));
        yValues.add(new BarEntry(353.65f, 3));
        yValues.add(new BarEntry(332.65f, 4));
        BarDataSet barDataSet = new BarDataSet(yValues, "交通、餐补");
        barDataSet.setColor(Color.rgb(114, 188, 223));

        // ------办公用品
        ArrayList<BarEntry> chengbenVal = new ArrayList<BarEntry>();

        chengbenVal.add(new BarEntry(289.86f, 1));
        chengbenVal.add(new BarEntry(0f, 2));
        chengbenVal.add(new BarEntry(228.54f, 3));
        chengbenVal.add(new BarEntry(326.93f, 4));
        BarDataSet cbDataSet = new BarDataSet(chengbenVal, "办公用品");
        cbDataSet.setColor(Color.parseColor("#669933"));

        // ------加入list
        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
        barDataSets.add(barDataSet); // add the datasets
        barDataSets.add(cbDataSet); // add the datasets
        BarData barData = new BarData(xValues, barDataSets);

        return barData;
    }
}
