package de.dreamy.system;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

import java.util.List;

import javax.inject.Inject;

import de.dreamy.DreamyDaydream;

/**
 * Provides access to device and system properties
 */
public class SystemProperties {

    private static final String SCREENSAVER_ENABLED = "screensaver_enabled";
    private static final String SCREENSAVER_COMPONENTS = "screensaver_components";
    private final Context context;

    @Inject
    public SystemProperties(Context context) {
        this.context = context;
    }

    /**
     * Gets the name of the current carrier
     *
     * @return A string with the carrier's name
     */
    public String getCarrierName() {
        final TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getNetworkOperatorName();
    }


    /**
     * Gets the name of the currently connected wifi
     *
     * @return the name of the currently connected wifi. NULL if no wifi connected
     */
    public String getCurrentWifi() {
        final ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final Network networks[] = connManager.getAllNetworks();
        for (final Network singleNetwork : networks) {
            final NetworkInfo networkInfo = connManager.getNetworkInfo(singleNetwork);
            if (ConnectivityManager.TYPE_WIFI == networkInfo.getType()) {
                if (!networkInfo.isConnected()) {
                    continue;
                }
                if (networkInfo.isConnected()) {
                    final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    final WifiInfo connectionInfo = wifiManager.getConnectionInfo();

                    if (connectionInfo != null) {
                        String ssid = connectionInfo.getSSID();
                        String quotes = String.valueOf('"');
                        if (ssid.startsWith(quotes)) {
                            ssid = ssid.substring(1, ssid.length());
                        }
                        if (ssid.endsWith(quotes)) {
                            ssid = ssid.substring(0, ssid.length() - 1);
                        }
                        return ssid;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Checks if the app has a certain permission
     *
     * @param permission permission string, use constants defined in {@link Manifest.permission}
     * @return {@code ture} if this app has the requested permission, {@code false} otherwise
     */
    public boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Gets whether Daydream is enabled in the Android settings menu.
     *
     * @return {@code true} if Daydream is enabled
     */
    public boolean isDaydreamEnabled() {
        return 1 == Settings.Secure.getInt(context.getContentResolver(), SCREENSAVER_ENABLED, -1);
    }

    /**
     * Checks if Dreamy is selected as the Daydream application
     *
     * @return {@code true} if Dreamy is selected as Daydream
     */
    public boolean isDreamySelected() {
        final String names = Settings.Secure.getString(context.getContentResolver(), SCREENSAVER_COMPONENTS);
        return names != null && DreamyDaydream.class.getName().equals(componentsFromString(names)[0].getClassName());
    }

    public List<ApplicationInfo> getInstalledApps() {
        final PackageManager pm = context.getPackageManager();
        return pm.getInstalledApplications(0);
    }

    // Copied from Android source code. Gets the ComponentNames for a given name
    private ComponentName[] componentsFromString(String names) {
        String[] namesArray = names.split(",");
        ComponentName[] componentNames = new ComponentName[namesArray.length];
        for (int i = 0; i < namesArray.length; i++) {
            componentNames[i] = ComponentName.unflattenFromString(namesArray[i]);
        }
        return componentNames;
    }
}
