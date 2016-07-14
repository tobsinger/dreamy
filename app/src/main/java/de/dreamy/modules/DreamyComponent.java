package de.dreamy.modules;

import javax.inject.Singleton;

import dagger.Component;
import de.dreamy.DreamyDaydream;
import de.dreamy.settings.DreamySettingsActivity;
import de.dreamy.view.adapters.NotificationListAdapter;

/**
 * Created by tobe on 13.07.16.
 */
@Singleton
@Component(modules = {DreamyModule.class})
public interface DreamyComponent {
    void inject(DreamySettingsActivity settingsActivity);

    void inject(DreamyDaydream dreamyDaydream);

    void inject(NotificationListAdapter notificationListAdapter);
}
