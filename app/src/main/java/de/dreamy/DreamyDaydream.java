package de.dreamy;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.service.dreams.DreamService;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import de.dreamy.notifications.NotificationListener;
import de.dreamy.settings.SettingsDao;
import de.dreamy.view.TimelyClock;
import de.dreamy.view.adapters.NotificationListAdapter;


/**
 * The actual day dream service implementation
 */
public class DreamyDaydream extends DreamService implements AdapterView.OnItemClickListener, View.OnClickListener {

    private final String TAG = DreamyDaydream.class.getCanonicalName();

    @Inject
    SettingsDao settingsDao;
    /**
     * The list that holds the notification views
     */
    private ListView listView;
    /**
     * Needed to receive updates about the notification list
     */
    private LocalBroadcastManager localBroadcastManager;

    private TimelyClock timelyClock;

    private BroadcastReceiver bcr = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            displayNotifications();
        }
    };


    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate() {
        super.onCreate();

        DreamyApplication.getDreamyComponent().inject(this);

        Log.d(TAG, "creating daydream service");
        initBroadcastManager();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttachedToWindow() {
        //setup daydream
        super.onAttachedToWindow();

        setInteractive(true);
        setFullscreen(true);

        final Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        setContentView(R.layout.daydream_layout);
        final NotificationListAdapter adapter = new NotificationListAdapter(this);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        timelyClock = (TimelyClock) findViewById(R.id.timelyClock);
        timelyClock.setOnClickListener(this);
//        Debug.waitForDebugger();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(bcr);
    }

    @Override
    public void onClick(View v) {

        if (timelyClock.equals(v) && settingsDao.getSettings(this).isWakeOnTimeClick()) {
            this.finish();
        }
        //this.finish();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDreamingStarted() {
        super.onDreamingStarted();

        if (settingsDao.getSettings(this).isShowNotifications()) {
            displayNotifications();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onitemclick");
        final StatusBarNotification notification = (StatusBarNotification) parent.getAdapter().getItem(position);
        try {
            notification.getNotification().contentIntent.send();
            this.finish();
        } catch (PendingIntent.CanceledException e) {
            Log.d(TAG, "intent canceled");
        }

    }

    /**
     * Initiate the broadcast manager and register the receiver for notifications about
     * new status bar notifications
     */
    private void initBroadcastManager() {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.INTENT_FILTER_NOTIFICATION_UPDATE);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(bcr, filter);
    }

    /**
     * Get the current list of status bar notifications, remove duplicates and display the rest in the list view
     */
    private void displayNotifications() {
        final List<StatusBarNotification> allNotifications = NotificationListener.getNotifications();
        final List<StatusBarNotification> filteredNotifications = new ArrayList<>();
        final List<Integer> notifications = new ArrayList<>();

        for (final StatusBarNotification n : allNotifications) {
            int singleNotificationIdentifier = getNotificationIdentifier(n.getNotification());
            if (!notifications.contains(singleNotificationIdentifier) && ((isOnlyNotificationWithGroupKey(n, allNotifications))
                    || (n.getNotification().visibility == Notification.VISIBILITY_PUBLIC || n.getNotification().publicVersion != null))) {
                filteredNotifications.add(n);
                notifications.add(singleNotificationIdentifier);
            }
        }
        if (listView != null) {
            ((NotificationListAdapter) listView.getAdapter()).setNotifications(new ArrayList<>(filteredNotifications));
        }
    }

    private boolean isOnlyNotificationWithGroupKey(final StatusBarNotification notification, final List<StatusBarNotification> allNotificatoins) {
        if (TextUtils.isEmpty(notification.getGroupKey())) {
            return true;
        }
        for (final StatusBarNotification singleNotification : allNotificatoins) {
            if (!notification.equals(singleNotification) && notification.getGroupKey().equals(singleNotification.getGroupKey())) {
                if ((singleNotification.getNotification().visibility == Notification.VISIBILITY_PUBLIC || singleNotification.getNotification().publicVersion != null)) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Get an identifier for the notification based on the notifications content
     *
     * @param notification The notification to generate an identifier for
     * @return The generated identifier
     */
    private int getNotificationIdentifier(Notification notification) {
        final Date date = new Date(notification.when);
        final String dateString = new SimpleDateFormat(Constants.TIME_PATTERN, Locale.GERMANY).format(date);
        final Bundle extras = notification.extras;
        final ApplicationInfo applicationInfo = ((ApplicationInfo) extras.get(Constants.NOTIFICATION_APP));
        final String identifierString = "" + dateString + (applicationInfo != null ? applicationInfo.className : "") + extras.get(Constants.NOTIFICATION_TITLE) + extras.get(Constants.NOTIFICATION_CONTENT);
        return identifierString.hashCode();
    }
}
