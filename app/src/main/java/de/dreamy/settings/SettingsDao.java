package de.dreamy.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.dreamy.Constants;

/**
 * Created by towe on 05/07/16.
 */
@Singleton
public class SettingsDao {

    final Gson gson = new Gson();
    private final String SETTINGS_KEY = "Settings";

    @Inject
    public SettingsDao() {
    }

    public Settings getSettings(Context context) {
        final String settings = getSharedPreferences(context).getString(SETTINGS_KEY, "");
        if (settings.isEmpty()) {
            return new Settings();
        } else {
            return gson.fromJson(settings, Settings.class);
        }
    }

    public void persistSettings(Settings settings, Context context) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        final String settingString = gson.toJson(settings);
        editor.putString(SETTINGS_KEY, settingString);
        editor.apply();
    }

    private SharedPreferences getSharedPreferences(Context context) {
        return context.getApplicationContext().getSharedPreferences(Constants.SHARED_PREFS_KEY, Context.MODE_PRIVATE);
    }


}
