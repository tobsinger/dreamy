package de.dreamy.settings;

import de.dreamy.R;

/**
 * Preferences as given by the user
 */
public class Settings {

    /**
     * Should the device wake up when the user clicks on the time?
     */
    private boolean wakeOnTimeClick = false;

    /**
     * Should the day dream display notifications
     */
    private boolean showNotifications = false;


    /**
     * Should the battery status be displayed
     */
    private boolean showBatteryStatus = true;

    /**
     * Should the wifi status be displayed
     */
    private boolean showWifiStatus = true;

    /**
     * Should the network carrier name be displayed
     */
    private boolean showCarrierName = false;


    /**
     * The alpha value of a notification
     */
    private float notificationVisibility = (float) .8;

    /**
     * The screen brightness
     */
    private float screenBrightness = (float) .8;

    /**
     * The font color of the time
     */
    private int timeColor = 0;

    /**
     * The font color of the device status
     */
    private int deviceStatusColor = 0;

    /**
     * The background color of notifications
     */
    private int notificationsBackgroundColor = 0;

    /**
     * The font color of notifications
     */
    private int notificationsFontColor = 0;

    /**
     * The style of the clock
     */
    private ClockStyle clockStyle = ClockStyle.ANIMATED;


    private ConnectionType connectionType = ConnectionType.ALWAYS;

    private NotificationPrivacy notificationPrivacy = NotificationPrivacy.SHOW_EVERYTHING;


    public boolean isWakeOnTimeClick() {
        return wakeOnTimeClick;
    }

    public void setWakeOnTimeClick(boolean wakeOnTimeClick) {
        this.wakeOnTimeClick = wakeOnTimeClick;
    }

    public boolean isShowNotifications() {
        return showNotifications;
    }

    public void setShowNotifications(final boolean showNotifications) {
        this.showNotifications = showNotifications;
    }

    public boolean isShowBatteryStatus() {
        return showBatteryStatus;
    }

    public void setShowBatteryStatus(final boolean showBatteryStatus) {
        this.showBatteryStatus = showBatteryStatus;
    }

    public boolean isShowWifiStatus() {
        return showWifiStatus;
    }

    public void setShowWifiStatus(final boolean showWifiStatus) {
        this.showWifiStatus = showWifiStatus;
    }

    public boolean isShowCarrierName() {
        return showCarrierName;
    }

    public void setShowCarrierName(final boolean showCarrierName) {
        this.showCarrierName = showCarrierName;
    }

    public float getNotificationVisibility() {
        return notificationVisibility;
    }

    public void setNotificationVisibility(final float notificationVisibility) {
        this.notificationVisibility = notificationVisibility;
    }


    public float getScreenBrightness() {
        return screenBrightness;
    }

    public void setScreenBrightness(final float screenBrightness) {
        this.screenBrightness = screenBrightness;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    public NotificationPrivacy getNotificationPrivacy() {
        return notificationPrivacy;
    }

    public void setNotificationPrivacy(final NotificationPrivacy notificationPrivacy) {
        this.notificationPrivacy = notificationPrivacy;
    }

    public int getTimeColor() {
        return timeColor;
    }

    public void setTimeColor(final int timeColor) {
        this.timeColor = timeColor;
    }

    public int getDeviceStatusColor() {
        return deviceStatusColor;
    }

    public void setDeviceStatusColor(final int deviceStatusColor) {
        this.deviceStatusColor = deviceStatusColor;
    }

    public int getNotificationsBackgroundColor() {
        return notificationsBackgroundColor;
    }

    public void setNotificationsBackgroundColor(final int notificationsBackgroundColor) {
        this.notificationsBackgroundColor = notificationsBackgroundColor;
    }

    public int getNotificationsFontColor() {
        return notificationsFontColor;
    }

    public void setNotificationsFontColor(final int notificationsFontColor) {
        this.notificationsFontColor = notificationsFontColor;
    }

    public ClockStyle getClockStyle() {
        return clockStyle;
    }

    public void setClockStyle(final ClockStyle clockStyle) {
        this.clockStyle = clockStyle;
    }

    public enum ConnectionType {
        CHARGER(R.string.connectionTypeCharger),
        PC(R.string.connectionTypePC),
        ALWAYS(R.string.connectionTypeAlways);

        public final int stringId;

        ConnectionType(int stringId) {
            this.stringId = stringId;
        }
    }

    public enum NotificationPrivacy {
        SHOW_EVERYTHING(R.string.showEverything),
        SHOW_TITLE(R.string.showTitle),
        SHOW_APP_NAME(R.string.showNameOfTheApp);

        public final int stringId;

        NotificationPrivacy(int stringId) {
            this.stringId = stringId;
        }


    }


    public enum ClockStyle {
        ANIMATED(R.string.animated),
        DIGITAL(R.string.digital);

        public final int stringId;

        ClockStyle(int stringId) {
            this.stringId = stringId;
        }


    }
}
