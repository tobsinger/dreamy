package de.dreamy.system;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import javax.inject.Inject;

/**
 * Provides access to device and system properties
 */
public class SystemProperties {

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
}
