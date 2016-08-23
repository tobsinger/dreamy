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

    private ConnectionType connectionType = ConnectionType.BOTH;


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

    public enum ConnectionType {
        CHARGER(R.string.connectionTypeCharger),
        PC(R.string.connectionTypePC),
        BOTH(R.string.connectionTypeBoth);

        public final int stringId;

        ConnectionType(int stringId) {
            this.stringId = stringId;
        }
    }
}
