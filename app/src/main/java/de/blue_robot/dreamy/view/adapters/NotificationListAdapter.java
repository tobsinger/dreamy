package de.blue_robot.dreamy.view.adapters;

import android.app.Notification;
import android.content.Context;
import android.os.Build;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tobe on 27.10.15.
 */
public class NotificationListAdapter extends BaseAdapter {

    private final Context context;
    private List<StatusBarNotification> notifications;

    public NotificationListAdapter(Context context) {
        this(context, new ArrayList<StatusBarNotification>());
    }

    public NotificationListAdapter(Context context, List<StatusBarNotification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @Override
    public int getCount() {
        return notifications.size();
    }

    @Override
    public Object getItem(int position) {
        return notifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return notifications.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("test", "item " + position + "; visibility: " + notifications.get(position).getNotification().visibility);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Notification publicVersion = notifications.get(position).getNotification().publicVersion;
            if (publicVersion != null)
                return publicVersion.contentView.apply(context, parent);
        }

        return notifications.get(position).getNotification().contentView.apply(context, parent);
    }

    public List<StatusBarNotification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<StatusBarNotification> notifications) {
        this.notifications = notifications;
        notifyDataSetChanged();
    }
}
