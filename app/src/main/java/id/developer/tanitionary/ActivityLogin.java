package id.developer.tanitionary;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class ActivityLogin extends AppCompatActivity {

    TextView textLogin, textForgetPass;
    EditText editUsername, editPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
    }

    private void initView(){
        textForgetPass = (TextView)findViewById(R.id.text_act_login_forget_pass);
        textLogin = (TextView)findViewById(R.id.text_act_login_login);
        editPass = (EditText)findViewById(R.id.edit_act_login_pass);
        editUsername = (EditText)findViewById(R.id.edit_act_login_username);

        textLogin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(1000);
                                        }catch (Exception e){

                                        }finally {
                                            startActivity(new Intent(ActivityLogin.this, ActivityMain.class));
                                            ActivityFirst.mActivityFirst.finish();
                                            finish();
                                        }
                                    }
                                }
                        ).start();
                    }
                }
        );

        textForgetPass.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.linear_act_login_place_fragment_forgetpass, new FragmentForgetPass());
                        transaction.addToBackStack("login");
                        transaction.commit();
                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return false;
    }
}

class FragmentForgetPass extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_forget_pass_email, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textNext = (TextView)view.findViewById(R.id.text_fragment_login_forget_pass_next);
        EditText editEmail = (EditText)view.findViewById(R.id.edit_fragment_login_forget_pass_email);

        textNext.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                        transaction.replace(R.id.linear_fragment_login_forget_pass_email, new FragmentForgetPassNumber());
                        transaction.addToBackStack("login");
                        transaction.commit();
                    }
                }
        );
    }
}

class FragmentForgetPassNumber extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_forget_pass_number, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textYourNumberIs = (TextView)view.findViewById(R.id.text_fragment_login_forget_pass_number_your_number);
        TextView textResetPass = (TextView)view.findViewById(R.id.text_fragment_login_forget_pass_number_reset_password);
        EditText editNumberPhone = (EditText)view.findViewById(R.id.edit_fragment_login_forget_pass_number_input_number);

        textResetPass.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(getContext())
                                .setTitle(getResources().getString(R.string.app_name))
                                .setMessage(getResources().getString(R.string.frag_forget_pass_account_reseted))
                                .setPositiveButton(getResources().getString(R.string.dialog_close),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                getActivity().onBackPressed();
                                            }
                                        })
                                .show();
                    }
                }
        );
    }
}