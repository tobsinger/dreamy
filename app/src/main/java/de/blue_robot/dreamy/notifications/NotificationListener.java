package de.blue_robot.dreamy.notifications;

import android.content.Intent;
import android.os.Debug;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tobias on 26.10.2015.
 */
public class NotificationListener extends NotificationListenerService {

    private static List<StatusBarNotification> notifications = new ArrayList<>();
    private LocalBroadcastManager localBroadcastManager;

    public static List<StatusBarNotification> getNotifications() {
        return notifications;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("test", "creating notification listener service");
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        int show_all = Settings.Secure.getInt(getContentResolver(), "lock_screen_allow_private_notifications", -1);
        int noti_enabled = Settings.Secure.getInt(getContentResolver(), "lock_screen_show_notifications", -1);

        Log.d("test", "show notifications on lock screen: " + (noti_enabled > 0 ? "yes" : "no") + ", show private info: " + (show_all > 0 ? "yes" : "no"));
    }

    @Override
    public void onListenerConnected() {
        Log.d("test", "notification listener connected");
        notificationUpdate();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.d("test", "notification posted");
        notificationUpdate();
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.d("test", "notification removed");
        notificationUpdate();
    }

    private void notificationUpdate() {
        notifications.clear();
        StatusBarNotification[] activeNotifications = getActiveNotifications();
        for (StatusBarNotification notification : activeNotifications) {
            notifications.add(notification);
        }
        localBroadcastManager.sendBroadcast(new Intent("notification_update"));
    }
}
