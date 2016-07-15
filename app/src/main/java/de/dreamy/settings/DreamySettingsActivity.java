package de.dreamy.settings;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import javax.inject.Inject;

import de.dreamy.DreamyApplication;
import de.dreamy.R;
import de.dreamy.notifications.NotificationListener;

/**
 * UI to set the preferences of the daydream's behavior
 */
public class DreamySettingsActivity extends Activity {

    private static final String TAG = DreamySettingsActivity.class.getCanonicalName();

    /**
     * the dao to load and save {@link Settings}
     */
    @Inject
    SettingsDao settingsDao;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DreamyApplication.getDreamyComponent().inject(this);
        setContentView(R.layout.dream_settings);

        final Settings settings = settingsDao.getSettings(this);
        final SeekBar notificationsDimBar = (SeekBar) findViewById(R.id.notificationsDimBar);

        // End day dream on click on clock
        final Switch endOnTimeClickSwitch = (Switch) findViewById(R.id.endOnTimeClickSwitch);
        endOnTimeClickSwitch.setChecked(settings.isWakeOnTimeClick());
        endOnTimeClickSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                settings.setWakeOnTimeClick(b);
                settingsDao.persistSettings(settings, DreamySettingsActivity.this);
            }
        });

        // Show notifications
        final Switch showNotificationsSwitch = (Switch) findViewById(R.id.displayNotificationsSwitch);
        showNotificationsSwitch.setChecked(settings.isShowNotifications() && isNLServiceRunning());
        showNotificationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean showNotifications) {
                // permission not granted
                if (showNotifications && !isNLServiceRunning()) {
                    Toast.makeText(DreamySettingsActivity.this, R.string.requestNotificationAccessMsg, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
                    showNotificationsSwitch.setChecked(false);
                    return;
                }

                settings.setShowNotifications(showNotifications);
                notificationsDimBar.setEnabled(showNotifications);
                settingsDao.persistSettings(settings, DreamySettingsActivity.this);
            }
        });

        // Notification visibility
        notificationsDimBar.setProgress((int) (settings.getNotificationVisibility() * 100));
        notificationsDimBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final SeekBar seekBar, final int i, final boolean b) {
                float newValue = (float) i;
                newValue = newValue / 100;
                settings.setNotificationVisibility(newValue);
                settingsDao.persistSettings(settings, DreamySettingsActivity.this);
            }

            @Override
            public void onStartTrackingTouch(final SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
            }
        });

        // Screen Brightness
        final SeekBar brightnessBar = (SeekBar) findViewById(R.id.displayDimBar);
        brightnessBar.setProgress((int) (settings.getScreenBrightness() * 100));
        brightnessBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final SeekBar seekBar, final int i, final boolean b) {
                float newValue = (float) i;
                newValue = newValue / 100;
                settings.setScreenBrightness(newValue);
                settingsDao.persistSettings(settings, DreamySettingsActivity.this);
            }

            @Override
            public void onStartTrackingTouch(final SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
            }
        });

        // Show battery info
        final Switch showBatterySwitch = (Switch) findViewById(R.id.showBatteryStatusSwitch);
        showBatterySwitch.setChecked(settings.isShowBatteryStatus());
        showBatterySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                final Settings settings = settingsDao.getSettings(DreamySettingsActivity.this);
                settings.setShowBatteryStatus(b);
                settingsDao.persistSettings(settings, DreamySettingsActivity.this);
            }
        });

        // Show wifi info
        final Switch showWifiSwitch = (Switch) findViewById(R.id.showWifiStatusSwitch);
        showWifiSwitch.setChecked(settings.isShowWifiStatus());
        showWifiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                final Settings settings = settingsDao.getSettings(DreamySettingsActivity.this);
                settings.setShowWifiStatus(b);
                settingsDao.persistSettings(settings, DreamySettingsActivity.this);
            }
        });

        // Show carrier info
        final Switch showCarrierSwitch = (Switch) findViewById(R.id.showCarrierSwitch);
        showCarrierSwitch.setChecked(settings.isShowCarrierName()
                && hasPermission(Manifest.permission.READ_PHONE_STATE));

        showCarrierSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                // permission not granted
                if (!hasPermission(Manifest.permission.READ_PHONE_STATE)
                        ) {
                    ActivityCompat.requestPermissions(DreamySettingsActivity.this,
                            new String[]{Manifest.permission.READ_PHONE_STATE},
                            4711);
                    compoundButton.setChecked(false);
                    return;
                }

                final Settings settings = settingsDao.getSettings(DreamySettingsActivity.this);
                settings.setShowCarrierName(b);
                settingsDao.persistSettings(settings, DreamySettingsActivity.this);
            }
        });
    }

    /**
     * Check if the notification listener service is running.
     * This indicates if the user has given the app the right to access the notifications
     *
     * @return true if the service is running
     */
    private boolean isNLServiceRunning() {
        final ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (NotificationListener.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(DreamySettingsActivity.this,
                permission)
                == PackageManager.PERMISSION_GRANTED;
    }
}