package de.blue_robot.dreamy.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import dagger.Module;
import de.blue_robot.dreamy.Constants;
import de.blue_robot.dreamy.entity.Settings;

/**
 * Created by towe on 05/07/16.
 */
@Module
public class SettingsDao {

    private final String SETTINGS_KEY = "Settings";

    final Gson gson = new Gson();


    public Settings getSettings(Context context) {
        final String settings = getSharedPreferences(context).getString(SETTINGS_KEY, "");
        return gson.fromJson(settings, Settings.class);
    }

    public void persistSettings(Settings settings, Context context) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        final String settingString = gson.toJson(settings);
        editor.putString(SETTINGS_KEY, settingString);
        editor.commit();
    }

    private SharedPreferences getSharedPreferences(Context context) {

        return context.getApplicationContext().getSharedPreferences(Constants.SHARED_PREFS_KEY, Context.MODE_PRIVATE);
    }


}
