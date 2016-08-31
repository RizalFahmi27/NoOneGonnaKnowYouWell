package id.developer.tanitionary;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Rizal Fahmi on 31-Aug-16.
 */
public class ViewPagerKamus extends ViewPager {

    private boolean isEnabled;



    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        if(this.isEnabled)
            return super.onInterceptHoverEvent(event);
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(this.isEnabled)
            return super.onTouchEvent(ev);

        return false;
    }

    public ViewPagerKamus(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.isEnabled = true;
    }

    public void setPagingEnabled(boolean enabled){
        this.isEnabled = enabled;
    }
}
