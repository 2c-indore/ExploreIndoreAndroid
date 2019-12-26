package org.kathmandulivinglabs.exploreindore.Customclass;

import androidx.viewpager.widget.ViewPager;
/**
 * Created by Bhawak on 3/11/2018.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }
}
