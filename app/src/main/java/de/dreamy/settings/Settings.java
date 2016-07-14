package de.dreamy.settings;

/**
 * Preferences as given by the user
 */
public class Settings {

    /**
     * Should the device wake up when the user clicks on the time?
     */
    private boolean wakeOnTimeClick;

    /**
     * Should the day dream display notifications
     */
    private boolean showNotifications;


    /**
     * Should the battery status be displayed
     */
    private boolean showBatteryStatus;

    /**
     * Should the wifi status be displayed
     */
    private boolean showWifiStatus;

    /**
     * Should the network carrier name be displayed
     */
    private boolean showCarrierName;


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
}
