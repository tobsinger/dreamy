package de.blue_robot.dreamy;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.service.dreams.DreamService;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import de.blue_robot.dreamy.notification.Settings;


/**
 * Created by Tobs on 24/10/15.
 */
public class RobotDaydream extends DreamService implements OnClickListener {


    private static RobotDaydream sInstance;

    private ImageView iconView;

    public static RobotDaydream getsInstance() {
        return sInstance;
    }


    public void setIcon(Drawable drawable){
        iconView.setImageDrawable(drawable);
    }

    public RobotDaydream(){
        super();
        sInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Settings s = new Settings(this);
        s.activateService();
    }

    @Override
    public void onAttachedToWindow() {
        //setup daydream
        super.onAttachedToWindow();

        setInteractive(false);
        setFullscreen(true);

        Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);

        setContentView(R.layout.daydream_layout);

        iconView = (ImageView) findViewById(R.id.iconView);
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
