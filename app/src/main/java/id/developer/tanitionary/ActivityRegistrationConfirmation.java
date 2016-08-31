package id.developer.tanitionary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class ActivityRegistrationConfirmation extends AppCompatActivity {

    private static final String ROOT_URL = "http://www.tanitionary.pe.hu";
    private static final String TAG_LINE = "Act Reg Confirmation";

    TextView textGreeting, textActivate;
    EditText editCode;

    String email;
    ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_confirmation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PD = new ProgressDialog(this);
        PD.setMessage("Cek Data");
        PD.setCancelable(false);

        initBundle();
        initView();
    }

    private void initBundle(){
        Bundle extras = getIntent().getExtras();
        email = extras.getString("email");
    }

    private void initView(){
        textGreeting = (TextView)findViewById(R.id.text_act_registration_conf_greeting);
        textActivate = (TextView)findViewById(R.id.text_act_registration_conf_activation);
        editCode = (EditText)findViewById(R.id.edit_act_registration_conf_code);

        String greeting = getResources().getString(R.string.act_registration_conf_activate_account)+ " <b>" +email+ "</b>";
        textGreeting.setText(Html.fromHtml(greeting));

        textActivate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String code = editCode.getText().toString();

                        if (code.equalsIgnoreCase("")) {
                            Toast.makeText(ActivityRegistrationConfirmation.this, "Ups, anda melupakan beberapa data.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        PD.show();
                        cekCode(code);
                    }
                }
        );
    }

    private void cekCode(String code){
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = ROOT_URL+ "/activateAccount.php?email=" +email+ "&code=" +code;
        Log.d(TAG_LINE, "This is the url : " +url);

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PD.hide();

                        if(response.equalsIgnoreCase("success")){
                            startActivity(new Intent(ActivityRegistrationConfirmation.this, ActivityActivatedAccount.class));
                            finish();
                        }else{
                            Toast.makeText(ActivityRegistrationConfirmation.this, getResources().getString(R.string.error_wrong_code_confirmation), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        PD.hide();
                        Toast.makeText(ActivityRegistrationConfirmation.this, getResources().getString(R.string.error_no_internet_connection), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        queue.add(request);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
