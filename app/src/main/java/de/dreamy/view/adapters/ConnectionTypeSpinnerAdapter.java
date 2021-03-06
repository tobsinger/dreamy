package de.dreamy.view.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dreamy.settings.Settings;

/**
 * Created by tobe on 23.08.16.
 */
public class ConnectionTypeSpinnerAdapter extends SimpleAdapter {

    private static final String KEY = "connectionType";

    public ConnectionTypeSpinnerAdapter(Context context) {
        super(context, getData(context.getResources()), android.R.layout.simple_spinner_item, new String[]{KEY}, new int[]{android.R.id.text1});
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private static List<Map<String, String>> getData(Resources resources) {
        final List<Map<String, String>> data = new ArrayList<>();
        for (Settings.ConnectionType type : Settings.ConnectionType.values()) {
            final Map<String, String> typeValueMap = new HashMap<>();
            typeValueMap.put(KEY, resources.getString(type.stringId));
            data.add(typeValueMap);
        }
        return data;
    }
}
