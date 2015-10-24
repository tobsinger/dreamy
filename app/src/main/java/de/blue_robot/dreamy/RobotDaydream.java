package de.blue_robot.dreamy;

import java.util.Date;
import java.util.Random;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.graphics.Color;
import android.graphics.Point;
import android.service.dreams.DreamService;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.github.adnansm.timelytextview.TimelyView;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by Tobs on 24/10/15.
 */
public class RobotDaydream extends DreamService implements OnClickListener {

    private Button dismissBtn;
    private TimelyView[] robotImgs;
    private AnimatorSet[] robotSets;
    private final int ROWS_COLS = 5;
    private final int NUM_ROBOTS = ROWS_COLS * ROWS_COLS;
    private int randPosn;


    public static final int DURATION = 1000;
    public static final int NO_VALUE = -1;
    private TimelyView timelyView = null;
    private SeekBar seekBar = null;
    private Spinner fromSpinner = null;
    private Spinner toSpinner = null;
    private volatile ObjectAnimator objectAnimator = null;

    private volatile int from = NO_VALUE;
    private volatile int to = NO_VALUE;

    @Override
    public void onDreamingStarted() {
        //daydream started
        super.onDreamingStarted();

        for (int r = 0; r < NUM_ROBOTS; r++) {
            if (r != randPosn) {
//                robotSets[r].start();
            }
        }
    }

    @Override
    public void onAttachedToWindow() {
        //setup daydream

        super.onAttachedToWindow();

        setInteractive(true);
        setFullscreen(true);

        Random rand = new Random();
        randPosn = rand.nextInt(NUM_ROBOTS);

        GridLayout ddLayout = new GridLayout(this);
        ddLayout.setColumnCount(ROWS_COLS);
        ddLayout.setRowCount(ROWS_COLS);

        robotSets = new AnimatorSet[NUM_ROBOTS];
        robotImgs = new TimelyView[NUM_ROBOTS];


        Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        int robotWidth = screenSize.x / ROWS_COLS;
        int robotHeight = screenSize.y / ROWS_COLS;

//        for (int r = 0; r < NUM_ROBOTS; r++) {
//            //add to grid
//            GridLayout.LayoutParams ddP = new GridLayout.LayoutParams();
//            ddP.width = robotWidth;
//            ddP.height = robotHeight;
//
//            if (r == randPosn) {
//                //stop button
//                dismissBtn = new Button(this);
//                dismissBtn.setText("stop");
//                dismissBtn.setBackgroundColor(Color.WHITE);
//                dismissBtn.setTextColor(Color.RED);
//                dismissBtn.setOnClickListener(this);
//                dismissBtn.setLayoutParams(ddP);
//                ddLayout.addView(dismissBtn);
//            } else {
//                //robot image view
//                robotImgs[r] = new TimelyView(this);
//                objectAnimator = robotImgs[r].animate(-1, 15);
//                objectAnimator.setDuration(DURATION);
//// robotImgs[r].animate(1, 15);
////                robotImgs[r].setImageResource(R.drawable.ic_launcher);
//                ddLayout.addView(robotImgs[r], ddP);
//
//                robotSets[r] = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.android_spin);
//
//                robotSets[r].setTarget(robotImgs[r]);
//                robotImgs[r].setOnClickListener(this);
//
//            }
//        }

        setContentView(R.layout.timely_layout);


        timelyView = (TimelyView) findViewById(R.id.textView1);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        fromSpinner = (Spinner) findViewById(R.id.fromSpinner);
        toSpinner = (Spinner) findViewById(R.id.toSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.from_numbers_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);
        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                from = position - 1;
                if (from != NO_VALUE && to != NO_VALUE) {
                    objectAnimator = timelyView.animate(from, to);
                    objectAnimator.setDuration(DURATION);
                } else {
                    objectAnimator = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                to = position - 1;
                if (from != NO_VALUE && to != NO_VALUE) {
                    objectAnimator = timelyView.animate(from, to);
                    objectAnimator.setDuration(DURATION);
                } else {
                    objectAnimator = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        seekBar.setMax(DURATION);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (objectAnimator != null) {
                    objectAnimator.setCurrentPlayTime(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onDetachedFromWindow() {
        //tidy up
        for (int r = 0; r < NUM_ROBOTS; r++) {
            if (r != randPosn) {
                robotImgs[r].setOnClickListener(null);
            }
        }
    }

    @Override
    public void onDreamingStopped() {
        //daydream

        for (int r = 0; r < NUM_ROBOTS; r++) {
            if (r != randPosn) {
                robotSets[r].cancel();
            }
        }
        super.onDreamingStopped();
    }

    @Override
    public void onClick(View v) {
        if (v instanceof Button && (Button) v == dismissBtn) {
            //stop button
            this.finish();
        } else {
            //robot image
            for (int r = 0; r < NUM_ROBOTS; r++) {
                //check array
                if (r != randPosn) {
                    //check image view
                    if ((TimelyView) v == robotImgs[r]) {
                        //is the current view
                        if (robotSets[r].isStarted()) {
                            robotSets[r].cancel();
                        } else {
                            robotSets[r].start();
                        }
                        break;
                    }
                }
            }
        }
    }
}
