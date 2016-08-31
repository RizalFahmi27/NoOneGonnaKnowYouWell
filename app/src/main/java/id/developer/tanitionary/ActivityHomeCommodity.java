package id.developer.tanitionary;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActivityHomeCommodity extends AppCompatActivity {

    List<ObjectCommodity> commodities;
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_commodity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        initVolley();
    }

    private void initView(){
        mListView = (ListView)findViewById(R.id.list_act_home_commodity);
    }

    private void initVolley(){

        commodities = new ArrayList<>();

        String url = Utils.ROOT_URL+ "/getCommodity.php";
        Log.d(Utils.TAG_LINE, Utils.LOG_CHECK_URL+ url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try{
                            if(response.getString("status").equalsIgnoreCase("ok")){
                                JSONArray array = response.getJSONArray("data");

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    String name = object.getString("name_commodity");
                                    String urlPic = object.getString("image_commodity");
                                    String recentPrice = object.getString("price_recent_commodity");
                                    String yesterdayPrice = object.getString("price_yest_commodity");

                                    commodities.add(new ObjectCommodity(name, urlPic, recentPrice, yesterdayPrice));
                                }

                                Collections.sort(commodities);
                                mListView.setAdapter(new ListCommodityAdapter(ActivityHomeCommodity.this, commodities));
                            }
                        }catch (Exception e){
                            Log.d(Utils.TAG_LINE, Utils.LOG_ERROR_TRY_CATCH+ e.getMessage());
                            Toast.makeText(ActivityHomeCommodity.this, getResources().getString(R.string.error_server_down), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(Utils.TAG_LINE, Utils.LOG_ERROR_VOLLEY+ error.getMessage());
                        Toast.makeText(ActivityHomeCommodity.this, getResources().getString(R.string.error_no_internet_connection), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(this).add(request);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_commodity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.action_about:
                new AlertDialog
                        .Builder(this)
                        .setMessage(Html.fromHtml(getResources().getString(R.string.act_home_commodity_data_grab_from)))
                        .setNegativeButton("Tutup", null)
                        .show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ListCommodityAdapter extends BaseAdapter{

        Context context;
        List<ObjectCommodity> commodities;

        public ListCommodityAdapter(Context context, List<ObjectCommodity> commodities){
            this.commodities = commodities;
            this.context = context;
        }

        @Override
        public int getCount() {
            return commodities.size();
        }

        @Override
        public Object getItem(int position) {
            return commodities.get(position);
        }

        @Override
        public long getItemId(int position) {
            return commodities.indexOf(commodities.get(position));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.layout_list_act_home_commodity, parent, false);
            }

            LinearLayout linearYestPrice = (LinearLayout)convertView.findViewById(R.id.linear_layout_list_act_home_commodity_yesterday_price);
            ImageView imageCommodity = (ImageView)convertView.findViewById(R.id.image_layout_list_act_home_commodity);
            Integer recentPrice = Integer.parseInt(commodities.get(position).getRecentPrice().replace(".",""));
            Integer yestPrice = Integer.parseInt(commodities.get(position).getYesterdayPrice().replace(".",""));

            ((TextView) convertView.findViewById(R.id.text_layout_list_act_home_commodity_name)).setText(commodities.get(position).getName());
            ((TextView)convertView.findViewById(R.id.text_layout_list_act_home_commodity_recent_price)).setText("Rp " + commodities.get(position).getRecentPrice() + "/Kg");
            ((TextView)convertView.findViewById(R.id.text_layout_list_act_home_commodity_yesterday_price)).setText("Rp " + commodities.get(position).getYesterdayPrice() + "/Kg");

            Log.d(Utils.TAG_LINE, "This is the price in pos " + position + " : " + recentPrice + " <=> " + yestPrice);

            if(recentPrice > yestPrice){
                linearYestPrice.setBackgroundColor(getResources().getColor(R.color.colorRedLight));
                ((ImageView)convertView.findViewById(R.id.image_layout_list_act_home_commodity_comparator)).setImageResource(R.mipmap.ic_arrow_up);
            }else if(recentPrice < yestPrice){
                linearYestPrice.setBackgroundColor(getResources().getColor(R.color.colorGreenLight));
                ((ImageView)convertView.findViewById(R.id.image_layout_list_act_home_commodity_comparator)).setImageResource(R.mipmap.ic_arrow_down);
            }else{
                linearYestPrice.setBackgroundColor(getResources().getColor(R.color.colorBlueLight));
                ((ImageView)convertView.findViewById(R.id.image_layout_list_act_home_commodity_comparator)).setImageResource(R.mipmap.ic_equal);
            }

            Picasso.with(context).load(commodities.get(position).getUrlPic()).into(imageCommodity);
            return convertView;
        }
    }
}
