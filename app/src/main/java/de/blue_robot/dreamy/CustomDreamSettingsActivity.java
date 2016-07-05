package de.blue_robot.dreamy;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

public class CustomDreamSettingsActivity extends Activity {
    public static final String PREFS_KEY = "SampleDreamPrefs";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dream_settings);
        final SharedPreferences settings = getSharedPreferences(PREFS_KEY, 0);
        boolean animate = settings.getBoolean("animateDream", true);

//        final ToggleButton toggle = (ToggleButton) findViewById(R.id.toggle_animate_button);
//        toggle.setChecked(animate);
//        toggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(final CompoundButton buttonView,
//                                         final boolean isChecked) {
//                SharedPreferences.Editor prefEditor = settings.edit();
//                prefEditor.putBoolean("animateDream", isChecked);
//                prefEditor.commit();
//            }
//        });
    }
}