package id.developer.tanitionary;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class ActivityOtherPassword extends AppCompatActivity {

    EditText editOld, editNew, editNewConfirm;
    TextView textChange;

    SessionLoginManager mLoginManager;
    Map<String, String> userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initSession();
        initView();
    }

    private void initSession(){
        mLoginManager = new SessionLoginManager(this);
        userDetails = mLoginManager.getUserDetails();
    }

    private void initView(){
        editOld = (EditText)findViewById(R.id.edit_act_other_pass_old);
        editNew = (EditText)findViewById(R.id.edit_act_other_pass_new);
        editNewConfirm = (EditText)findViewById(R.id.edit_act_other_pass_new_confirmation);
        textChange = (TextView)findViewById(R.id.text_act_other_pass_change_pass);

        textChange.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String inpOld = editOld.getText().toString();
                        String inpNew = editNew.getText().toString();
                        String inpNewConf = editNewConfirm.getText().toString();

                        if(inpOld.equalsIgnoreCase("") || inpNew.equalsIgnoreCase("") || inpNewConf.equalsIgnoreCase("")){
                            Toast.makeText(ActivityOtherPassword.this, "Ups, anda melupakan beberapa data.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(!inpOld.equals(userDetails.get(SessionLoginManager.KEY_PASS))){
                            Toast.makeText(ActivityOtherPassword.this, "Ups, password lama anda salah.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(!inpNew.equals(inpNewConf)){
                            Toast.makeText(ActivityOtherPassword.this, "Ups, password baru anda tidak sama.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        setPass(inpNew);
                        mLoginManager.setPass(inpNew);
                        setResult(RESULT_OK);
                        finish();
                    }
                }
        );
    }

    private void setPass(final String pass){
        String url = Utils.ROOT_URL+ "/setPass.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equalsIgnoreCase("ok")){
                            mLoginManager.setPass(pass);
                            setResult(RESULT_OK);
                            finish();
                        }else{
                            Toast.makeText(ActivityOtherPassword.this, getResources().getString(R.string.error_server_down), Toast.LENGTH_SHORT).show();
                        }

                        Log.d(Utils.TAG_LINE, "This is the response : " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(Utils.TAG_LINE, "This is the error from volley : " +error.getMessage());
                        Toast.makeText(ActivityOtherPassword.this, getResources().getString(R.string.error_no_internet_connection), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("email", userDetails.get(SessionLoginManager.KEY_EMAIL));
                map.put("pass", pass);

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
