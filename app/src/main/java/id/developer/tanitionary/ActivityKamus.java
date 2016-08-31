package id.developer.tanitionary;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



public class ActivityKamus extends AppCompatActivity {

    FragmentKamusAdapter fa;
    ViewPagerKamus viewPagerKamus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_kamus);
        findViewById(R.id.toolbar);
        //setSupportActionBar();

        viewPagerKamus = (ViewPagerKamus) findViewById(R.id.viewPagerKamus);
        fa = new FragmentKamusAdapter(getSupportFragmentManager(),this);

        viewPagerKamus.setPagingEnabled(false);

        viewPagerKamus.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Fragment fragment = ((FragmentKamusAdapter)viewPagerKamus.getAdapter()).getFragment(position);
                if(position==0 && fragment!=null){
                    ((FragmentDiseaseList)fragment).onResume();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPagerKamus.setAdapter(fa);
    }

    @Override
    public void onBackPressed() {
        if(viewPagerKamus.getCurrentItem()==1){
            viewPagerKamus.setCurrentItem(0);
        }
        else if(viewPagerKamus.getCurrentItem()==0){
            finish();
        }
    }
}
