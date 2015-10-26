package de.blue_robot.dreamy.notification;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import de.blue_robot.dreamy.RobotDaydream;
import de.blue_robot.dreamy.listener.NotificationListener;

/**
 * Created by Tobs on 26/10/15.
 */
public class Settings {

    public Settings(RobotDaydream context) {
        this.context = context;
    }

    private static final String TAG = Settings.class.getSimpleName();
    private RobotDaydream context;

    public void activateService() {
        if (!this.isAccessibilityEnabled()) {
            final Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    /**
     * determines if the accessibility service is enabled
     *
     * @return true if accessibility service is enabled
     */
    public boolean isAccessibilityEnabled() {
        int accessibilityEnabled = 0;
        final String notificationListener = NotificationListener.class.getCanonicalName();
        final boolean accessibilityFound = false;
        try {
            accessibilityEnabled = android.provider.Settings.Secure.getInt(this.context.getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (final android.provider.Settings.SettingNotFoundException e) {
            Log.d(TAG,
                    "Error finding setting, default accessibility to not found: " + e.getMessage());
        }

        final TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(
                ':');

        if (accessibilityEnabled == 1) {

            final String settingValue = android.provider.Settings.Secure.getString(this.context.getContentResolver(),
                    android.provider.Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                final TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
                splitter.setString(settingValue);
                while (splitter.hasNext()) {
                    final String accessabilityService = splitter.next();
                    if (accessabilityService.contains(notificationListener)) {
                        return true;
                    }
                }
            }

        }
        return accessibilityFound;
    }
}
