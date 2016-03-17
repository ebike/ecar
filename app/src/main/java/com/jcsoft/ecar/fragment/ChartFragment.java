package com.jcsoft.ecar.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcsoft.ecar.R;
import com.jcsoft.ecar.activity.MainActivity;
import com.jcsoft.ecar.base.BaseFragment;
import com.jcsoft.ecar.bean.DayDataBean;
import com.jcsoft.ecar.bean.ResponseBean;
import com.jcsoft.ecar.callback.DCommonCallback;
import com.jcsoft.ecar.constants.AppConfig;
import com.jcsoft.ecar.db.XUtil;
import com.jcsoft.ecar.http.DHttpUtils;
import com.jcsoft.ecar.http.DRequestParamsUtils;
import com.jcsoft.ecar.http.HttpConstants;
import com.jcsoft.ecar.utils.CommonUtils;
import com.jcsoft.ecar.utils.PreferencesUtil;
import com.jcsoft.ecar.utils.ReportUtils;
import com.jcsoft.ecar.view.TopBarView;

import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jimmy on 15/12/28.
 */
public class ChartFragment extends BaseFragment implements View.OnClickListener {
    @ViewInject(R.id.top_bar)
    TopBarView topBarView;
    @ViewInject(R.id.hsv_chart)
    HorizontalScrollView chartHorizontalScrollView;
    @ViewInject(R.id.bar_chart)
    BarChart barChart;
    @ViewInject(R.id.rl_today_mileage)
    RelativeLayout todayMileageRelativeLayout;
    @ViewInject(R.id.rl_average_speed)
    RelativeLayout averageSpeedRelativeLayout;
    @ViewInject(R.id.rl_max_speed)
    RelativeLayout maxSpeedRelativeLayout;
    @ViewInject(R.id.rl_min_speed)
    RelativeLayout minSpeedRelativeLayout;
    @ViewInject(R.id.tv_today_mileage)
    TextView todayMileageTextView;
    @ViewInject(R.id.tv_average_speed)
    TextView averageSpeedTextView;
    @ViewInject(R.id.tv_max_speed)
    TextView maxSpeedTextView;
    @ViewInject(R.id.tv_min_speed)
    TextView minSpeedTextView;
    @ViewInject(R.id.iv_today_mileage)
    ImageView todayMileageImageView;
    @ViewInject(R.id.iv_average_speed)
    ImageView averageSpeedImageView;
    @ViewInject(R.id.iv_max_speed)
    ImageView maxSpeedImageView;
    @ViewInject(R.id.iv_min_speed)
    ImageView minSpeedImageView;
    //标志位，标志已经初始化完成
    private boolean isPrepared;
    //统计数据
    private List<DayDataBean> dayDataBeans;
    //选中的统计(0：里程；1：平均速度；2：最大速度；3：最小速度)
    private int which;
    private Handler handler;
    //初始打开的页面是该页面时，需要在初始化时加载数据
    private int initPosition;

    public ChartFragment() {
    }

    @SuppressLint("ValidFragment")
    public ChartFragment(int initPosition) {
        this.initPosition = initPosition;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        x.view().inject(this, view);
        isPrepared = true;
        if (initPosition == 1) {
            requestDatas();
        }
        initListener();
        return view;
    }

    private void initListener() {
        todayMileageRelativeLayout.setOnClickListener(this);
        averageSpeedRelativeLayout.setOnClickListener(this);
        maxSpeedRelativeLayout.setOnClickListener(this);
        minSpeedRelativeLayout.setOnClickListener(this);
        topBarView.setLeftCallback(new TopBarView.TopBarLeftCallback() {
            @Override
            public void setLeftOnClickListener() {
                ((MainActivity) getActivity()).setMenuToggle();
            }
        });
    }

    @Override
    public void requestDatas() {
        if (!isPrepared || !isVisible || hasLoadedOnce || !isAdded()) {
            return;
        }
        //每天调一次半月的统计数据
        String isUsedDate = PreferencesUtil.getPrefString(getActivity(), AppConfig.IS_USED_DATE, "");
        if (!isUsedDate.equals(CommonUtils.getCurrentDateString(null))) {
            getSomeDayData();
        }
        //查询数据库中统计数据
        try {
            if (dayDataBeans == null || dayDataBeans.size() == 0) {
                dayDataBeans = XUtil.db.selector(DayDataBean.class).where("carId", "=", AppConfig.userInfoBean.getCarId()).orderBy("date", false).findAll();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        //请求今日统计数据
        RequestParams params = DRequestParamsUtils.getRequestParams_Header(HttpConstants.getDayDataUrl(CommonUtils.DateToString(new Date(), "yyyy-MM-dd")));
        DHttpUtils.get_String((MainActivity) getActivity(), true, params, new DCommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ResponseBean<DayDataBean> bean = new Gson().fromJson(result, new TypeToken<ResponseBean<DayDataBean>>() {
                }.getType());
                if (bean.getCode() == 1) {
                    try {
                        boolean isUpdate = false;
                        if (dayDataBeans == null) {
                            dayDataBeans = new ArrayList<DayDataBean>();
                        } else {
                            if (dayDataBeans.get(dayDataBeans.size() - 1).getDate().equals(bean.getData().getDate())) {
                                isUpdate = true;
                            }
                        }
                        //数据入库
                        if (isUpdate) {
                            dayDataBeans.remove(dayDataBeans.size() - 1);
                            dayDataBeans.add(bean.getData());
                            KeyValue[] keyValues = new KeyValue[4];
                            keyValues[0] = new KeyValue("maxSpeed", bean.getData().getMaxSpeed());
                            keyValues[1] = new KeyValue("minSpeed", bean.getData().getMinSpeed());
                            keyValues[2] = new KeyValue("avgSpeed", bean.getData().getAvgSpeed());
                            keyValues[3] = new KeyValue("mileage", bean.getData().getMileage());
                            XUtil.db.update(bean.getData().getClass(), WhereBuilder.b("date", "=", bean.getData().getDate()), keyValues);
                        } else {
                            dayDataBeans.add(bean.getData());
                            XUtil.db.save(bean.getData());
                        }
                        //初始报表
                        BarData mBarData = getBarData();
                        showChart(mBarData);
                        //赋值今日统计信息
                        initViews();
                    } catch (DbException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    showShortText(bean.getErrmsg());
                }
            }
        });

    }

    //每天调一次半月的统计数据
    private void getSomeDayData() {
        RequestParams params = DRequestParamsUtils.getRequestParams_Header(HttpConstants.getSomeDayDataUrl());
        DHttpUtils.get_String((MainActivity) getActivity(), true, params, new DCommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    ResponseBean<List<DayDataBean>> bean = new Gson().fromJson(result, new TypeToken<ResponseBean<List<DayDataBean>>>() {
                    }.getType());
                    if (bean.getCode() == 1) {
                        dayDataBeans = bean.getData();
                        //删除表中数据
                        XUtil.db.delete(DayDataBean.class);
                        //插入新数据
                        for (DayDataBean dataBean : dayDataBeans) {
                            XUtil.db.save(dataBean);
                        }
                        PreferencesUtil.setPrefString(getActivity(), AppConfig.IS_USED_DATE, CommonUtils.getCurrentDateString(null));
                    } else {
                        showShortText(bean.getErrmsg());
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initViews() {
        todayMileageTextView.setText(dayDataBeans.get(dayDataBeans.size() - 1).getMileage() + "公里");
        averageSpeedTextView.setText(dayDataBeans.get(dayDataBeans.size() - 1).getAvgSpeed() + "km/h");
        maxSpeedTextView.setText(dayDataBeans.get(dayDataBeans.size() - 1).getMaxSpeed() + "km/h");
        minSpeedTextView.setText(dayDataBeans.get(dayDataBeans.size() - 1).getMinSpeed() + "km/h");
    }

    private void showChart(BarData barData) {
        ReportUtils.setChartLayout(getActivity(), barChart, dayDataBeans.size());
        // 如果没有数据的时候，会显示这个
        barChart.setNoDataTextDescription("");
        barChart.setNoDataText("");
        barChart.setBackgroundColor(getResources().getColor(R.color.blue));
        barChart.setDrawBorders(false);  ////是否在折线图上添加边框
        barChart.setDescription("");// 数据描述
        barChart.setDrawGridBackground(false); // 是否显示表格颜色
        barChart.setTouchEnabled(false); // 设置是否可以触摸
        barChart.setDragEnabled(false);// 是否可以拖拽
        barChart.setScaleEnabled(false);// 是否可以缩放
        barChart.setPinchZoom(false);//
        barChart.setViewPortOffsets(0, 0, 0, 80f);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(0);
        xAxis.setLabelsToSkip(0);
        xAxis.setTextSize(14f);
        xAxis.setYOffset(8f);
        xAxis.setTextColor(getResources().getColor(R.color.white));
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setEnabled(false);
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);
        Legend mLegend = barChart.getLegend(); // 设置比例图标示
        mLegend.setEnabled(false);
        barChart.fitScreen();
        barChart.setData(barData); // 设置数据
        barChart.animateXY(1000, 1000); // 立即执行的动画
        scrollToRight();
    }

    private void scrollToRight() {
        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                chartHorizontalScrollView.smoothScrollTo(barChart.getWidth(), 0);
            }
        });
    }

    private BarData getBarData() throws ParseException {
        ArrayList<String> xValues = new ArrayList<String>();
        for (int i = 0; i < dayDataBeans.size(); i++) {
            if (dayDataBeans.get(i).getDate().equals(CommonUtils.getCurrentDateString(null))) {
                xValues.add("今天");
            } else {
                xValues.add(CommonUtils.changeDateFormat1(dayDataBeans.get(i).getDate()));
            }
        }
        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
        for (int i = 0; i < dayDataBeans.size(); i++) {
            switch (which) {
                case 0:
                    yValues.add(new BarEntry((float) dayDataBeans.get(i).getMileage(), i));
                    break;
                case 1:
                    yValues.add(new BarEntry((float) dayDataBeans.get(i).getAvgSpeed(), i));
                    break;
                case 2:
                    yValues.add(new BarEntry((float) dayDataBeans.get(i).getMaxSpeed(), i));
                    break;
                case 3:
                    yValues.add(new BarEntry((float) dayDataBeans.get(i).getMinSpeed(), i));
                    break;
            }
        }
        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, "");
        barDataSet.setColor(getResources().getColor(R.color.chart_data));// 显示颜色
        barDataSet.setDrawValues(true);
        barDataSet.setValueTextColor(getResources().getColor(R.color.white));
        barDataSet.setValueTextSize(14f);
        barDataSet.setValueFormatter(ReportUtils.formatterOnePoint);
        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
        barDataSets.add(barDataSet);
        barDataSet.setBarSpacePercent(30);
        BarData barData = new BarData(xValues, barDataSets);
        return barData;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_today_mileage:
                todayMileageRelativeLayout.setBackgroundColor(getResources().getColor(R.color.chart_select));
                averageSpeedRelativeLayout.setBackgroundColor(getResources().getColor(R.color.chart_unselect));
                maxSpeedRelativeLayout.setBackgroundColor(getResources().getColor(R.color.chart_unselect));
                minSpeedRelativeLayout.setBackgroundColor(getResources().getColor(R.color.chart_unselect));
                todayMileageImageView.setVisibility(View.VISIBLE);
                averageSpeedImageView.setVisibility(View.GONE);
                maxSpeedImageView.setVisibility(View.GONE);
                minSpeedImageView.setVisibility(View.GONE);
                which = 0;
                //初始报表
                try {
                    barChart.setData(getBarData()); // 设置数据
                    barChart.animateXY(1000, 1000); // 立即执行的动画
                    scrollToRight();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.rl_average_speed:
                todayMileageRelativeLayout.setBackgroundColor(getResources().getColor(R.color.chart_unselect));
                averageSpeedRelativeLayout.setBackgroundColor(getResources().getColor(R.color.chart_select));
                maxSpeedRelativeLayout.setBackgroundColor(getResources().getColor(R.color.chart_unselect));
                minSpeedRelativeLayout.setBackgroundColor(getResources().getColor(R.color.chart_unselect));
                todayMileageImageView.setVisibility(View.GONE);
                averageSpeedImageView.setVisibility(View.VISIBLE);
                maxSpeedImageView.setVisibility(View.GONE);
                minSpeedImageView.setVisibility(View.GONE);
                which = 1;
                //初始报表
                try {
                    barChart.setData(getBarData()); // 设置数据
                    barChart.animateXY(1000, 1000); // 立即执行的动画
                    scrollToRight();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.rl_max_speed:
                todayMileageRelativeLayout.setBackgroundColor(getResources().getColor(R.color.chart_unselect));
                averageSpeedRelativeLayout.setBackgroundColor(getResources().getColor(R.color.chart_unselect));
                maxSpeedRelativeLayout.setBackgroundColor(getResources().getColor(R.color.chart_select));
                minSpeedRelativeLayout.setBackgroundColor(getResources().getColor(R.color.chart_unselect));
                todayMileageImageView.setVisibility(View.GONE);
                averageSpeedImageView.setVisibility(View.GONE);
                maxSpeedImageView.setVisibility(View.VISIBLE);
                minSpeedImageView.setVisibility(View.GONE);
                which = 2;
                //初始报表
                try {
                    barChart.setData(getBarData()); // 设置数据
                    barChart.animateXY(1000, 1000); // 立即执行的动画
                    scrollToRight();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.rl_min_speed:
                todayMileageRelativeLayout.setBackgroundColor(getResources().getColor(R.color.chart_unselect));
                averageSpeedRelativeLayout.setBackgroundColor(getResources().getColor(R.color.chart_unselect));
                maxSpeedRelativeLayout.setBackgroundColor(getResources().getColor(R.color.chart_unselect));
                minSpeedRelativeLayout.setBackgroundColor(getResources().getColor(R.color.chart_select));
                todayMileageImageView.setVisibility(View.GONE);
                averageSpeedImageView.setVisibility(View.GONE);
                maxSpeedImageView.setVisibility(View.GONE);
                minSpeedImageView.setVisibility(View.VISIBLE);
                which = 3;
                //初始报表
                try {
                    barChart.setData(getBarData()); // 设置数据
                    barChart.animateXY(1000, 1000); // 立即执行的动画
                    scrollToRight();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
