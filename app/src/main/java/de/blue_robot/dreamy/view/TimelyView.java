package de.blue_robot.dreamy.view;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;

import java.lang.reflect.Field;

/**
 * Extension to {@link com.github.adnansm.timelytextview.TimelyView}.
 * It enables to set styling attributes like size and color via XML
 * <p/>
 * Created by Tobs on 25/10/15.
 */
public class TimelyView extends com.github.adnansm.timelytextview.TimelyView {

    private int color;
    private float thickness;

    public TimelyView(Context context) {
        super(context);
    }

    public TimelyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setColor(int color) {
        this.color = color;
    }


    public int getColor() {
        return color;
    }

    public void setThickness(float thickness) {
        this.thickness = thickness;
    }

    public float getThickness() {
        return thickness;
    }


    /**
     * Set color and size from attributes
     *
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public void init() throws NoSuchFieldException, IllegalAccessException {


        Field paintField
                = com.github.adnansm.timelytextview.TimelyView.class.
                getDeclaredField("mPaint");

        paintField.setAccessible(true);
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(this.color);
        mPaint.setStrokeWidth(this.thickness);
        mPaint.setStyle(Paint.Style.STROKE);

        paintField.set(this, mPaint);
    }


}
