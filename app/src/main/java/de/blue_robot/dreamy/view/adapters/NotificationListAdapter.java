package de.blue_robot.dreamy.view.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.service.notification.StatusBarNotification;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tobe on 27.10.15.
 */
public class NotificationListAdapter extends BaseAdapter {

    private List<StatusBarNotification> notifications;
    private final Context context;

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
