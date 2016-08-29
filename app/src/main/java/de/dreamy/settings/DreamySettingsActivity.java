package de.dreamy.settings;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Date;

import javax.inject.Inject;

import de.dreamy.Constants;
import de.dreamy.DreamyApplication;
import de.dreamy.R;
import de.dreamy.notifications.NotificationListener;
import de.dreamy.system.SystemProperties;
import de.dreamy.view.ColorPicker;
import de.dreamy.view.adapters.ConnectionTypeSpinnerAdapter;
import de.dreamy.view.adapters.NotificationDetailsSpinnerAdapter;

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
    @Inject
    SystemProperties systemProperties;

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
        final Spinner notificationDetailsSpinner = (Spinner) findViewById(R.id.notificationPrivacySpinner);

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


        final ColorPicker colorPickerTime = (ColorPicker) findViewById(R.id.colorPickerClock);
        colorPickerTime.addColorPickedListener(new ColorPicker.ColorPickedListener() {
            @Override
            public void onColorPicked(final int colorId) {
                settings.setTimeColor(ContextCompat.getColor(DreamySettingsActivity.this, colorId));
                settingsDao.persistSettings(settings, DreamySettingsActivity.this);
            }
        });
        if (settings.getTimeColor() != 0) {
            colorPickerTime.onColorPicked(settings.getTimeColor());
        }


        final ColorPicker colorPickerDeviceStatus = (ColorPicker) findViewById(R.id.colorPickerDeviceStatusInformation);
        colorPickerDeviceStatus.addColorPickedListener(new ColorPicker.ColorPickedListener() {
            @Override
            public void onColorPicked(final int colorId) {
                settings.setDeviceStatusColor(ContextCompat.getColor(DreamySettingsActivity.this, colorId));
                settingsDao.persistSettings(settings, DreamySettingsActivity.this);
            }
        });

        if (settings.getDeviceStatusColor() != 0) {
            colorPickerDeviceStatus.onColorPicked(settings.getDeviceStatusColor());
        }

        final ColorPicker colorPickerNotificationFontColor = (ColorPicker) findViewById(R.id.colorPickerNotificationsFont);
        colorPickerNotificationFontColor.addColorPickedListener(new ColorPicker.ColorPickedListener() {
            @Override
            public void onColorPicked(final int colorId) {
                settings.setNotificationsFontColor(ContextCompat.getColor(DreamySettingsActivity.this, colorId));
                settingsDao.persistSettings(settings, DreamySettingsActivity.this);
            }
        });

        if (settings.getNotificationsFontColor() != 0) {
            colorPickerNotificationFontColor.onColorPicked(settings.getNotificationsFontColor());
        }


        final ColorPicker colorPickerNotificationsBackground = (ColorPicker) findViewById(R.id.colorPickerNotificationsBackground);
        colorPickerNotificationsBackground.addColorPickedListener(new ColorPicker.ColorPickedListener() {
            @Override
            public void onColorPicked(final int colorId) {
                settings.setNotificationsBackgroundColor(ContextCompat.getColor(DreamySettingsActivity.this, colorId));
                settingsDao.persistSettings(settings, DreamySettingsActivity.this);
            }
        });

        if (settings.getNotificationsBackgroundColor() != 0) {
            colorPickerNotificationsBackground.onColorPicked(settings.getNotificationsBackgroundColor());
        }

        // Show notifications
        final Switch showNotificationsSwitch = (Switch) findViewById(R.id.displayNotificationsSwitch);
        boolean notificationsEnabled = settings.isShowNotifications() && isNLServiceRunning();
        showNotificationsSwitch.setChecked(notificationsEnabled);
        notificationsDimBar.setEnabled(notificationsEnabled);
        notificationDetailsSpinner.setEnabled(notificationsEnabled);
        final View selectedView = notificationDetailsSpinner.getSelectedView();
        if (selectedView != null) {
            selectedView.setEnabled(notificationsEnabled);
        }
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
                notificationDetailsSpinner.setEnabled(showNotifications);
                notificationDetailsSpinner.getSelectedView().setEnabled(showNotifications);
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
            private float initialBrightness;

            @Override
            public void onProgressChanged(final SeekBar seekBar, final int i, final boolean b) {
                final float newValue = i / 100f;
                settings.setScreenBrightness(newValue);
                WindowManager.LayoutParams layout = getWindow().getAttributes();
                layout.screenBrightness = newValue;
                getWindow().setAttributes(layout);
                settingsDao.persistSettings(settings, DreamySettingsActivity.this);
            }

            @Override
            public void onStartTrackingTouch(final SeekBar seekBar) {
                WindowManager.LayoutParams layout = getWindow().getAttributes();
                initialBrightness = layout.screenBrightness;
                layout.screenBrightness = settings.getScreenBrightness();
            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
                WindowManager.LayoutParams layout = getWindow().getAttributes();
                layout.screenBrightness = initialBrightness;
                getWindow().setAttributes(layout);
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
                && systemProperties.hasPermission(Manifest.permission.READ_PHONE_STATE));

        showCarrierSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                // permission not granted
                if (!systemProperties.hasPermission(Manifest.permission.READ_PHONE_STATE)
                        ) {
                    ActivityCompat.requestPermissions(DreamySettingsActivity.this,
                            new String[]{Manifest.permission.READ_PHONE_STATE},
                            4711);
                    compoundButton.setChecked(false);
                    return;
                }

                settings.setShowCarrierName(b);
                settingsDao.persistSettings(settings, DreamySettingsActivity.this);
            }
        });

        final Spinner connectionTypeSpinner = (Spinner) findViewById(R.id.connectionTypeSpinner);
        final ConnectionTypeSpinnerAdapter spinnerAdapter = new ConnectionTypeSpinnerAdapter(this);
        connectionTypeSpinner.setAdapter(spinnerAdapter);
        connectionTypeSpinner.setSelection(settings.getConnectionType().ordinal());
        connectionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                settings.setConnectionType(Settings.ConnectionType.values()[i]);
                settingsDao.persistSettings(settings, DreamySettingsActivity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing
            }
        });


        // Notification details
        final NotificationDetailsSpinnerAdapter notificationDetailsSpinnerAdapter = new NotificationDetailsSpinnerAdapter(this);
        notificationDetailsSpinner.setAdapter(notificationDetailsSpinnerAdapter);
        notificationDetailsSpinner.setSelection(settings.getNotificationPrivacy().ordinal());
        notificationDetailsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                settings.setNotificationPrivacy(Settings.NotificationPrivacy.values()[i]);
                settingsDao.persistSettings(settings, DreamySettingsActivity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!systemProperties.isDaydreamEnabled()) {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setMessage(R.string.daydream_disabled)
                    .setPositiveButton(R.string.go_to_daydream_settings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(android.provider.Settings.ACTION_DREAM_SETTINGS));
                        }
                    })
                    .setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            dialogBuilder.create().show();
        } else if (!systemProperties.isDreamySelected()) {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setMessage(R.string.daydream_not_selected)
                    .setPositiveButton(R.string.go_to_daydream_settings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(android.provider.Settings.ACTION_DREAM_SETTINGS));
                        }
                    })
                    .setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            dialogBuilder.create().show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.the_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_start_daydream:
                final Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setClassName("com.android.systemui", "com.android.systemui.Somnambulator");
                intent.putExtra(Constants.TEST_MODE, true);
                startActivity(intent);

                final SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS_KEY, Context.MODE_PRIVATE);
                final SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
                preferencesEditor.putLong(Constants.TEST_MODE, new Date().getTime());
                preferencesEditor.apply();
                break;
            default:
                break;
        }

        return true;
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


}