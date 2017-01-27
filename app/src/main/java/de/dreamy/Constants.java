package de.dreamy;

/**
 * Application wide constants
 */
public interface Constants {

    /**
     * Key to address the shared preferences of this app
     */
    String SHARED_PREFS_KEY = "shared_prefs";
    /**
     * The pattern to extract hours and minutes in a 24h format
     */
    String TIME_PATTERN = "HH:mm";
    /**
     * The key to address the content of a status bar notification
     */
    String NOTIFICATION_CONTENT = "android.text";
    /**
     * The key to address the title of a status bar notification
     */
    String NOTIFICATION_TITLE = "android.title";
    /**
     * The key to address the name of the application that threw a status bar notification
     */
    String NOTIFICATION_APP = "android.rebuild.applicationInfo";
    /**
     * Intent filter used to send notifications about new status bar notifications
     */
    String INTENT_FILTER_NOTIFICATION_UPDATE = "notification_update";
    /**
     * The key to address the test mode flag for intents that start the day dream
     */
    String TEST_MODE = "test_mode";
}
