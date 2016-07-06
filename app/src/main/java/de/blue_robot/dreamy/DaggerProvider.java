package de.blue_robot.dreamy;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.blue_robot.dreamy.dao.SettingsDao;

/**
 * Provider class for dependency injection with dagger
 */
@Module
public class DaggerProvider {

    @Provides
    @Singleton
    SettingsDao provideSettingsDao() {
        return new SettingsDao();
    }


}
