package com.sjtu.se2017.positivetime.view.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.jn.chart.charts.LineChart;
import com.jn.chart.data.Entry;
import com.jn.chart.manager.LineChartManager;
import com.sjtu.se2017.positivetime.R;
import com.sjtu.se2017.positivetime.dao.ATDao;

import java.util.ArrayList;
import java.util.Calendar;

public class LineChartActivity extends Activity {
    private LineChart mLineChart;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linechart);

        mLineChart = (LineChart) findViewById(R.id.lineChart);
        //设置图表的描述
        mLineChart.setDescription("AT变化图");

        //设置x轴的数据
        ArrayList<String> xValues = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            xValues.add(i+"点");
        }

        //设置y轴的数据
        /*ArrayList<Entry> yValue = new ArrayList<>();
        yValue.add(new Entry(13, 1));
        yValue.add(new Entry(6, 2));
        yValue.add(new Entry(3, 3));
        yValue.add(new Entry(7, 4));
        yValue.add(new Entry(2, 5));
        yValue.add(new Entry(5, 6));
        yValue.add(new Entry(12, 7));*/
        ATDao atDao = new ATDao(this);
        ArrayList<Entry> yValue = atDao.checkATofToday(Calendar.getInstance().get(Calendar.DAY_OF_YEAR));
        //设置折线的名称
        LineChartManager.setLineName("AT");
        //创建一条折线的图表
        LineChartManager.initSingleLineChart(context,mLineChart,xValues,yValue);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
