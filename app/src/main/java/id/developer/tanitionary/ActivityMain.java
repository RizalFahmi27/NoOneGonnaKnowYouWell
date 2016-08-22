package id.developer.tanitionary;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import id.developer.tanitionary.main.FragmentOthers;
import id.developer.tanitionary.main.FragmentForum;
import id.developer.tanitionary.main.FragmentHome;

public class ActivityMain extends AppCompatActivity {

    public static ActivityMain mActivityMain;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivityMain = this;

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);

        mViewPager.setAdapter(mSectionsPagerAdapter);


        tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(mViewPager);

        initTabsIcon();
    }

    private void initTabsIcon(){
        LinearLayout tabOne = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_custom_tab, null);
        ((TextView)tabOne.findViewById(R.id.tabs_text)).setText(getResources().getString(R.string.tab_home));
        ((ImageView)tabOne.findViewById(R.id.tabs_image)).setImageResource(R.drawable.image_tab_home);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        LinearLayout tabTwo = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_custom_tab, null);
        ((TextView)tabTwo.findViewById(R.id.tabs_text)).setText(getResources().getString(R.string.tab_forum));
        ((ImageView)tabTwo.findViewById(R.id.tabs_image)).setImageResource(R.drawable.image_tab_forum);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        LinearLayout tabThree = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_custom_tab, null);
        ((TextView)tabThree.findViewById(R.id.tabs_text)).setText(getResources().getString(R.string.tab_other));
        ((ImageView)tabThree.findViewById(R.id.tabs_image)).setImageResource(R.drawable.image_tab_other);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        tabLayout.getTabAt(0).getCustomView().setSelected(true);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(position == 0){
                return new FragmentHome();
            }else if(position == 1){
                return new FragmentForum();
            }else{
                return new FragmentOthers();
            }
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 3;
        }
    }
}
