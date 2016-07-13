package de.dreamy.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.dreamy.settings.SettingsDao;

@Module
public class DreamyModule {

    @Provides
    @Singleton
    SettingsDao provideSettingsDao() {
        return new SettingsDao();
    }

}
