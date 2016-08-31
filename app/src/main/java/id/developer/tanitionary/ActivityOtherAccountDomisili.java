package id.developer.tanitionary;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class ActivityOtherAccountDomisili extends AppCompatActivity {

    private boolean isListViewShowed = false;

    ExpandableStickyListHeadersListView mExpandableListView;
    WeakHashMap<View,Integer> mOriginalViewHeightPool = new WeakHashMap<View, Integer>();

    TextView textGreeting, textChange;
    EditText editDomisili;
    LinearLayout linearContent;

    SessionLoginManager mLoginManager;
    Map<String, String> userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_account_domisili);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initSession();
        initListView();
        initView();
    }

    private void initSession(){
        mLoginManager = new SessionLoginManager(this);
        userDetails = mLoginManager.getUserDetails();
    }

    private void initListView(){
        mExpandableListView = (ExpandableStickyListHeadersListView)findViewById(R.id.list_act_other_account_domisili);

        //custom expand/collapse animation
        mExpandableListView.setAnimExecutor(new SystemAnimationExpandableListView(mOriginalViewHeightPool));
        mExpandableListView.setAdapter(new SystemAdapterListDomisili(this));
        mExpandableListView.setOnHeaderClickListener(new StickyListHeadersListView.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
                if (mExpandableListView.isHeaderCollapsed(headerId)) {
                    mExpandableListView.expand(headerId);
                } else {
                    mExpandableListView.collapse(headerId);
                }
            }
        });
        mExpandableListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        isListViewShowed = false;
                        mExpandableListView.setVisibility(View.GONE);
                        linearContent.setVisibility(View.VISIBLE);
                        editDomisili.setText(((ObjectDomisili)mExpandableListView.getItemAtPosition(position)).getName());
                    }
                }
        );
    }

    private void initView(){
        textGreeting = (TextView)findViewById(R.id.text_act_other_account_domisili_greeting);
        textChange = (TextView)findViewById(R.id.text_act_other_account_domisili_change_domisili);
        editDomisili = (EditText)findViewById(R.id.edit_act_other_account_domisili);
        linearContent = (LinearLayout)findViewById(R.id.linear_act_other_account_domisili_content);

        textGreeting.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/segoeui.ttf"));
        textGreeting.setText("Hai, " + userDetails.get(SessionLoginManager.KEY_NAME) + " !");
        editDomisili.setText(userDetails.get(SessionLoginManager.KEY_LOCATION));

        editDomisili.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linearContent.setVisibility(View.GONE);
                        mExpandableListView.setVisibility(View.VISIBLE);
                        isListViewShowed = true;
                    }
                }
        );
        textChange.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String domisili = editDomisili.getText().toString();

                        setDomisili(domisili);
                    }
                }
        );
    }

    private void setDomisili(final String domisili){
        String url = Utils.ROOT_URL+ "/setDomisili.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equalsIgnoreCase("ok")){
                            mLoginManager.setLocation(domisili);
                            setResult(RESULT_OK);
                            finish();
                        }else{
                            Toast.makeText(ActivityOtherAccountDomisili.this, getResources().getString(R.string.error_server_down), Toast.LENGTH_SHORT).show();
                        }

                        Log.d(Utils.TAG_LINE, "This is the response : " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(Utils.TAG_LINE, "This is the error from volley : " +error.getMessage());
                        Toast.makeText(ActivityOtherAccountDomisili.this, getResources().getString(R.string.error_no_internet_connection), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("email", userDetails.get(SessionLoginManager.KEY_EMAIL));
                map.put("domisili", domisili);

                return map;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {

        if(isListViewShowed){
            mExpandableListView.setVisibility(View.GONE);
            linearContent.setVisibility(View.VISIBLE);
            isListViewShowed = false;
            return;
        }

        super.onBackPressed();
    }
}