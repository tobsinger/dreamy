package de.blue_robot.dreamy;

import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.Debug;
import android.os.PowerManager;
import android.service.dreams.DreamService;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import de.blue_robot.dreamy.notifications.NotificationListener;
import de.blue_robot.dreamy.view.adapters.NotificationListAdapter;


/**
 * Created by Tobs on 24/10/15.
 */
public class RobotDaydream extends DreamService implements AdapterView.OnItemClickListener { //implements OnClickListener {

    //    private ImageView iconView;
    private ListView listView;

    private LocalBroadcastManager localBroadcastManager;

//    /**
//     * Set icon of latest notification
//     *
//     * @param drawable Icon to set
//     */
//    public void setIcon(Drawable drawable) {
//        ColorMatrix matrix = new ColorMatrix();
//        matrix.setSaturation(0);
//
//        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
//
//        drawable.setColorFilter(filter);
//        iconView.setImageDrawable(drawable);
//    }
    private BroadcastReceiver bcr = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            viewNotifications();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("test", "creating daydream service");
        initBroadcastManager();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(bcr);
    }

    @Override
    public void onAttachedToWindow() {
        //setup daydream
        super.onAttachedToWindow();

        setInteractive(true);
        setFullscreen(true);

        Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        setContentView(R.layout.daydream_layout);
//        iconView = (ImageView) findViewById(R.id.iconView);
        listView = (ListView) findViewById(R.id.listView);
        NotificationListAdapter adapter = new NotificationListAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

    }


//    @Override
//    public void onClick(View v) {
//        Log.d("test", "klick");
//        //this.finish();
//    }

    @Override
    public void onDreamingStarted() {
        //daydream started
        super.onDreamingStarted();
        viewNotifications();
//        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 20);
    }

    private void initBroadcastManager() {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("notification_update");
        localBroadcastManager.registerReceiver(bcr, filter);
    }

    private void viewNotifications() {
        List<StatusBarNotification> notifications = NotificationListener.getNotifications();
        Log.d("test", "number of notifications: " + notifications.size());
        ((NotificationListAdapter) listView.getAdapter()).setNotifications(new ArrayList<>(notifications));
//        if (notifications.size() > 0) {
//            StatusBarNotification nf = notifications.get(0);
//            try {
//                setIcon(createPackageContext(nf.getPackageName(), Context.CONTEXT_IGNORE_SECURITY)
//                        .getResources().getDrawable(nf.getNotification().icon));
//            } catch (PackageManager.NameNotFoundException e) {
//            }
//        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("test", "onitemclick");
        StatusBarNotification notification = (StatusBarNotification) parent.getAdapter().getItem(position);
        try {
            notification.getNotification().contentIntent.send();
            KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            final KeyguardManager.KeyguardLock kl = km .newKeyguardLock("MyKeyguardLock");
            kl.disableKeyguard();

            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                    | PowerManager.ACQUIRE_CAUSES_WAKEUP
                    | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
            wakeLock.acquire();
            this.finish();
        } catch (PendingIntent.CanceledException e) {
            Log.d("test", "intent canceled");
        }

    }
}
