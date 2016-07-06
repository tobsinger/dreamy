package de.blue_robot.dreamy;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import javax.inject.Inject;

import de.blue_robot.dreamy.dao.SettingsDao;
import de.blue_robot.dreamy.entity.Settings;

/**
 * Ui to set the preferences of the daydream's behavior
 */
public class CustomDreamSettingsActivity extends Activity {

    @Inject
    SettingsDao settingsDao;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dream_settings);

        final Switch endOnTimeClickSwitch = (Switch) findViewById(R.id.endOnTimeClickSwitch);

        endOnTimeClickSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                final Settings settings = settingsDao.getSettings(CustomDreamSettingsActivity.this);
                settings.setWakeOnTimeClick(b);
                settingsDao.persistSettings(settings, CustomDreamSettingsActivity.this);
            }
        });
    }
}