package de.dreamy.settings;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
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
        final Settings settings = settingsDao.getSettings(this);
        setContentView(R.layout.dream_settings);

        // End day dream on click on clock
        final Switch endOnTimeClickSwitch = (Switch) findViewById(R.id.endOnTimeClickSwitch);
        endOnTimeClickSwitch.setChecked(settings.isWakeOnTimeClick());
        endOnTimeClickSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                final Settings settings = settingsDao.getSettings(DreamySettingsActivity.this);
                settings.setWakeOnTimeClick(b);
                settingsDao.persistSettings(settings, DreamySettingsActivity.this);
            }
        });

        // Show notifications
        final Switch showNotificationsSwitch = (Switch) findViewById(R.id.displayNotificationsSwitch);
        showNotificationsSwitch.setChecked(settings.isShowNotifications());
        showNotificationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                final Settings settings = settingsDao.getSettings(DreamySettingsActivity.this);
                settings.setShowNotifications(b);
                settingsDao.persistSettings(settings, DreamySettingsActivity.this);
                if (b && !isNLServiceRunning()) {
                    Toast.makeText(DreamySettingsActivity.this, R.string.requestNotificationAccessMsg, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
                }
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
        showCarrierSwitch.setChecked(settings.isShowCarrierName());
        showCarrierSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                final Settings settings = settingsDao.getSettings(DreamySettingsActivity.this);
                settings.setShowCarrierName(b);
                settingsDao.persistSettings(settings, DreamySettingsActivity.this);
            }
        });


    }


    /**
     * Check if the notification listener service is running
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
}