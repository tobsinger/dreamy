package de.dreamy.view.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.dreamy.R;
import de.dreamy.system.SystemProperties;

/**
 * List view Adapter for App selection List
 */
public class AppListAdapter extends BaseAdapter {

    private static final String TAG = AppListAdapter.class.getSimpleName();

    private final List<SystemProperties.AppData> appDataList;
    private final Set<String> selectedApps;
    private final Context context;

    public AppListAdapter(List<SystemProperties.AppData> appDataList, Set<String> selectedApps, Context context) {
        this.appDataList = appDataList;
        this.context = context;
        this.selectedApps = new HashSet<>(selectedApps);
    }


    @Override
    public int getCount() {
        return appDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return appDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final SystemProperties.AppData appData = appDataList.get(i);
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.app_list_row, viewGroup, false);
            final ImageView iconImageView = (ImageView) convertView.findViewById(R.id.app_list_icon);
            final TextView titleTextView = (TextView) convertView.findViewById(R.id.app_list_title);
            final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.app_list_checkbox);
            viewHolder = new ViewHolder(iconImageView, titleTextView, checkBox);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.iconImageView.setImageDrawable(appData.icon);
        viewHolder.titleTextView.setText(appData.appName);
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                Log.d(TAG, (checked ? "" : "de") + "selected app " + appData.packageName);
                if (checked) {
                    selectedApps.add(appData.packageName);
                } else {
                    selectedApps.remove(appData.packageName);
                }
            }
        });
        viewHolder.checkBox.setChecked(selectedApps.contains(appData.packageName));
        return convertView;
    }

    public Set<String> getSelectedApps() {
        return selectedApps;
    }

    public void deselectAll() {
        selectedApps.clear();
        notifyDataSetChanged();
    }

    private static class ViewHolder {

        private final ImageView iconImageView;
        private final TextView titleTextView;
        private final CheckBox checkBox;

        private ViewHolder(ImageView iconImageView, TextView titleTextView, CheckBox checkBox) {
            this.iconImageView = iconImageView;
            this.titleTextView = titleTextView;
            this.checkBox = checkBox;
        }
    }

}
