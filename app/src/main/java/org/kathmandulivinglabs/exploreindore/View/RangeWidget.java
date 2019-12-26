package org.kathmandulivinglabs.exploreindore.View;

import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.appyvet.materialrangebar.RangeBar;

import org.kathmandulivinglabs.exploreindore.R;

public class RangeWidget extends RelativeLayout{

    View rootView;
    //TextView titleView;
    RangeBar rangeView;
    Float minValue, maxValue;

    public RangeWidget(Context context) {
        super(context);
        init(context);
    }

    public RangeWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RangeWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RangeWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){
        rootView = inflate(context, R.layout.custom_range_slider, this);
        //titleView = rootView.findViewById(R.id.range_slider_title);
        rangeView = rootView.findViewById(R.id.range_slider_bar);
        rangeView.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex, String leftPinValue, String rightPinValue) {
                minValue = Float.valueOf(leftPinValue);
                maxValue = Float.valueOf(rightPinValue);

            }

        });
    }

    public void setRange(float minValue, float maxValue){
        rangeView.setTickStart(minValue);
        rangeView.setTickEnd(maxValue);
    }

//    public void setTitle(String title){
//        titleView.setText(title);
//    }

    public Float getMinValue() {
        return minValue;
    }

    public void setMinValue(Float minValue) {
        this.minValue = minValue;
    }

    public Float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Float maxValue) {
        this.maxValue = maxValue;
    }
}

/*public class RangeWidget extends View{

    private int maxValue = 100;
    private int currentMax = 100;
    private int minValue = 0;
    private int currentMin = 0;

    private float valueToDraw; //for use during an animation
    private float maxDrawValue;
    private float minDrawValue;

    private int barHeight;
    private int circleRadius;
    private int spaceAfterBar;
    private int circleTextSize;
    private int maxValueTextSize;
    private int labelTextSize;
    private int labelTextColor;
    private int currentValueTextColor;
    private int circleTextColor;
    private int baseColor;
    private int fillColor;
    private String labelText;

    private Paint labelPaint;
    private Paint maxValuePaint;
    private Paint barBasePaint;
    private Paint barFillPaint;
    private Paint circlePaint;
    private Paint currentValuePaint;

    public RangeWidget(Context context) {
        super(context);
    }

    public RangeWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RangeWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RangeWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attributeSet){
        TypedArray ta = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.RangeWidget, 0, 0);
        barHeight = ta.getDimensionPixelSize(R.styleable.RangeWidget_barHeight, 0);

        circleRadius = ta.getDimensionPixelSize(R.styleable.RangeWidget_circleRadius, 0);
        spaceAfterBar = ta.getDimensionPixelSize(R.styleable.RangeWidget_spaceAfterBar, 0);
        circleTextSize = ta.getDimensionPixelSize(R.styleable.RangeWidget_circleTextSize, 0);
        maxValueTextSize = ta.getDimensionPixelSize(R.styleable.RangeWidget_maxValueTextSize, 0);
        labelTextSize = ta.getDimensionPixelSize(R.styleable.RangeWidget_labelTextSize, 0);
        labelTextColor = ta.getColor(R.styleable.RangeWidget_labelTextColor, Color.BLACK);
        currentValueTextColor = ta.getColor(R.styleable.RangeWidget_maxValueTextColor, Color.BLACK);
        circleTextColor = ta.getColor(R.styleable.RangeWidget_circleTextColor, Color.BLACK);
        baseColor = ta.getColor(R.styleable.RangeWidget_baseColor, Color.BLACK);
        fillColor = ta.getColor(R.styleable.RangeWidget_fillColor, Color.BLACK);
        labelText = ta.getString(R.styleable.RangeWidget_labelText);
        ta.recycle();

        labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelPaint.setTextSize(labelTextSize);
        labelPaint.setColor(labelTextColor);
        labelPaint.setTextAlign(Paint.Align.LEFT);
        labelPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        maxValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        maxValuePaint.setTextSize(maxValueTextSize);
        maxValuePaint.setColor(currentValueTextColor);
        maxValuePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        maxValuePaint.setTextAlign(Paint.Align.RIGHT);



        barBasePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        barBasePaint.setColor(baseColor);

        barFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        barFillPaint.setColor(fillColor);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(fillColor);

        currentValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        currentValuePaint.setTextSize(circleTextSize);
        currentValuePaint.setColor(circleTextColor);
        currentValuePaint.setTextAlign(Paint.Align.CENTER);

    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
        invalidate();
        requestLayout();
    }

    public int getCurrentMax() {
        return currentMax;
    }

    public void setCurrentMax(int currentMax) {
        if (currentMax <= currentMin){
            this.currentMax = currentMin + 1;
        }
        if (currentMax > maxValue){
            this.currentMax = maxValue;
        }
        this.currentMax = currentMax;
        invalidate();
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
        invalidate();
        requestLayout();
    }

    public int getCurrentMin() {
        return currentMin;
    }

    public void setCurrentMin(int currentMin) {
        if (currentMin >= currentMax){
            this.currentMin = currentMax - 1;
        }
        if (currentMin < minValue){
            this.currentMin = minValue;
        }
        this.currentMin = currentMin;
        invalidate();
    }

    public String getLabelText() {
        return labelText;
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
        invalidate();
        requestLayout();
    }

    private int measureHeight(int measureSpec) {

        int size = getPaddingTop() + getPaddingBottom();
        size += labelPaint.getFontSpacing();
        float maxValueTextSpacing = maxValuePaint.getFontSpacing();
        size += Math.max(maxValueTextSpacing, Math.max(barHeight, circleRadius * 2));

        return resolveSizeAndState(size, measureSpec, 0);
    }

    private int measureWidth(int measureSpec) {

        int size = getPaddingLeft() + getPaddingRight();
        Rect bounds = new Rect();
        labelPaint.getTextBounds(labelText, 0, labelText.length(), bounds);
        size += bounds.width();

        bounds = new Rect();
        String maxValueText = String.valueOf(maxValue);
        maxValuePaint.getTextBounds(maxValueText, 0, maxValueText.length(), bounds);
        size += bounds.width();

        return resolveSizeAndState(size, measureSpec, 0);
    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    @Override
    protected void onDraw (Canvas canvas) {
        drawLabel(canvas);
        drawBar(canvas);
        drawMaxValue(canvas);
    }

    private void drawLabel(Canvas canvas) {
        float x = getPaddingLeft();
        //the y coordinate marks the bottom of the text, so we need to factor in the height
        Rect bounds = new Rect();
        labelPaint.getTextBounds(labelText, 0, labelText.length(), bounds);
        float y = getPaddingTop() + bounds.height();
        canvas.drawText(labelText, x, y, labelPaint);
    }

    private void drawBar(Canvas canvas) {
        String maxValueString = String.valueOf(maxValue);
        Rect maxValueRect = new Rect();
        maxValuePaint.getTextBounds(maxValueString, 0, maxValueString.length(), maxValueRect);
        float barLength = getWidth() - getPaddingRight() - getPaddingLeft() - circleRadius - maxValueRect.width() - spaceAfterBar;

        float barCenter = getBarCenter();

        float halfBarHeight = barHeight / 2;
        float top = barCenter - halfBarHeight;
        float bottom = barCenter + halfBarHeight;
        float left = getPaddingLeft();
        float right = getPaddingLeft() + barLength;
        RectF rect = new RectF(left, top, right, bottom);
        canvas.drawRoundRect(rect, halfBarHeight, halfBarHeight, barBasePaint);


        float percentFilled = (float) valueToDraw / (float) maxValue;
        float fillLength = barLength * percentFilled;
        float fillPosition = left + fillLength;
        RectF fillRect = new RectF(left, top, fillPosition, bottom);
        canvas.drawRoundRect(fillRect, halfBarHeight, halfBarHeight, barFillPaint);

        canvas.drawCircle(fillPosition, barCenter, circleRadius, circlePaint);

        Rect bounds = new Rect();
        String valueString = String.valueOf(Math.round(valueToDraw));
        currentValuePaint.getTextBounds(valueString, 0, valueString.length(), bounds);
        float y = barCenter + (bounds.height() / 2);
        canvas.drawText(valueString, fillPosition, y, currentValuePaint);
    }

    private float getBarCenter() {
        //position the bar slightly below the middle of the drawable area
        float barCenter = (getHeight() - getPaddingTop() - getPaddingBottom()) / 2; //this is the center
        barCenter += getPaddingTop() + .1f * getHeight(); //move it down a bit
        return barCenter;
    }

    private void drawMaxValue(Canvas canvas) {
        String maxValue = String.valueOf(this.maxValue);
        Rect maxValueRect = new Rect();
        maxValuePaint.getTextBounds(maxValue, 0, maxValue.length(), maxValueRect);

        float xPos = getWidth() - getPaddingRight();
        float yPos = getBarCenter() + maxValueRect.height() / 2;
        canvas.drawText(maxValue, xPos, yPos, maxValuePaint);

    }


    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.currentMax = currentMax;
        ss.currentMin = currentMin;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        currentMax = ss.currentMax;
        currentMin = ss.currentMin;
        valueToDraw = currentMax; //set valueToDraw directly to prevent re-animation
    }

    private static class SavedState extends BaseSavedState {
        int currentMax, currentMin;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentMax = in.readInt();
            currentMin = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(currentMax);
            out.writeInt(currentMin);
        }

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }


}*/
