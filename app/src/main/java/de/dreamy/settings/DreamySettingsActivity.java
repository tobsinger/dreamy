package de.dreamy.settings;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.Toast;

import javax.inject.Inject;

import de.dreamy.DreamyApplication;
import de.dreamy.R;
import de.dreamy.notifications.NotificationListener;
import de.dreamy.system.SystemProperties;
import de.dreamy.view.adapters.AppListAdapter;
import de.dreamy.view.adapters.ConnectionTypeSpinnerAdapter;

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

        final TableRow appSelectionRow = (TableRow) findViewById(R.id.app_selection_row);
        appSelectionRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ListView appList = new ListView(DreamySettingsActivity.this);
                final AppListAdapter adapter = new AppListAdapter(systemProperties.getInstalledApps(), settings.getSelectedApps(), DreamySettingsActivity.this);
                appList.setAdapter(adapter);
                appList.setDividerHeight(3); //todo dp value
                appList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        ((CheckBox) view.findViewById(R.id.app_list_checkbox)).toggle();
                    }
                });
                final AlertDialog appListDialog = new AlertDialog.Builder(DreamySettingsActivity.this)
                        .setView(appList)
                        .setPositiveButton("Übernehmen", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                settings.setSelectedApps(adapter.getSelectedApps());
                                settingsDao.persistSettings(settings, DreamySettingsActivity.this);
                            }
                        })
                        .setNeutralButton("alle abwählen", null)
                        .setNegativeButton("abbrechen", null)
                        .create();
                appListDialog.show();
                appListDialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adapter.deselectAll();
                    }
                });
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
                    .setNegativeButton(R.string.i_dont_care, new DialogInterface.OnClickListener() {
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
                    .setNegativeButton(R.string.i_dont_care, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            dialogBuilder.create().show();
        }
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