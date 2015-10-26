package de.blue_robot.dreamy.notification;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import de.blue_robot.dreamy.RobotDaydream;

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
        final Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (!this.isAccessibilityEnabled()) {
            context.startActivity(intent);
        }
    }

    /**
     * determines if the accessibility service is enabled
     *
     * @return
     */
    public boolean isAccessibilityEnabled() {
        int accessibilityEnabled = 0;
        final String LIGHTFLOW_ACCESSIBILITY_SERVICE = "de.bluerobot.flashnotification/de.bluerobot.flashnotification.Notificationdetector";
        final boolean accessibilityFound = false;
        try {
            accessibilityEnabled = android.provider.Settings.Secure.getInt(this.context.getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.d(TAG, "ACCESSIBILITY: " + accessibilityEnabled);
        } catch (final android.provider.Settings.SettingNotFoundException e) {
            Log.d(TAG,
                    "Error finding setting, default accessibility to not found: " + e.getMessage());
        }

        final TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(
                ':');

        if (accessibilityEnabled == 1) {
            Log.d(TAG, "***ACCESSIBILIY IS ENABLED***: ");

            final String settingValue = android.provider.Settings.Secure.getString(this.context.getContentResolver(),
                    android.provider.Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            Log.d(TAG, "Setting: " + settingValue);
            if (settingValue != null) {
                final TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
                splitter.setString(settingValue);
                while (splitter.hasNext()) {
                    final String accessabilityService = splitter.next();
                    Log.d(TAG, "Setting: " + accessabilityService);
                    if (accessabilityService.equalsIgnoreCase(LIGHTFLOW_ACCESSIBILITY_SERVICE)) {
                        Log.d(TAG,
                                "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }

            Log.d(TAG, "***END***");
        } else {
            Log.d(TAG, "***ACCESSIBILIY IS DISABLED***");
        }
        return accessibilityFound;
    }
}
