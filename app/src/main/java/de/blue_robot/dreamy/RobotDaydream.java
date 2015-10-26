package de.blue_robot.dreamy;

import android.graphics.Point;
import android.service.dreams.DreamService;
import android.view.View;
import android.view.View.OnClickListener;


/**
 * Created by Tobs on 24/10/15.
 */
public class RobotDaydream extends DreamService implements OnClickListener {


    @Override
    public void onAttachedToWindow() {
        //setup daydream
        super.onAttachedToWindow();

        setInteractive(false);
        setFullscreen(true);

        Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);

        setContentView(R.layout.daydream_layout);
    }


    @Override
    public void onDreamingStarted() {
        //daydream started
        super.onDreamingStarted();
    }


    @Override
    public void onClick(View v) {
        this.finish();
    }
}
