package id.developer.tanitionary;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ActivityOtherAccountName extends AppCompatActivity {

    SessionLoginManager sessionLoginManager;
    TextView textGreeting, textChange;
    EditText editName;

    Map<String, String> userDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_account_name);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initSession();
        initView();
    }

    private void initSession(){
        sessionLoginManager = new SessionLoginManager(this);

        userDetail = sessionLoginManager.getUserDetails();
    }

    private void initView(){
        textGreeting = (TextView)findViewById(R.id.text_act_other_account_name_greeting);
        textChange = (TextView)findViewById(R.id.text_act_other_account_name_change_name);
        editName = (EditText)findViewById(R.id.edit_act_other_account_name);

        textChange.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newName = editName.getText().toString();

                        if (newName.length() < 2) {
                            Toast.makeText(ActivityOtherAccountName.this, "Ups, nama anda terlalu pendek.", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (newName.length() > 20) {
                            Toast.makeText(ActivityOtherAccountName.this, "Ups, nama anda terlalu panjang.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        changeName(newName);
                    }
                }
        );

        textGreeting.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/segoeui.ttf" ));
        textGreeting.setText("Hai, " +userDetail.get(SessionLoginManager.KEY_NAME)+ " !");
//        textGreeting.setText("Hai, " +userDetail.get(SessionLoginManager.KEY_NAME+ " !"));
    }

    private void changeName(final String name){

        String url = Utils.ROOT_URL+ "/setName.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equalsIgnoreCase("ok")){
                            sessionLoginManager.setName(name);
                            setResult(RESULT_OK);
                            finish();
                        }else{
                            Toast.makeText(ActivityOtherAccountName.this, getResources().getString(R.string.error_server_down), Toast.LENGTH_SHORT).show();
                        }

                        Log.d(Utils.TAG_LINE, "This is the response : " +response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(Utils.TAG_LINE, "This is the error from volley : " +error.getMessage());
                        Toast.makeText(ActivityOtherAccountName.this, getResources().getString(R.string.error_no_internet_connection), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("email", userDetail.get(SessionLoginManager.KEY_EMAIL));
                map.put("name", name);

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
}
