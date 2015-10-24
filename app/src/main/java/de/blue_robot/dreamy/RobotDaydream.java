package de.blue_robot.dreamy;

import java.util.Random;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.graphics.Color;
import android.graphics.Point;
import android.service.dreams.DreamService;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;

/**
 * Created by Tobs on 24/10/15.
 */
public class RobotDaydream extends DreamService implements OnClickListener {

    private Button dismissBtn;
    private ImageView[] robotImgs;
    private AnimatorSet[] robotSets;
    private final int ROWS_COLS = 5;
    private final int NUM_ROBOTS = ROWS_COLS * ROWS_COLS;
    private int randPosn;

    @Override
    public void onDreamingStarted() {
        //daydream started
        super.onDreamingStarted();

        for (int r = 0; r < NUM_ROBOTS; r++) {
            if (r != randPosn) {
                robotSets[r].start();
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
        robotImgs = new ImageView[NUM_ROBOTS];


        Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        int robotWidth = screenSize.x / ROWS_COLS;
        int robotHeight = screenSize.y / ROWS_COLS;

        for (int r = 0; r < NUM_ROBOTS; r++) {
            //add to grid
            GridLayout.LayoutParams ddP = new GridLayout.LayoutParams();
            ddP.width = robotWidth;
            ddP.height = robotHeight;

            if (r == randPosn) {
                //stop button
                dismissBtn = new Button(this);
                dismissBtn.setText("stop");
                dismissBtn.setBackgroundColor(Color.WHITE);
                dismissBtn.setTextColor(Color.RED);
                dismissBtn.setOnClickListener(this);
                dismissBtn.setLayoutParams(ddP);
                ddLayout.addView(dismissBtn);
            } else {
                //robot image view
                robotImgs[r] = new ImageView(this);
                robotImgs[r].setImageResource(R.drawable.ic_launcher);
                ddLayout.addView(robotImgs[r], ddP);

                robotSets[r] = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.android_spin);

                robotSets[r].setTarget(robotImgs[r]);
                robotImgs[r].setOnClickListener(this);

            }
        }

        setContentView(ddLayout);
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
                    if ((ImageView) v == robotImgs[r]) {
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
