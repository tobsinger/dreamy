package de.dreamy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.ObjectAnimator;

import java.util.Calendar;

import de.dreamy.R;

/**
 * Created by Tobs on 25/10/15.
 */
public class TimelyClock extends LinearLayout {

    public static final int DURATION = 300;
    private static final String TAG = TimelyClock.class.getSimpleName();
    private volatile ObjectAnimator objectAnimator = null;

    private TimelyView hours1;
    private TimelyView hours2;
    private TimelyView minutes1;
    private TimelyView minutes2;
    private TimelyView seconds1;
    private TimelyView seconds2;

    public static final int NO_VALUE = -1;
    private int mSecondFirstDigit = NO_VALUE;
    private int mSecondSecondDigit = NO_VALUE;
    private int mMinuteFirstDigit = NO_VALUE;
    private int mMinuteSecondDigit = NO_VALUE;
    private int mHourFirstDigit = NO_VALUE;
    private int mHourSecondDigit = NO_VALUE;

    private Calendar c;
    final Handler handler = new Handler();
    private Runnable timeSetter;

    public TimelyClock(Context context) {
        super(context);
        try {
            init(context, null);
        } catch (NoSuchFieldException e) {
            Log.w(TAG, e);
        } catch (IllegalAccessException e) {
            Log.w(TAG, e);
        }
    }

    public TimelyClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        try {
            init(context, attrs);
        } catch (NoSuchFieldException e) {
            Log.w(TAG, e);
        } catch (IllegalAccessException e) {
            Log.w(TAG, e);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }


    @Override
    protected void onDetachedFromWindow() {
        this.stop();
        super.onDetachedFromWindow();
    }

    public void start() {
        handler.post(timeSetter);
    }

    public void stop() {
        handler.removeCallbacks(timeSetter);
    }

    private void init(Context context, AttributeSet attrs) throws NoSuchFieldException, IllegalAccessException {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.timely_clock, this);
        }

        int textSize = 0;
        float colonTextSize = 0;
        float thickness = 0;
        int color = getResources().getColor(android.R.color.holo_blue_dark);
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.TimelyClock,
                    0, 0);
            try {
                color = a.getInteger(R.styleable.TimelyClock_clockTextColor, color);
                textSize = a.getDimensionPixelSize(R.styleable.TimelyClock_textSize, textSize);
                colonTextSize = a.getDimension(R.styleable.TimelyClock_textSize, colonTextSize);
                thickness = a.getDimension(R.styleable.TimelyClock_strokeThickness, thickness);
            } finally {
                a.recycle();
            }
        }

        hours1 = (TimelyView) findViewById(R.id.hours1);
        initTimelyView(hours1, color, textSize, thickness);

        hours2 = (TimelyView) findViewById(R.id.hours2);
        initTimelyView(hours2, color, textSize, thickness);

        minutes1 = (TimelyView) findViewById(R.id.minutes1);
        initTimelyView(minutes1, color, textSize, thickness);

        minutes2 = (TimelyView) findViewById(R.id.minutes2);
        initTimelyView(minutes2, color, textSize, thickness);

        seconds1 = (TimelyView) findViewById(R.id.seconds1);
        initTimelyView(seconds1, color, textSize / 2, thickness / 2);

        seconds2 = (TimelyView) findViewById(R.id.seconds2);
        initTimelyView(seconds2, color, textSize / 2, thickness / 2);

        final TextView separator = (TextView) findViewById(R.id.separator);
        separator.setTextSize(colonTextSize / 6);
        separator.setTextColor(color);

        timeSetter = new Runnable() {

            @Override
            public void run() {
                c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);
                int mSecond = c.get(Calendar.SECOND);

                // Set hours
                int[] hours = getSeparateDigits(mHour, 2);
                if (mHourFirstDigit != hours[0]) {
                    objectAnimator = hours1.animate(mHourFirstDigit, hours[0]);
                    objectAnimator.setDuration(DURATION);
                    objectAnimator.start();
                    mHourFirstDigit = hours[0];
                }

                if (mHourSecondDigit != hours[1]) {
                    objectAnimator = hours2.animate(mHourSecondDigit, hours[1]);
                    objectAnimator.setDuration(DURATION);
                    objectAnimator.start();
                    mHourSecondDigit = hours[1];
                }

                // Set minutes
                int[] minutes = getSeparateDigits(mMinute, 2);
                if (mMinuteFirstDigit != minutes[0]) {
                    objectAnimator = minutes1.animate(mMinuteFirstDigit, minutes[0]);
                    objectAnimator.setDuration(DURATION);
                    objectAnimator.start();
                    mMinuteFirstDigit = minutes[0];
                }

                if (mMinuteSecondDigit != minutes[1]) {
                    objectAnimator = minutes2.animate(mMinuteSecondDigit, minutes[1]);
                    objectAnimator.setDuration(DURATION);
                    objectAnimator.start();
                    mMinuteSecondDigit = minutes[1];
                }

                // Set seconds
                int[] seconds = getSeparateDigits(mSecond, 2);
                if (mSecondFirstDigit != seconds[0]) {
                    objectAnimator = seconds1.animate(mSecondFirstDigit, seconds[0]);
                    objectAnimator.setDuration(DURATION);
                    objectAnimator.start();
                    mSecondFirstDigit = seconds[0];
                }


                if (mSecondSecondDigit != seconds[1]) {
                    objectAnimator = seconds2.animate(mSecondSecondDigit, seconds[1]);
                    objectAnimator.setDuration(DURATION);
                    objectAnimator.start();
                    mSecondSecondDigit = seconds[1];
                }
                handler.postDelayed(this, 1000);
            }
        };
        this.start();
    }

    /**
     * Get separate digits from a number
     *
     * @param number         numper to extract digits from
     * @param numberOfDigits the number of digits to get
     * @return
     */
    private int[] getSeparateDigits(int number, int numberOfDigits) {
        String formatted = String.format("%0" + numberOfDigits + "d", number);
        int[] result = new int[numberOfDigits];

        for (int i = 0; i < numberOfDigits; i++) {
            result[i] = Integer.valueOf(formatted.substring(i, i + 1));
        }
        return result;
    }

    private void initTimelyView(TimelyView view, int color, int textSize, float thickness) throws NoSuchFieldException, IllegalAccessException {
        view.setColor(color);
        view.setThickness(thickness);
        view.getLayoutParams().height = textSize;
        view.init();
    }
}
