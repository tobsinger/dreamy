package de.dreamy.settings;

import android.app.Activity;
import android.os.Bundle;
import android.os.Debug;
import android.widget.CompoundButton;
import android.widget.Switch;

import javax.inject.Inject;

import de.dreamy.DreamyApplication;
import de.dreamy.R;
import de.dreamy.modules.DreamyComponent;

/**
 * Ui to set the preferences of the daydream's behavior
 */
public class DreamySettingsActivity extends Activity {

    @Inject
    SettingsDao settingsDao;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DreamyApplication.getDreamyComponent().inject(this);

        setContentView(R.layout.dream_settings);

        final Switch endOnTimeClickSwitch = (Switch) findViewById(R.id.endOnTimeClickSwitch);

        endOnTimeClickSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                final Settings settings = settingsDao.getSettings(DreamySettingsActivity.this);
                settings.setWakeOnTimeClick(b);
                settingsDao.persistSettings(settings, DreamySettingsActivity.this);
            }
        });
    }
}