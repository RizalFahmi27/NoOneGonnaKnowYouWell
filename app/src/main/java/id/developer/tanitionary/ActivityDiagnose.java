package id.developer.tanitionary;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import junit.framework.Test;

import java.util.Random;

public class ActivityDiagnose extends AppCompatActivity {

    TextView textIlness, textYes, textNo;
    ViewGroup layoutParent;

    int randNumb = 0;
    String illness = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnose);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        initIllness();
    }

    private void initView(){
        textIlness = (TextView)findViewById(R.id.text_act_diagnose_illness);
        textYes = (TextView)findViewById(R.id.text_act_diagnose_yes);
        textNo = (TextView)findViewById(R.id.text_act_diagnose_no);
        layoutParent = (ViewGroup)findViewById(R.id.linear_act_diagnose_as_parent);

        textYes.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addNewChild();
                    }
                }
        );

        textNo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initIllness();
                    }
                }
        );
    }

    private void initIllness(){
        Random rand = new Random();
        String[] arr = {getResources().getString(R.string.first_act_string_1)
                ,getResources().getString(R.string.first_act_string_2),
                getResources().getString(R.string.first_act_string_3),
                getResources().getString(R.string.first_act_string_4),
                getResources().getString(R.string.title_activity_activity_first)};

        randNumb = rand.nextInt(4);
        illness = arr[randNumb];

        textIlness.setText(getString(R.string.act_diagnose_are_your_plant)
                + " "
                + illness);
    }

    private void addNewChild(){
        ViewGroup view = (ViewGroup)LayoutInflater.from(this).inflate(R.layout.layout_child_act_diagnose_illness, layoutParent, false);
        ((TextView)view.findViewById(R.id.text_child_act_diagnose_illness)).setText(illness);
        layoutParent.addView(view, 0);
        initIllness();
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
