package de.dreamy;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.service.dreams.DreamService;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import de.dreamy.notifications.NotificationListener;
import de.dreamy.settings.Settings;
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
     * The animated clock
     */
    private TimelyClock timelyClock;

    /**
     * The field to display the current battery level
     */
    private TextView batteryPercentage;
    /**
     * The battery icon
     */
    private ImageView batteryIcon;

    private LocalBroadcastManager localBroadcastManager;


    /**
     * Broadcast receiver to handle incoming notifications
     */
    private BroadcastReceiver notificationBroadcastReceiver = new BroadcastReceiver() {
        /**
         * {@inheritDoc}
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            displayNotifications();
        }
    };

    /**
     * Broadcast receiver to handle battery state change events
     */
    private BroadcastReceiver batteryBroadcastReceiver = new BroadcastReceiver() {
        /**
         * {@inheritDoc}
         */
        @Override
        public void onReceive(Context context, Intent batteryStatus) {
            // Are we charging / charged?
            int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;

            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int batteryPct = (int) (level / (float) scale * 100);

            if (batteryIcon != null) {
                batteryIcon.setVisibility(View.VISIBLE);
                if (isCharging) {
                    batteryIcon.setImageResource(R.drawable.ic_battery_charging_full_white_48dp);
                } else {
                    batteryIcon.setImageResource(R.drawable.ic_battery_full_white_48dp);
                }
            }
            if (batteryPercentage != null) {
                batteryPercentage.setText(batteryPct + "%");
            }
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
        final Settings settings = settingsDao.getSettings(this);

        // Change Screen brightness
        WindowManager.LayoutParams layout = getWindow().getAttributes();
        layout.screenBrightness = settings.getScreenBrightness();
        getWindow().setAttributes(layout);
        setInteractive(true);
        setFullscreen(true);


        final Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        setContentView(R.layout.daydream_layout);
        final NotificationListAdapter adapter = new NotificationListAdapter(this);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setAlpha(settings.getNotificationVisibility());
        timelyClock = (TimelyClock) findViewById(R.id.timelyClock);
        timelyClock.setOnClickListener(this);
        batteryPercentage = (TextView) findViewById(R.id.batteryPercentage);
        batteryIcon = (ImageView) findViewById(R.id.batteryIcon);


        if (settings.isShowBatteryStatus()) {
            findViewById(R.id.batteryInfo).setVisibility(View.VISIBLE);
        }

        if (settings.isShowWifiStatus()) {
            final View wifiInfo = findViewById(R.id.wifiInfo);
            final String currentWifi = getCurrentWifi();
            if (currentWifi != null) {
                wifiInfo.setVisibility(View.VISIBLE);
                final TextView wifiName = (TextView) findViewById(R.id.wifiName);
                wifiName.setText(currentWifi);

            } else {
                wifiInfo.setVisibility(View.GONE);
            }

        }

        if (settings.isShowCarrierName()) {
            final TextView carrierTextView = (TextView) findViewById(R.id.carrierName);
            carrierTextView.setVisibility(View.VISIBLE);
            carrierTextView.setText(getCarrierName());
        }


//        Debug.waitForDebugger();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {
        if (timelyClock.equals(v) && settingsDao.getSettings(this).isWakeOnTimeClick()) {
            this.finish();
        }
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


    @Override
    public void onDetachedFromWindow() {
        this.unregisterReceiver(batteryBroadcastReceiver);
        localBroadcastManager.unregisterReceiver(notificationBroadcastReceiver);
        super.onDetachedFromWindow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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

        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        final IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.INTENT_FILTER_NOTIFICATION_UPDATE);
        localBroadcastManager.registerReceiver(notificationBroadcastReceiver, filter);

        if (settingsDao.getSettings(this).isShowBatteryStatus()) {
            final IntentFilter batteryStatusIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            this.registerReceiver(batteryBroadcastReceiver, batteryStatusIntentFilter);
        }
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
            if (!notifications.contains(singleNotificationIdentifier)
                    && ((isOnlyNotificationWithGroupKey(n, allNotifications))
                    || (n.getNotification().visibility == Notification.VISIBILITY_PUBLIC
                    || n.getNotification().publicVersion != null))) {
                filteredNotifications.add(n);
                notifications.add(singleNotificationIdentifier);
            }
        }
        if (listView != null) {
            ((NotificationListAdapter) listView.getAdapter()).setNotifications(new ArrayList<>(filteredNotifications));
        }
    }


    /**
     * Check if there isn't a public notification with the same group key
     *
     * @param notification     The notification to find a public version for
     * @param allNotifications The list of all  notifications
     * @return True if there is no public version of the notification
     */
    private boolean isOnlyNotificationWithGroupKey(final StatusBarNotification notification, final List<StatusBarNotification> allNotifications) {
        if (TextUtils.isEmpty(notification.getGroupKey())) {
            return true;
        }
        for (final StatusBarNotification singleNotification : allNotifications) {
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
    private int getNotificationIdentifier(final Notification notification) {
        final Date date = new Date(notification.when);
        final String dateString = new SimpleDateFormat(Constants.TIME_PATTERN, Locale.GERMANY).format(date);
        final Bundle extras = notification.extras;
        final ApplicationInfo applicationInfo = ((ApplicationInfo) extras.get(Constants.NOTIFICATION_APP));
        final String identifierString = "" + dateString + (applicationInfo != null ? applicationInfo.className : "") + extras.get(Constants.NOTIFICATION_TITLE) + extras.get(Constants.NOTIFICATION_CONTENT);
        return identifierString.hashCode();
    }

    /**
     * Get the name of the currently connected wifi
     *
     * @return the name of the currently connected wifi. NULL if no wifi connected
     */
    private String getCurrentWifi() {
        final ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final Network networks[] = connManager.getAllNetworks();
        for (final Network singleNetwork : networks) {
            final NetworkInfo networkInfo = connManager.getNetworkInfo(singleNetwork);
            if (ConnectivityManager.TYPE_WIFI == networkInfo.getType()) {
                if (!networkInfo.isConnected()) {
                    continue;
                }
                if (networkInfo.isConnected()) {
                    final WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
                    final WifiInfo connectionInfo = wifiManager.getConnectionInfo();

                    if (connectionInfo != null) {
                        String ssid = connectionInfo.getSSID();
                        String quotes = String.valueOf('"');
                        if (ssid.startsWith(quotes)) {
                            ssid = ssid.substring(1, ssid.length());
                        }
                        if (ssid.endsWith(quotes)) {
                            ssid = ssid.substring(0, ssid.length() - 1);
                        }
                        return ssid;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Get the name of the current carrier
     *
     * @return A string with the carrier's name
     */
    private String getCarrierName() {
        final TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        final String carrierName = manager.getNetworkOperatorName();
        return carrierName;
    }

}
