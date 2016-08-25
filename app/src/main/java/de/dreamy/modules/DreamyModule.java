package de.dreamy.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.dreamy.DreamyApplication;
import de.dreamy.settings.SettingsDao;
import de.dreamy.system.SystemProperties;

@Module
public class DreamyModule {

    @Provides
    @Singleton
    Context provideContext() {
        return DreamyApplication.getContext();
    }

    @Provides
    @Singleton
    SettingsDao provideSettingsDao() {
        return new SettingsDao();
    }

    @Provides
    @Singleton
    SystemProperties provideSystemProperties(Context context) {
        return new SystemProperties(context);
    }

}
