package de.dreamy.notifications;

import android.content.Intent;
import android.os.Debug;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.dreamy.Constants;

/**
 * Listener to be notified when notifications occur
 */
public class NotificationListener extends NotificationListenerService {


    private final static List<StatusBarNotification> notifications = new ArrayList<>();
    private final String TAG = NotificationListener.class.getCanonicalName();

    private LocalBroadcastManager localBroadcastManager;

    /**
     * Get the current list of notifications
     *
     * @return The notification list
     */
    public static List<StatusBarNotification> getNotifications() {
        return notifications;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "creating notification listener service");
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        final int show_all = Settings.Secure.getInt(getContentResolver(), "lock_screen_allow_private_notifications", -1);
        final int noti_enabled = Settings.Secure.getInt(getContentResolver(), "lock_screen_show_notifications", -1);
//        Debug.waitForDebugger();
        Log.d(TAG, "show notifications on lock screen: " + (noti_enabled > 0 ? "yes" : "no") + ", show private info: " + (show_all > 0 ? "yes" : "no"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onListenerConnected() {
        Log.d(TAG, "notification listener connected");
        notificationUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.d(TAG, "notification posted: " + sbn.toString());
        notificationUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.d(TAG, "notification removed");
        notificationUpdate();
    }

    /**
     * Update the list of notifications
     */
    private void notificationUpdate() {
        notifications.clear();
        final StatusBarNotification[] activeNotifications = getActiveNotifications();
        Collections.addAll(notifications, activeNotifications);
        localBroadcastManager.sendBroadcast(new Intent(Constants.INTENT_FILTER_NOTIFICATION_UPDATE));
    }
}
