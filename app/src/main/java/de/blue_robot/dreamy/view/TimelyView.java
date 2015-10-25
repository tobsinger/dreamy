package de.blue_robot.dreamy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import java.lang.reflect.Field;

import de.blue_robot.dreamy.R;

/**
 * Extension to {@link com.github.adnansm.timelytextview.TimelyView}.
 * It enables to set styling attributes like size and color via XML
 * <p/>
 * Created by Tobs on 25/10/15.
 */
public class TimelyView extends com.github.adnansm.timelytextview.TimelyView {

    private final String TAG = TimelyView.class.getName();
    private int color;

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
        mPaint.setStrokeWidth(5.0F);
        mPaint.setStyle(Paint.Style.STROKE);

        paintField.set(this, mPaint);
    }
}
