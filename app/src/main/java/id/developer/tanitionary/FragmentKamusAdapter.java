package id.developer.tanitionary;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.HashMap;

/**
 * Created by Rizal Fahmi on 31-Aug-16.
 */
public class FragmentKamusAdapter extends FragmentPagerAdapter {

    private HashMap<Integer,String> mFragmentsTags;
    private FragmentManager mFragmentManager;
    private Context context;

    public FragmentKamusAdapter(FragmentManager fm, Context context) {
        super(fm);
        mFragmentManager = fm;
        mFragmentsTags = new HashMap<Integer, String>();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new FragmentDiseaseList();
                break;
            case 1:
                fragment = new FragmentDiseaseDescription();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object obj = super.instantiateItem(container,position);
        if(obj instanceof Fragment){
            Fragment f = (Fragment) obj;
            String tag = f.getTag();
            mFragmentsTags.put(position,tag);
        }
        return obj;
    }

    public Fragment getFragment(int position){
        String tag = mFragmentsTags.get(position);
        if(tag==null){
            return null;
        }
        return mFragmentManager.findFragmentByTag(tag);
    }
}
