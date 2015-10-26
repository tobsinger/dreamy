package de.blue_robot.dreamy.listener;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class NotificationListener extends AccessibilityService {

    private static NotificationListener sSharedInstance;

    private static final String TAG = NotificationListener.class.getSimpleName();

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // current status for the notifications
        final int eventType = event.getEventType();
        if (eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            final Parcelable parcelable = event.getParcelableData();

            if (parcelable instanceof Notification) {
                Log.d(TAG, event.getText().toString());
                CharSequence packageName = event.getPackageName();
                try {
                    Drawable icon = getPackageManager().getApplicationIcon(packageName.toString());
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(TAG, "Notification Message is empty. Can not broadcast");
            }
        }
    }


    @Override
    public void onInterrupt() {

    }

    @Override
    public void onServiceConnected() {

        sSharedInstance = this;
    }

    public static NotificationListener getSharedInstance() {
        return sSharedInstance;
    }

}

