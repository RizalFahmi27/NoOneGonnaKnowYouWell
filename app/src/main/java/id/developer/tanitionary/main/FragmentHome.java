package id.developer.tanitionary.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.developer.tanitionary.ActivityDiagnose;
import id.developer.tanitionary.ObjectWeather;
import id.developer.tanitionary.R;

/**
 * Created by Naufal on 8/13/2016.
 */
public class FragmentHome extends Fragment{

    LinearLayout linearDiagnose, linearDictionary, linearManage, linearMarket;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        linearDiagnose = (LinearLayout)view.findViewById(R.id.linear_fragment_main_home_diagnose);
        linearDictionary = (LinearLayout)view.findViewById(R.id.linear_fragment_main_home_dictionary);
        linearMarket = (LinearLayout)view.findViewById(R.id.linear_fragment_main_home_market);
        linearManage = (LinearLayout)view.findViewById(R.id.linear_fragment_main_home_manage);

        GridView gridView = (GridView)view.findViewById(R.id.grid_fragment_main_home_weather);
        gridView.setAdapter(new GridAdapter(getContext(), getData()));

        linearDiagnose.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new Thread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(500);
                                        }catch (Exception e){

                                        }finally {
                                            getContext().startActivity(new Intent(getContext(), ActivityDiagnose.class));
                                        }
                                    }
                                }
                        ).start();
                    }
                }
        );
    }

    private List<ObjectWeather> getData(){
        List<ObjectWeather> list = new ArrayList<>();

        list.add(new ObjectWeather("Rabu","Cerah","32 C"));
        list.add(new ObjectWeather("Kamis","Cerah","33 C"));
        list.add(new ObjectWeather("Jumat","Berawan","30 C"));
        list.add(new ObjectWeather("Sabtu","Hujan","27 C"));
        list.add(new ObjectWeather("Minggu","Cerah","32 C"));
        list.add(new ObjectWeather("Senin","Badai","22 C"));

        return list;
    }
}

class GridAdapter extends BaseAdapter{

    Context context;
    List<ObjectWeather> list;

    GridAdapter(Context context, List<ObjectWeather> list){
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.indexOf(list.get(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
           convertView = LayoutInflater.from(context).inflate(R.layout.layout_grid_fragment_main_home_weather, parent, false);
        }

        ((TextView)convertView.findViewById(R.id.text_layout_grid_home_weahter_day)).setText(list.get(position).getDay());
        ((TextView)convertView.findViewById(R.id.text_layout_grid_home_weahter_temperature)).setText(list.get(position).getTemperature());

        return convertView;
    }
}
