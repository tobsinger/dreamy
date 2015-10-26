package de.blue_robot.dreamy.notifications;

import android.content.Intent;
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

	private LocalBroadcastManager localBroadcastManager;

	private static List<StatusBarNotification> notifications = new ArrayList<>();

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("test", "creating notification listener service");
		localBroadcastManager = LocalBroadcastManager.getInstance(this);
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

	public static List<StatusBarNotification> getNotifications() {
		return notifications;
	}
}
