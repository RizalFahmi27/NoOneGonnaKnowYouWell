package id.developer.tanitionary;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ActivitySplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Thread.sleep(1000);
                        }catch (Exception e){

                        }finally {
                            SessionLoginManager sessionLoginManager = new SessionLoginManager(ActivitySplashScreen.this);

                            if(sessionLoginManager.isLoggedIn()){
                                startActivity(new Intent(ActivitySplashScreen.this, ActivityMain.class));
                            }else{
                                startActivity(new Intent(ActivitySplashScreen.this, ActivityFirst.class));
                            }
                        }
                    }
                }
        ).start();
    }

}
