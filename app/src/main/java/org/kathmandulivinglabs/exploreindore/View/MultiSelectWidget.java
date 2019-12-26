package org.kathmandulivinglabs.exploreindore.View;

import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class MultiSelectWidget extends LinearLayout {
    public MultiSelectWidget(Context context) {
        super(context);
    }

    public MultiSelectWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiSelectWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MultiSelectWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(){

    }
}
