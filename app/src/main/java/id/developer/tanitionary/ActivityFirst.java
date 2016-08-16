package id.developer.tanitionary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

public class ActivityFirst extends AppCompatActivity {

    public static ActivityFirst mActivityFirst;

    private SectionsPagerAdapter mAdapter;
    private ViewPager mViewPager;

    TextView textRegister, textLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        mActivityFirst = this;

        mAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAdapter);

        CirclePageIndicator mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mViewPager);

        initView();
    }

    private void initView(){
        textRegister = (TextView)findViewById(R.id.text_act_first_register);
        textLogin = (TextView)findViewById(R.id.text_act_first_login);

        textLogin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ActivityFirst.this, ActivityLogin.class));
                    }
                }
        );
    }

    class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(position == 0){
                return FragmentDummy.initFragment(1);
            }else if(position == 1){
                return FragmentDummy.initFragment(2);
            }else if(position == 2){
                return FragmentDummy.initFragment(3);
            }else{
                return FragmentDummy.initFragment(4);
            }
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }
    }
}

class FragmentDummy extends Fragment{

    Integer id;

    public static FragmentDummy initFragment(Integer id){
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);

        FragmentDummy dummy = new FragmentDummy();
        dummy.setArguments(bundle);

        return dummy;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.id = getArguments().getInt("id");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first_viewpager, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((ImageView)view.findViewById(R.id.image_fragment_first_viewpager)).setImageResource(getResources().getIdentifier("dummy_" + id, "drawable", getContext().getPackageName()));
    }
}