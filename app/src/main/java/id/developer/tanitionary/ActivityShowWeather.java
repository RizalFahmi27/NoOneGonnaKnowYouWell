package id.developer.tanitionary;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ActivityShowWeather extends AppCompatActivity {

    GridView gridWeather;
    TextView textDomisili, textTemp, textWeather;
    LineChart chartWeather;

    List<ObjectWeather> weathers;
    Map<String, String> userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_weather);

        initSession();
        initBundle();
        initView();
    }

    private void initSession(){
        userDetails = new SessionLoginManager(this).getUserDetails();
    }

    private void initBundle(){
        Bundle extras = getIntent().getExtras();
        weathers = extras.getParcelableArrayList("weather");
    }

    private void initView(){
        gridWeather = (GridView)findViewById(R.id.grid_act_show_weather);
        textDomisili = (TextView)findViewById(R.id.text_act_show_weather_domisili);
        textTemp = (TextView)findViewById(R.id.text_act_show_weather_temperature);
        textWeather = (TextView)findViewById(R.id.text_act_show_weather_status);
        chartWeather = (LineChart)findViewById(R.id.chart_act_show_weather);

        textDomisili.setText(userDetails.get(SessionLoginManager.KEY_LOCATION));
        textTemp.setText(Math.round(weathers.get(0).getTmpDay()) + "");
        textWeather.setText(weathers.get(0).getWeather());

        gridWeather.setAdapter(new GridAdapter(this, weathers));

        initChart();
    }

    private void initChart(){

        // set description
        chartWeather.setDescription("");

        // enable touch gestures
        chartWeather.setTouchEnabled(false);

        chartWeather.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        chartWeather.setDragEnabled(false);
        chartWeather.setScaleEnabled(false);
        chartWeather.setDrawGridBackground(false);
        chartWeather.setHighlightPerDragEnabled(false);

        // hide grid
        chartWeather.getAxisLeft().setDrawGridLines(false);
        chartWeather.getXAxis().setDrawGridLines(false);
        chartWeather.getAxisLeft().setDrawAxisLine(false);
        chartWeather.getXAxis().setEnabled(false);
        chartWeather.getLegend().setEnabled(false);

        // if disabled, scaling can be done on x- and y-axis separately
        chartWeather.setPinchZoom(false);

        // set an alternative background color
//        chartWeather.setBackgroundColor(Color.LTGRAY);

        // add data
        setData();

//        chartWeather.animateX(2500);

        // get the legend (only possible after setting data)
//        Legend l = chartWeather.getLegend();

        YAxis leftAxis = chartWeather.getAxisLeft();
        YAxis rightAxis = chartWeather.getAxisRight();

        leftAxis.setEnabled(false);
        rightAxis.setEnabled(false);

        Float max = 0f;
        Float min = 1000f;

        for (int i = 0; i < weathers.size()-1; i++) {
            if(max < weathers.get(i).getTmpDay()){
                max = weathers.get(i).getTmpDay();
            }

            if(min > weathers.get(i).getTmpDay()){
                min = weathers.get(i).getTmpDay();
            }
        }

        leftAxis.setAxisMaxValue(max+2);
        leftAxis.setAxisMinValue(min);
    }

    private void setData() {

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < weathers.size()-1; i++) {
            yVals1.add(new Entry(i, weathers.get(i).getTmpDay()));
        }

        LineDataSet set1;

        if (chartWeather.getData() != null &&
                chartWeather.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chartWeather.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            chartWeather.getData().notifyDataChanged();
            chartWeather.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(yVals1, "DataSet 1");

            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(Color.WHITE);
            set1.setCircleColor(Color.WHITE);
            set1.setLineWidth(3f);
            set1.setCircleRadius(3f);
            set1.setFillAlpha(65);
            set1.setFillColor(Color.WHITE);
            set1.setHighLightColor(Color.WHITE);
            set1.setDrawCircleHole(false);
            //set1.setFillFormatter(new MyFillFormatter(0f));
            //set1.setDrawHorizontalHighlightIndicator(false);
            //set1.setVisible(false);
            //set1.setCircleHoleColor(Color.WHITE);

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(0f);

            // set data
            chartWeather.setData(data);
        }
    }

    private class GridAdapter extends BaseAdapter{

        Context context;
        List<ObjectWeather> weathers;

        GridAdapter(Context context, List<ObjectWeather> weathers){
            this.context = context;
            this.weathers = weathers;
        }

        @Override
        public int getCount() {
            return weathers.size()-1;
        }

        @Override
        public Object getItem(int position) {
            return weathers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return weathers.indexOf(weathers.get(position));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.layout_grid_fragment_main_home_weather, parent, false);
            }

            ImageView imageView = (ImageView)convertView.findViewById(R.id.image_layout_grid_home_weather);

            ((TextView) convertView.findViewById(R.id.text_layout_grid_home_weahter_day)).setText(weathers.get(position).getDay());

            ((TextView)convertView
                    .findViewById(R.id.text_layout_grid_home_weahter_temperature))
                    .setText("" + Math.round(weathers.get(position).getTmpDay()));

            Picasso
                    .with(context)
                    .load(weathers.get(position).getIconWeather())
                    .into(imageView);

            return convertView;
        }
    }
}
