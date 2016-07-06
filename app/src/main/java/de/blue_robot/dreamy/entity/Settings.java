package de.blue_robot.dreamy.entity;

/**
 * Preferences as given by the user
 */
public class Settings {

    /**
     * Should the device wake up when the user clicks on the time?
     */
    private boolean wakeOnTimeClick;


    public boolean isWakeOnTimeClick() {
        return wakeOnTimeClick;
    }

    public void setWakeOnTimeClick(boolean wakeOnTimeClick) {
        this.wakeOnTimeClick = wakeOnTimeClick;
    }
}
